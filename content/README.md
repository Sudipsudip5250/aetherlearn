# M1 Content Contract

Lessons in `content/core/` are Markdown files with a YAML frontmatter block delimited by `---`. The validator in `scripts/validate_content.py` is the source of truth for structural checks; this document explains the contract for authors.

## Required frontmatter

```yaml
id: dl-01-digital-information
title: How digital devices represent information
strand: digital-literacy
level: beginner
version: 1.0.0
prerequisites: []
estimated_minutes: 35
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain that digital devices represent information using discrete states.
  - Distinguish information meaning from the physical representation used to store it.
review_status: draft
last_reviewed: 2026-08-24
```

The required fields are `id`, `title`, `strand`, `level`, `version`, `prerequisites`, `estimated_minutes`, `availability`, `risk_tier`, `core_asset_bytes`, `optional_asset_bytes`, `objectives`, `review_status`, and `last_reviewed`.

`id` is lowercase kebab-case, begins with the curriculum prefix such as `dl-01`, `py-02`, or `dev-01`, and is stable after release. `title` is non-empty. `strand` is one of `digital-literacy`, `python-fundamentals`, `algorithms`, or `developer-foundations` for the MVP. `level` is one of `beginner` or `intermediate`. `version` and `last_reviewed` use the ISO-compatible formats accepted by the validator. `prerequisites` is a list of stable module IDs or an empty list. `estimated_minutes` is a positive integer.

`availability` must be `offline`, `offline-pack`, `termux-optional`, or `network-optional`. `risk_tier` must be `S0`, `S1`, or `S2` for the MVP; higher tiers are rejected. Asset sizes are non-negative integers and are declared in bytes. `objectives` contains at least two non-empty learner-centered statements. `review_status` is `draft`, `in-review`, `released`, `needs-update`, or `deprecated`.

## Required body sections

Each lesson must contain the following level-two headings exactly once, in any order:

```text
## Objectives
## Prerequisites
## Availability
## Explanation
## Worked example
## Common mistakes
## Offline practice
## Knowledge check
## Project or application
## Accessibility notes
## Safety and responsible use
## Further reading
## Change log
```

A lesson may include additional headings, but it must not omit a required section. `Further reading` must contain at least one Markdown link, and `Change log` must record the initial version. `Safety and responsible use` is required even for S0 modules so that the author can state why no special control is needed.

## Authoring rules

Keep examples local, synthetic, and safe. Do not include real credentials, personal data, public targets, exploit kits, malware, or commands that test systems without authorization. A `termux-optional` module must explain the prerequisites and provide an in-app or written alternative. Network-dependent work must be labeled and must not block the core lesson.

The validator checks module structure, stable references, declared risk and availability, internal Markdown links, and declared asset sizes. The pack builder validates first, then emits a deterministic manifest and payload archive under `build/core-pack/`.
