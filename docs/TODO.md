# AetherLearn MVP TODO

This file is the recoverable task list. A task is complete only when its checkbox is checked, the listed evidence exists, and any decision or risk update has been recorded in `PLAN.md`.

## How to resume

At the start of each work session, read `PLAN.md`, inspect the first unchecked P0 task, check the repository state, and run the smallest relevant validation command. Do not begin a later milestone while a required earlier checkpoint is incomplete. If a task is blocked, record the blocker and next action in the session notes or issue tracker rather than silently skipping it.

## M0 — Repository and governance

- [x] Create the repository with the selected open-source license; MIT is now committed in `LICENSE`.
- [x] Add the planning documents under `docs/` and keep the repository root focused on project entry points and governance.
- [x] Add `CONTRIBUTING.md`, `CODE_OF_CONDUCT.md`, `SECURITY.md`, and content-review instructions.
- [x] Add issue templates for code, content, accessibility, and safety plus a pull-request template.
- [x] Add GitHub Actions checks for local Markdown links, secret patterns, content frontmatter, stable IDs, prerequisites, sizes, checksums, deterministic pack builds, and unit tests.
- [x] **Checkpoint M0:** A new contributor can understand the project, run the repository checks, and submit a safe sample contribution.

## M1 — Content contract and sample pack

- [x] Define the lesson frontmatter schema and required body sections in `content/README.md`.
- [x] Define stable module IDs, the canonical 20-module registry, prerequisites, availability labels, risk tiers, and asset-size rules.
- [x] Implement `scripts/validate_content.py` and `scripts/build_pack.py`.
- [x] Implement deterministic manifest versioning, per-file SHA-256 checksums, total-size reporting, and pack metadata.
- [x] Author five representative modules: DL-01, DL-05, PY-01, PY-02, and DEV-01.
- [x] Add knowledge-check content with explanations for every answer in the five sample modules.
- [x] Add content QA and review-status rules to `content/README.md` and `CONTRIBUTING.md`.
- [x] **Checkpoint M1:** The five-module sample pack builds deterministically, validates cleanly, and has unit coverage for unknown prerequisites, cycles, broken links, tampered manifests, and the valid pack path.

## M2 — Android shell

- [ ] Create the native Android/Kotlin/Jetpack Compose application.
- [ ] Implement Learn, Practice, Search, Progress, and Settings navigation.
- [ ] Implement light/dark theme, system text scaling, visible focus, reduced-motion behavior, and accessible labels.
- [ ] Implement app-private storage abstraction and schema versioning.
- [ ] Add a privacy screen explaining local-first behavior and what the app does not collect.
- [ ] Verify that no analytics, advertising, account, or network-required dependency enters the core app.
- [ ] **Checkpoint M2:** Fresh install launches into the app shell on the reference Android device and survives rotation, backgrounding, process termination, and restart.

## M3 — Offline learning loop

- [ ] Import and activate the signed core content pack.
- [ ] Render objectives, prerequisites, estimated time, availability, content, examples, and exercises.
- [ ] Implement local completion state, quiz attempts, best score, and retry behavior.
- [ ] Implement notes and bookmarks stored locally.
- [ ] Build the SQLite full-text search index from the active pack.
- [ ] Add storage usage and content-pack status screens.
- [ ] Test fresh install, airplane mode, interrupted startup, missing assets, invalid pack, and low-storage paths.
- [ ] **Checkpoint M3:** A learner completes the first strand in airplane mode, searches offline, saves a note and bookmark, restarts the app, and sees preserved state.

## M4 — Exports and optional packs

- [ ] Implement user-initiated Markdown and JSON export.
- [ ] Add export warnings and confirm that no export is automatic.
- [ ] Implement optional pack download, pause, resume, delete, and re-download.
- [ ] Validate size declarations, checksums, schema versions, and minimum app version.
- [ ] Implement temporary download location and atomic activation.
- [ ] Preserve the last known-good pack after corruption, interruption, or incompatible update.
- [ ] **Checkpoint M4:** Export and optional-pack flows work without an account; invalid or incomplete packs cannot replace valid content.

## M5 — Termux pilot

- [ ] Define the versioned exercise-wrapper contract and allowlist.
- [ ] Select two or three benign local exercises from PY-02, DEV-01, and DEV-02.
- [ ] Implement Termux package detection and clear setup guidance.
- [ ] Implement explicit confirmation showing wrapper ID, path, arguments, working directory, prerequisites, and expected effects.
- [ ] Implement the native RUN_COMMAND handoff without arbitrary shell text.
- [ ] Add manual copy-and-run fallback and in-app alternative.
- [ ] Add validated result-file import or explicit learner confirmation; do not trust terminal output as completion proof.
- [ ] Fuzz unknown exercise IDs, altered arguments, malformed result files, replayed nonces, and oversized imports.
- [ ] **Checkpoint M5:** Termux present, absent, denied-permission, misconfigured, and successful paths work without executing an unallowlisted command.

## M6 — Web/PWA fallback

- [ ] Build a static web reader using the same content-pack contract.
- [ ] Add service-worker caching for the app shell and explicit IndexedDB storage for cached content packs.
- [ ] Implement offline lesson reading, one practice flow, and local-only state in the browser.
- [ ] Add a clear message that full native Termux integration is Android-only in the MVP.
- [ ] Test cached use in a desktop browser and Android browser with the network disabled.
- [ ] **Checkpoint M6:** Core reading and practice remain usable offline after the user has cached the content pack.

## M7 — Release hardening

- [ ] Run the Android device matrix, including a low-memory or aggressive-battery device.
- [ ] Run accessibility checks for text scaling, focus, screen reader labels, contrast, touch targets, and reduced motion.
- [ ] Run privacy and network inspection to verify no learning data leaves the device.
- [ ] Run content and safety review for all 20 modules.
- [ ] Run dependency and release-artifact checks; document limitations.
- [ ] Prepare release notes, installation instructions, content-pack recovery instructions, and contributor handoff.
- [ ] Publish checksums and signed release metadata.
- [ ] **Checkpoint M7:** All release gates in `PLAN.md` pass, or each exception has an owner, rationale, mitigation, and follow-up issue.

## Deferred backlog

- [ ] Portable encrypted backup with tested key ownership and recovery.
- [ ] iOS client.
- [ ] On-device semantic search.
- [ ] General-purpose in-app language runtimes.
- [ ] Advanced network, security, AI, systems, and research tracks.
- [ ] Community contribution UI.
- [ ] Optional privacy-preserving sync proposal.

## Session log template

```text
Date:
Contributor:
Milestone/task:
Evidence produced:
Validation run:
Decision or risk update:
Blocker:
Next exact action:
```
