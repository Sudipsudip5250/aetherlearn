# AetherLearn MVP Specification

AetherLearn is a working-name concept for a free, open-source, privacy-first computer-science learning platform. This repository contains the bounded v1.0 planning and governance documents for a phone-first native Android MVP with optional Termux integration and a secondary offline-capable web/PWA client.

## Start here

Read [`docs/PRODUCT_SPEC.md`](docs/PRODUCT_SPEC.md) first. It defines the primary audience, MVP boundary, user journeys, offline/online model, Termux flow, measurable acceptance criteria, and definition of done.

Then read [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md) for the native Android decision, content-pack design, local storage, update strategy, Termux bridge, and threat model. [`docs/CURRICULUM.md`](docs/CURRICULUM.md) lists the exact 20-module MVP curriculum. [`docs/SAFETY.md`](docs/SAFETY.md) governs dual-use topics, labs, contributions, and Termux constraints. [`docs/PLAN.md`](docs/PLAN.md) contains milestones and the decision log, while [`docs/TODO.md`](docs/TODO.md) is the recoverable implementation checklist. [`docs/VISION.md`](docs/VISION.md) preserves the long-term mission and differentiators.

## Repository status

M0 and the first M1 slice are complete. The repository now contains specifications, governance, a canonical content contract, a deterministic validator and pack builder, five representative lessons, focused tests, and a CI workflow. The application implementation has not started; M2 is the next milestone.

## Core decisions

| Decision | Choice |
|---|---|
| Primary client | Native Android using Kotlin and Jetpack Compose |
| Web client | Secondary static PWA sharing content contracts |
| Data model | Local-first; no accounts, sync, analytics, or backend in MVP |
| Curriculum | 20 modules across four strands |
| Termux | Optional, explicit, allowlisted, and never required for the core path |
| License | MIT by default, pending repository-owner confirmation |

## Evidence

Platform-specific architecture notes cite Android’s official documentation and the Termux project’s RUN_COMMAND documentation. Supporting findings are kept in [`docs/references/research_notes.md`](docs/references/research_notes.md).

## Contribution direction

The content contract is documented in [`content/README.md`](content/README.md), and the sample lessons live under [`content/core/`](content/core/). Contributions should begin with the documents and checks in this repository. Code, content, accessibility, and safety changes must follow the review and checkpoint rules in `docs/PLAN.md`, `docs/TODO.md`, and `docs/SAFETY.md`. Do not submit real credentials, personal data, exploit kits, or commands targeting third-party systems.
