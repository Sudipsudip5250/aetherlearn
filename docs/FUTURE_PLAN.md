# Future content roadmap and source plan

**Status:** Stage 1 and Stage 2 automated checkpoints are complete; Stage 3 is approved and implemented as a four-lesson safety-bounded slice; the original `mvp-20` baseline remains stable and Stages 4–5 remain gated.

AetherLearn’s original MVP curriculum is intentionally frozen at 20 modules. It provides a coherent foundation in digital literacy, Python, basic algorithms, and developer practice. The approved Stage 1 expansion adds three bundled, offline history/systems lessons, Stage 2 adds four software-engineering lessons, and Stage 3 adds four high-level defensive security-ethics lessons, without renumbering or replacing that baseline. It does not attempt to cover every programming language, operating system, organization, security topic, or career path. This document records what could come next so future contributors do not expand the product ad hoc or turn historical and security material into unsafe operational instruction.

## 1. Coverage assessment

| Area | Current coverage | Evidence-based gap | Safe future direction |
|---|---|---|---|
| Digital literacy | Devices, files, Android settings, browsers, privacy, passwords, phishing | No deeper systems or network mental model | Add operating systems, processes, memory, DNS, HTTP, and data storage as conceptual lessons |
| Programming | Python fundamentals, tracing, functions, collections, conditions, loops | No comparative view of language design or older ecosystems | Add history and comparison rather than a full second language track initially |
| Algorithms | Data structures, searching, sorting, complexity, recursion, trees, graphs | No implementation testing or profiling practice | Add paper tracing, benchmark interpretation, and test-design exercises using bounded local data |
| Developer practice | Terminal, Git, debugging | No collaboration, testing, packaging, APIs, dependency, or release practice | Add local-only software-engineering foundations with reproducible examples |
| Web and data | Browser use and offline PWA experience | No HTML/CSS/HTTP, databases, accessibility engineering, or data modeling | Add browser-platform and data-literacy tracks with static/local examples |
| Security and society | Privacy, phishing awareness, safe updates, bounded Termux wrappers | No ethical-security history, threat modeling, disclosure, or defensive frameworks | Add non-operational ethics, incident analysis, secure-design, and coordinated-disclosure material |
| Organizations and community | Open-source project behavior is implied by Git and contribution docs | Learners lack a map of standards bodies, foundations, research groups, and career roles | Add an organization-literacy lesson with roles, governance, evidence, and contribution paths |
| Career orientation | No explicit role map | Learners may not know how skills connect to real work | Add role-based pathways using skills and outcomes, not job promises or salary claims |

The gap analysis is a planning assessment, not evidence that the MVP was incomplete. Stages 1–3 are deliberately limited and remain draft until human pedagogical, technical, accessibility, source, and—where applicable—safety review; later releases must wait for their own stage decisions and checkpoints.

## 2. Recommended future strands

### A. Computing and language history

A short historical strand should show why languages exist and how constraints shape design. Suggested coverage is machine code and assembly as a hardware-near model; FORTRAN for scientific computing; COBOL for business systems; BASIC for accessibility to beginners; Pascal for teaching structured programming; C for systems and portability; Lisp and Smalltalk for symbolic and object-oriented ideas; SQL as a declarative data language; and shell languages as command composition. The goal is comparison—data model, execution model, readability, portability, ecosystem, and maintenance—not memorizing dates or installing obsolete toolchains.

The Computer History Museum’s timeline provides a useful primary-history starting point for Plankalkül, A-0, FORTRAN, COBOL, BASIC, Simula, LOGO, UNIX, Pascal, C, C++, Perl, and other milestones.[1] Historical claims should be checked against original manuals, archival material, or reputable institutional histories before publication. A lesson should label what is documented fact, what is an interpretation, and what remains debated.

A safe offline exercise can ask learners to translate the same tiny task—such as counting words in a fixed text—into pseudocode, a Python solution, and a historical-language-style description. It should not require downloading an unmaintained compiler or executing unknown binaries. Any example syntax must be clearly labeled as illustrative unless it has been tested against a pinned, trusted implementation.

