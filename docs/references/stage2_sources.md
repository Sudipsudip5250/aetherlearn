# Stage 2 source and claims matrix

**Status:** research basis for the next gated curriculum slice. This file records inspected external sources; it is not approval to add modules by itself.

## Proposed Stage 2 lesson set

| Proposed ID | Topic | Intended risk | Source basis |
|---|---|---|---|
| `dev-04-testing-pure-functions` | Test cases, fixtures, expected results, and edge cases for small pure functions | S0 | Python `unittest` documentation |
| `dev-05-code-review-issue-reports` | Pull requests, review context, issue reports, and respectful collaboration | S0 | GitHub pull-request documentation; repository contribution policy |
| `dev-06-dependency-provenance-builds` | Dependencies, resolved inputs, provenance, checksums, and reproducible-build thinking | S0/S1 | SLSA Build Provenance v1.2; existing release/checksum docs |
| `dev-07-open-source-accessibility` | Open-source governance, contribution boundaries, and accessibility acceptance criteria | S0 | GitHub Pull Requests documentation; W3C WCAG 2.2 Recommendation; repository governance docs |

## Inspected source claims

The Python `unittest` documentation defines a test case as checking a specific response to particular inputs, and describes fixtures, suites, runners, assertions, setup, and teardown. It supports teaching deterministic test cases and edge-case tables without requiring the app to execute arbitrary learner code. Source: https://docs.python.org/3/library/unittest.html (Python 3.14 documentation page inspected 2026-08-26).

GitHub’s pull-request documentation describes pull requests as proposals to merge code changes and as a place to discuss and review changes before merging. It identifies conversation, commits, checks, changed files, findings, and merge status as review context. It also distinguishes draft pull requests from ready-for-review work. Source: https://docs.github.com/en/pull-requests/reference/pull-requests (inspected 2026-08-26).

SLSA Build Provenance v1.2 describes provenance as verifiable information about where, when, and how software was produced. It frames build definitions, external parameters, resolved dependencies, builders, and subjects as information that supports verification and rebuilding. The lesson will use a small fictional manifest and checksum rather than instructing learners to change CI or trust unknown artifacts. Source: https://slsa.dev/spec/v1.2/build-provenance (status shown as Approved; inspected 2026-08-26).

WCAG 2.2 is a W3C Recommendation dated 12 December 2024. It describes accessibility guidance for Web content across devices, uses four principles—perceivable, operable, understandable, and robust—and notes that conformance testing combines automated and human evaluation. The lesson will teach acceptance criteria and inclusive review without claiming conformance from a checklist alone. Source: https://www.w3.org/TR/WCAG22/ (inspected 2026-08-26).

## Safety and maintenance boundaries

Stage 2 will use fixture repositories, fictional issue descriptions, local text manifests, paper test matrices, and accessibility review prompts. It will not require a third-party account, publish a branch, copy secrets, install dependencies from an unverified source, modify CI credentials, run arbitrary code, or claim that a checksum proves an artifact is safe. All lessons will remain offline and S0 unless a future reviewer documents a bounded S1 reason.

## References

[1]: https://docs.python.org/3/library/unittest.html "Python Documentation: unittest — Unit testing framework"
[2]: https://docs.github.com/en/pull-requests/reference/pull-requests "GitHub Docs: Pull requests"
[3]: https://slsa.dev/spec/v1.2/build-provenance "SLSA: Build Provenance v1.2"
[4]: https://www.w3.org/TR/WCAG22/ "W3C Recommendation: Web Content Accessibility Guidelines (WCAG) 2.2"
