# AetherLearn documentation index

This directory is organized by purpose. **Start with the current documents** below; files under `references/` are evidence records, and `references/ARCHIVE.md` summarizes superseded notes retained for provenance rather than current instructions.

## Read first

| Document | Use it for |
|---|---|
| [`PRODUCT_SPEC.md`](PRODUCT_SPEC.md) | Product boundary, audience, journeys, acceptance criteria, and non-goals |
| [`ARCHITECTURE.md`](ARCHITECTURE.md) | Android/Web architecture, local storage, packs, Termux boundary, and threat model |
| [`CURRICULUM.md`](CURRICULUM.md) | Immutable `mvp-20` baseline, approved expansions, prerequisites, and lesson catalog |
| [`SAFETY.md`](SAFETY.md) | Risk tiers, prohibited material, lab rules, privacy, Termux, and release safety criteria |

## Current implementation and release guides

| Document | Use it for |
|---|---|
| [`PLAN.md`](PLAN.md) | Milestones, decisions, checkpoint evidence, and ownership |
| [`TODO.md`](TODO.md) | Recoverable implementation checklist and open gates |
| [`RELEASE_HANDOFF.md`](RELEASE_HANDOFF.md) | Build/install instructions, CI artifacts, pack testing, and human handoff |
| [`SIGNING.md`](SIGNING.md) | Human-operated signing, key custody, verification, and distribution |
| [`NETWORK_PACKS.md`](NETWORK_PACKS.md) | Optional-pack format, HTTPS download, validation, rollback, and recovery |
| [`TERMUX_WRAPPERS.md`](TERMUX_WRAPPERS.md) | Fixed wrapper contract and Termux safety boundary |

## Review and future planning

| Document | Use it for |
|---|---|
| [`CONTENT_REVIEW.md`](CONTENT_REVIEW.md) | Current lesson consistency review and draft status |
| [`DEVICE_TEST_CHECKLIST.md`](DEVICE_TEST_CHECKLIST.md) | Android, Web/PWA, Termux, persistence, privacy, accessibility, Stage 5 content, signing, and distribution testing |
| [`FUTURE_PLAN.md`](FUTURE_PLAN.md) | Approved stage sequencing, governance, longer-term topic analysis, and intentionally gated future ideas; no Stage 6 scope is authorized |


## Evidence and references

Current source matrices and browser evidence are in [`references/STAGE_EVIDENCE.md`](references/STAGE_EVIDENCE.md) and should be read alongside the relevant decision in [`PLAN.md`](PLAN.md). Early research, build notes, dated network-contract notes, and superseded observations are summarized in [`references/ARCHIVE.md`](references/ARCHIVE.md); the original files remain recoverable from the local pre-consolidation backup branch.

## Documentation rule

If a document describes a current requirement, release procedure, or decision, keep it in the `docs/` root and link it from this index. If it records a completed investigation or superseded observation, place it under `references/` and label it as historical. Do not use an archived note as evidence of current behavior.
