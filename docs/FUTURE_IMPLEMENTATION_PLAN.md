# Future curriculum implementation plan

**Status:** Stage 1 through Stage 5 automated checkpoints are complete; Stage 5’s exact two-lesson scope is closed under decision D-039. All five post-MVP stages remain draft pending human technical, pedagogical, accessibility, source, safety, privacy, historical/cultural, and career-framing review.

## Decision boundary

The current AetherLearn MVP remains a validated, frozen 20-module curriculum. The roadmap is not a license to add an encyclopedic catalog. A future implementation must begin with an explicit decision-log entry that names the approved batch, changes the curriculum scope intentionally, identifies maintainers and reviewers, and accepts the additional Android/Web parity and long-term source-maintenance burden.

The user-approved Stage 1 and Stage 2 slices are represented in `content/curriculum.yml`, `content/core/`, Android assets, and the Web payload. Both passed their automated validators, client-parity, browser-smoke, deterministic-pack, and hosted-CI checkpoints. Stage 3 has its own exact module list, source matrix, and safety boundary, and its automated checkpoint passed hosted Quality workflow `32975751921`. Stage 4 is represented by its exact approved list, source matrix, synchronized clients, browser evidence, commit, and hosted Quality checkpoint `32979503787`. Stage 5 is represented by its exact approved list, source matrix, synchronized clients, browser evidence, deterministic 37-lesson pack, commit, and hosted Quality checkpoint `32982750065` under D-039.

## Recommended staged implementation

| Stage | Proposed scope | Suggested size | Why this order | Required reviewers | Current status |
|---|---|---:|---|---|---|
| 0 | Release the current 20-module MVP and complete device, accessibility, pedagogical, and safety gates | 0 new modules | Establish a trustworthy baseline before expanding product scope | Release owner, device tester, pedagogical reviewer, safety reviewer | Baseline complete; human release gates remain |
| 1 | Computing history and systems vocabulary | 3 lessons | Extends digital literacy without requiring risky tooling or external services | Technical and pedagogical reviewers | Automated checkpoint complete; human review open |
| 2 | Software engineering and open-source practice | 4 lessons | Builds directly on Git and debugging already taught | Technical, pedagogical, accessibility, and supply-chain reviewers | Automated checkpoint complete; human review open |
| 3 | Ethical security, disclosure, and organization literacy | 4 lessons | Adds only high-level defensive security context after the safety governance and source process are exercised | Technical, pedagogical, safety, and source reviewers | Automated checkpoint complete; human safety/pedagogical review open |
| 4 | Web/data foundations and local-only application practice | 4 lessons | Adds browser and data concepts with static/local fixtures | Technical, pedagogical, accessibility, and privacy reviewers | Automated checkpoint complete under D-036; human review open |
| 5 | Historical security case studies and career orientation | 2 lessons | Builds historical and career literacy only after source review and careful framing | Historical/source, pedagogical, safety, accessibility, and career-framing reviewers | Automated checkpoint complete under D-039; human review open |

The table is a delivery sequence, not blanket approval of new modules. Stages 1–5 have completed their automated implementation checkpoints but remain draft until human review. Stage 5 closed its automated checkpoint under the exact D-038 scope and D-039 evidence; no later stage is authorized without a new exact decision. Approval to continue does not waive human review or release gates.

## Stage 1: computing history and systems vocabulary

The approved Stage 1 slice is:

1. **`dl-06-computing-language-history` — How programming languages reflect constraints.** Compare language design choices, historical problem domains, and high-level compiled/interpreted/declarative vocabulary without requiring a compiler installation.
2. **`dl-07-how-programs-run` — How a computer runs a program.** Introduce source text, translation, processes, files, and error stages through fixed local traces.
3. **`dl-08-networks-web-concepts` — Networks and the Web: requests, responses, and resources.** Introduce client/server, HTTP messages, resources, and caching through a fictional written trace.

All three lessons are offline, use bounded synthetic examples, and are currently marked S0. The remaining Stage 1 ideas—DNS depth and data storage/serialization—are deferred until a later stage decision because the approved slice must remain small and coherent.

