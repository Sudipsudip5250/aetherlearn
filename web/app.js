const CONTENT_MANIFEST = "./content/manifest.json";
const state = { lessons: [], currentId: null };

const $ = (selector) => document.querySelector(selector);

function parseScalar(value) {
  const trimmed = value.trim();
  if (trimmed === "[]") return [];
  if (/^\d+$/.test(trimmed)) return Number(trimmed);
  if ((trimmed.startsWith('"') && trimmed.endsWith('"')) || (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
    return trimmed.slice(1, -1);
  }
  return trimmed;
}

function parseFrontmatter(source) {
  const match = source.match(/^---\s*\n([\s\S]*?)\n---\s*\n?([\s\S]*)$/);
  if (!match) throw new Error("Lesson is missing a YAML frontmatter block.");
  const metadata = {};
  const nested = {};
  let listKey = null;
  let nestedKey = null;

  for (const line of match[1].split(/\r?\n/)) {
    if (!line.trim()) continue;
    const indent = line.length - line.trimStart().length;
    const listItem = line.trim().match(/^-\s+(.*)$/);
    if (indent >= 2 && nestedKey && /^\w[\w-]*:\s*/.test(line.trim())) {
      const [key, ...rest] = line.trim().split(":");
      nested[nestedKey][key] = parseScalar(rest.join(":").trim());
      continue;
    }
    if (indent >= 2 && listKey && listItem) {
      if (!Array.isArray(metadata[listKey])) metadata[listKey] = [];
      metadata[listKey].push(parseScalar(listItem[1]));
      continue;
    }
    const field = line.match(/^(\w[\w-]*):\s*(.*)$/);
    if (!field) continue;
    const [, key, rawValue] = field;
    if (rawValue.trim() === "") {
      if (key === "termux") {
        nestedKey = key;
        nested[key] = {};
        listKey = null;
      } else {
        listKey = key;
        nestedKey = null;
        metadata[key] = [];
      }
    } else {
      metadata[key] = parseScalar(rawValue);
      listKey = null;
      nestedKey = null;
    }
  }
  if (nested.termux) metadata.termux = nested.termux;
  return { metadata, body: match[2].trim() };
}

function parseSections(body) {
  const sections = [];
  let current = null;
  for (const line of body.split(/\r?\n/)) {
    const heading = line.match(/^##\s+(.+?)\s*$/);
    if (heading) {
      if (current) current.body = current.body.join("\n").trim();
      current = { title: heading[1], body: [] };
      sections.push(current);
    } else if (current) {
      current.body.push(line);
    }
  }
  if (current) current.body = current.body.join("\n").trim();
  return sections;
}

function lessonFromMarkdown(markdown, path) {
  const parsed = parseFrontmatter(markdown);
  const metadata = parsed.metadata;
  return {
    ...metadata,
    path,
    sections: parseSections(parsed.body),
    searchText: `${metadata.title || ""} ${parsed.body}`.toLowerCase(),
  };
}

async function loadLessons() {
  const manifestResponse = await fetch(CONTENT_MANIFEST, { cache: "no-store" });
  if (!manifestResponse.ok) throw new Error(`Could not load content manifest (${manifestResponse.status}).`);
  const manifest = await manifestResponse.json();
  if (manifest.schema_version !== 1 || !Array.isArray(manifest.lessons)) {
    throw new Error("Unsupported or malformed shared content manifest.");
  }
  const lessons = await Promise.all(manifest.lessons.map(async (entry) => {
    const response = await fetch(`./${entry.path}`, { cache: "no-store" });
    if (!response.ok) throw new Error(`Could not load ${entry.path} (${response.status}).`);
    const lesson = lessonFromMarkdown(await response.text(), entry.path);
    if (lesson.id !== entry.id || lesson.title !== entry.title) throw new Error(`Manifest mismatch for ${entry.id}.`);
    return lesson;
  }));
  return lessons;
}

function escapeHtml(value) {
  return String(value).replace(/[&<>"']/g, (character) => ({
    "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;",
  })[character]);
}

function linkTarget(href) {
  if (/^https:\/\//i.test(href)) return { href, external: true };
  if (/^[a-z0-9-]+\.md$/i.test(href)) return { href: `#/lesson/${href.slice(0, -3)}`, external: false };
  if (href.startsWith("#")) return { href, external: false };
  return { href: "#", external: false };
}

function renderInline(text) {
  let rendered = escapeHtml(text);
  rendered = rendered.replace(/\[([^\]]+)\]\(([^)]+)\)/g, (_, label, rawHref) => {
    const target = linkTarget(rawHref);
    const external = target.external ? ' target="_blank" rel="noreferrer"' : "";
    return `<a href="${escapeHtml(target.href)}"${external}>${label}</a>`;
  });
  rendered = rendered.replace(/`([^`]+)`/g, '<code>$1</code>');
  rendered = rendered.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>');
  return rendered;
}

function renderMarkdown(markdown) {
  const lines = markdown.split(/\r?\n/);
  const output = [];
  let list = false;
  let code = false;
  let codeLines = [];
  const closeList = () => { if (list) { output.push("</ul>"); list = false; } };
  for (const line of lines) {
    if (line.trim().startsWith("```")) {
      if (code) {
        output.push(`<pre><code>${escapeHtml(codeLines.join("\n"))}</code></pre>`);
        codeLines = [];
      }
      code = !code;
      closeList();
      continue;
    }
    if (code) { codeLines.push(line); continue; }
    const bullet = line.match(/^\s*(?:[-*]|\d+\.)\s+(.+)$/);
    if (bullet) {
      if (!list) { output.push("<ul>"); list = true; }
      output.push(`<li>${renderInline(bullet[1])}</li>`);
    } else if (line.trim()) {
      closeList();
      output.push(`<p>${renderInline(line.trim())}</p>`);
    } else {
      closeList();
    }
  }
  closeList();
  if (code) output.push(`<pre><code>${escapeHtml(codeLines.join("\n"))}</code></pre>`);
  return output.join("");
}

