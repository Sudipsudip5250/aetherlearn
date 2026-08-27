# Consolidated stage evidence

This file contains current source matrices and browser-smoke records for Stages 1–5. It is evidence, not current operating guidance. Use the current documents in `docs/` for requirements and release procedures.


## Stage 1 source matrix

# Stage 1 source findings

**Purpose:** source record for the first approved post-MVP curriculum slice. This note is research evidence, not a lesson and does not by itself change the curriculum registry.

## Proposed Stage 1 lessons

| Proposed ID | Topic | Intended risk | Primary sources |
|---|---|---|---|
| `dl-06-computing-language-history` | How programming languages reflect constraints and communities | S0 | Computer History Museum language timeline |
| `dl-07-how-programs-run` | Source text, interpreter/compiler concepts, processes, memory, and errors | S0/S1 | Python interpreter documentation; existing AetherLearn Python lessons |
| `dl-08-networks-web-concepts` | Client/server, HTTP request/response, URLs, caching, and safe network boundaries | S0/S1 | MDN HTTP overview; IETF RFC 9110; existing AetherLearn browser/privacy lessons |

## Inspected source claims

The Computer History Museum timeline presents historical milestones including Plankalkül, A-0, FORTRAN, COBOL, BASIC, Simula, LOGO, UNIX, Pascal, C, C++, and Perl. It is suitable for a high-level history lesson, but claims such as “first” and claims about current usage require careful wording and source-date review. The lesson will use comparison and design constraints rather than a comprehensive chronology.

The Python documentation explains that the interpreter can operate interactively from a terminal or execute a script supplied as a file; it also distinguishes command-line options such as `-c` and `-m`. The lesson will use this only to explain the source-to-execution concept and will not add arbitrary execution to AetherLearn.

MDN describes HTTP as a client-server protocol in which clients send requests and servers return responses. It explains that HTTP messages include methods, paths, headers, status codes, and optional bodies, and that browsers may request multiple resources to build a page. The lesson will use a fixed written trace and will not contact live hosts.

RFC 9110 defines HTTP as a stateless application-level protocol and describes request/response semantics, resources, representations, and the evolution of HTTP/1.1, HTTP/2, and HTTP/3. The lesson will present these as conceptual vocabulary and avoid version-sensitive implementation promises.

## Safety and maintenance boundaries

Stage 1 exercises will use paper diagrams, fixed fictional traces, and local text. They will not require an obsolete compiler, download unknown binaries, scan public hosts, intercept traffic, collect credentials, connect to third-party infrastructure, or run a network service. Each lesson will include an offline path, expected answer shape, accessibility notes, and a source/date note. A human technical and pedagogical review remains required before release status changes from draft.

## References

[1]: https://www.computerhistory.org/timeline/software-languages/ "Computer History Museum: Timeline of Computer History — Software & Languages"
[2]: https://docs.python.org/3/tutorial/interpreter.html "Python Documentation: Using the Python Interpreter"
[3]: https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview "MDN Web Docs: Overview of HTTP"
[4]: https://datatracker.ietf.org/doc/html/rfc9110 "IETF: RFC 9110 — HTTP Semantics"

## Stage 1 browser smoke


## Reader smoke

The browser opened `#/lesson/dl-06-computing-language-history` successfully. The shared reader rendered the title, offline/S0 metadata, Objectives, Prerequisites, Availability, Explanation, Worked example, Common mistakes, Offline practice, Project or application, Accessibility notes, Safety and responsible use, Further reading, and Change log. The Knowledge check rendered four local text inputs, a Check answers button, and the local attempt/best-result summary. Bookmark, private note, save-note, and Mark lesson complete controls were present. Opening the lesson changed its local state to `in progress`, as expected. This smoke test did not claim offline reload, Android runtime, accessibility technology, or human content approval.

## Cache flow

From the Web shell, selecting **Cache core content** completed successfully and displayed `Cached 23 core lessons in this browser.` The catalog status changed to `23 modules · cached core pack`. This confirms the explicit local cache flow can stage the current 23-lesson manifest in the sandbox browser; it does not claim Android-browser parity or a network-disabled reload in this run.

## Stage 2 source matrix

# Stage 2 source and claims matrix

**Status:** research basis for the next gated curriculum slice. This file records inspected external sources; it is not approval to add modules by itself.