### B. Systems, networks, and the Web

A second strand could connect the existing digital-literacy foundation to programming practice through CPU and memory models, processes and files, permissions, DNS, HTTP, browsers, databases, serialization, and client/server boundaries. The exercises should use diagrams, fixed local files, and loopback-only examples. They should not ask learners to scan public hosts, intercept traffic, collect credentials, or connect to infrastructure they do not own.

The Web portion should distinguish the browser’s document model, styling, scripting, network requests, caching, and accessibility. A static local page and a small local dataset are enough to teach the concept. A later optional exercise may use a local-only server with explicit permissions, but the default remains offline-first.

### C. Software engineering and open-source practice

The next practical strand should cover tests, readable interfaces, error handling, code review, issue reports, semantic versioning, dependency provenance, reproducible builds, accessibility acceptance criteria, and release notes. It should extend DEV-02 and DEV-03 rather than introduce a new tool-heavy workflow.

Suggested exercises are a local test plan for a small pure function, a review of a deliberately flawed patch, a dependency inventory using a fixture project, and a release checklist that verifies checksums and documents limitations. No exercise should require publishing a branch, exposing private notes, or copying secrets into a repository.

### D. Ethical security and responsible research

A future security strand should begin with permission, scope, harm minimization, privacy, evidence handling, and coordinated vulnerability disclosure. The ACM Code of Ethics frames computing responsibility around the public good and explicitly discusses avoiding harm, honesty, fairness, privacy, confidentiality, and professional review.[2] The CERT Guide to Coordinated Vulnerability Disclosure describes finders, vendors, coordinators, phases, and operational problems; CISA’s program describes collection, analysis, mitigation coordination, mitigation application, and disclosure.[4] [5]

OWASP’s Web Security Testing Guide can provide a structured vocabulary for defensive review, but its versioned references should be pinned because the project itself warns that identifiers and latest content change over time.[3] A learner-facing lesson should use a local toy application or a written fixture, not a live target. It should teach how to write a bounded finding, reproduce a problem locally, assess impact without exploiting real users, and route a report through the owner’s published policy.

MITRE ATT&CK can be used as a defensive taxonomy for discussing adversary behavior, detections, mitigations, and incident timelines. It should not be turned into a checklist for attacking real systems.[6] Examples should be high-level and mapped to prevention or detection outcomes. The curriculum should avoid step-by-step intrusion, credential theft, persistence, evasion, malware construction, or instructions for targeting named organizations.

### E. Organizations, groups, and the human side of computing

Learners would benefit from an organization-literacy section that explains what different groups do and how to evaluate their authority and source quality:

| Organization or community | Role to teach | Safe learner outcome |
|---|---|---|
| ACM | Professional society and ethics/community resource | Apply a professional principle to a design decision |
| OWASP | Community-led application-security projects and testing guidance | Use a versioned defensive checklist against a local fixture |
| CISA / CVE ecosystem | Public-sector coordination, advisories, and vulnerability information | Read an advisory and identify affected versions, mitigations, and uncertainty |
| CERT/CC / SEI | Vulnerability coordination and operational guidance | Draft a non-sensitive disclosure timeline |
| MITRE | Knowledge bases and structured defensive/adversary modeling | Map a fictional incident to a defensive tactic and mitigation |
| NIST NICE | Common language for cybersecurity work and skills | Compare a learner’s skills with role descriptions without promising employment |
| IETF and W3C | Open standards processes for Internet protocols and the Web | Distinguish a standard, a recommendation, an implementation, and an opinion |
| Free and open-source foundations | Stewardship, licenses, governance, and contribution norms | Choose a license-compatible contribution path |

The NIST NICE Framework is appropriate for career discovery because it provides a common language for cybersecurity work and the knowledge and skills needed for roles across sectors.[7] It should be presented as a framework, not as a guarantee of employment or a substitute for regional labor-market research.

### F. Historical security case studies without glorification