## Stage 2: software engineering and open source

The approved Stage 2 slice is:

1. **`dev-04-testing-pure-functions` — Testing small programs with examples and expected results.** Design deterministic test cases, boundary examples, and invalid-input rules on paper.
2. **`dev-05-code-review-issue-reports` — Code review and useful issue reports.** Use fictional repository context to practice reproducible issue reports and respectful review comments.
3. **`dev-06-dependency-provenance-builds` — Dependencies, provenance, and reproducible builds.** Distinguish checksums, provenance, resolved dependencies, and repeatability using a fictional manifest.
4. **`dev-07-open-source-accessibility` — Open-source contribution and accessibility review.** Use project boundaries and WCAG 2.2 principles to draft small, testable acceptance checks.

All four lessons are offline and use synthetic fixtures. DEV-06 is S1 because supply-chain evidence can be misunderstood; it does not authorize installation, execution, or trust in an artifact. No lesson requires publishing personal information, copying secrets, or connecting to a third-party account.

## Stage 3: ethics, security, and organizations

The approved Stage 3 slice is:

1. **`sec-01-ethics-scope-and-harm` — Ethics, scope, and harm minimization.** Apply the ACM framework to fictional decisions and replace unnecessary realism with safer local simulations.
2. **`sec-02-threat-modeling-defensive-controls` — Threat models and defensive controls.** Model assets, actors, flows, trust boundaries, and mitigations for a fictional offline note app using OWASP’s high-level process.
3. **`sec-03-coordinated-disclosure` — Coordinated vulnerability disclosure.** Classify roles and draft a private, redacted fictional handoff using CISA and CERT/CC process vocabulary.
4. **`sec-04-security-organizations-and-roles` — Security organizations, roles, and evidence.** Distinguish ethics bodies, technical communities, government programs, workforce frameworks, and defensive knowledge bases using NIST NICE and MITRE ATT&CK vocabulary.

All four lessons are offline and use synthetic fixtures. SEC-01 and SEC-04 are S0; SEC-02 and SEC-03 are provisionally S1. The source matrix in `docs/references/stage3_sources.md` records the scope and review limitations.

A security lesson must use synthetic data, toy applications, or written fixtures. It must not include public-target scanning, credential capture, exploit kits, malware, persistence, evasion, bypass instructions, or real-target testing. MITRE ATT&CK may be used as a defensive vocabulary, not as an attack recipe. Historical “hacker” case studies should focus on impact, affected stakeholders, response, ethics, and defensive lessons; they should not glorify criminal groups or reproduce operational details.

## Stage 4: web and data foundations

The approved Stage 4 slice is:

1. **`web-01-semantic-html-accessibility` — Semantic HTML and accessible structure.** Practice landmarks, labels, source order, native controls, and text alternatives with fictional static markup.
2. **`web-02-css-layout-responsive-design` — CSS layout and responsive presentation.** Use box-model, sizing, overflow, and responsive-layout vocabulary with fixed offline layout cards.
3. **`web-03-javascript-events-and-state` — JavaScript events and local state.** Trace event, state, and render changes on paper without arbitrary execution or network access.
4. **`web-04-data-modeling-and-json` — Data modeling and JSON fixtures.** Model fictional local records and distinguish JSON syntax from application validation and privacy semantics.

All four lessons are bundled-only, offline, and use static fictional fixtures or paper/text traces. WEB-01 and WEB-02 are S0; WEB-03 and WEB-04 are provisionally S1. They require no network requests, external APIs, third-party scripts, real personal data, arbitrary JavaScript execution, or Termux wrappers. The source matrix in `docs/references/stage4_sources.md` records MDN, W3C WCAG 2.2, and IETF RFC 8259 evidence and limitations.

## Stage 5: case studies and career orientation

The approved Stage 5 slice is:

