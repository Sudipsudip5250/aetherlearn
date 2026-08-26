# AetherLearn MVP Curriculum

## Curriculum decision

The original MVP contains **four strands and 20 modules**. It is designed to take a motivated beginner from phone and digital foundations through Python, basic algorithms, and developer workflow. That `mvp-20` baseline remains stable for learner progress and historical release comparison. Approved post-MVP Stage 1 adds three offline digital-literacy lessons, Stage 2 adds four offline developer-foundations lessons, Stage 3 adds four offline security-ethics lessons, and Stage 4 adds four offline Web/data foundations lessons, without renumbering or replacing any MVP module.

The order is recommended rather than compulsory. Prerequisites are explicit, and the learner can choose a diagnostic starting point. Every module is available as offline text and lightweight assets. Only selected practical modules offer an optional Termux exercise.

Content expansion batch 1 added DL-02, DL-03, DL-04, PY-03, PY-04, and PY-05. Content expansion batch 2 added PY-06, PY-07, AL-01 through AL-05, DEV-02, and DEV-03. Those 20 modules remain the complete `mvp-20` baseline. The user-approved Stage 1 adds DL-06, DL-07, and DL-08; Stage 2 adds DEV-04 through DEV-07; and approved Stage 3 adds SEC-01 through SEC-04; approved Stage 4 adds WEB-01 through WEB-04; active Stage 5 adds SEC-05 and SEC-06, bringing the current canonical registry to 37 lessons. Stages 1–5 remain draft pending final human technical/pedagogical review; Stage 3 requires explicit safety review, Stage 4 requires Web/data accessibility and privacy review, and Stage 5 requires historical/cultural, source, and career-framing review. Accessibility, device/emulator runtime evidence, supply-chain review for DEV-06, and release gates remain separate open requirements.

## Availability labels

| Label | Meaning |
|---|---|
| `offline` | The module and its required exercise can be completed without a network or external runtime. |
| `offline-pack` | The module is offline after the learner downloads an optional content or media pack. |
| `termux-optional` | The core module is offline, while an optional local terminal exercise uses Termux if installed. |
| `network-optional` | The core module is offline, but an optional extension may use the network or a learner-owned service. |

## Strand 1: Digital literacy and phone computing

| ID | Module | Prerequisites | Time | Availability |
|---|---|---|---:|---|
| DL-01 | How digital devices represent information | None | 35 min | `offline` |
| DL-02 | Files, folders, storage, and backups | DL-01 | 40 min | `offline` |
| DL-03 | Android phone settings, permissions, and apps | None | 35 min | `offline` |
| DL-04 | Internet basics, browsers, URLs, and search | DL-01 | 45 min | `offline` |
| DL-05 | Privacy, passwords, phishing awareness, and safe updates | DL-03, DL-04 | 50 min | `offline` |
| DL-06 | How programming languages reflect constraints | DL-01 | 50 min | `offline` |
| DL-07 | How a computer runs a program | DL-01, PY-01 | 55 min | `offline` |
| DL-08 | Networks and the Web: requests, responses, and resources | DL-01, DL-04 | 55 min | `offline` |

DL-06 through DL-08 are the approved Stage 1 history/systems slice. They use high-level historical and protocol vocabulary, fixed fictional traces, and paper or local-text practice; they do not require a compiler, network, Termux, or external account.

These modules establish the practical vocabulary needed for later programming and terminal work. They include device-safe exercises such as identifying file types, inspecting permission categories using fictional examples, and recognizing suspicious messages without asking the learner to interact with real malicious content.

## Strand 2: Computational thinking and Python fundamentals

| ID | Module | Prerequisites | Time | Availability |
|---|---|---|---:|---|
| PY-01 | Problems, algorithms, and precise instructions | DL-01 | 40 min | `offline` |
| PY-02 | Python setup, expressions, and values | PY-01 | 50 min | `termux-optional` |
| PY-03 | Variables, types, input, and output | PY-02 | 55 min | `termux-optional` |
| PY-04 | Conditions and Boolean logic | PY-03 | 55 min | `offline` |
| PY-05 | Loops, repetition, and tracing | PY-04 | 60 min | `termux-optional` |
| PY-06 | Functions, scope, and reusable code | PY-05 | 65 min | `termux-optional` |
| PY-07 | Lists, dictionaries, strings, and simple data processing | PY-06 | 75 min | `termux-optional` |