Historical “hacker” material can motivate learners when it focuses on curiosity, consequences, responsible choices, and defenders rather than celebrity or operational technique. The recommended format is:

1. State the source, date, and uncertainty level.
2. Describe the technical idea only at the level needed to understand the impact.
3. Explain who was affected and what harm or risk resulted.
4. Identify the defensive response, governance change, or lesson for builders.
5. Give the learner a safe local exercise, such as writing a timeline, threat model, disclosure report, or mitigation checklist.
6. End with permission boundaries and a route for reporting concerns.

Suitable case-study categories include the history of early worms and software failures, the evolution of bug-bounty and coordinated-disclosure practices, major public incidents described through official advisories, and biographies of engineers, researchers, educators, and defenders whose work improved computing. Avoid glamorizing criminal groups, reproducing payloads, naming targets unnecessarily, or treating public notoriety as a career model. Every case study should pass a separate human safety and historical-accuracy review before inclusion.

## 3. Source and freshness policy

Future lessons should prefer primary sources and stable institutional documentation. Each external source should be recorded with title, publisher, URL, access date, version or publication date when available, and the exact claim it supports. A link should not be used merely because it appears authoritative; the lesson author must inspect the source and distinguish source-reported fact from interpretation.

| Source class | Preferred use | Maintenance rule |
|---|---|---|
| Official standards and documentation | Language semantics, protocols, APIs, security guidance | Pin versions where identifiers or behavior change |
| Museums, archives, universities | Computing and language history | Cross-check dates and avoid unsupported “first” claims |
| Professional societies | Ethics, professional practice, community norms | Recheck policy wording before each major content release |
| Government advisories and catalogs | Public incidents, vulnerability status, mitigations | Record publication/update date and avoid stale severity claims |
| Open-source project documentation | Governance, contribution, implementation guidance | Link to tagged/versioned documents where possible |
| News and biographies | Discovery and context only | Never use as the sole source for technical or safety claims |
| Community forums and social media | Leads and learner perspectives | Do not treat as authoritative evidence without independent confirmation |

The current research set supports the roadmap with the Computer History Museum language timeline, the ACM Code of Ethics, OWASP WSTG, CISA CVD, the CERT CVD Guide, MITRE ATT&CK, and the NIST NICE Framework.[1]–[7] These links are starting points, not permission to copy large portions of copyrighted material or to skip project review.

## 4. Proposed content-gate process

Before any future strand becomes an authored module, the contributor should submit a scope note identifying the learner outcome, prerequisites, offline/online requirement, risk tier, exercise boundary, source list, accessibility considerations, and maintenance owner. A reviewer should then check technical accuracy, citation support, answerability, reading level, cultural and historical framing, and whether the exercise can be completed without unauthorized access or personal data.

A release candidate should keep unapproved content outside `content/curriculum.yml`. Once a batch is explicitly approved, it must use the versioned registry, existing validator, Android/Web byte-parity process, and human pedagogical/safety review. This preserves the stable `mvp-20` baseline and prevents a roadmap from silently becoming product scope. Stages 1–5 are the recorded exceptions under decisions D-031, D-032, D-034, D-036, and D-038; Stage 3 also has the safety matrix in `docs/references/STAGE_EVIDENCE.md`, Stage 4 has the Web/data source matrix in `docs/references/STAGE_EVIDENCE.md`, and Stage 5 has the historical/career source matrix in `docs/references/STAGE_EVIDENCE.md`.

## 5. Recommended order after release gates

The first post-MVP content increment should be a small, non-operational **computing history and systems vocabulary** set. The second should be **software engineering and open-source practice**. The third should be **ethical security, disclosure, and organization literacy**. Historical security case studies and career pathways should follow only after the project has a named human reviewer for source quality, safety framing, and local-law sensitivity.

The immediate content priority is the human technical, pedagogical, accessibility, source, safety, privacy, historical, cultural, and career-framing review of Stage 5 without claiming that repository checks replace human approval. Stage 5 is authorized under D-038 and its automated checkpoint is complete under D-039 for exactly two lessons. The roadmap is successful when each increment remains bounded, source-backed, safety-reviewed, and reversible.