## Proposed Stage 2 lesson set

| Proposed ID | Topic | Intended risk | Source basis |
|---|---|---|---|
| `dev-04-testing-pure-functions` | Test cases, fixtures, expected results, and edge cases for small pure functions | S0 | Python `unittest` documentation |
| `dev-05-code-review-issue-reports` | Pull requests, review context, issue reports, and respectful collaboration | S0 | GitHub pull-request documentation; repository contribution policy |
| `dev-06-dependency-provenance-builds` | Dependencies, resolved inputs, provenance, checksums, and reproducible-build thinking | S0/S1 | SLSA Build Provenance v1.2; existing release/checksum docs |
| `dev-07-open-source-accessibility` | Open-source governance, contribution boundaries, and accessibility acceptance criteria | S0 | GitHub Pull Requests documentation; W3C WCAG 2.2 Recommendation; repository governance docs |

## Inspected source claims

The Python `unittest` documentation defines a test case as checking a specific response to particular inputs, and describes fixtures, suites, runners, assertions, setup, and teardown. It supports teaching deterministic test cases and edge-case tables without requiring the app to execute arbitrary learner code. Source: https://docs.python.org/3/library/unittest.html (Python 3.14 documentation page inspected 2026-08-26).

GitHub’s pull-request documentation describes pull requests as proposals to merge code changes and as a place to discuss and review changes before merging. It identifies conversation, commits, checks, changed files, findings, and merge status as review context. It also distinguishes draft pull requests from ready-for-review work. Source: https://docs.github.com/en/pull-requests/reference/pull-requests (inspected 2026-08-26).

SLSA Build Provenance v1.2 describes provenance as verifiable information about where, when, and how software was produced. It frames build definitions, external parameters, resolved dependencies, builders, and subjects as information that supports verification and rebuilding. The lesson will use a small fictional manifest and checksum rather than instructing learners to change CI or trust unknown artifacts. Source: https://slsa.dev/spec/v1.2/build-provenance (status shown as Approved; inspected 2026-08-26).

WCAG 2.2 is a W3C Recommendation dated 12 December 2024. It describes accessibility guidance for Web content across devices, uses four principles—perceivable, operable, understandable, and robust—and notes that conformance testing combines automated and human evaluation. The lesson will teach acceptance criteria and inclusive review without claiming conformance from a checklist alone. Source: https://www.w3.org/TR/WCAG22/ (inspected 2026-08-26).

## Safety and maintenance boundaries

Stage 2 will use fixture repositories, fictional issue descriptions, local text manifests, paper test matrices, and accessibility review prompts. It will not require a third-party account, publish a branch, copy secrets, install dependencies from an unverified source, modify CI credentials, run arbitrary code, or claim that a checksum proves an artifact is safe. All lessons will remain offline and S0 unless a future reviewer documents a bounded S1 reason.

## References

[1]: https://docs.python.org/3/library/unittest.html "Python Documentation: unittest — Unit testing framework"
[2]: https://docs.github.com/en/pull-requests/reference/pull-requests "GitHub Docs: Pull requests"
[3]: https://slsa.dev/spec/v1.2/build-provenance "SLSA: Build Provenance v1.2"
[4]: https://www.w3.org/TR/WCAG22/ "W3C Recommendation: Web Content Accessibility Guidelines (WCAG) 2.2"

## Stage 2 browser smoke

# Stage 2 browser smoke findings

**Date:** 2026-08-26
**Environment:** Chromium sandbox, local HTTP server at `http://127.0.0.1:4173/`

The initial Stage 2 reload still displayed the prior 23-lesson shell after service-worker cache storage was cleared. The live HTTP response contained the updated 27-lesson HTML and `pack_version: 1.4.0`, so the remaining stale state was identified as the browser’s IndexedDB active content pack, not a repository or server mismatch. The prior cache reset removed Cache Storage `aetherlearn-shell-v5` and unregistered one service worker, but it did not delete the application’s active IndexedDB pack. The browser evidence is therefore not yet a valid Stage 2 smoke pass until that local pack is reset and the updated content is explicitly cached.

