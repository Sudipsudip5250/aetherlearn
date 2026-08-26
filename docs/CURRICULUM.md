# AetherLearn MVP Curriculum

## Curriculum decision

The MVP contains **four strands and 20 modules**. It is designed to take a motivated beginner from phone and digital foundations through Python, basic algorithms, and developer workflow. The curriculum is deliberately narrow enough to author, review, test, translate later, and complete on a phone.

The order is recommended rather than compulsory. Prerequisites are explicit, and the learner can choose a diagnostic starting point. Every module is available as offline text and lightweight assets. Only selected practical modules offer an optional Termux exercise.

The current content batch adds DL-02, DL-03, DL-04, PY-03, PY-04, and PY-05. The canonical registry remains the authority for the frozen MVP scope and now contains eleven authored modules. Device/emulator runtime evidence remains a separate open release gate.

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

The effective MVP registry currently ends this strand at DEV-03. The earlier DEV-04 web-page module and DEV-05 local-publishing project are deferred from the frozen 20-module scope until a future curriculum decision. Any future addition must first update `content/curriculum.yml`, the validator-backed registry, and the clients together.

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

Later releases may add computer architecture, operating systems, networks, databases, software engineering, distributed systems, cybersecurity foundations, cryptography, AI and machine learning, computer graphics, embedded systems, robotics, quantum computing, formal methods, scientific computing, and domain-specific tracks. Offensive-security labs, model-behavior research, anonymity networks, and other dual-use material require the governance described in [`SAFETY.md`](SAFETY.md) before they are scheduled.
