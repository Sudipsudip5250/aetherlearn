# Future curriculum implementation plan

**Status:** Stage 1 approved and in execution; later stages remain sequentially gated by completion, review, and validation checkpoints.

## Decision boundary

The current AetherLearn MVP remains a validated, frozen 20-module curriculum. The roadmap is not a license to add an encyclopedic catalog. A future implementation must begin with an explicit decision-log entry that names the approved batch, changes the curriculum scope intentionally, identifies maintainers and reviewers, and accepts the additional Android/Web parity and long-term source-maintenance burden.

The user-approved Stage 1 slice is now intentionally represented in `content/curriculum.yml`, `content/core/`, Android assets, and the Web payload after the registry-policy change. All later stages remain planning-only until their own exact module list is recorded in `docs/PLAN.md` and their prior stage has passed its checkpoint.

## Recommended staged implementation

| Stage | Proposed scope | Suggested size | Why this order | Required reviewers | Current status |
|---|---|---:|---|---|---|
| 0 | Release the current 20-module MVP and complete device, accessibility, pedagogical, and safety gates | 0 new modules | Establish a trustworthy baseline before expanding product scope | Release owner, device tester, pedagogical reviewer, safety reviewer | Baseline complete; human release gates remain |
| 1 | Computing history and systems vocabulary | 3 lessons | Extends digital literacy without requiring risky tooling or external services | Technical and pedagogical reviewers | Approved and executing |
| 2 | Software engineering and open-source practice | 3–4 lessons | Builds directly on Git and debugging already taught | Technical, pedagogical, and accessibility reviewers | Gated pending Stage 1 checkpoint |
| 3 | Ethical security, disclosure, and organization literacy | 3–4 lessons | Adds security context only after the safety governance and source process are exercised | Technical, pedagogical, safety, and source reviewers | Gated pending Stage 2 checkpoint |
| 4 | Web/data foundations and local-only application practice | 3–5 lessons | Adds browser and data concepts with static/local fixtures | Technical, pedagogical, accessibility, and privacy reviewers | Gated pending Stage 3 checkpoint |
| 5 | Historical security case studies and career orientation | 2–4 lessons | Depends on stable source review, careful framing, and a named maintainer | Historical/source, pedagogical, safety, and accessibility reviewers | Gated pending Stage 4 checkpoint |

The table is a delivery sequence, not blanket approval of new modules. Stage 1 is the sole approved post-MVP slice at this checkpoint; Stages 2–5 begin only after the previous stage is complete, validated, reviewed, committed, and documented.

## Stage 1: computing history and systems vocabulary

The approved Stage 1 slice is:

1. **`dl-06-computing-language-history` — How programming languages reflect constraints.** Compare language design choices, historical problem domains, and high-level compiled/interpreted/declarative vocabulary without requiring a compiler installation.
2. **`dl-07-how-programs-run` — How a computer runs a program.** Introduce source text, translation, processes, files, and error stages through fixed local traces.
3. **`dl-08-networks-web-concepts` — Networks and the Web: requests, responses, and resources.** Introduce client/server, HTTP messages, resources, and caching through a fictional written trace.

All three lessons are offline, use bounded synthetic examples, and are currently marked S0. The remaining Stage 1 ideas—DNS depth and data storage/serialization—are deferred until a later stage decision because the approved slice must remain small and coherent.

## Stage 2: software engineering and open source

Candidate lessons cover test cases for pure functions, readable interfaces and error handling, code review and issue reports, dependency provenance and reproducible builds, versioning and release notes, accessibility acceptance criteria, and open-source governance. Exercises should use fixture repositories and synthetic files. They must not require publishing personal information, copying secrets, or connecting to a third-party account.

## Stage 3: ethics, security, and organizations

Candidate lessons cover permission and scope, threat modeling, harm minimization, evidence handling, coordinated vulnerability disclosure, defensive incident analysis, and organization literacy. Organizations may include ACM, OWASP, CISA/CVE, CERT/SEI, MITRE, NIST NICE, IETF/W3C, and open-source foundations. The learner outcome should be the ability to distinguish an authority’s role, read a source critically, draft a bounded report, or map a fictional incident to a mitigation.

A security lesson must use synthetic data, toy applications, or written fixtures. It must not include public-target scanning, credential capture, exploit kits, malware, persistence, evasion, bypass instructions, or real-target testing. MITRE ATT&CK may be used as a defensive vocabulary, not as an attack recipe. Historical “hacker” case studies should focus on impact, affected stakeholders, response, ethics, and defensive lessons; they should not glorify criminal groups or reproduce operational details.

## Stage 4: web and data foundations

Candidate lessons cover HTML structure, CSS presentation, JavaScript behavior, HTTP requests, browser accessibility, data modeling, SQL concepts, serialization, and local APIs. The first exercises should be static or loopback-only, use bounded fixtures, and require no account. Any network-optional activity must state the destination, data flow, failure mode, and offline alternative before the learner starts.

## Stage 5: case studies and career orientation

Case studies require a source table with publication date, update date, uncertainty, affected parties, and defensive response. Career content should use frameworks such as NIST NICE to describe skills and role families, not to promise jobs, salaries, legal outcomes, or regional portability. The project should name a maintainer for source freshness and a reviewer for historical and cultural framing.

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

Stage 1 is approved by the user and is being authored one lesson at a time under the exact three-module scope above. After its automated, parity, and human-review checkpoint is recorded, the next stage may be proposed and implemented using the same source, safety, registry, client, and release process. Approval to continue later does not waive those per-stage gates.
