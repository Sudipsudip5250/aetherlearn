# AetherLearn MVP Delivery Plan

## Objective

Ship a maintainable first public release of AetherLearn: a phone-first native Android learning app with a bounded offline curriculum, local progress and notes, offline search, safe in-app practice, optional Termux exercises, and a secondary web/PWA reader using the same content contracts.

## Delivery principles

The project is delivered in vertical slices. Each milestone must produce something testable on a phone. No milestone may expand the MVP curriculum or introduce a backend without a decision-log entry. The core path must remain usable without an account or network after the core pack is installed.

## Milestones

| Milestone | Outcome | Definition of done |
|---|---|---|
| M0: Repository and governance | Repository, license, contribution rules, document set, issue templates, and safety contact | Documents are versioned; license is selected; contributor and safety-review paths are published; CI can validate Markdown/frontmatter |
| M1: Content contract and sample pack | Five representative modules, schema, validation CLI, manifest, checksums, and sample assets | A clean pack builds deterministically; invalid frontmatter, duplicate IDs, broken prerequisites, oversized assets, and broken links fail validation |
| M2: Android shell | Installable Android app with navigation, themes, accessibility baseline, and settings | Fresh install opens Learn, Practice, Search, Progress, and Settings; state survives restart; no account or analytics SDK is present |
| M3: Offline learning loop | Core pack import, lesson reader, quizzes, progress, notes, bookmarks, and full-text search | The first strand can be completed in airplane mode; search works offline; state survives process termination; acceptance targets in `PRODUCT_SPEC.md` are measured |
| M4: Exports and content-pack updates | Markdown/JSON export, optional pack lifecycle, validation, rollback, and storage controls | User-initiated export works offline; a valid optional pack can be installed and deleted; corrupted or incomplete packs never replace the active pack |
| M5: Termux pilot | Two or three safe local exercises with explicit confirmation, allowlisted wrappers, and manual fallback | Termux present, absent, misconfigured, and denied-permission paths are tested; no arbitrary command execution is possible through the app |
| M6: Web/PWA fallback | Static web reader and practice client with shared content pack and offline cache | Core lessons and at least one practice flow work in a supported desktop and Android browser after caching; no user learning data is sent to a server |
| M7: Release hardening | Device matrix, accessibility review, security review, content review, release notes, and reproducible build notes | All P0/P1 defects are closed or documented; release checklist passes; known limitations and recovery steps are public |

## Current status — 2026-08-24

M0 is complete. The repository has a root governance surface, documentation under `docs/`, references under `docs/references/`, an MIT license, contribution and security policies, community templates, and a GitHub Actions quality workflow. M1 is complete for the initial slice: the canonical 20-module registry, lesson contract, validator, deterministic pack builder, five representative lessons, and focused unit tests are present.

M2 is implemented as a native Android shell under `android/`. The project uses Kotlin, Jetpack Compose, Material 3, AGP 9.2.0, Gradle 9.4.1, Kotlin 2.3.21, Compose BOM 2026.08.00, compile/target SDK 37, and min SDK 26. It includes four bottom destinations, Settings, theme override, first-run privacy disclosure, schema-versioned SQLite metadata, and local assets for the five sample modules. `assembleDebug`, `test`, and `lintDebug` all pass in the configured SDK environment. The generated APK is 12 MB with SHA-256 `f6e1f83a0359056d589caa734038216c1824b77dd4e6c4a5550058b50be9e9f6`. No Android device or emulator is attached to this sandbox, so install and runtime smoke testing remains the final M2 gate.

## Suggested sequencing

M0 and M1 establish governance and content contracts before application work. M2 and M3 build the smallest useful product. M4 adds portability and safe updates. M5 proves the differentiator without allowing Termux to become a dependency. M6 provides secondary web access. M7 is a release gate, not an optional cleanup phase.

## Dependencies

The Android client depends on the content-pack schema and validation output. Search depends on stable module IDs and generated searchable text. Termux depends on a native Android integration layer, a versioned exercise-wrapper contract, and safety review. Optional pack updates depend on manifest validation and last-known-good rollback. The PWA depends on the same content source but not on native Termux capabilities.

## Risk register

| Risk | Likelihood | Impact | Mitigation | Verification |
|---|---:|---:|---|---|
| Curriculum expands beyond maintainable scope | High | High | Freeze 20-module MVP list; require decision log for additions | Review every pull request against `CURRICULUM.md` |
| Termux behavior differs by version or installation source | Medium | High | Optional integration, manual fallback, version detection, no completion trust | Test supported versions and missing-permission paths |
| Arbitrary command or unsafe input crosses the bridge | Medium | Critical | Allowlisted wrappers, fixed arguments, explicit confirmation, no raw shell text | Negative tests and code review of every wrapper |
| Content pack or update is corrupted | Medium | High | Checksums, schema validation, atomic activation, last-known-good pack | Corruption and interrupted-download tests |
| Local exports expose private notes | Medium | Medium | User-initiated export, clear warnings, future encrypted backup design | Inspect share/export flows and generated files |
| Large assets exhaust phone storage | Medium | Medium | Declared sizes, storage budget, optional packs, deletion controls | Low-storage and interrupted-download tests |
| Accessibility is deferred until late | Medium | High | Accessibility criteria in every milestone; manual testing from M2 | Text scaling, focus, screen reader, contrast, reduced-motion checks |
| Content becomes inaccurate or stale | Medium | High | Technical and pedagogical review, version metadata, last-reviewed field | Content QA and periodic review queue |
| Lack of operating sustainability | Medium | High | Document volunteer, donation, sponsorship, and mirror model | Publish operating-cost assumptions and ownership |