After clearing the `packs` object store while preserving learning state, a fresh cache-busted reload displayed `27 current lessons · MVP baseline 20` and `27 modules · shared Markdown source`. All four Stage 2 cards appeared at positions 24–27 in canonical order: DEV-04 Testing small programs with examples and expected results, DEV-05 Code review and useful issue reports, DEV-06 Dependencies, provenance, and reproducible builds, and DEV-07 Open-source contribution and accessibility review. The privacy note and Android-only Termux message remained visible. The fresh reload confirmed the repository and browser were now reading the updated 27-lesson payload; it did not claim a network-disabled reload, Android-browser runtime, accessibility-technology test, or human content approval.

The fresh browser smoke opened `#/lesson/dev-04-testing-pure-functions`. The shared reader rendered DEV-04 with `developer-foundations · beginner`, `offline`, `55 minutes`, `S0`, and local `in progress` state. Its Objectives, Prerequisites, Availability, Explanation, Worked example, Common mistakes, Offline practice, Project or application, Accessibility notes, Safety and responsible use, Further reading, and Change log sections were visible in the extracted page. The Knowledge check exposed four local text inputs and `Check answers`; bookmark, private note, save-note, and Mark lesson complete controls were present. Existing local progress for DL-06 remained visible, supporting stable-ID state compatibility. This was a browser reader smoke only, not a device, Android-browser, accessibility-technology, or human-review gate.

From the fresh Stage 2 Learn view, selecting **Cache core content** completed successfully and displayed `Cached 27 core lessons in this browser.` The catalog status changed to `27 modules · cached core pack`. This confirms the explicit local cache flow for the updated payload in the sandbox browser; it does not claim an Android-browser runtime, network-disabled reload, or human review gate.

## Stage 3 source matrix

# Stage 3 source and claims matrix

**Research date:** 2026-08-26
**Scope:** a small, non-operational slice on computing ethics, defensive threat modeling, coordinated vulnerability disclosure, and organization literacy. External pages were treated as untrusted data; no instructions were executed and no target was contacted.