The in-app exercises use deterministic questions, tracing tables, code reading, and small visualizations. Termux exercises are optional and use local files created for the lesson. The MVP does not promise a general-purpose in-app Python runtime.

## Strand 3: Data structures and basic algorithms

| ID | Module | Prerequisites | Time | Availability |
|---|---|---|---:|---|
| AL-01 | What makes a good data structure? | PY-07 | 45 min | `offline` |
| AL-02 | Arrays, lists, stacks, and queues | AL-01 | 65 min | `offline` |
| AL-03 | Searching and sorting by example | AL-02 | 75 min | `offline` |
| AL-04 | Complexity intuition and growth rates | AL-03 | 60 min | `offline` |
| AL-05 | Recursion, trees, and graph vocabulary | AL-04 | 75 min | `offline` |

This strand focuses on intuition, tracing, visual explanations, and small problem-solving projects rather than exhaustive theory. Formal proofs, advanced graph algorithms, and competitive-programming optimization are later-track content.

## Strand 4: Developer foundations

| ID | Module | Prerequisites | Time | Availability |
|---|---|---|---:|---|
| DEV-01 | The terminal and command-line mental model | DL-02, PY-02 | 55 min | `termux-optional` |
| DEV-02 | Git concepts, local repositories, and useful history | DEV-01 | 70 min | `termux-optional` |
| DEV-03 | Debugging, error messages, and minimal reproduction | PY-06 | 65 min | `offline` |
| DEV-04 | Testing small programs with examples and expected results | DEV-03 | 55 min | `offline` |
| DEV-05 | Code review and useful issue reports | DEV-02, DEV-03 | 60 min | `offline` |
| DEV-06 | Dependencies, provenance, and reproducible builds | DEV-02, DEV-03 | 65 min | `offline` |
| DEV-07 | Open-source contribution and accessibility review | DEV-05, DL-05 | 65 min | `offline` |


The `mvp-20` registry ends this strand at DEV-03. DEV-04 through DEV-07 are the approved Stage 2 software-engineering and open-source slice. Their exercises use fictional repositories, paper test tables, local manifests, and accessibility review prompts; they do not publish changes, install dependencies, or require a third-party account.

## Strand 5: Security ethics and defensive literacy

| ID | Module | Prerequisites | Time | Availability |
|---|---|---|---:|---|
| SEC-01 | Ethics, scope, and harm minimization | DL-05, DEV-05 | 55 min | `offline` |
| SEC-02 | Threat models and defensive controls | SEC-01, DL-08, DEV-07 | 65 min | `offline` |
| SEC-03 | Coordinated vulnerability disclosure | SEC-01, SEC-02, DEV-05 | 65 min | `offline` |
| SEC-04 | Security organizations, roles, and evidence | SEC-01, DEV-07 | 55 min | `offline` |

These four lessons are the approved Stage 3 security-ethics slice. SEC-01 and SEC-04 are S0; SEC-02 and SEC-03 are provisionally S1. They use ACM, OWASP, CISA, CERT/CC, NIST NICE, and MITRE sources for high-level concepts only. All activities use fictional local fixtures and stop before any external contact, scanning, exploitation, credential handling, or publication.

## Strand 6: Web and data foundations

| ID | Module | Prerequisites | Time | Availability |
|---|---|---|---:|---|
| WEB-01 | Semantic HTML and accessible structure | DEV-03, DL-05 | 55 min | `offline` |
| WEB-02 | CSS layout and responsive presentation | WEB-01 | 55 min | `offline` |
| WEB-03 | JavaScript events and local state | WEB-02, PY-04 | 65 min | `offline` |
| WEB-04 | Data modeling and JSON fixtures | WEB-03, DEV-06 | 60 min | `offline` |

