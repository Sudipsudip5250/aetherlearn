# AetherLearn final content consistency review

**Review date:** 2026-08-26
**Scope:** the stable 20-module `mvp-20` baseline plus the approved Stage 1 and Stage 2 slices, four Stage 3 defensive-security drafts, and four Stage 4 Web/data drafts
**Review type:** contract, source, safety-boundary, and cross-client consistency review; not a substitute for final human pedagogical or safety approval

## Outcome

The 35 canonical lessons pass the repository content contract and prerequisite-graph checks. Every module has the required frontmatter, required body sections, at least three numbered knowledge-check prompts with inline answers and explanations, an offline-practice section, accessibility guidance, safety guidance, further reading, and a current change-log entry. All current modules remain within the requested **S0/S1** risk range. The three Stage 1, four Stage 2, four Stage 3, and four Stage 4 lessons are explicitly marked `draft` and remain subject to human technical, pedagogical, accessibility, source, and safety review.

One real clarity issue was fixed during this review. DL-04 asked the learner to rank “fictional results” without supplying the results. The practice now includes three concrete fictional results, including a library page, a password-gated unknown page, and an opaque urgent-prize short link. The exercise also retains URL-part labeling and a rule about information never to enter into an unfamiliar page.

The audit heuristic flagged DL-04, PY-02, and DEV-01 because their practice sections were concise. Manual inspection found PY-02 and DEV-01 answerable and aligned with their objectives. DL-04 was strengthened as described above. Stage 1 uses paper or fixed fictional traces rather than live tooling. Stage 2 uses fictional test tables, repository forms, provenance records, and accessibility findings; it does not publish changes, install dependencies, or claim standards conformance. Stage 3 uses only fictional decision cards, local system models, redacted disclosure forms, and organization/source-selection worksheets; it does not contact targets, scan systems, handle credentials, publish reports, or provide operational attack detail. Stage 4 uses static fictional markup, layout cards, paper-only event/state traces, and JSON fixtures; it does not require network requests, third-party scripts, arbitrary execution, or real data. No module was rejected for a rewrite in this repository consistency pass, but all 35 remain subject to the human teaching-quality, accessibility, source, and safety review listed in the release gates.

## Module review matrix

| Module | Main consistency finding | Availability / risk | Review disposition |
|---|---|---|---|
| DL-01 | Information representation lesson; practice and three-question check are aligned. | offline / S0 | Retain; human review open |
| DL-02 | File, folder, storage, and backup concepts match the prerequisite chain. | offline / S0 | Retain; human review open |
| DL-03 | Android settings and permission examples remain conceptual and non-invasive. | offline / S0 | Retain; human review open |
| DL-04 | Practice was clarified with supplied fictional search results and explicit ranking criteria. | offline / S0 | Fixed and retained; human review open |
| DL-05 | Privacy, passwords, phishing, and update examples remain defensive and local. | offline / S0 | Retain; human review open |
| DL-06 | Historical language comparisons are high-level, sourced, and avoid obsolete-tool execution. | offline / S0 | Draft; technical and pedagogical review open |
| DL-07 | Program/file/process distinctions use a bounded fictional trace and no arbitrary execution. | offline / S0 | Draft; technical and pedagogical review open |
| DL-08 | Client/server, HTTP, and caching vocabulary uses fictional messages and no live target. | offline / S0 | Draft; technical and pedagogical review open |
| PY-01 | Algorithm and precise-instruction concepts connect to later Python work. | offline / S1 | Retain; human review open |
| PY-02 | Expression prediction is bounded, local, and answerable; Termux fallback is present. | termux-optional / S1 | Retain; human review open |
| PY-03 | Variables, types, input, and output use fictional values and a fixed local wrapper. | termux-optional / S1 | Retain; human review open |
| PY-04 | Boolean and condition practice has a clear offline path with no external effects. | offline / S1 | Retain; human review open |
| PY-05 | Loop tracing includes a stopping rule and a fixed local wrapper. | termux-optional / S1 | Retain; human review open |
| PY-06 | Functions, return values, local scope, and hand-traced tests are coherent. | termux-optional / S1 | Retain; human review open |
| PY-07 | List, dictionary, string, and summary examples use separate fictional result data. | termux-optional / S1 | Retain; human review open |
| AL-01 | Data-structure choice is introduced through operations and explicit trade-offs. | offline / S0 | Retain; human review open |
| AL-02 | Stack/queue order rules are traceable with explicit invariants. | offline / S0 | Retain; human review open |
| AL-03 | Linear search, sorted-input binary search, and sorting are distinguished. | offline / S0 | Retain; human review open |
| AL-04 | Constant, linear, and quadratic growth are presented as rough patterns, not stopwatch promises. | offline / S0 | Retain; human review open |
| AL-05 | Recursion has base-case/progress rules; tree and graph vocabulary includes cycle handling. | offline / S0 | Retain; human review open |
| DEV-01 | Terminal concepts use a fictional tree and read-only local inspection fallback. | termux-optional / S1 | Retain; human review open |
| DEV-02 | Git working tree, staging area, commits, and history are separated; no publishing is implied. | termux-optional / S1 | Retain; human review open |
| DEV-03 | Error categories, traceback clues, and minimal reproduction steps are clearly separated. | offline / S0 | Retain; human review open |
| DEV-04 | Test cases connect inputs to expected results and include boundary and invalid-input examples. | offline / S0 | Draft; technical and pedagogical review open |
| DEV-05 | Pull-request context and issue-report fields use a fictional repository and respectful, actionable feedback. | offline / S0 | Draft; technical and pedagogical review open |
| DEV-06 | Checksums, provenance, dependencies, and reproducibility are kept distinct through a fictional manifest. | offline / S1 | Draft; technical and supply-chain review open |
| DEV-07 | Open-source boundaries and WCAG principles are framed as review vocabulary, not a conformance claim. | offline / S0 | Draft; technical, accessibility, and pedagogical review open |
| SEC-01 | ACM ethics principles are applied to fictional scope and harm decisions without legal claims. | offline / S0 | Draft; technical, pedagogical, and safety review open |
| SEC-02 | OWASP threat-modeling questions are applied to a fictional local app with defensive controls only. | offline / S1 | Draft; technical, pedagogical, and safety review open |
| SEC-03 | CISA/CERT disclosure roles are practiced with a redacted fictional report and no real contact. | offline / S1 | Draft; technical, pedagogical, and safety review open |
| SEC-04 | NIST NICE and MITRE ATT&CK are distinguished as bounded vocabularies, not authority or attack instructions. | offline / S0 | Draft; technical, pedagogical, and safety review open |
| WEB-01 | Semantic elements, labels, source order, and native controls are practiced with fictional offline markup. | offline / S0 | Draft; technical, accessibility, and pedagogical review open |
| WEB-02 | CSS box-model and responsive-layout concepts use fixed local cards without a live browser exercise. | offline / S0 | Draft; technical, accessibility, and pedagogical review open |
| WEB-03 | Event/state vocabulary is taught through paper traces; arbitrary JavaScript execution and network access are excluded. | offline / S1 | Draft; technical, accessibility, privacy, and pedagogical review open |
| WEB-04 | JSON types, fixture validation, and privacy boundaries are taught with fictional local data; syntax is not treated as application validation. | offline / S1 | Draft; technical, accessibility, privacy, and pedagogical review open |

