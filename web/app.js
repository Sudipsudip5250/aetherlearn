import { clearActivePack, clearLearningState, readActivePack, readLearningState, replaceActivePack, writeLearningState } from "./idb.js?v=22";

const CONTENT_MANIFEST = "./content/manifest.json";
const VISUAL_MANIFEST_URL = "./visuals/visual-foundations/manifest.json";
const VISUAL_CACHE_PREFIX = "aetherlearn-visuals-v1-";
const VISUAL_ACTIVE_KEY = "aetherlearn-visual-active";
const DEFAULT_STATE = { version: 2, progress: {}, notes: {}, bookmarks: {}, quiz: {}, startingLevel: null, startingLevelDismissed: false, goals: [] };
const state = { lessons: [], learning: { ...DEFAULT_STATE }, activePack: null, visualPack: null };

const $ = (selector) => document.querySelector(selector);

function parseScalar(value) {
  const trimmed = value.trim();
  if (trimmed === "[]") return [];
  if (/^\d+$/.test(trimmed)) return Number(trimmed);
  if ((trimmed.startsWith('"') && trimmed.endsWith('"')) || (trimmed.startsWith("'") && trimmed.endsWith("'"))) return trimmed.slice(1, -1);
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
    } else if (current) current.body.push(line);
  }
  if (current) current.body = current.body.join("\n").trim();
  return sections;
}

function lessonFromMarkdown(markdown, path) {
  const parsed = parseFrontmatter(markdown);
  const metadata = parsed.metadata;
  return { ...metadata, path, sections: parseSections(parsed.body), searchText: normalizeText(`${metadata.title || ""} ${parsed.body}`) };
}

async function fetchNetworkPack() {
  const manifestResponse = await fetch(CONTENT_MANIFEST, { cache: "no-store" });
  if (!manifestResponse.ok) throw new Error(`Could not load content manifest (${manifestResponse.status}).`);
  const manifest = await manifestResponse.json();
  if (manifest.schema_version !== 1 || !Array.isArray(manifest.lessons) || manifest.lessons.length === 0) throw new Error("Unsupported or empty core content manifest.");
  const lessons = await Promise.all(manifest.lessons.map(async (entry) => {
    const response = await fetch(`./${entry.path}`, { cache: "no-store" });
    if (!response.ok) throw new Error(`Could not load ${entry.path} (${response.status}).`);
    const markdown = await response.text();
    const lesson = lessonFromMarkdown(markdown, entry.path);
    if (lesson.id !== entry.id || lesson.title !== entry.title) throw new Error(`Manifest mismatch for ${entry.id}.`);
    return { ...entry, markdown, lesson };
  }));
  return { schemaVersion: manifest.schema_version, packId: manifest.pack_id, packVersion: manifest.pack_version, cachedAt: new Date().toISOString(), entries: lessons.map(({ lesson, ...entry }) => entry) };
}

function lessonsFromPack(pack) {
  return pack.entries.map((entry) => lessonFromMarkdown(entry.markdown, entry.path));
}

function normalizeLearningState(value) {
  return {
    ...DEFAULT_STATE,
    ...(value || {}),
    version: 2,
    progress: { ...(value?.progress || {}) },
    notes: { ...(value?.notes || {}) },
    bookmarks: { ...(value?.bookmarks || {}) },
    quiz: { ...(value?.quiz || {}) },
    startingLevel: ["NEW_TO_COMPUTING", "DIGITAL_BASICS", "TRIED_PROGRAMMING"].includes(value?.startingLevel) ? value.startingLevel : null,
    startingLevelDismissed: value?.startingLevelDismissed === true,
    goals: Array.isArray(value?.goals) ? value.goals.filter(validGoal).slice(0, 50) : [],
  };
}

function validGoal(goal) {
  return goal && typeof goal.title === "string" && goal.title.trim() && goal.title.length <= 160 && (goal.lessonId == null || typeof goal.lessonId === "string") && typeof goal.done === "boolean";
}

function persistLearning() {
  writeLearningState(normalizeLearningState(state.learning)).catch((error) => console.error("Could not save local learning state", error));
}

function updateProgress(id, nextState) {
  state.learning.progress[id] = nextState;
  persistLearning();
  renderLessonList();
  renderProgress();
}

function statusLabel(id) {
  return state.learning.progress[id] || "not started";
}

function parseKnowledgeCheck(body) {
  return body.split(/\r?\n/).map((line) => line.trim()).filter((line) => /^(?:\d+\.|[-*])\s+/.test(line)).map((line) => {
    const text = line.replace(/^(?:\d+\.|[-*])\s+/, "");
    const answerMatch = text.match(/\*\*([^*]+)\*\*\.?\s*(.*)$/);
    const answer = answerMatch ? answerMatch[1].trim() : "";
    return { question: answerMatch ? answerMatch[1].length ? text.slice(0, answerMatch.index).trim() : text : text, answer, acceptedAnswers: answer ? [answer, ...answer.split(/,\s*|\s+or\s+/).map((value) => value.trim()).filter((value) => value.length >= 4)] : [], type: "short-answer-v1", explanation: answerMatch?.[2]?.trim() || "" };
  }).filter((item) => item.question && item.answer);
}

function lessonQuiz(lesson) {
  const section = lesson.sections.find((candidate) => candidate.title.toLowerCase() === "knowledge check");
  return section ? parseKnowledgeCheck(section.body) : [];
}

function escapeHtml(value) {
  return String(value).replace(/[&<>"']/g, (character) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[character]);
}

function linkTarget(href) {
  if (/^https:\/\//i.test(href)) return { href, external: true };
  if (/^[a-z0-9-]+\.md$/i.test(href)) return { href: `#/lesson/${href.slice(0, -3)}`, external: false };
  if (href.startsWith("#")) return { href, external: false };
  return { href: "#", external: false };
}

function renderInline(text) {
  let rendered = escapeHtml(text);
  rendered = rendered.replace(/\[([^\]]+)\]\(([^)]+)\)/g, (_, label, rawHref) => { const target = linkTarget(rawHref); const external = target.external ? ' target="_blank" rel="noopener noreferrer"' : ""; return `<a href="${escapeHtml(target.href)}"${external}>${label}</a>`; });
  rendered = rendered.replace(/`([^`]+)`/g, "<code>$1</code>");
  rendered = rendered.replace(/\*\*([^*]+)\*\*/g, "<strong>$1</strong>");
  return rendered;
}

function renderMarkdown(markdown) {
  const output = [];
  let list = false;
  let code = false;
  let codeLines = [];
  let tableRows = null;
  const closeList = () => { if (list) { output.push("</ul>"); list = false; } };
  const closeTable = () => {
    if (!tableRows || tableRows.length === 0) return;
    const rows = tableRows;
    const hasSeparator = rows.length > 1 && rows[1].every((cell) => /^:?-{3,}:?$/.test(cell));
    const header = rows[0];
    const body = rows.slice(hasSeparator ? 2 : 1).filter((row) => row.length > 0);
    output.push(`<div class="table-wrap"><table><thead><tr>${header.map((cell) => `<th scope="col">${renderInline(cell)}</th>`).join("")}</tr></thead>${body.length ? `<tbody>${body.map((row) => `<tr>${row.map((cell) => `<td>${renderInline(cell)}</td>`).join("")}</tr>`).join("")}</tbody>` : ""}</table></div>`);
    tableRows = null;
  };
  const codeBlock = (value) => `<div class="code-block"><div class="code-toolbar"><span>Code example</span><button class="secondary-button copy-code" type="button" data-copy-code="${escapeHtml(value)}">Copy code</button></div><pre><code>${escapeHtml(value)}</code></pre></div>`;
  for (const line of markdown.split(/\r?\n/)) {
    if (line.trim().startsWith("```")) { if (code) { output.push(codeBlock(codeLines.join("\n"))); codeLines = []; } code = !code; closeList(); closeTable(); continue; }
    if (code) { codeLines.push(line); continue; }
    const table = line.match(/^\s*\|(.+)\|\s*$/);
    if (table) { closeList(); if (!tableRows) tableRows = []; tableRows.push(table[1].split("|").map((cell) => cell.trim())); continue; }
    closeTable();
    const bullet = line.match(/^\s*(?:[-*]|\d+\.)\s+(.+)$/);
    if (bullet) { if (!list) { output.push("<ul>"); list = true; } output.push(`<li>${renderInline(bullet[1])}</li>`); }
    else if (line.trim()) { closeList(); output.push(`<p>${renderInline(line.trim())}</p>`); }
    else closeList();
  }
  closeList();
  closeTable();
  if (code) output.push(codeBlock(codeLines.join("\n")));
  return output.join("");
}