1. **`sec-05-morris-worm-history-and-response` — Morris worm: history, impact, and response.** Use CMU SEI, FBI, Computer History Museum, and ACM sources to separate intent, impact, affected stakeholders, institutional response, ethics, and accountability. The lesson must not glorify an actor or reproduce operational details.
2. **`sec-06-cybersecurity-career-role-families` — Cybersecurity role families and learning paths.** Use the NIST NICE Framework to describe broad task lenses, skills, and safe learning evidence without employment, salary, legal-outcome, or regional-portability claims.

Both lessons are S0, offline, bundled-only, and draft. Exercises use fictional discussion cards or learning-plan worksheets. No live target, malware, exploit or evasion detail, credential, personal data, external contact, network exercise, arbitrary execution, or Termux wrapper is allowed. The source matrix in `docs/references/stage5_sources.md` records CMU SEI, FBI, Computer History Museum, ACM, and NIST sources, their intended claims, and limitations. Case studies require a source table with publication date, update date, uncertainty, affected parties, and defensive response. The project must retain a maintainer for source freshness and a reviewer for historical, cultural, accessibility, and career framing.

## Per-module implementation contract

Each approved lesson must:

- use the existing frontmatter and required-section contract;
- have a stable ID, explicit prerequisites, version, review status, and ISO review date;
- state whether it is offline, offline-pack, termux-optional, or network-optional;
- declare a risk tier based on realistic misuse, not the title;
- include at least two observable objectives and an answerable knowledge check with explanations;
- include an offline practice path, expected result, cleanup, and fallback;
- identify runtime and version assumptions for code examples;
- include accessibility notes, safety boundaries, and source references;
- use synthetic data and bounded inputs;
- provide a maintainer, review scope, and update trigger; and
- pass source validation, prerequisite validation, link validation, Android/Web parity, and human technical/pedagogical/safety review before release.

A Termux-optional lesson additionally requires a fixed wrapper ID, immutable executable and arguments, visible confirmation text, package/version prerequisites, expected output shape, cleanup, no secrets, and a written fallback. No user-authored command text may cross the app boundary.

## Implementation workflow

1. Approve the exact stage and lesson list in `docs/PLAN.md`.
2. Create a source and claims table before drafting.
3. Draft each lesson outside the release payload and run the validator early.
4. Review technical accuracy, answerability, reading level, accessibility, privacy, and realistic misuse cases.
5. Decide whether any lesson is S0, S1, or needs the S2/S3 governance path in `docs/SAFETY.md`.
6. Add only approved IDs to `content/curriculum.yml` and update the registry-backed clients together.
7. Synchronize Android and Web copies, regenerate manifests, and build a deterministic pack.
8. Run all automated gates and compare source/client bytes.
9. Conduct human content review and representative client testing.
10. Release with a changelog, source freshness record, rollback path, and explicit scope statement.

## Acceptance criteria for a future batch

| Gate | Pass condition |
|---|---|
| Scope | The decision log names the exact approved modules and no unapproved module enters a client payload |
| Content | Every approved lesson validates with the current contract and has a named technical/pedagogical reviewer |
| Safety | Any S2/S3 material has the specialist review and controlled-lab evidence required by `SAFETY.md`; S4 is rejected |
| Sources | Every material factual claim has an inspected source with version/date and maintenance owner |
| Practice | A learner can complete the exercise offline or sees a clear, consented network boundary and fallback |
| Client parity | Android and Web copies, manifests, and deterministic pack entries match the canonical source |
| Regression | Existing 20-module progress, notes, bookmarks, quiz attempts, exports, and offline reading remain compatible |
| Accessibility | Text scaling, labels, keyboard/focus behavior, non-color cues, and manual assistive-technology checks are recorded |
| Operations | Build artifacts, checksums, rollback, and release notes identify the exact commit and content version |

## Current action and later gate

Stages 1–5 are approved and implemented one bounded lesson slice at a time under their exact scopes. All five stages have passed their automated, parity, browser, pack, commit/push, and hosted-CI checkpoints, while human technical, pedagogical, accessibility, source, safety, privacy, cultural, and career-framing review remains open. Stage 5 closed under D-039 with exactly two lessons; no Stage 6 scope is authorized. Approval to continue later does not waive any per-stage gate.