## Termux consistency

The seven `termux-optional` lessons are mapped to seven immutable registry entries: `py-02-local-expressions`, `py-03-local-variables-output`, `py-05-local-loop-trace`, `py-06-local-functions`, `py-07-local-data-summary`, `dev-01-safe-navigation`, and `dev-02-local-git-version`. Each wrapper uses a fixed executable path, fixed argument array, dedicated practice directory, no network, and learner-confirmed completion. Every lesson supplies an offline fallback, and the Android reader’s confirmation dialog exposes the wrapper ID, contract version, executable, arguments, working directory, prerequisites, expected effects, and fallback.

The static audit does not prove that every Termux package, permission, service configuration, or device behavior works. Those are device-only checks and are covered in [`DEVICE_TEST_CHECKLIST.md`](DEVICE_TEST_CHECKLIST.md).

## Verification record

The following checks were completed for this review:

| Check | Result |
|---|---|
| Canonical registry membership and exact titles | 35/35 aligned; original `mvp-20` order and IDs preserved |
| Required frontmatter and body sections | 35/35 passed |
| Prerequisite names and ordering | No unknown or forward prerequisite found |
| Knowledge checks | 35/35 have at least three answer/explanation prompts |
| Availability and risk scope | All modules are offline or termux-optional; all are S0/S1 |
| Canonical source validation | Passed `scripts/validate_content.py` with schema-v2 registry |
| Android content parity | Passed `scripts/check_android_content.py` and direct comparisons |
| Web content parity | Passed `scripts/check_web_content.py` and direct comparisons |
| Source record | Stage 1–4 claims and URLs recorded in the staged reference notes |
| Safety/privacy scan | Stage 3 contains no operational security detail; Stage 4 contains no network, real-data, arbitrary-execution, or third-party-script requirement |
| New clarity issue | DL-04 corrected in the prior review; Stage 4 added with no automated contract issue |

## Remaining decision

The content is **repository-consistent and client-ready for controlled testing**, but it is not yet human-approved for public release. A reviewer should read all 35 current lessons for age-appropriate pacing, cultural and linguistic clarity, factual accuracy, accessibility of examples, and safety framing, with particular attention to the fifteen post-MVP drafts and their source wording. The Stage 3 safety boundary must be reviewed by a maintainer who can assess dual-use risk; if any lesson becomes S2, it requires the documented S2 safety review before release. Findings should be fixed in place through the current stage process; Stage 5 must not be added until Stage 4 is closed and documented. The release owner should record approval or required rewrites before signing or publicly distributing an artifact.

## References

- [`content/README.md`](../content/README.md) — M1 Content Contract.
- [`content/curriculum.yml`](../content/curriculum.yml) — canonical schema-v2 registry preserving `mvp-20` and approved stages.
- [`scripts/validate_content.py`](../scripts/validate_content.py) — structural and graph validator.
- [`docs/TERMUX_WRAPPERS.md`](TERMUX_WRAPPERS.md) — fixed Termux wrapper contract.
- [`docs/DEVICE_TEST_CHECKLIST.md`](DEVICE_TEST_CHECKLIST.md) — human runtime and accessibility procedure.
