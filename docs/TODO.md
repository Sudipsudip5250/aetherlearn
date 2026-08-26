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

- [x] Load and parse the eleven bundled core lesson assets offline.
- [x] Render title, objectives, prerequisites, availability, explanation, worked example, common mistakes, offline practice, knowledge check, project, accessibility, safety, further reading, and change log sections.
- [x] Implement versioned SQLite tables for module progress, quiz attempts, notes, and bookmarks without breaking M2 metadata.
- [x] Implement not-started, in-progress, and completed states with Learn and Progress indicators.
- [x] Implement knowledge checks with answer feedback, explanations, retry behavior, attempt counts, and best scores.
- [x] Implement private local notes and lesson bookmarks, surfaced in Progress.
- [x] Implement offline title/body search over all eleven lessons.
- [x] Implement a simple offline Practice tab listing each lesson’s exercise.
- [ ] **Checkpoint M3:** Device/emulator smoke testing must confirm the complete offline journey, persistence after restart, and accessibility behavior; static build, test, lint, and repository checks pass.

## M4 — Exports and optional packs

- [x] Implement user-initiated Markdown and JSON export through the Android file picker.
- [x] Add a clear personal-notes warning and confirm that no export is automatic or uploaded.
- [x] Implement a local, pre-bundled optional-pack listing and install/delete lifecycle.
- [x] Validate optional-pack checksum, schema version, manifest ID, version, and name before activation.
- [x] Implement staging, atomic activation, and last-known-good rollback for local optional packs.
- [x] Protect the core eleven-module pack and preserve learning data when an optional pack is deleted.
- [x] Add Settings storage accounting and core/optional-pack status UI.
- [x] Implement real user-initiated HTTPS network download, pause, resume, retry, cancellation, and re-download flows in the Android Settings surface.
- [x] Stream downloads into app-private partial storage, resume with HTTP Range when supported, reject cleartext/credentialed URLs, cap transfer sizes, validate ZIP paths/manifest/lesson hashes, and activate atomically with last-known-good rollback.
- [ ] **Checkpoint M4:** Device/emulator smoke testing must confirm file-picker export, restart persistence, optional-pack install/delete, interrupted transfer recovery, rollback behavior, and core-content protection; static build, test, lint, and repository checks pass.

## M5 — Termux pilot

- [x] Define the versioned exercise-wrapper contract and strict allowlist in [`TERMUX_WRAPPERS.md`](TERMUX_WRAPPERS.md) and `TermuxWrappers.kt`.
- [x] Select four benign local exercises: `py-02-local-expressions`, `py-03-local-variables-output`, `py-05-local-loop-trace`, and `dev-01-safe-navigation`. All are fixed, local-only, learner-confirmed exercises with in-app fallbacks.
- [x] Implement Termux package detection and clear setup guidance without automatic permission changes.
- [x] Implement explicit confirmation showing wrapper ID, path, arguments, working directory, prerequisites, expected effects, and fallback.
- [x] Implement the native RUN_COMMAND handoff with only fixed, validated arguments and no arbitrary shell text.
- [x] Add a manual/in-app fallback path that keeps the lesson completable without Termux.
- [x] Use learner-confirmed completion for the pilot; no process callback or terminal output is trusted as proof.
- [x] Add focused negative tests for unknown IDs, altered arguments, altered executable/path, and contract-version tampering.
- [x] Add code-level handling for missing Termux, denied permission, and unsupported/misconfigured service paths.
- [ ] Run device/emulator runtime tests for missing Termux, denied permission, misconfiguration, and successful handoff; no device/emulator is available in this environment.
- [ ] **Checkpoint M5:** Device/emulator evidence must confirm Termux present/absent, denied-permission, misconfigured, fallback, and successful paths without executing an unallowlisted command.

## M6 — Web/PWA fallback

- [x] Build the initial static web shell under `web/` with responsive, keyboard-accessible HTML/CSS/JavaScript.
- [x] Reuse the eleven canonical Markdown lessons through `web/content/manifest.json` and the shared frontmatter/section parser; `scripts/check_web_content.py` prevents payload drift.
- [x] Add the initial lesson list and full reader route with objectives, metadata, sections, safe inline Markdown rendering, and external-link handling.
- [x] Add a clear privacy note and message that full native Termux integration is Android-only in the MVP.
- [x] Add service-worker caching for the app shell and explicit IndexedDB storage for cached content packs; updates stage before activating the new pack.
- [x] Implement offline lesson reading, one practice flow, and local-only progress/notes/bookmarks in the browser.
- [x] Implement simple local search over the eleven modules.
- [x] Test cached use in a Chromium desktop browser with the local server stopped: catalog, reader, practice, search, progress, quiz, note, bookmark, completion, and reload persistence all worked offline.
- [ ] Repeat the cached offline smoke test in an Android browser; no Android browser or device is attached to this environment.
- [x] **Checkpoint M6:** After explicitly caching the core pack, core reading and practice remained usable offline in the verified desktop browser; the Android-browser runtime evidence gate remains open.

## Content expansion — batch 1

- [x] Author and validate DL-02, DL-03, DL-04, PY-03, PY-04, and PY-05 against the existing lesson contract.
- [x] Synchronize all eleven canonical Markdown lessons into Android assets and the web payload; preserve byte-parity checks.
- [x] Update Android catalog discovery and web manifest/runtime discovery without changing the lesson schema or existing learning-state keys.
- [x] Add only the narrowly required safe local wrappers for the new `termux-optional` lessons and retain manual fallbacks.
- [x] Correct stale five-module wording and align `docs/CURRICULUM.md` with the effective `mvp-20` registry.
- [ ] Next content batch: PY-06, PY-07, and the approved algorithms strand, after this batch receives pedagogical and safety review.
- [ ] Deferred: DEV-04 and DEV-05 remain outside the validator-backed 20-module registry and are not authored in this slice.

## M7 — Release hardening

- [ ] Run the Android device matrix, including a low-memory or aggressive-battery device.
- [x] Implement web accessibility hardening for text scaling/reflow, focus restoration, semantic labels/live regions, touch targets, forced colors, reduced motion, restrictive CSP, and safe external links; manual screen-reader and Android-browser checks remain open.
- [x] Implement the privacy/network boundary: explicit HTTPS-only pack requests, no cookies or credentials, no learning-data request fields, cleartext denial, no broad external-storage permissions, and no analytics path. Physical network inspection remains open.
- [ ] Run content and safety review for all 20 modules; only 11 modules are authored so far.
- [x] Add dependency/build/release-artifact gates, including Android debug/release build, unit tests, lint, manifest boundary checks, and APK SHA-256 sidecars.
- [x] Prepare release notes, installation instructions, content-pack recovery instructions, and contributor handoff in `docs/M7_RELEASE_NOTES.md` and `docs/NETWORK_PACKS.md`.
- [ ] Publish signed release metadata; unsigned release artifacts and checksums are verified, but no authorized signing key or public pack host is configured.
- [ ] **Checkpoint M7:** Static and CI gates pass; device matrix, Android-browser, assistive-technology, full-20-module review, signed metadata, and production distribution gates remain explicitly assigned as release follow-ups.

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
