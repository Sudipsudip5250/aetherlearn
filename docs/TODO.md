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

- [x] Create the native Android/Kotlin/Jetpack Compose application under `android/` with API 26 minimum support.
- [x] Implement exactly four bottom destinations: Learn, Practice, Search, and Progress, with Settings accessible from the shell.
- [x] Implement light/dark theme, system text scaling, visible focus semantics, and accessible labels/content descriptions.
- [x] Implement app-private SQLite storage abstraction with schema-versioned metadata, separated from content assets.
- [x] Add a first-run privacy screen explaining no-account use, on-device progress, and data not collected.
- [x] Verify that the manifest requests no network permission and that no analytics, advertising, account, or backend dependency enters the core app.
- [x] Add local asset readiness for the five M1 modules and document Android build/run steps in `android/README.md`.
- [ ] **Checkpoint M2:** Debug APK builds successfully; fresh-install device/emulator smoke testing for launch, navigation, lifecycle, and privacy persistence remains to be completed.

## M3 — Offline learning loop

- [x] Load and parse the five bundled core lesson assets offline.
- [x] Render title, objectives, prerequisites, availability, explanation, worked example, common mistakes, offline practice, knowledge check, project, accessibility, safety, further reading, and change log sections.
- [x] Implement versioned SQLite tables for module progress, quiz attempts, notes, and bookmarks without breaking M2 metadata.
- [x] Implement not-started, in-progress, and completed states with Learn and Progress indicators.
- [x] Implement knowledge checks with answer feedback, explanations, retry behavior, attempt counts, and best scores.
- [x] Implement private local notes and lesson bookmarks, surfaced in Progress.
- [x] Implement offline title/body search over all five lessons.
- [x] Implement a simple offline Practice tab listing each lesson’s exercise.
- [ ] **Checkpoint M3:** Device/emulator smoke testing must confirm the complete offline journey, persistence after restart, and accessibility behavior; static build, test, lint, and repository checks pass.

## M4 — Exports and optional packs

- [x] Implement user-initiated Markdown and JSON export through the Android file picker.
- [x] Add a clear personal-notes warning and confirm that no export is automatic or uploaded.
- [x] Implement a local, pre-bundled optional-pack listing and install/delete lifecycle.
- [x] Validate optional-pack checksum, schema version, manifest ID, version, and name before activation.
- [x] Implement staging, atomic activation, and last-known-good rollback for local optional packs.
- [x] Protect the core five-module pack and preserve learning data when an optional pack is deleted.
- [x] Add Settings storage accounting and core/optional-pack status UI.
- [ ] Implement real network download, pause, resume, and re-download flows; deferred by M4 scope.
- [ ] **Checkpoint M4:** Device/emulator smoke testing must confirm file-picker export, restart persistence, optional-pack install/delete, rollback behavior, and core-content protection; static build, test, lint, and repository checks pass.

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
