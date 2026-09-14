# Contributing to AetherLearn

Thank you for helping build AetherLearn. The project is an implemented privacy-first offline CS learning app: a native Android client and a static Web/PWA fallback, with a frozen 20-lesson MVP baseline plus 17 approved follow-on lessons (37 current). Contributions should preserve the project’s core commitments: free access, open source, local-first privacy, offline continuity, phone-first usability, accessibility, and responsible treatment of dual-use topics.

## Before opening a contribution

Read [`docs/PRODUCT_SPEC.md`](docs/PRODUCT_SPEC.md), [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md), [`docs/CURRICULUM.md`](docs/CURRICULUM.md), [`docs/SAFETY.md`](docs/SAFETY.md), and [`docs/TODO.md`](docs/TODO.md). Do not add Stage 6 or other new curriculum tracks, a backend, accounts or analytics, or arbitrary command execution without an approved decision-log entry. The 20-lesson MVP baseline remains frozen; keep existing lesson IDs and learner-state keys stable.

Use a focused branch and keep a pull request small enough to review. Explain the problem, the intended behavior, the files changed, the validation performed, and any remaining limitations. Never commit credentials, private keys, personal learning data, generated caches, build artifacts, or local device state.

## Code contributions

Code changes should have a clear acceptance criterion and tests for normal and failure paths. Keep platform-specific code behind narrow interfaces. For content-pack, import, export, update, and Termux boundaries, validate all external input and fail closed. Do not add network calls to the core learning path or introduce a dependency that transmits learner data without an approved privacy review.

Before opening a pull request, run the repository checks described in the README and confirm that the working tree contains only intentional changes. If a change affects architecture, privacy, security, storage, command execution, or public behavior, update the relevant document and `docs/PLAN.md` in the same pull request.

## Content contributions

Content lives under `content/`. Use the frontmatter schema and required body sections defined in `docs/CURRICULUM.md` and `content/README.md`. Every module needs stable metadata, prerequisites, an availability label, a risk tier, measurable learning objectives, explanations, examples, practice, assessment feedback, further reading, and a change log.

Content must be understandable to its intended learner and technically honest about version assumptions. Use synthetic data and local fixtures. Cite authoritative sources where claims are version-sensitive or safety-relevant. Run the content validator before submitting a pull request. A module is not considered released until technical, pedagogical, accessibility, and—when applicable—safety review is complete.

## Accessibility contributions

Accessibility improvements are welcome in both content and code. Use descriptive headings, meaningful link text, text alternatives for non-text content, non-color cues, readable code examples, keyboard-friendly interaction, and language that remains clear when text is enlarged. Describe the assistive-technology or zoom scenario tested in the pull request.

## Safety-sensitive contributions

Do not submit exploit kits, malware, credential-capture workflows, unauthorized-access instructions, public-target scanning procedures, persistence or evasion techniques, or material designed to weaken safety controls. Do not test a real system, account, device, or person as part of a contribution.

If a proposal touches cybersecurity, phishing, anonymity networks, model behavior, or another dual-use area, label its proposed risk tier and explain why the learning objective cannot be met with a safer local simulation or defensive example. Follow [`docs/SAFETY.md`](docs/SAFETY.md). S2 material requires safety review; S3 material is excluded from the MVP unless a separate decision record approves it; S4 material is rejected.

If you discover a vulnerability rather than a normal contribution, do not open a public issue. Follow [`SECURITY.md`](SECURITY.md).

## Pull-request checklist

- [ ] The change has a focused purpose and does not expand MVP scope.
- [ ] Tests or validation checks were run and their results are described.
- [ ] Documentation, schema, and planning files are updated where needed.
- [ ] No secrets, personal data, caches, or build artifacts are included.
- [ ] Accessibility impact was considered.
- [ ] Safety classification and review are included when relevant.
- [ ] The pull request is ready for review and explains known limitations.