## Implementation plan and stage governance

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

All four lessons are offline and use synthetic fixtures. SEC-01 and SEC-04 are S0; SEC-02 and SEC-03 are provisionally S1. The source matrix in `docs/references/STAGE_EVIDENCE.md` records the scope and review limitations.

A security lesson must use synthetic data, toy applications, or written fixtures. It must not include public-target scanning, credential capture, exploit kits, malware, persistence, evasion, bypass instructions, or real-target testing. MITRE ATT&CK may be used as a defensive vocabulary, not as an attack recipe. Historical “hacker” case studies should focus on impact, affected stakeholders, response, ethics, and defensive lessons; they should not glorify criminal groups or reproduce operational details.

## Stage 4: web and data foundations

The approved Stage 4 slice is:

1. **`web-01-semantic-html-accessibility` — Semantic HTML and accessible structure.** Practice landmarks, labels, source order, native controls, and text alternatives with fictional static markup.
2. **`web-02-css-layout-responsive-design` — CSS layout and responsive presentation.** Use box-model, sizing, overflow, and responsive-layout vocabulary with fixed offline layout cards.
3. **`web-03-javascript-events-and-state` — JavaScript events and local state.** Trace event, state, and render changes on paper without arbitrary execution or network access.
4. **`web-04-data-modeling-and-json` — Data modeling and JSON fixtures.** Model fictional local records and distinguish JSON syntax from application validation and privacy semantics.

All four lessons are bundled-only, offline, and use static fictional fixtures or paper/text traces. WEB-01 and WEB-02 are S0; WEB-03 and WEB-04 are provisionally S1. They require no network requests, external APIs, third-party scripts, real personal data, arbitrary JavaScript execution, or Termux wrappers. The source matrix in `docs/references/STAGE_EVIDENCE.md` records MDN, W3C WCAG 2.2, and IETF RFC 8259 evidence and limitations.

## Stage 5: case studies and career orientation

The approved Stage 5 slice is:

1. **`sec-05-morris-worm-history-and-response` — Morris worm: history, impact, and response.** Use CMU SEI, FBI, Computer History Museum, and ACM sources to separate intent, impact, affected stakeholders, institutional response, ethics, and accountability. The lesson must not glorify an actor or reproduce operational details.
2. **`sec-06-cybersecurity-career-role-families` — Cybersecurity role families and learning paths.** Use the NIST NICE Framework to describe broad task lenses, skills, and safe learning evidence without employment, salary, legal-outcome, or regional-portability claims.

Both lessons are S0, offline, bundled-only, and draft. Exercises use fictional discussion cards or learning-plan worksheets. No live target, malware, exploit or evasion detail, credential, personal data, external contact, network exercise, arbitrary execution, or Termux wrapper is allowed. The source matrix in `docs/references/STAGE_EVIDENCE.md` records CMU SEI, FBI, Computer History Museum, ACM, and NIST sources, their intended claims, and limitations. Case studies require a source table with publication date, update date, uncertainty, affected parties, and defensive response. The project must retain a maintainer for source freshness and a reviewer for historical, cultural, accessibility, and career framing.

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


## References

[1]: https://www.computerhistory.org/timeline/software-languages/ "Computer History Museum: Timeline of Computer History — Software & Languages"
[2]: https://www.acm.org/code-of-ethics "ACM: Code of Ethics and Professional Conduct"
[3]: https://owasp.org/www-project-web-security-testing-guide/ "OWASP: Web Security Testing Guide"
[4]: https://www.cisa.gov/resources-tools/programs/coordinated-vulnerability-disclosure-program "CISA: Coordinated Vulnerability Disclosure Program"
[5]: https://certcc.github.io/CERT-Guide-to-CVD/ "CERT/CC: Guide to Coordinated Vulnerability Disclosure"
[6]: https://attack.mitre.org/ "MITRE: ATT&CK Knowledge Base"
[7]: https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center "NIST: NICE Framework Resource Center"
