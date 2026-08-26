# AetherLearn MVP Specification

AetherLearn is a working-name concept for a free, open-source, privacy-first computer-science learning platform. This repository contains the bounded v1.0 planning and governance documents for a phone-first native Android MVP with optional Termux integration and a secondary offline-capable web/PWA client.

## Start here

Read [`docs/PRODUCT_SPEC.md`](docs/PRODUCT_SPEC.md) first. It defines the primary audience, MVP boundary, user journeys, offline/online model, Termux flow, measurable acceptance criteria, and definition of done.

Then read [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for the native Android decision, content-pack design, local storage, update strategy, Termux bridge, and threat model. [`docs/CURRICULUM.md`](docs/CURRICULUM.md) lists the exact 20-module MVP curriculum. [`docs/SAFETY.md`](docs/SAFETY.md) governs dual-use topics, labs, contributions, and Termux constraints. [`docs/PLAN.md`](docs/PLAN.md) contains milestones and the decision log, while [`docs/TODO.md`](docs/TODO.md) is the recoverable implementation checklist. [`docs/VISION.md`](docs/VISION.md) preserves the long-term mission and differentiators.

## Repository status

M0 through M6 are implemented for the current feasible scope, and M7 release hardening plus the deferred M4 network-pack lifecycle are now implemented in the repository. The native Kotlin/Jetpack Compose Android app discovers eleven bundled lessons, tracks learning state and quiz attempts, stores private notes/bookmarks, searches locally, lists practice exercises, exports learning data as Markdown/JSON through a user-controlled file picker, manages validated optional packs, and provides a strict versioned Termux allowlist, package detection, explicit confirmation, guarded RUN_COMMAND handoff, fallback guidance, and learner-confirmed completion for four local-only exercises. Settings now accepts an explicit HTTPS ZIP pack URL and supports foreground download, pause/resume, retry, cancellation, bounded extraction, per-file SHA-256 validation, atomic activation, and last-known-good rollback. The `web/` fallback reuses the eleven canonical Markdown lessons, caches its shell with a versioned service worker, stores the explicit eleven-lesson pack and browser-local learning state in IndexedDB, and provides offline reading, practice, search, progress, notes, bookmarks, knowledge checks, CSP, and focus/touch-target hardening. Release signing, a public pack host, Android device/emulator runtime tests, Android-browser smoke tests, and assistive-technology validation remain open evidence or operational gates. See [`android/README.md`](android/README.md), [`web/README.md`](web/README.md), [`docs/NETWORK_PACKS.md`](docs/NETWORK_PACKS.md), [`docs/M7_RELEASE_NOTES.md`](docs/M7_RELEASE_NOTES.md), and [`docs/TERMUX_WRAPPERS.md`](docs/TERMUX_WRAPPERS.md).

## Core decisions

| Decision | Choice |
|---|---|
| Primary client | Native Android using Kotlin and Jetpack Compose |
| Web client | Secondary static PWA sharing content contracts |
| Data model | Local-first; no accounts, sync, analytics, or backend in MVP |
| Curriculum | 20 modules across four strands |
| Termux | Optional, explicit, allowlisted, and never required for the core path |
| License | MIT |

## Evidence

Platform-specific architecture notes cite Android’s official documentation and the Termux project’s RUN_COMMAND documentation. Supporting findings are kept in [`docs/references/research_notes.md`](docs/references/research_notes.md).

## Contribution direction

The content contract is documented in [`content/README.md`](content/README.md), and the sample lessons live under [`content/core/`](content/core/). Contributions should begin with the documents and checks in this repository. Code, content, accessibility, and safety changes must follow the review and checkpoint rules in `docs/PLAN.md`, `docs/TODO.md`, and `docs/SAFETY.md`. Do not submit real credentials, personal data, exploit kits, or commands targeting third-party systems.