## Release gates

A release cannot proceed if the core learning path requires a cloud account, if local learning data is transmitted automatically, if a corrupted pack can replace a valid pack, if the Termux bridge can execute arbitrary app-provided shell text, or if a Termux lesson has no fallback. A release also cannot claim accessibility or security conformance from a single automated scan; manual checks are required.

## Immediate implementation sequence

The first implementation slice should create the repository, license, content schema, module validator, and one vertical lesson flow. The second slice should render a small pack in the Android shell and persist a local completion state. The third slice should add notes, bookmarks, search, and quiz attempts. Only after this offline loop works should the Termux pilot be implemented.

## Decision log

| ID | Date | Decision | Rationale | Revisit trigger |
|---|---|---|---|---|
| D-001 | 2026-08-24 | Use AetherLearn as the working name | Clear enough for documents while final name availability is checked separately | Name search, trademark conflict, or community objection |
| D-002 | 2026-08-24 | Native Android/Kotlin/Jetpack Compose is the MVP client | Direct Android integration and smallest trust boundary for Termux, deep links, storage, and lifecycle | iOS becomes a release-blocking requirement or Android implementation proves unmaintainable |
| D-003 | 2026-08-24 | PWA is secondary, not the primary client | PWA supports offline reading but cannot be the trusted Termux bridge | Browser capabilities materially improve or product focus changes to web-first |
| D-004 | 2026-08-24 | No backend, accounts, sync, or analytics in MVP | Preserves local-first privacy and reduces operational and security scope | Users explicitly approve a privacy-preserving sync design in a later proposal |
| D-005 | 2026-08-24 | MVP curriculum is fixed at 20 modules across four strands | Demonstrates a complete progression without attempting encyclopedic coverage | Content review capacity and learner evidence justify a controlled expansion |
| D-006 | 2026-08-24 | No general-purpose in-app Python runtime in MVP | Reduces code-execution risk and device resource demands | A separate sandbox threat model and implementation pass review |
| D-007 | 2026-08-24 | Termux exercises use allowlisted wrappers and explicit confirmation | Prevents the learning app from becoming an arbitrary command launcher | A future signed plugin model passes security review |
| D-008 | 2026-08-24 | Encrypted portable backup is post-MVP | Device-bound key storage alone does not solve cross-device recovery | Key ownership, recovery, password handling, and export tests are specified |
| D-009 | 2026-08-24 | Keep governance documents in `docs/`, implementation under `content/` and `scripts/`, and repository entry points in the root | Separates stable project guidance from executable/content assets and keeps future app code discoverable | A future app scaffold requires a revised top-level layout |
| D-010 | 2026-08-24 | Validate prerequisites against a canonical 20-module registry, even when only five modules are authored | The sample pack must remain small without treating approved curriculum prerequisites as invalid | Curriculum scope or stable IDs change through an approved decision record |
| D-011 | 2026-08-24 | Start M2 with a native Android shell rather than a cross-platform framework | Direct control over Android lifecycle, private storage, Compose UI, and future Termux intents; iOS is deferred | iOS becomes a release-blocking requirement or Android implementation proves unmaintainable |
| D-012 | 2026-08-24 | Use AGP 9.2.0 with built-in Kotlin support and pin Gradle 9.4.1, Kotlin 2.3.21, Compose BOM 2026.08.00, compile/target SDK 37, and min SDK 26 | Matches the current Android documentation reviewed for this M2 build and avoids obsolete Kotlin plugin configuration | Official compatibility guidance changes or a dependency update is deliberately approved |
| D-013 | 2026-08-24 | Keep M2 storage limited to app metadata and first-run/theme state; defer learning tables to M3 | Preserves a small, testable local boundary while making schema versioning ready for progress, notes, bookmarks, and scores | M3 storage schema is approved |

## Decision ownership

The project maintainer owns product-scope decisions. A designated Android maintainer owns client architecture and release integration. A curriculum maintainer owns module sequencing and review status. A safety reviewer must approve any S2 content and all future proposals for S3 material. Decisions affecting privacy, command execution, or public release require a written record before implementation.

## References

The architecture decision is supported by Android’s documentation on intents and deep links and by the Termux RUN_COMMAND integration documentation. See the reference list in [`ARCHITECTURE.md`](ARCHITECTURE.md). The M2 build-tool findings are recorded in [`references/android_m2_build_notes.md`](references/android_m2_build_notes.md).