These four lessons are the approved Stage 4 Web/data slice. WEB-01 and WEB-02 are S0; WEB-03 and WEB-04 are provisionally S1 because they introduce state and data-handling concepts. Activities use fictional, static local fixtures and paper/text traces only. They require no network requests, external APIs, third-party scripts, real personal data, arbitrary JavaScript execution, or Termux wrapper.

## Stage 5: Historical security and career orientation

| ID | Module | Prerequisites | Time | Availability |
|---|---|---|---:|---|
| SEC-05 | Morris worm: history, impact, and response | SEC-04 | 60 min | `offline` |
| SEC-06 | Cybersecurity role families and learning paths | SEC-04 | 55 min | `offline` |

These two lessons are the approved Stage 5 slice. Both are S0, offline, bundled-only, and use fictional discussion cards or learning-plan worksheets. SEC-05 focuses on impact, affected stakeholders, institutional response, ethics, and accountability using CMU SEI, FBI, Computer History Museum, and ACM sources; it does not glorify an actor or reproduce operational details. SEC-06 uses the NIST NICE Framework to describe role families and learning evidence without employment, salary, legal-outcome, or regional-portability claims. Neither lesson uses live targets, credentials, real personal data, external contact, arbitrary execution, network access, or a Termux wrapper.

## MVP assessment model

Each module contains clear objectives, a short explanation, worked examples, at least one interactive exercise, a knowledge check, and a completion rule. Each objective is written as an observable action, such as “trace a loop for a given input,” “identify the difference between a file and a folder,” or “create a local Git commit with a meaningful message.”

A strand project demonstrates transfer across modules. The learner may retry knowledge checks without penalty. Scores are stored locally as attempts and best scores rather than as a single opaque grade. The learner can override a recommendation and revisit completed content.

## Standard module template

Every source module must include the following frontmatter and sections:

```yaml
id: py-03-variables-types-input-output
title: Variables, types, input, and output
strand: python-fundamentals
level: beginner
version: 1.0.0
prerequisites:
  - py-02-python-setup-expressions-values
estimated_minutes: 55
availability: termux-optional
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain what a variable name refers to in a simple program.
  - Predict the output of a short example involving values and types.
review_status: draft
last_reviewed: 2026-08-25
```

The body must contain: a plain-language introduction; objectives; prerequisites; concept explanation; worked examples; common mistakes; an offline exercise; an assessment; a project or application; accessibility notes; safety or legal notes when relevant; further reading; source references; and a change log. Code examples must state the intended runtime and version assumptions. Every Termux exercise must include package prerequisites, exact user-visible commands, expected output shape, cleanup steps, and a manual fallback.

## Content lifecycle

Content moves through **draft**, **technical review**, **pedagogical review**, **safety review when applicable**, **released**, **needs update**, and **deprecated**. A module cannot be released until its required fields validate, its internal links resolve, its examples have been checked, its quiz has an explanation for every answer, and its accessibility review is complete.

Version-sensitive lessons identify the relevant software or protocol version. A reviewer records the date, scope, and unresolved uncertainty. Emerging or rapidly changing topics are not included in the MVP merely because they are popular; they must have a stable learning objective and a maintainer willing to review them.

Community contributions use pull requests and cannot publish directly to the application’s release channel. A contribution must include sources, a proposed module ID, estimated asset size, safety classification, and a test or review plan. Sensitive contributions require an additional reviewer with relevant expertise.

## Post-MVP curriculum parking lot

Stage 1 covers the first approved history/systems slice: DL-06, DL-07, and DL-08. Stage 2 covers DEV-04 through DEV-07. Stage 3 covers SEC-01 through SEC-04. Stage 4 covers WEB-01 through WEB-04. Stage 5 covers SEC-05 and SEC-06. Later releases may add computer architecture, operating systems, networks, databases, software engineering, distributed systems, cybersecurity foundations, cryptography, AI and machine learning, computer graphics, embedded systems, robotics, quantum computing, formal methods, scientific computing, and domain-specific tracks. Each later stage remains gated by the preceding stage’s validation and review checkpoint. Offensive-security labs, model-behavior research, anonymity networks, and other dual-use material require the governance described in [`SAFETY.md`](SAFETY.md) before they are scheduled.