function preview(lesson) {
  const explanation = lesson.sections.find((section) => section.title.toLowerCase() === "explanation");
  const text = (explanation?.body || "").replace(/[*`#]/g, "").replace(/\s+/g, " ").trim();
  return text.length > 145 ? `${text.slice(0, 142)}…` : text;
}

function setCacheStatus(message, warning = false) {
  $("#cache-status").textContent = message;
  $("#cache-dot").classList.toggle("warning", warning);
}

function renderLessonList() {
  $("#module-count").textContent = `${state.lessons.length} modules · shared Markdown source`;
  $("#lesson-list").innerHTML = state.lessons.map((lesson, index) => `
    <article class="lesson-card">
      <div class="card-top"><span class="lesson-index">${String(index + 1).padStart(2, "0")}</span><span class="pill">${escapeHtml(lesson.availability)}</span></div>
      <h3>${escapeHtml(lesson.title)}</h3>
      <p>${escapeHtml(preview(lesson))}</p>
      <div class="card-meta"><span class="pill">${escapeHtml(lesson.strand)}</span><span class="pill">${escapeHtml(String(lesson.estimated_minutes))} min</span><button class="open-card" type="button" data-open-lesson="${escapeHtml(lesson.id)}">Read lesson →</button></div>
    </article>`).join("");
  document.querySelectorAll("[data-open-lesson]").forEach((button) => button.addEventListener("click", () => { window.location.hash = `#/lesson/${button.dataset.openLesson}`; }));
}

function renderReader(lesson) {
  const objectives = Array.isArray(lesson.objectives) ? lesson.objectives : [];
  const sections = lesson.sections.filter((section) => section.title.toLowerCase() !== "objectives").map((section) => `
    <section class="lesson-section" aria-labelledby="section-${cssId(section.title)}">
      <h2 id="section-${cssId(section.title)}">${escapeHtml(section.title)}</h2>
      <div>${renderMarkdown(section.body)}</div>
    </section>`).join("");
  const termuxNote = lesson.availability === "termux-optional" ? `<aside class="termux-note"><strong>Termux is Android-only.</strong><span>This browser fallback includes the lesson and offline practice, but not the native terminal handoff.</span></aside>` : "";
  $("#reader-content").innerHTML = `
    <div class="reader-kicker">${escapeHtml(lesson.strand)} · ${escapeHtml(lesson.level)}</div>
    <h1 id="reader-title">${escapeHtml(lesson.title)}</h1>
    <p class="reader-summary">${escapeHtml(preview(lesson))}</p>
    <div class="reader-meta"><span class="pill">${escapeHtml(lesson.availability)}</span><span class="pill">${escapeHtml(String(lesson.estimated_minutes))} minutes</span><span class="pill">${escapeHtml(lesson.risk_tier)}</span></div>
    ${objectives.length ? `<section class="lesson-section objectives-section"><h2>Objectives</h2><ul>${objectives.map((objective) => `<li>${escapeHtml(objective)}</li>`).join("")}</ul></section>` : ""}
    ${termuxNote}
    ${sections}`;
}

function cssId(value) { return value.toLowerCase().replace(/[^a-z0-9]+/g, "-").replace(/(^-|-$)/g, ""); }

function readRoute() {
  const hash = window.location.hash || "#/learn";
  const match = hash.match(/^#\/lesson\/([^/]+)$/);
  if (match) return { type: "lesson", id: decodeURIComponent(match[1]) };
  if (hash === "#/about") return { type: "about" };
  return { type: "learn" };
}

function renderRoute() {
  const route = readRoute();
  const listSection = $(".content-section");
  const reader = $("#reader-view");
  const about = $("#about-view");
  const hero = $(".shell-grid");
  listSection.hidden = route.type !== "learn";
  reader.hidden = route.type !== "lesson";
  about.hidden = route.type !== "about";
  hero.hidden = route.type === "lesson";
  document.querySelectorAll("[data-route]").forEach((link) => link.setAttribute("aria-current", link.dataset.route === route.type ? "page" : "false"));
  if (route.type === "lesson") {
    const lesson = state.lessons.find((candidate) => candidate.id === route.id);
    if (lesson) renderReader(lesson);
    else reader.innerHTML = `<p>That lesson is not present in the cached core catalog.</p>`;
    window.scrollTo({ top: 0, behavior: "smooth" });
  }
}

function setupTheme() {
  const saved = localStorage.getItem("aetherlearn-web-theme");
  if (saved === "dark") document.documentElement.dataset.theme = "dark";
  $("#theme-toggle").addEventListener("click", () => {
    const dark = document.documentElement.dataset.theme === "dark";
    if (dark) delete document.documentElement.dataset.theme;
    else document.documentElement.dataset.theme = "dark";
    localStorage.setItem("aetherlearn-web-theme", dark ? "light" : "dark");
  });
}

async function start() {
  setupTheme();
  $("#cache-button").addEventListener("click", () => setCacheStatus("The offline cache flow is the next M6 slice; the shared lesson source is loaded now."));
  window.addEventListener("hashchange", renderRoute);
  try {
    state.lessons = await loadLessons();
    renderLessonList();
    setCacheStatus("Five core lessons loaded from the shared content contract.");
    renderRoute();
  } catch (error) {
    console.error(error);
    setCacheStatus("The local lesson catalog could not be loaded.", true);
    $("#lesson-list").innerHTML = `<div class="loading-card"><strong>Content unavailable.</strong><span>${escapeHtml(error.message)}</span></div>`;
  }
}

start();
