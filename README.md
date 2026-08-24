# AetherLearn MVP Specification

AetherLearn is a working-name concept for a free, open-source, privacy-first computer-science learning platform. This repository contains the bounded v1.0 planning and governance documents for a phone-first native Android MVP with optional Termux integration and a secondary offline-capable web/PWA client.

## Start here

Read [`PRODUCT_SPEC.md`](PRODUCT_SPEC.md) first. It defines the primary audience, MVP boundary, user journeys, offline/online model, Termux flow, measurable acceptance criteria, and definition of done.

Then read [`ARCHITECTURE.md`](ARCHITECTURE.md) for the native Android decision, content-pack design, local storage, update strategy, Termux bridge, and threat model. [`CURRICULUM.md`](CURRICULUM.md) lists the exact 20-module MVP curriculum. [`SAFETY.md`](SAFETY.md) governs dual-use topics, labs, contributions, and Termux constraints. [`PLAN.md`](PLAN.md) contains milestones and the decision log, while [`TODO.md`](TODO.md) is the recoverable implementation checklist. [`VISION.md`](VISION.md) preserves the long-term mission and differentiators.

## Repository status

This repository currently contains specifications and governance materials, not the application implementation. The next implementation slice is M0/M1: repository governance, the content schema, a deterministic validator, a sample content pack, and one vertical lesson flow.

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

Platform-specific architecture notes cite Android’s official documentation and the Termux project’s RUN_COMMAND documentation. Supporting findings are kept in [`research_notes.md`](research_notes.md).

## Contribution direction

Contributions should begin with the documents in this repository. Code, content, accessibility, and safety changes must follow the review and checkpoint rules in `PLAN.md`, `TODO.md`, and `SAFETY.md`. Do not submit real credentials, personal data, exploit kits, or commands targeting third-party systems.