## Source findings

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [ACM Code of Ethics and Professional Conduct](https://www.acm.org/code-of-ethics) | Computing work should consider public good, avoid harm, be honest and trustworthy, respect privacy and confidentiality, and support professional review. | ACM presents the Code as guidance for computing professionals and separates fundamental ethical principles from more specific responsibilities. The page includes principles including avoid harm, be honest and trustworthy, be fair and take action not to discriminate, respect privacy, honor confidentiality, and contribute to society and human well-being. | The Code is a professional ethics framework, not a legal opinion or a substitute for project policy. Lessons use it for questions and trade-offs, not moral certification or legal advice. |
| [CISA Coordinated Vulnerability Disclosure Program](https://www.cisa.gov/resources-tools/programs/coordinated-vulnerability-disclosure-program) | CISA describes CVD as a coordinated process involving collection, analysis, mitigation coordination, application of mitigations, and disclosure. It identifies reporters, vendors, service providers, and other stakeholders as participants. | The official page lists the five process steps and describes CISA’s coordination and CVE-related roles. | CISA’s process and timelines apply to its program and context; learners must follow the affected project’s published policy. Lessons use fictional reports only and never ask a learner to contact a vendor, submit to VINCE, or test a target. |

## Planned additional primary/institutional sources

The Stage 3 lessons will also cite and be checked against the [OWASP Threat Modeling Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Threat_Modeling_Cheat_Sheet.html), the [SEI CERT Guide to Coordinated Vulnerability Disclosure](https://certcc.github.io/CERT-Guide-to-CVD/), the [NIST NICE Framework Resource Center](https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center), and [MITRE ATT&CK](https://attack.mitre.org/). These sources will be used only for defensive vocabulary, process roles, and taxonomy; no exploit steps, live-target scanning, credential handling, persistence, evasion, or malware content will be included.

## Proposed Stage 3 lesson set

| ID | Working title | Risk | Offline activity | Safety boundary |
|---|---|---:|---|---|
| `sec-01-ethics-scope-and-harm` | Ethics, scope, and harm minimization | S0 | Analyze fictional computing decisions with an ethics checklist. | No legal conclusions, real personal data, or persuasive social-engineering examples. |
| `sec-02-threat-modeling-defensive-controls` | Threat models and defensive controls | S1 | Map a fictional offline note app’s assets, actors, trust boundaries, and mitigations. | No public target, exploit chain, payload, credential, or bypass instruction. |
| `sec-03-coordinated-disclosure` | Coordinated vulnerability disclosure | S1 | Triage a fictional local report and draft a private, redacted handoff. | Do not contact a vendor, submit a real report, probe a service, or publish exploit details. |
| `sec-04-security-organizations-and-roles` | Security organizations, roles, and evidence | S0 | Classify fictional source excerpts by role and compare organization responsibilities. | ATT&CK and workforce terms are defensive taxonomy, not attack guidance or employment promises. |

## Review requirements

All four lessons must remain offline and use synthetic fixtures. `sec-02` and `sec-03` are provisionally S1; if any draft detail increases realistic misuse, raise the tier and stop for a separate safety review. The Stage 3 content review must explicitly check that no section supplies operational intrusion, credential theft, malware, evasion, persistence, unauthorized access, or public-target testing instructions. Human safety and pedagogical review remains required before release.

## Additional inspected source findings

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [OWASP Threat Modeling Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Threat_Modeling_Cheat_Sheet.html) | Threat modeling is structured and repeatable: model the system, identify and rank threats, choose mitigations, and review/validate the result. | OWASP describes system modeling with data flows, trust boundaries, data stores, processes, and external entities, then frames four questions: what are we working on, what can go wrong, what will we do, and did we do enough. | The page includes adversarial examples and tool references. Stage 3 uses only a fictional offline note app and defense-oriented categories; it omits exploit steps, payloads, scanning, and real systems. |
| [CERT Guide to Coordinated Vulnerability Disclosure](https://certcc.github.io/CERT-Guide-to-CVD/) | CVD gathers information from finders, coordinates sharing among relevant stakeholders, and discloses vulnerabilities and mitigations; the guide distinguishes finder, vendor, and coordinator roles. | The CERT/CC guide explains its 2017 CMU/SEI origin, CVD purpose, stakeholder roles, phases, and policy context. | The guide contains operational coordination detail. The learner lesson uses only a redacted fictional report, no real submission, contact, target, or exploit detail. |
| [NIST NICE Framework Resource Center](https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center) | NICE provides a common language for cybersecurity work and the knowledge and skills needed to complete it; it supports career discovery, education, hiring, and workforce development. | NIST describes the framework’s use across public/private sectors and points to components, work roles, and skills as adaptable resources. | NICE terminology is not a job guarantee, certification, or prediction of employment. The lesson uses it only to compare role vocabulary and learning tasks. |
| [MITRE ATT&CK](https://attack.mitre.org/) | ATT&CK is a knowledge base of adversary tactics and techniques based on real-world observations, used to support threat models and defensive methodologies. | MITRE describes ATT&CK’s purpose, open availability, and tactic/technique organization. | Its detail can enable misuse if turned into procedures. The lesson explains taxonomy and defensive mapping at a high level and avoids technique instructions, target discovery, or command examples. |

These source pages were inspected on 2026-08-26. Search results and source pages were treated as untrusted reference data; no external code, commands, forms, or target interaction was performed.

## Stage 3 browser smoke

# Stage 3 Web browser smoke evidence

**Date:** 2026-08-26
**Environment:** Chromium sandbox browser, local HTTP server at `http://127.0.0.1:4173/`

The Stage 3 shell loaded with the updated visible copy: `Private by default · 31 current lessons · MVP baseline 20`, the privacy note, and the message that Termux integration is available only in the native Android app. The page rendered 31 lesson controls, including the new Stage 3 cards in the catalog.

The first load still reported `Cached 27 core lessons available offline` and `27 modules · cached core pack`. This was not a repository mismatch: the service-worker shell was v6 and the live manifest was 1.5.0, but the browser’s IndexedDB active pack still contained the prior Stage 2 payload. `indexedDB.databases()` identified `aetherlearn-web` version 1 with stores `packs` and `state`. Only the `packs` store was cleared; the `state` store was preserved. The reset returned `cleared packs; preserved stores: state`.

A fresh reload and explicit cache action are still required to confirm the Stage 3 pack status and reader flow. This evidence does not claim Android-browser behavior, a network-disabled reload, assistive-technology validation, or human content/safety approval.

After the storage-preserving reset, the fresh reload displayed `31 modules · shared Markdown source`, all 31 lesson cards, the four Stage 3 cards in positions 28–31, the privacy copy, and the Android-only Termux message. Clicking the cache control completed successfully and displayed `Cached 31 core lessons in this browser.` with `31 modules · cached core pack`. Existing browser-local learning state remained visible on previously progressed lessons, confirming that only the pack store had been cleared. This confirms the local Web catalog and explicit cache flow in the sandbox browser; it does not claim a network-disabled reload, Android-browser runtime, accessibility-technology validation, or human content/safety approval.

The local reader opened `sec-01-ethics-scope-and-harm`. It rendered the `security-ethics · beginner` metadata, offline/S0 labels, objectives, prerequisites, availability, explanation, worked example, common mistakes, offline practice, project/application, accessibility notes, safety guidance, further reading, and change log. The reader exposed four local quiz inputs with **Check answers**, a bookmark control, private note textarea/save control, and **Mark lesson complete**. The page clearly states that notes and bookmarks remain in the browser and are never synced. This is a browser rendering smoke result only and does not substitute for human pedagogical or safety review.

## Stage 4 source matrix

# Stage 4 source and claims matrix

**Research date:** 2026-08-26
**Scope:** a small offline Web/data foundation slice covering semantic HTML and accessibility, CSS responsive presentation, JavaScript events/state, and JSON/data modeling. External pages were treated as untrusted reference data; no code was copied into the app, no forms were submitted, and no external target was contacted.

## Sources inspected

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [MDN: HTML: A good basis for accessibility](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Accessibility/HTML) | Semantic HTML uses elements for their intended purpose and gives browsers built-in accessibility hooks; native controls such as buttons provide keyboard behavior. Labels, useful link text, table headings, plain language, and source order support accessibility. | MDN’s learning page states these learning outcomes and explains semantic elements, native controls, labels, links, tables, and keyboard access. | MDN is practical developer guidance, not a conformance certificate. Lessons use static fictional markup and state that an example is not a complete accessibility audit. |
| [W3C Web Content Accessibility Guidelines 2.2](https://www.w3.org/TR/WCAG22/) | WCAG 2.2 is a W3C Recommendation with testable, technology-neutral success criteria covering web content on desktop and mobile devices. | The Recommendation identifies its publication date as 12 December 2024 and describes success criteria, supporting documents, and the relationship to WCAG 2.0/2.1. | WCAG success criteria require context and testing; the lesson must not claim conformance from a paper exercise or a few checks. |

## Planned additional primary sources

The remaining lessons will cite [MDN CSS basics](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Styling_basics), [MDN JavaScript scripting](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting), and [RFC 8259: The JavaScript Object Notation (JSON) Data Interchange Format](https://www.rfc-editor.org/rfc/rfc8259). These sources will support only stable, high-level concepts and fixed offline fixtures.

## Proposed Stage 4 lesson set

| ID | Working title | Risk | Offline activity | Safety/privacy boundary |
|---|---|---:|---|---|
| `web-01-semantic-html-accessibility` | Semantic HTML and accessible structure | S0 | Label a fictional page outline and replace generic containers with appropriate elements. | No live site, user data, or claim of WCAG conformance. |
| `web-02-css-layout-responsive-design` | CSS layout and responsive presentation | S0 | Predict layout changes at two fixed viewport widths using a static style card. | No tracking, remote assets, or browser automation. |
| `web-03-javascript-events-and-state` | JavaScript events and local state | S1 | Trace a fictional button event and state transition on paper; no code execution in the app. | No arbitrary execution, storage of personal data, network calls, or third-party scripts. |
| `web-04-data-modeling-and-json` | Data modeling and JSON fixtures | S1 | Design a bounded local record schema and identify valid/invalid JSON fixture cases. | No real personal data, credentials, imports, or external API calls. |

## Review requirements

All four lessons must remain offline, use synthetic examples, and state that the exercises are educational practice rather than standards conformance or production security review. `web-03` and `web-04` are provisionally S1 because state and data-format misunderstandings can lead to unsafe implementations; they must not become arbitrary code execution, network, credential, or real-data exercises. Human technical, pedagogical, accessibility, and privacy review remains required before release.

## Source freshness

MDN pages are living documentation and may change; lesson claims should remain generic and be rechecked at each content release. WCAG 2.2 is a versioned W3C Recommendation and should be cited by version. RFC 8259 is the normative JSON format reference; examples in lessons must remain small and clearly marked as fictional fixtures.

## Additional inspected source findings

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [MDN: CSS styling basics](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Styling_basics) | CSS styles and lays out web pages; its fundamentals include syntax, selectors, values, sizing, box-model behavior, and layout. | MDN describes CSS as the styling and layout language applied to HTML and organizes learning around selectors, the box model, sizing, overflow, tables, and debugging. | Examples are instructional and version-sensitive. Stage 4 uses fixed style cards and viewport predictions, not a promise of identical rendering across browsers. |
| [MDN: Dynamic scripting with JavaScript](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting) | JavaScript can manipulate the DOM through browser APIs; beginner scripting includes events, DOM scripting, JSON, network requests, and debugging as distinct topics. | MDN lists events and DOM scripting in its core curriculum and explains DOM manipulation as changing document structure through built-in browser APIs. | The page also covers network requests and broader APIs. Stage 4 deliberately excludes network access, arbitrary execution, third-party scripts, and personal data. |

These pages were inspected on 2026-08-26. The planned lesson examples remain high-level and local so they do not inherit the operational breadth of the full MDN curriculum.

## JSON standard finding

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [RFC 8259: The JavaScript Object Notation (JSON) Data Interchange Format](https://www.rfc-editor.org/rfc/rfc8259) | JSON is a lightweight, text-based, language-independent format for exchanging structured data. RFC 8259 describes strings, numbers, booleans, null, objects, and arrays, and defines JSON text as a serialized value. | The RFC Editor page identifies RFC 8259 as Internet Standard STD 90, published December 2017, obsoleting RFC 7159. Its grammar and values sections define the core syntax and data types. | JSON syntax does not define an application’s meaning, validation policy, privacy policy, or authorization. Stage 4 uses tiny fictional fixtures and teaches schema decisions separately from syntax. |

The RFC page was inspected on 2026-08-26. No external data was submitted and no code or network operation was performed from the source.

## Stage 4 browser smoke

# Stage 4 Web browser smoke evidence

**Date:** 2026-08-26
**Environment:** Chromium sandbox browser against `http://localhost:4173/`, serving the repository `web/` directory.

## Initial catalog and explicit cache

The updated shell displayed `Private by default · 35 current lessons · MVP baseline 20`, the local privacy note, and the message that Termux integration is available only in the native Android app. The Learn view displayed `35 modules` and 35 lesson controls, including the four Stage 4 cards: `web-01-semantic-html-accessibility`, `web-02-css-layout-responsive-design`, `web-03-javascript-events-and-state`, and `web-04-data-modeling-and-json`.

Selecting **Cache core content** succeeded and displayed `Cached 35 core lessons in this browser.`; the catalog status changed to `35 modules · cached core pack`. This confirms the explicit local IndexedDB cache flow for the Stage 4 payload. Further storage-preserving reset, reader-open, and offline-network checks remain to be recorded below.

## Limitations

This evidence does not yet claim Android-browser runtime behavior, assistive-technology validation, or a network-disabled reload. The local server remains available for the next smoke steps.

## Storage-preserving reset

The browser database `aetherlearn-web` reported separate object stores named `packs` and `state`. Before reset it contained one pack record and no state records in this fresh browser profile. Clearing only `packs` completed successfully, leaving `packs: 0` and `state: 0`; the state store was not deleted or modified by the reset operation.

After reloading following the packs-only reset, the shell still displayed 35 lesson controls and `35 modules · shared Markdown source`, while the cache status returned to the uncached state. Selecting **Cache core content** again succeeded and restored `Cached 35 core lessons in this browser.` with `35 modules · cached core pack`.

## Stage 4 reader smoke

The cached catalog opened `#/lesson/web-01-semantic-html-accessibility`. The reader rendered the Stage 4 lesson metadata (`web-data`, `beginner`, `offline`, 55 minutes, `S0`), the required lesson sections, source links, four knowledge-check prompts with answer inputs and feedback controls, and browser-local bookmark, note, and completion controls. The lesson was marked `in progress` on open, as expected for the local learning-state flow.

## Cached reload with server stopped

The local server was stopped after the core pack had been cached. Reloading `#/lesson/web-01-semantic-html-accessibility` still delivered the shell and opened WEB-01 from the service-worker/IndexedDB cache. The full reader, Stage 4 metadata, lesson sections, knowledge-check inputs, and local study controls remained visible. A subsequent console inspection reported no console output. This is evidence of cached use with the local server unavailable; it is not Android-browser or assistive-technology evidence.

The cached `#/lesson/web-04-data-modeling-and-json` route also opened with the server stopped. Its S1 offline metadata, JSON/data-model explanation, privacy cautions, four knowledge checks, and local study controls rendered. Console inspection again reported no console output.

## Stage 5 source matrix

# Stage 5 source and claims matrix

**Research date:** 2026-08-26
**Implemented scope:** a bounded historical-security and career-orientation slice. The lessons remain offline, bundled-only, draft, and non-operational. Historical material focuses on impact, affected stakeholders, response, accountability, and defensive lessons; it omits attack construction, evasion, persistence, credential handling, and live-target activity. Career material describes role families and learning evidence, not jobs, salary, legal outcomes, or regional portability.

## Source findings

| Source | Observed claim | Lesson use | Limitation / safety boundary |
|---|---|---|---|
| Carnegie Mellon Software Engineering Institute, “Fostering Growth in Professional Cyber Incident Management” | SEI describes the 1988 Morris worm as a catalyst for DARPA asking SEI to establish CERT/CC. It describes CERT/CC as a neutral third party that reports vulnerabilities to vendors without revealing the reporter’s identity and publishes Vulnerability Notes with summaries, remediation information, and affected-vendor lists. | Historical response lesson: connect a major incident to institutional coordination, vulnerability communication, remediation, and neutral intermediaries. | This institutional-history page is a concise retrospective and does not provide a complete incident chronology or independent damage estimate. Do not reproduce operational worm behavior or treat the page as a complete technical account. |
| FBI, “The Morris Worm — 30 Years Since First Major Attack on the Internet” (2018-11-02) | The FBI reports the November 2, 1988 incident, describes widespread disruption and delayed email, explains that affected institutions used measures such as disconnection or system restoration, and records the subsequent investigation, conviction, and community-service sentence. | Historical case study: separate intent from impact, identify affected communities, compare immediate response with institutional learning, and discuss accountability without glorifying the actor. | This official retrospective has a law-enforcement perspective and includes technical descriptors. Stage 5 summarizes consequences and defensive lessons only; it does not include exploit paths, concealment, or replication details. |
| NIST, “NICE Framework Resource Center” | NIST states that the NICE Framework establishes a common language for cybersecurity work and the knowledge and skills needed to complete it. It describes uses including career discovery, education and training, work-role description, and workforce development. | Career orientation: map fictional learner evidence to broad role families and skills, then identify learning questions and safe local artifacts without treating a framework as a job guarantee. | The resource center describes a framework, not a promise of employment, salary, legal status, or regional portability. The lesson avoids labor-market predictions and states that role names and local requirements change. |
| Computer History Museum, “The Internet Comes From Behind” | The museum places the early network in an academic and publicly supported context, describes the 1988 Morris worm as affecting more than 6,000 computers, and frames the incident as part of the network’s social and institutional history. It also presents the later conviction and apology in a museum context. | Cultural framing: show that infrastructure history includes institutions, public investment, communities, affected users, and consequences—not only a technically skilled individual. | This curated retrospective uses broad summary language. Stage 5 cross-checks dates and consequences with CMU SEI/FBI, avoids hero/villain framing, and omits operational mechanisms. |
| ACM Code of Ethics and Professional Conduct | ACM presents principles including contributing to society and human well-being, avoiding harm, being honest and trustworthy, being fair and not discriminating, respecting privacy, and honoring confidentiality. It describes the Code as guidance for ethical decision-making rather than an algorithm that mechanically resolves every case. | Reflection framework for historical impact, affected stakeholders, intent-versus-consequence, privacy, and accountability. Learners apply the principles to fictional discussion cards rather than judge a real person or provide legal advice. | The Code is professional ethical guidance, not a complete legal or cultural framework. The lesson invites multiple perspectives and states that legal and organizational duties vary by place and context. |

## Source inspection status

The NIST NICE Framework Resource Center, Computer History Museum retrospective, and ACM Code of Ethics were inspected on 2026-08-26 and included in the matrix above. The matrix supports the D-038 scope and D-039 implementation review. Career claims remain limited to framework language and avoid employment or compensation promises. The matrix is not a substitute for human historical, cultural, pedagogical, accessibility, safety, or source-freshness review.

## Stage 5 lessons

1. `sec-05-morris-worm-history-and-response` — a high-level history-and-impact case study using CMU SEI, FBI, and Computer History Museum sources, with ACM ethics reflection. Risk: S0 because activities are fictional, historical, and non-operational.
2. `sec-06-cybersecurity-career-role-families` — a role-and-learning orientation using the NIST NICE Framework. Risk: S0 because it describes role families, skills, reflection, and portfolio evidence without employment, salary, legal, or regional-portability claims.

Stage 5 remains offline and bundled-only. No live targets, attack recipes, exploit or malware details, credentials, real personal data, external contact, arbitrary execution, network exercise, or Termux wrapper is permitted.

## References

[1]: https://www.sei.cmu.edu/history-of-innovation/fostering-growth-in-professional-cyber-incident-management/ "Carnegie Mellon Software Engineering Institute: Fostering Growth in Professional Cyber Incident Management"
[2]: https://www.fbi.gov/news/stories/morris-worm-30-years-since-first-major-attack-on-internet-110218 "FBI: The Morris Worm — 30 Years Since First Major Attack on the Internet"
[3]: https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center "NIST: NICE Framework Resource Center"
[4]: https://www.computerhistory.org/revolution/networking/19/378 "Computer History Museum: The Internet Comes From Behind"
[5]: https://www.acm.org/code-of-ethics "ACM: Code of Ethics and Professional Conduct"

## Stage 5 browser smoke

# Stage 5 browser smoke evidence

**Date:** 2026-08-26
**Environment:** Chromium sandbox browser, local static server at `http://localhost:4173/`, desktop viewport.

## Initial v8 shell and stale-pack observation

The refreshed shell loaded from the local server and visibly reported `Private by default · 37 current lessons · MVP baseline 20`. The page showed the privacy note and the message that Termux integration is Android-only. The existing browser-local IndexedDB cache still reported `Cached 35 core lessons available offline` and `35 modules · cached core pack`; this is expected because the prior Stage 4 pack remained active in browser storage. The next step is to clear only the `packs` object store, preserve the `state` store, reload, and explicitly cache the current Stage 5 pack.

## Packs-only reset

The browser database `aetherlearn-web` exposed separate `packs` and `state` object stores. A controlled reset targeted only `packs`; no database deletion or `state` mutation was requested. The first diagnostic serialized IndexedDB count requests as empty objects, so exact before/after record counts are intentionally not claimed here; the reset will be verified through the application’s uncached/recached lesson counts.

## Updated cache and catalog

After the packs-only reset, the application showed `Core lesson pack not loaded. Cache it for offline use.` while still rendering the updated shell with `37 current lessons` and `37 modules · shared Markdown source`. Selecting **Cache core content** completed successfully and changed the status to `Cached 37 core lessons in this browser.` The Learn view then showed `37 modules · cached core pack`; the visible catalog included module 36 `Morris worm: history, impact, and response` and module 37 `Cybersecurity role families and learning paths`. The stale 35-lesson pack was therefore replaced without deleting the browser-local state store.

The cached catalog DOM contained a lesson card numbered 36 with `data-open-lesson="sec-05-morris-worm-history-and-response"` and a lesson card numbered 37 with `data-open-lesson="sec-06-cybersecurity-career-role-families"`. Both Stage 5 lessons also appeared in the offline practice-card list. This confirms discovery and stable route wiring for both new IDs in the refreshed browser cache.

## SEC-05 reader smoke

The cached route `#/lesson/sec-05-morris-worm-history-and-response` opened successfully. The reader displayed the S0/offline metadata, objectives, prerequisites, explanation, stakeholder/timeline tables, fictional offline practice, four knowledge-check inputs, accessibility notes, safety boundary, and four source links. The page explicitly stated that it does not teach malware creation, spreading, hiding, or investigation and did not expose operational instructions. Console inspection after the route load reported no console output.

## SEC-06 reader smoke

The cached route `#/lesson/sec-06-cybersecurity-career-role-families` opened successfully. The reader displayed the S0/offline metadata, objectives, prerequisites, NIST NICE explanation, broad role-lens tables, fictional task-card practice, four knowledge-check inputs, accessibility notes, privacy safeguards, and the explicit statement that the lesson does not promise employment, compensation, legal outcomes, or regional portability. Console inspection after the route load reported no console output.