function preview(lesson) {
  const explanation = lesson.sections.find((section) => section.title.toLowerCase() === "explanation");
  const text = (explanation?.body || "").replace(/[*`#]/g, "").replace(/\s+/g, " ").trim();
  return text.length > 145 ? `${text.slice(0, 142)}…` : text;
}

function setCacheStatus(message, tone = "ready") {
  $("#cache-status").textContent = message;
  $("#cache-dot").className = `status-dot ${tone === "ready" ? "" : tone}`;
}

function setVisualPackStatus(message) {
  const element = $("#visual-pack-status");
  if (element) element.textContent = message;
}

function visualCacheName(version, digest = "") { return `${VISUAL_CACHE_PREFIX}${version}-${digest || "active"}`; }
function visualStagingCacheName(version, token) { return `${VISUAL_CACHE_PREFIX}staging-${version}-${token}`; }

async function sha256Hex(bytes) {
  const digest = await crypto.subtle.digest("SHA-256", bytes);
  return [...new Uint8Array(digest)].map((value) => value.toString(16).padStart(2, "0")).join("");
}

function visualAssetUrl(asset) {
  if (!asset || typeof asset.path !== "string" || !/^assets\/[a-z0-9-]+\.svg$/.test(asset.path)) return null;
  return `./visuals/visual-foundations/${asset.path.split("/").map(encodeURIComponent).join("/")}`;
}

function validateVisualManifest(manifest) {
  if (!manifest || manifest.schema_version !== 1 || manifest.pack_id !== "visual-foundations" || manifest.pack_kind !== "visuals") throw new Error("Unsupported visual pack manifest.");
  if (typeof manifest.name !== "string" || !manifest.name.trim() || typeof manifest.description !== "string" || !manifest.description.trim() || typeof manifest.created_at !== "string" || !manifest.created_at.trim() || (manifest.expires_at !== null && typeof manifest.expires_at !== "string")) throw new Error("Visual pack descriptive metadata is invalid.");
  if (!/^\d+\.\d+\.\d+(?:-[0-9A-Za-z.-]+)?$/.test(manifest.pack_version || "")) throw new Error("Visual pack version is invalid.");
  if (manifest.revocation_status !== "not-revoked" || manifest.distribution_status !== "unsigned-development" || manifest.signature !== null) throw new Error("Only the unsigned, non-revoked development visual fixture is accepted locally.");
  if (!Array.isArray(manifest.module_ids) || manifest.module_ids.length < 1 || manifest.module_ids.length > 48 || new Set(manifest.module_ids).size !== manifest.module_ids.length) throw new Error("Visual pack module associations are invalid.");
  if (!Array.isArray(manifest.assets) || manifest.assets.length < 1 || manifest.assets.length > 48) throw new Error("Unsupported or incomplete visual pack manifest.");
  const ids = new Set();
  const paths = new Set();
  let installedTotal = 0;
  let compressedTotal = 0;
  for (const asset of manifest.assets) {
    if (!asset || typeof asset.asset_id !== "string" || !/^[a-z0-9]+(?:-[a-z0-9]+)+$/.test(asset.asset_id) || ids.has(asset.asset_id)) throw new Error("Visual asset IDs are invalid or duplicated.");
    if (typeof asset.module_id !== "string" || !manifest.module_ids.includes(asset.module_id) || !state.lessons.some((lesson) => lesson.id === asset.module_id)) throw new Error(`Unknown visual module association: ${asset.module_id || "missing"}.`);
    if (typeof asset.path !== "string" || !/^assets\/[a-z0-9-]+\.svg$/.test(asset.path) || paths.has(asset.path)) throw new Error("Visual asset paths are invalid or duplicated.");
    if (typeof asset.kind !== "string" || !asset.kind.trim() || asset.mime !== "image/svg+xml" || typeof asset.alt_text !== "string" || !asset.alt_text.trim() || typeof asset.caption !== "string" || !asset.caption.trim() || typeof asset.text_equivalent !== "string" || !asset.text_equivalent.trim() || typeof asset.license !== "string" || !asset.license.trim() || typeof asset.attribution !== "string" || !asset.attribution.trim() || typeof asset.author !== "string" || !asset.author.trim() || typeof asset.locale !== "string" || !asset.locale.trim() || typeof asset.reduced_motion_alternative !== "string" || !asset.reduced_motion_alternative.trim()) throw new Error(`Visual metadata is incomplete for ${asset.asset_id}.`);
    if (!asset.practical || asset.practical.mode !== "observe-and-trace" || typeof asset.practical.prompt !== "string" || !asset.practical.prompt.trim() || !Array.isArray(asset.practical.steps) || asset.practical.steps.length < 1 || asset.practical.steps.length > 5 || asset.practical.steps.some((step) => typeof step !== "string" || !step.trim()) || typeof asset.practical.success_criteria !== "string" || !asset.practical.success_criteria.trim()) throw new Error(`Visual practical metadata is incomplete for ${asset.asset_id}.`);
    if (asset.source_url !== null && (typeof asset.source_url !== "string" || !asset.source_url.startsWith("https://"))) throw new Error(`Visual source URL is invalid for ${asset.asset_id}.`);
    if (!Number.isInteger(asset.installed_bytes) || !Number.isInteger(asset.compressed_bytes) || asset.installed_bytes < 0 || asset.compressed_bytes < 0 || asset.installed_bytes > 256 * 1024) throw new Error(`Visual size metadata is invalid for ${asset.asset_id}.`);
    if (typeof asset.sha256 !== "string" || !/^[0-9a-f]{64}$/.test(asset.sha256)) throw new Error(`Visual checksum metadata is invalid for ${asset.asset_id}.`);
    ids.add(asset.asset_id); paths.add(asset.path); installedTotal += asset.installed_bytes; compressedTotal += asset.compressed_bytes;
  }
  if (manifest.installed_bytes !== installedTotal || manifest.compressed_bytes !== compressedTotal || installedTotal > 2 * 1024 * 1024) throw new Error("Visual pack size metadata is invalid.");
  return manifest;
}

async function readVisualPackFromCache(cacheName) {
  if (!cacheName || !("caches" in window)) return null;
  const cache = await caches.open(cacheName);
  const manifestResponse = await cache.match(VISUAL_MANIFEST_URL);
  if (!manifestResponse) return null;
  const manifest = validateVisualManifest(await manifestResponse.json());
  for (const asset of manifest.assets) {
    const url = visualAssetUrl(asset);
    if (!url || !(await cache.match(url))) return null;
  }
  return { cacheName, manifest };
}

async function loadVisualPack() {
  const activeName = localStorage.getItem(VISUAL_ACTIVE_KEY);
  if (!activeName || !activeName.startsWith(VISUAL_CACHE_PREFIX)) {
    state.visualPack = null;
    setVisualPackStatus("Not installed. Core lessons remain complete without optional diagrams.");
    return;
  }
  try {
    state.visualPack = await readVisualPackFromCache(activeName);
    if (!state.visualPack) throw new Error("The visual pack cache is incomplete.");
    setVisualPackStatus(`Installed locally · ${state.visualPack.manifest.assets.length} diagrams available offline.`);
  } catch (error) {
    state.visualPack = null;
    localStorage.removeItem(VISUAL_ACTIVE_KEY);
    setVisualPackStatus(`Visual pack unavailable; core lessons are unaffected. ${error.message}`);
  }
}

async function installVisualPack() {
  if (!("caches" in window) || !window.crypto?.subtle) throw new Error("This browser does not support the visual-pack safety checks.");
  const installButton = $("#install-visual-pack");
  if (installButton) installButton.disabled = true;
  setVisualPackStatus("Validating and installing optional diagrams…");
  let stagingName = null;
  try {
    const manifestResponse = await fetch(VISUAL_MANIFEST_URL, { cache: "no-store" });
    if (!manifestResponse.ok) throw new Error(`Could not load visual manifest (${manifestResponse.status}).`);
    const manifestBytes = new Uint8Array(await manifestResponse.clone().arrayBuffer());
    const manifest = JSON.parse(new TextDecoder().decode(manifestBytes));
    validateVisualManifest(manifest);
    const manifestDigest = await sha256Hex(manifestBytes);
    stagingName = visualStagingCacheName(manifest.pack_version, manifestDigest);

    await caches.delete(stagingName);
    const staging = await caches.open(stagingName);
    await staging.put(VISUAL_MANIFEST_URL, new Response(manifestBytes, { headers: { "Content-Type": "application/json" } }));
    for (const asset of manifest.assets) {
      const url = visualAssetUrl(asset);
      if (!url || asset.mime !== "image/svg+xml" || !asset.module_id || !asset.alt_text || !asset.caption || !asset.text_equivalent || !asset.license || !asset.attribution || !asset.practical?.prompt || !Array.isArray(asset.practical?.steps) || !asset.practical?.success_criteria) throw new Error(`Visual metadata is incomplete for ${asset.asset_id || "unknown asset"}.`);
      const response = await fetch(url, { cache: "no-store" });
      if (!response.ok) throw new Error(`Could not load visual asset ${asset.asset_id}.`);
      const bytes = new Uint8Array(await response.arrayBuffer());
      if (bytes.length !== asset.installed_bytes || bytes.length > 256 * 1024) throw new Error(`Visual asset size is invalid for ${asset.asset_id}.`);
      if (await sha256Hex(bytes) !== asset.sha256) throw new Error(`Visual asset checksum is invalid for ${asset.asset_id}.`);
      const text = new TextDecoder().decode(bytes).toLowerCase();
      if (text.includes("<script") || text.includes("javascript:") || text.includes("onload=") || text.includes("onclick=") || text.includes("<foreignobject")) throw new Error(`Visual asset contains active content: ${asset.asset_id}.`);
      await staging.put(url, new Response(bytes, { headers: { "Content-Type": asset.mime, "Cache-Control": "no-store" } }));
    }
    const activeName = visualCacheName(manifest.pack_version, `${manifestDigest}-${Date.now()}`);
    const active = await caches.open(activeName);
    for (const request of await staging.keys()) {
      const response = await staging.match(request);
      if (response) await active.put(request, response);
    }
    await caches.delete(stagingName);
    const previousName = localStorage.getItem(VISUAL_ACTIVE_KEY);
    localStorage.setItem(VISUAL_ACTIVE_KEY, activeName);
    if (previousName && previousName !== activeName) await caches.delete(previousName);
    await loadVisualPack();
    renderRoute();
  } catch (error) {
    if (stagingName) await caches.delete(stagingName);
    setVisualPackStatus(`Visual pack install failed; any previous pack was kept. ${error.message}`);
    throw error;
  } finally {
    if (installButton) installButton.disabled = false;
  }
}

async function deleteVisualPack() {
  if (!window.confirm("Delete the optional visual diagrams from this browser? Core lessons and learning data will be kept.")) return;
  const activeName = localStorage.getItem(VISUAL_ACTIVE_KEY);
  if (activeName && "caches" in window) await caches.delete(activeName);
  localStorage.removeItem(VISUAL_ACTIVE_KEY);
  state.visualPack = null;
  setVisualPackStatus("Optional visual diagrams deleted. Core lessons and learning data were kept.");
  renderRoute();
}

function prefersReducedMotion() {
  return window.matchMedia?.("(prefers-reduced-motion: reduce)")?.matches === true;
}

function statusPillClass(label) {
  return `pill status-pill pill-${String(label).replace(/\s+/g, "-")}`;
}

function renderOptionalVisuals(lesson) {
  const pack = state.visualPack;
  if (!pack) return "";
  const assets = pack.manifest.assets.filter((asset) => asset.module_id === lesson.id);
  if (!assets.length) return "";
  const reduceMotion = prefersReducedMotion();
  return `<section class="lesson-section visual-pack-section" aria-labelledby="optional-visuals-title"><h2 id="optional-visuals-title">See the idea</h2><p>This diagram sits with the explanation. It is supplementary; the text equivalent remains complete without it.</p>${assets.map((asset) => `<figure class="visual-figure"><figcaption class="visual-caption"><span class="visual-kicker">Diagram</span>${escapeHtml(asset.caption)}</figcaption><div class="visual-frame"><img src="${escapeHtml(visualAssetUrl(asset))}" alt="${escapeHtml(asset.alt_text)}" loading="lazy" /></div>${reduceMotion && asset.reduced_motion_alternative ? `<p class="visual-reduced-motion">${escapeHtml(asset.reduced_motion_alternative)}</p>` : ""}<details class="visual-text-details"${reduceMotion ? " open" : ""}><summary>In words</summary><p class="visual-text-equivalent">${escapeHtml(asset.text_equivalent)}</p></details><details class="visual-practice"><summary>Try it: trace the idea</summary><p>${escapeHtml(asset.practical.prompt)}</p><ol>${asset.practical.steps.map((step) => `<li>${escapeHtml(step)}</li>`).join("")}</ol><p><strong>Self-check:</strong> ${escapeHtml(asset.practical.success_criteria)}</p></details><small>License: ${escapeHtml(asset.license)} · ${escapeHtml(asset.attribution)}</small></figure>`).join("")}</section>`;
}

function renderLessonSection(section) {
  return `<section class="lesson-section" aria-labelledby="section-${cssId(section.title)}"><h2 id="section-${cssId(section.title)}">${escapeHtml(section.title)}</h2><div>${renderMarkdown(section.body)}</div></section>`;
}

function visualAnchorIndex(sections) {
  const match = sections.findIndex((section) => /explanation|concept|introduction/i.test(section.title));
  return match >= 0 ? match : 0;
}

function getRecommendedLesson() {
  const completed = new Set(state.lessons.filter((lesson) => statusLabel(lesson.id) === "completed").map((lesson) => lesson.id));
  const inProgress = state.lessons.find((lesson) => statusLabel(lesson.id) === "in progress");
  if (inProgress) return inProgress;
  const preferred = state.learning.startingLevel === "TRIED_PROGRAMMING"
    ? state.lessons.filter((lesson) => lesson.strand === "python-fundamentals")
    : state.learning.startingLevel
      ? state.lessons.filter((lesson) => lesson.strand === "digital-literacy")
      : state.lessons;
  const candidates = preferred.length ? preferred : state.lessons;
  return candidates.find((lesson) => statusLabel(lesson.id) !== "completed" && (lesson.prerequisites || []).every((prerequisite) => completed.has(prerequisite)))
    || state.lessons.find((lesson) => statusLabel(lesson.id) !== "completed");
}

function recommendationReason(lesson) {
  if (statusLabel(lesson.id) === "in progress") return "You started this lesson already, so it is the easiest place to continue.";
  const completed = new Set(state.lessons.filter((candidate) => statusLabel(candidate.id) === "completed").map((candidate) => candidate.id));
  if (!(lesson.prerequisites || []).length) return "It has no prerequisites, so it is a calm place to start.";
  if ((lesson.prerequisites || []).every((prerequisite) => completed.has(prerequisite))) return "Its prerequisites are complete, so it is a sensible next step.";
  return "It matches your chosen starting point; you can browse or complete prerequisites in any order.";
}

function renderGuidedPath() {
  const startingButton = $("#starting-level-button");
  if (startingButton) startingButton.textContent = state.learning.startingLevel ? "Change starting point" : "Choose starting point";
  const container = $("#guided-path");
  if (!container) return;
  const lesson = getRecommendedLesson();
  if (!lesson) {
    container.innerHTML = `<div class="guided-card"><div><span class="eyebrow">Guided path</span><h3>Core path complete</h3><p>You can revisit any lesson or use Search and Practice for review.</p></div></div>`;
    return;
  }
  const continuing = statusLabel(lesson.id) === "in progress";
  container.innerHTML = `<div class="guided-card ${continuing ? "is-continue" : "is-recommended"}"><div><span class="eyebrow">${continuing ? "Continue" : "Next up"}</span><h3>${escapeHtml(continuing ? "Continue learning" : "Recommended next")}</h3><p class="guided-title">${escapeHtml(lesson.title)}</p><p>${escapeHtml(recommendationReason(lesson))}</p><div class="card-meta"><span class="pill">${escapeHtml(lesson.strand)}</span><span class="pill">${escapeHtml(String(lesson.estimated_minutes))} min</span><span class="${statusPillClass(statusLabel(lesson.id))}">${escapeHtml(statusLabel(lesson.id))}</span><span class="pill">${escapeHtml(state.learning.startingLevel ? "Personalized" : "Open path")}</span></div></div><button class="primary-button purple-button" type="button" data-open-lesson="${escapeHtml(lesson.id)}">${continuing ? "Resume lesson" : "Open lesson"}</button></div>`;
  container.querySelector("[data-open-lesson]")?.addEventListener("click", () => { window.location.hash = `#/lesson/${lesson.id}`; });
}

function renderFilterOptions() {
  const definitions = [
    ["filter-strand", "All strands", (lesson) => lesson.strand],
    ["filter-level", "All levels", (lesson) => lesson.level],
    ["filter-availability", "All availability", (lesson) => lesson.availability],
  ];
  definitions.forEach(([id, placeholder, getValue]) => {
    const select = document.querySelector(`#${id}`);
    if (!select) return;
    const selected = select.value;
    const values = [...new Set(state.lessons.map(getValue).filter(Boolean))];
    select.innerHTML = `<option value="">${placeholder}</option>${values.map((value) => `<option value="${escapeHtml(value)}">${escapeHtml(value)}</option>`).join("")}`;
    if (values.includes(selected)) select.value = selected;
  });
}

function filteredLessons() {
  const strand = $("#filter-strand")?.value || "";
  const level = $("#filter-level")?.value || "";
  const status = $("#filter-status")?.value || "";
  const availability = $("#filter-availability")?.value || "";
  return state.lessons.filter((lesson) => (!strand || lesson.strand === strand) && (!level || lesson.level === level) && (!status || statusLabel(lesson.id) === status) && (!availability || lesson.availability === availability));
}

function renderLessonList() {
  renderFilterOptions();
  const visibleLessons = filteredLessons();
  const filterActive = Boolean($("#filter-strand")?.value || $("#filter-level")?.value || $("#filter-status")?.value || $("#filter-availability")?.value);
  $("#module-count").textContent = `${filterActive ? `${visibleLessons.length} of ` : ""}${state.lessons.length} lessons · ${state.activePack ? "cached core pack" : "not cached for offline use"}`;
  const cards = [];
  let previousStrand = null;
  visibleLessons.forEach((lesson, index) => {
    if (lesson.strand !== previousStrand) {
      previousStrand = lesson.strand;
      cards.push(`<h3 class="strand-heading">${escapeHtml(lesson.strand)}</h3>`);
    }
    const status = statusLabel(lesson.id);
    cards.push(`<article class="lesson-card is-${status.replace(/\s+/g, "-")}">
      <div class="card-top"><span class="lesson-index">${String(index + 1).padStart(2, "0")}</span><span class="${statusPillClass(status)}">${escapeHtml(status)}</span></div>
      <h3>${escapeHtml(lesson.title)}</h3><p>${escapeHtml(preview(lesson))}</p>
      <div class="card-meta"><span class="pill">${escapeHtml(lesson.strand)}</span><span class="pill">${escapeHtml(String(lesson.estimated_minutes))} min</span><span class="pill">${escapeHtml(lesson.availability)}</span><button class="open-card" type="button" data-open-lesson="${escapeHtml(lesson.id)}">Read lesson →</button></div>
    </article>`);
  });
  $("#lesson-list").innerHTML = (cards.length ? cards.join("") : `<aside class="empty-note"><strong>No lessons match these filters.</strong><span>Clear one or more filters to browse the full catalog. Filtering stays in this browser.</span></aside>`);
  document.querySelectorAll("#lesson-list [data-open-lesson]").forEach((button) => button.addEventListener("click", () => { window.location.hash = `#/lesson/${button.dataset.openLesson}`; }));
  renderGuidedPath();
}

function renderReader(lesson) {
  if (statusLabel(lesson.id) === "not started") updateProgress(lesson.id, "in progress");
  const objectives = Array.isArray(lesson.objectives) ? lesson.objectives : [];
  const sectionList = lesson.sections.filter((section) => section.title.toLowerCase() !== "objectives" && section.title.toLowerCase() !== "knowledge check");
  const anchor = visualAnchorIndex(sectionList);
  const beforeExplanation = sectionList.slice(0, anchor + 1).map(renderLessonSection).join("");
  const afterExplanation = sectionList.slice(anchor + 1).map(renderLessonSection).join("");
  const optionalVisuals = renderOptionalVisuals(lesson);
  const questions = lessonQuiz(lesson);
  const quizState = state.learning.quiz[lesson.id] || { attempts: 0, best: 0 };
  const termuxNote = lesson.availability === "termux-optional" ? `<aside class="termux-note"><strong>Termux is Android-only.</strong><span>This browser fallback includes the lesson and offline practice, but not the native terminal handoff.</span></aside>` : "";
  const bookmarkText = state.learning.bookmarks[lesson.id] ? "Bookmarked" : "Bookmark lesson";
  const objectivesHtml = objectives.length ? `<section class="lesson-section objectives-section"><h2>Objectives</h2><ul>${objectives.map((objective) => `<li>${escapeHtml(objective)}</li>`).join("")}</ul></section>` : "";
  const quizHtml = questions.length ? `<section class="lesson-section quiz-section"><div class="section-heading"><div><h2>Knowledge check</h2><p>Answer locally for feedback. Your best result is kept in this browser.</p></div><span class="module-count">${quizState.attempts} attempt${quizState.attempts === 1 ? "" : "s"} · best ${quizState.best}%</span></div><form id="quiz-form">${questions.map((question, index) => `<label class="quiz-question" for="quiz-${index}"><span>${index + 1}. ${escapeHtml(question.question)}</span><input id="quiz-${index}" name="quiz-${index}" autocomplete="off" required /></label>`).join("")}<div class="tool-row"><button class="primary-button purple-button" type="submit">Check answers</button><button class="secondary-button" id="quiz-retry" type="button" hidden>Try again</button></div><div id="quiz-feedback" class="feedback" aria-live="polite"></div><div id="quiz-review" class="quiz-review" aria-live="polite"></div></form></section>` : "";
  const localTools = `<section class="lesson-section local-tools"><div class="section-heading"><div><h2>Your local study tools</h2><p>Notes and bookmarks stay in this browser and are never synced.</p></div><button id="bookmark-toggle" class="secondary-button" type="button">${bookmarkText}</button></div><label class="search-label" for="lesson-note">Private note</label><textarea id="lesson-note" rows="5" placeholder="Write a note about this lesson…">${escapeHtml(state.learning.notes[lesson.id] || "")}</textarea><div class="tool-row"><button id="save-note" class="secondary-button" type="button">Save note</button><button id="complete-lesson" class="primary-button purple-button" type="button">${statusLabel(lesson.id) === "completed" ? "Completed" : "Mark lesson complete"}</button></div><div id="note-status" class="module-count" role="status" aria-live="polite"></div></section>`;
  const header = `<div class="reader-kicker">${escapeHtml(lesson.strand)} · ${escapeHtml(lesson.level)}</div><h1 id="reader-title">${escapeHtml(lesson.title)}</h1><p class="reader-summary">${escapeHtml(preview(lesson))}</p><div class="reader-meta"><span class="pill">${escapeHtml(lesson.availability)}</span><span class="pill">${escapeHtml(String(lesson.estimated_minutes))} minutes</span><span class="pill">${escapeHtml(lesson.risk_tier)}</span><span class="${statusPillClass(statusLabel(lesson.id))}">${escapeHtml(statusLabel(lesson.id))}</span><span class="pill">Completion: explicit learner choice</span></div>`;
  $("#reader-content").innerHTML = [header, objectivesHtml, termuxNote, beforeExplanation, optionalVisuals, afterExplanation, quizHtml, localTools].join("");
  attachReaderEvents(lesson, questions);
}

function attachReaderEvents(lesson, questions) {
  $("#bookmark-toggle")?.addEventListener("click", () => { state.learning.bookmarks[lesson.id] = !state.learning.bookmarks[lesson.id]; persistLearning(); renderReader(lesson); renderProgress(); });
  $("#save-note")?.addEventListener("click", () => { state.learning.notes[lesson.id] = $("#lesson-note").value.trim(); persistLearning(); $("#note-status").textContent = "Saved locally in this browser."; renderProgress(); });
  $("#complete-lesson")?.addEventListener("click", () => { updateProgress(lesson.id, "completed"); renderReader(lesson); });
  $("#quiz-form")?.addEventListener("submit", (event) => {
    event.preventDefault();
    const answers = questions.map((_, index) => $(`#quiz-${index}`).value);
    const correct = questions.reduce((total, question, index) => total + (isAnswerCorrect(answers[index], question) ? 1 : 0), 0);
    const score = Math.round((correct / questions.length) * 100);
    const previous = state.learning.quiz[lesson.id] || { attempts: 0, best: 0 };
    state.learning.quiz[lesson.id] = { attempts: previous.attempts + 1, best: Math.max(previous.best, score), last: score, lastAt: new Date().toISOString() };
    persistLearning();
    const passed = score === 100;
    const feedback = $("#quiz-feedback");
    feedback.className = `feedback ${passed ? "feedback-pass" : "feedback-review"}`;
    feedback.innerHTML = `<strong>${passed ? "All correct" : "Review and retry"} — ${correct} of ${questions.length} (${score}%).</strong> ${passed ? "Excellent work." : "Read the notes below, then try again."}<br /><span>Best result is stored locally; quiz score does not block lesson completion.</span>`;
    $("#quiz-review").innerHTML = questions.map((question, index) => {
      const ok = isAnswerCorrect(answers[index], question);
      return `<p class="${ok ? "quiz-item-pass" : "quiz-item-review"}"><strong>${index + 1}. ${ok ? "Correct" : "Review this one"}</strong>${ok ? "" : ` Expected key answer: ${escapeHtml(question.answer)}.`} ${escapeHtml(question.explanation || "Use the lesson explanation to check your reasoning.")}</p>`;
    }).join("");
    const retry = $("#quiz-retry");
    if (retry) retry.hidden = false;
    renderProgress();
  });
  $("#quiz-retry")?.addEventListener("click", () => {
    questions.forEach((_, index) => { const input = $(`#quiz-${index}`); if (input) input.value = ""; });
    const feedback = $("#quiz-feedback");
    if (feedback) { feedback.className = "feedback"; feedback.innerHTML = ""; }
    const review = $("#quiz-review");
    if (review) review.innerHTML = "";
    const retry = $("#quiz-retry");
    if (retry) retry.hidden = true;
    $(`#quiz-0`)?.focus();
  });
  document.querySelectorAll("[data-copy-code]").forEach((button) => button.addEventListener("click", async () => { const value = button.dataset.copyCode || ""; try { await navigator.clipboard.writeText(value); button.textContent = "Copied"; } catch { button.textContent = "Copy unavailable"; } }));
}

function isAnswerCorrect(answer, question) {
  const actual = normalizeText(answer);
  if (!actual) return false;
  return (question.acceptedAnswers || [question.answer]).some((expected) => normalizeText(expected) === actual);
}

function normalizeText(value) { return String(value).toLowerCase().replace(/[^a-z0-9]+/g, " ").trim(); }
function cssId(value) { return value.toLowerCase().replace(/[^a-z0-9]+/g, "-").replace(/(^-|-$)/g, ""); }
function displayState(value) { return value === "in progress" ? "In progress" : value.replace(/^./, (letter) => letter.toUpperCase()); }

function renderPractice() {
  if (!state.lessons.length) {
    $("#practice-list").innerHTML = `<aside class="empty-note"><strong>No practice is loaded yet.</strong><span>Cache the core lessons from Learn. Practice stays in this browser and never requires an account.</span></aside>`;
    return;
  }
  const noneStarted = state.lessons.every((lesson) => statusLabel(lesson.id) === "not started");
  const intro = noneStarted ? `<aside class="empty-note"><strong>No checks attempted yet.</strong><span>Open any lesson to try its knowledge check. Results stay in this browser and do not block completion.</span></aside>` : "";
  const corePractice = state.lessons.map((lesson) => {
    const section = lesson.sections.find((candidate) => candidate.title.toLowerCase() === "offline practice");
    const questionCount = lessonQuiz(lesson).length;
    const status = statusLabel(lesson.id);
    return `<article class="practice-card is-${status.replace(/\s+/g, "-")}"><div><span class="lesson-index">${escapeHtml(lesson.id)}</span><h3>${escapeHtml(lesson.title)}</h3><div>${renderMarkdown(section?.body || "Practice content is included in the reader.")}</div><div class="card-meta"><span class="${statusPillClass(status)}">${escapeHtml(status)}</span><span class="pill">short-answer-v1</span><span class="pill">${questionCount} checks</span><span class="pill">${escapeHtml(String(lesson.estimated_minutes))} min</span></div></div><button class="secondary-button" type="button" data-open-lesson="${escapeHtml(lesson.id)}">Open lesson</button></article>`;
  }).join("");
  const visualPractice = state.visualPack ? `<section class="visual-practice-index" aria-labelledby="visual-practice-title"><div class="section-heading"><div><p class="eyebrow">Optional visual practice</p><h3 id="visual-practice-title">Trace the idea</h3></div><span class="module-count">${state.visualPack.manifest.assets.length} local activities</span></div>${state.visualPack.manifest.assets.map((asset) => { const lesson = state.lessons.find((candidate) => candidate.id === asset.module_id); return `<article class="practice-card visual-practice-card"><div><span class="lesson-index">${escapeHtml(asset.module_id)}</span><h3>${escapeHtml(lesson?.title || asset.module_id)}</h3><p>${escapeHtml(asset.practical.prompt)}</p><div class="card-meta"><span class="pill">visual trace</span><span class="pill">${asset.practical.steps.length} steps</span><span class="pill">optional</span></div></div><button class="secondary-button" type="button" data-open-lesson="${escapeHtml(asset.module_id)}">Open visual aid</button></article>`; }).join("")}</section>` : `<aside class="empty-note visual-practice-empty"><strong>Optional visual practice is not installed.</strong><span>Install it from About when you want diagrams and trace prompts. Core practice remains available here.</span></aside>`;
  $("#practice-list").innerHTML = intro + corePractice + visualPractice;
  document.querySelectorAll("#practice-list [data-open-lesson]").forEach((button) => button.addEventListener("click", () => { window.location.hash = `#/lesson/${button.dataset.openLesson}`; }));
}

function renderSearchResults(query = "") {
  const normalized = normalizeText(query);
  const matches = normalized ? state.lessons.filter((lesson) => lesson.searchText.includes(normalized)) : [];
  $("#search-count").textContent = normalized ? `${matches.length} match${matches.length === 1 ? "" : "es"}` : "";
  $("#search-results").innerHTML = normalized ? (matches.length ? matches.map((lesson) => `<article class="search-result"><span class="lesson-index">${escapeHtml(lesson.strand)}</span><h3>${escapeHtml(lesson.title)}</h3><p>${escapeHtml(preview(lesson))}</p><button class="open-card" type="button" data-open-lesson="${escapeHtml(lesson.id)}">Read lesson →</button></article>`).join("") : `<aside class="empty-note"><strong>No lessons match “${escapeHtml(query)}”.</strong><span>Try a broader term. Search only uses the cached lessons in this browser.</span></aside>`) : `<aside class="empty-note"><strong>Search stays in this browser.</strong><span>Type a word to look through cached lesson titles and bodies. Nothing is sent to a server.</span></aside>`;
  document.querySelectorAll("#search-results [data-open-lesson]").forEach((button) => button.addEventListener("click", () => { window.location.hash = `#/lesson/${button.dataset.openLesson}`; }));
}

function renderProgress() {
  if (!$("#progress-list")) return;
  const completed = state.lessons.filter((lesson) => statusLabel(lesson.id) === "completed").length;
  const inProgress = state.lessons.filter((lesson) => statusLabel(lesson.id) === "in progress").length;
  const remaining = Math.max(0, state.lessons.length - completed);
  $("#progress-summary").innerHTML = `<div class="stat stat-completed"><strong>${completed}</strong><span>completed</span></div><div class="stat stat-in-progress"><strong>${inProgress}</strong><span>in progress</span></div><div class="stat stat-remaining"><strong>${remaining}</strong><span>remaining</span></div>`;
  const weekAgo = Date.now() - 7 * 24 * 60 * 60 * 1000;
  const weekly = Object.values(state.learning.quiz).filter((item) => item.lastAt && Date.parse(item.lastAt) >= weekAgo).length;
  const openGoals = (state.learning.goals || []).filter((goal) => !goal.done).length;
  const dashboard = $("#local-dashboard");
  if (dashboard) dashboard.innerHTML = `<strong>Local dashboard.</strong><span>${weekly} knowledge-check update${weekly === 1 ? "" : "s"} in the last 7 days · ${openGoals} open goal${openGoals === 1 ? "" : "s"}. This stays in this browser.</span>`;
  const goalList = $("#goal-list");
  if (goalList) {
    const goals = state.learning.goals || [];
    goalList.innerHTML = goals.length ? goals.map((goal, index) => `<article class="goal-card"><input type="checkbox" data-goal-toggle="${index}" ${goal.done ? "checked" : ""} /><div><strong>${escapeHtml(goal.title)}</strong>${goal.lessonId ? `<p><button class="secondary-button" type="button" data-open-lesson="${escapeHtml(goal.lessonId)}">Open linked lesson</button></p>` : ""}<button class="secondary-button" type="button" data-goal-delete="${index}">Delete</button></div></article>`).join("") : `<aside class="empty-note"><strong>No goals yet.</strong><span>Add a private task above. It never leaves this browser unless you export it.</span></aside>`;
    goalList.querySelectorAll("[data-goal-toggle]").forEach((input) => input.addEventListener("change", () => {
      const index = Number(input.dataset.goalToggle);
      if (state.learning.goals[index]) { state.learning.goals[index].done = input.checked; persistLearning(); renderProgress(); }
    }));
    goalList.querySelectorAll("[data-goal-delete]").forEach((button) => button.addEventListener("click", () => {
      const index = Number(button.dataset.goalDelete);
      state.learning.goals.splice(index, 1);
      persistLearning();
      renderProgress();
    }));
    goalList.querySelectorAll("[data-open-lesson]").forEach((button) => button.addEventListener("click", () => { window.location.hash = `#/lesson/${button.dataset.openLesson}`; }));
  }
  const empty = completed === 0 && inProgress === 0 ? `<aside class="empty-note"><strong>No progress yet.</strong><span>Open a lesson when you are ready. Completion, bookmarks, notes, and goals stay in this browser and are never synced.</span></aside>` : "";
  $("#progress-list").innerHTML = empty + state.lessons.map((lesson) => {
    const status = statusLabel(lesson.id);
    return `<article class="progress-card is-${status.replace(/\s+/g, "-")}"><div><span class="lesson-index">${escapeHtml(lesson.id)}</span><h3>${escapeHtml(lesson.title)}</h3><p><span class="${statusPillClass(status)}">${escapeHtml(displayState(status))}</span>${state.learning.bookmarks[lesson.id] ? ' <span class="pill">bookmarked</span>' : ""}${state.learning.notes[lesson.id] ? ' <span class="pill">note saved</span>' : ""}</p></div><button class="secondary-button" type="button" data-open-lesson="${escapeHtml(lesson.id)}">Open</button></article>`;
  }).join("");
  document.querySelectorAll("#progress-list [data-open-lesson]").forEach((button) => button.addEventListener("click", () => { window.location.hash = `#/lesson/${button.dataset.openLesson}`; }));
}

function readRoute() {
  const hash = window.location.hash || "#/learn";
  const match = hash.match(/^#\/lesson\/([^/]+)$/);
  if (match) return { type: "lesson", id: decodeURIComponent(match[1]) };
  if (["practice", "search", "progress", "about"].includes(hash.slice(2))) return { type: hash.slice(2) };
  return { type: "learn" };
}

function focusRouteHeading(route) {
  const heading = route.type === "lesson" ? $("#reader-title") : document.querySelector(`[data-section="${route.type}"] h2, #about-view h2`);
  if (!heading) return;
  heading.tabIndex = -1;
  heading.focus({ preventScroll: true });
}

function renderRoute() {
  const route = readRoute();
  const hero = $(".shell-grid");
  document.querySelectorAll("[data-section]").forEach((section) => { section.hidden = section.dataset.section !== route.type; });
  $("#reader-view").hidden = route.type !== "lesson";
  $("#about-view").hidden = route.type !== "about";
  hero.hidden = route.type !== "learn";
  document.querySelectorAll("[data-route]").forEach((link) => link.setAttribute("aria-current", link.dataset.route === route.type ? "page" : "false"));
  if (route.type === "lesson") {
    const lesson = state.lessons.find((candidate) => candidate.id === route.id);
    if (lesson) renderReader(lesson); else $("#reader-content").innerHTML = "<p>That lesson is not available in the cached core pack.</p>";
    window.scrollTo({ top: 0, behavior: "smooth" });
  } else if (route.type === "practice") renderPractice();
  else if (route.type === "search") renderSearchResults($("#search-input")?.value || "");
  else if (route.type === "progress") renderProgress();
}

function saveStartingLevel(value) {
  const valid = ["NEW_TO_COMPUTING", "DIGITAL_BASICS", "TRIED_PROGRAMMING"].includes(value) ? value : null;
  state.learning.startingLevel = valid;
  state.learning.startingLevelDismissed = true;
  persistLearning();
  renderLessonList();
}

function openStartingLevelDialog() {
  const dialog = $("#starting-level-dialog");
  if (!dialog) return;
  const selected = state.learning.startingLevel;
  dialog.querySelectorAll("input[name=starting-level]").forEach((input) => { input.checked = input.value === selected; });
  if (!dialog.open) dialog.showModal();
}

function setupStartingLevel() {
  const dialog = $("#starting-level-dialog");
  const form = $("#starting-level-form");
  $("#starting-level-button")?.addEventListener("click", openStartingLevelDialog);
  form?.addEventListener("submit", (event) => {
    event.preventDefault();
    const submitter = event.submitter;
    if (submitter?.id === "save-starting-level") {
      saveStartingLevel(form.querySelector("input[name=starting-level]:checked")?.value || null);
    } else {
      state.learning.startingLevel = null;
      state.learning.startingLevelDismissed = true;
      persistLearning();
      renderLessonList();
    }
    dialog?.close();
  });
}

function setupCatalogFilters() {
  ["filter-strand", "filter-level", "filter-status", "filter-availability"].forEach((id) => { document.querySelector(`#${id}`)?.addEventListener("change", renderLessonList); });
  $("#clear-filters")?.addEventListener("click", () => {
    ["filter-strand", "filter-level", "filter-status", "filter-availability"].forEach((id) => { const select = document.querySelector(`#${id}`); if (select) select.value = ""; });
    renderLessonList();
  });
}

function downloadLearningExport() {
  const payload = {
    format: "aetherlearn-web-learning-export",
    format_version: 1,
    exported_at: new Date().toISOString(),
    contains_personal_notes: Object.keys(state.learning.notes).length > 0,
    warning: "Plain JSON export; not an encrypted backup.",
    state: normalizeLearningState(state.learning),
  };
  const blob = new Blob([JSON.stringify(payload, null, 2)], { type: "application/json" });
  const link = document.createElement("a");
  link.href = URL.createObjectURL(blob);
  link.download = "aetherlearn-web-learning.json";
  link.click();
  URL.revokeObjectURL(link.href);
  $("#browser-data-status").textContent = "Learning data export created. Store it carefully; it may contain private notes.";
}

function validateImportedLearningState(payload) {
  if (!payload || payload.format !== "aetherlearn-web-learning-export" || payload.format_version !== 1 || !payload.state || typeof payload.state !== "object") throw new Error("Unsupported learning export format.");
  const candidate = normalizeLearningState(payload.state);
  const knownIds = new Set(state.lessons.map((lesson) => lesson.id));
  const assertKnownIds = (record, label) => {
    if (Object.keys(record).some((id) => !knownIds.has(id))) throw new Error(`${label} contains an unknown lesson ID.`);
  };
  assertKnownIds(candidate.progress, "Progress");
  assertKnownIds(candidate.notes, "Notes");
  assertKnownIds(candidate.bookmarks, "Bookmarks");
  assertKnownIds(candidate.quiz, "Quiz history");
  if (Object.values(candidate.progress).some((value) => !["not started", "in progress", "completed"].includes(value))) throw new Error("Progress contains an unsupported state.");
  if (Object.values(candidate.notes).some((value) => typeof value !== "string" || value.length > 20000)) throw new Error("A note is invalid or too large.");
  if (Object.values(candidate.bookmarks).some((value) => value !== true && value !== false)) throw new Error("Bookmarks contain an invalid value.");
  if (Object.values(candidate.quiz).some((value) => !value || !Number.isInteger(value.attempts) || value.attempts < 0 || !Number.isFinite(value.best) || value.best < 0 || value.best > 100 || (value.last !== undefined && (!Number.isFinite(value.last) || value.last < 0 || value.last > 100)))) throw new Error("Quiz history contains an invalid result.");
  candidate.goals.forEach((goal) => {
    if (goal.lessonId && !knownIds.has(goal.lessonId)) throw new Error("A goal links an unknown lesson ID.");
  });
  return candidate;
}

async function importLearningData(file) {
  if (!file) return;
  if (file.size > 1024 * 1024) throw new Error("The learning export is larger than the 1 MB safety limit.");
  const payload = JSON.parse(await file.text());
  const candidate = validateImportedLearningState(payload);
  if (!window.confirm("Replace this browser’s learning data with the validated import? Export the current data first if you may need to restore it.")) return;
  await writeLearningState(candidate);
  state.learning = candidate;
  renderLessonList(); renderProgress(); renderRoute();
  $("#browser-data-status").textContent = "Learning data imported and saved locally. Cached content was unchanged.";
}

async function clearBrowserCache() {
  if (!window.confirm("Clear the cached lesson pack from this browser? Learning data will be kept.")) return;
  try {
    await clearActivePack();
    state.activePack = null;
    const pack = await fetchNetworkPack();
    state.lessons = lessonsFromPack(pack);
    renderLessonList(); renderPractice(); renderProgress(); renderRoute();
    $("#browser-data-status").textContent = "Cached content cleared; the shared source is visible until you cache it again.";
    setCacheStatus("Core lessons loaded. Cache them for offline use.", "warning");
  } catch (error) {
    $("#browser-data-status").textContent = `Could not clear cached content safely: ${error.message}`;
  }
}

async function clearBrowserLearning() {
  if (!window.confirm("Delete all browser-local progress, notes, bookmarks, and quiz attempts? This cannot be undone from this client.")) return;
  await clearLearningState();
  state.learning = normalizeLearningState(null);
  renderLessonList(); renderProgress(); renderGuidedPath(); renderRoute();
  $("#browser-data-status").textContent = "Browser-local learning data deleted. Cached content was kept.";
  window.setTimeout(openStartingLevelDialog, 0);
}

function setupTheme() {
  const saved = localStorage.getItem("aetherlearn-web-theme");
  if (saved === "dark") document.documentElement.dataset.theme = "dark";
  const reading = localStorage.getItem("aetherlearn-web-reading-theme") || "default";
  document.documentElement.dataset.reading = reading;
  const select = $("#reading-theme");
  if (select) select.value = reading;
  $("#theme-toggle").addEventListener("click", () => {
    const dark = document.documentElement.dataset.theme === "dark";
    if (dark) delete document.documentElement.dataset.theme;
    else document.documentElement.dataset.theme = "dark";
    localStorage.setItem("aetherlearn-web-theme", dark ? "light" : "dark");
  });
  select?.addEventListener("change", () => {
    const value = select.value || "default";
    document.documentElement.dataset.reading = value;
    localStorage.setItem("aetherlearn-web-reading-theme", value);
  });
}

async function cacheCorePack() {
  const button = $("#cache-button");
  button.disabled = true;
  setCacheStatus("Updating the local core cache…", "updating");
  try {
    const pack = await fetchNetworkPack();
    await replaceActivePack(pack);
    state.activePack = pack;
    state.lessons = lessonsFromPack(pack);
    renderLessonList(); renderPractice(); renderProgress(); renderRoute();
    setCacheStatus(`Cached ${state.lessons.length} core lessons in this browser.`, "ready");
  } catch (error) {
    console.error(error);
    setCacheStatus(`Cache update failed; previous cache kept. ${error.message}`, "warning");
  } finally { button.disabled = false; }
}

async function loadInitialContent() {
  const cached = await readActivePack();
  if (cached?.entries?.length > 0) {
    state.activePack = cached;
    state.lessons = lessonsFromPack(cached);
    setCacheStatus(`Cached ${state.lessons.length} core lessons available offline.`, "ready");
    return;
  }
  try {
    const pack = await fetchNetworkPack();
    state.lessons = lessonsFromPack(pack);
    setCacheStatus("Core lessons loaded. Cache them for offline use.", "warning");
  } catch (error) {
    throw new Error(`No cached core pack is available. ${error.message}`);
  }
}

async function start() {
  setupTheme();
  setupStartingLevel();
  setupCatalogFilters();
  $("#cache-button").addEventListener("click", cacheCorePack);
  $("#export-browser-learning")?.addEventListener("click", downloadLearningExport);
  $("#import-browser-learning")?.addEventListener("click", () => $("#import-learning-file")?.click());
  $("#import-learning-file")?.addEventListener("change", (event) => { importLearningData(event.target.files?.[0]).catch((error) => { $("#browser-data-status").textContent = `Import rejected: ${error.message}`; }).finally(() => { event.target.value = ""; }); });
  $("#clear-browser-cache")?.addEventListener("click", clearBrowserCache);
  $("#clear-browser-learning")?.addEventListener("click", () => { clearBrowserLearning().catch((error) => { $("#browser-data-status").textContent = `Could not delete browser data: ${error.message}`; }); });
  $("#install-visual-pack")?.addEventListener("click", () => { installVisualPack().catch(() => {}); });
  $("#delete-visual-pack")?.addEventListener("click", () => { deleteVisualPack().catch((error) => { setVisualPackStatus(`Could not delete visual pack: ${error.message}`); }); });
  $("#reader-back")?.addEventListener("click", () => { window.location.hash = "#/learn"; });
  $("#search-input")?.addEventListener("input", (event) => renderSearchResults(event.target.value));
  $("#goal-form")?.addEventListener("submit", (event) => {
    event.preventDefault();
    const input = $("#goal-title");
    const title = input?.value.trim();
    if (!title) return;
    state.learning.goals = state.learning.goals || [];
    state.learning.goals.unshift({ title, lessonId: null, done: false, createdAt: new Date().toISOString() });
    persistLearning();
    input.value = "";
    renderProgress();
  });
  window.addEventListener("hashchange", () => { renderRoute(); requestAnimationFrame(() => focusRouteHeading(readRoute())); });
  if ("serviceWorker" in navigator) navigator.serviceWorker.register("./sw.js").catch((error) => console.warn("Service worker unavailable", error));
  try {
    state.learning = normalizeLearningState(await readLearningState());
    await loadInitialContent();
    await loadVisualPack();
    renderLessonList(); renderPractice(); renderProgress(); renderSearchResults(); renderRoute();
    if (!state.learning.startingLevelDismissed) window.setTimeout(openStartingLevelDialog, 0);
  } catch (error) {
    console.error(error);
    setCacheStatus("Core content unavailable.", "warning");
    $("#lesson-list").innerHTML = `<div class="loading-card"><strong>Content unavailable.</strong><span>${escapeHtml(error.message)}</span></div>`;
  }
}

start();
