---
id: sec-02-threat-modeling-defensive-controls
title: Threat models and defensive controls
strand: security-ethics
level: beginner
version: 1.0.0
prerequisites:
  - sec-01-ethics-scope-and-harm
  - dl-08-networks-web-concepts
  - dev-07-open-source-accessibility
estimated_minutes: 65
availability: offline
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Describe assets, actors, trust boundaries, data flows, and assumptions in a fictional system.
  - Use the questions what are we building, what can go wrong, what will we do, and did we do enough.
  - Pair a fictional risk with a proportionate defensive control and a testable review question.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can sketch a small system model, name defensive concerns without turning them into attack instructions, and connect each concern to a mitigation, owner, and review question.

## Prerequisites

You should understand client/server vocabulary, privacy boundaries, accessibility review, and the distinction between permission and curiosity. No security scanner, live target, account, credential, or network connection is required.

## Availability

This lesson is fully offline. It uses a fictional local note app, paper data-flow cards, and synthetic records. The exercise does not run code or inspect an external system.

## Explanation

Threat modeling is a structured, repeatable way to reason about a system’s security characteristics. OWASP describes a process that includes modeling the system, identifying and ranking threats, choosing responses or mitigations, and reviewing and validating the result.[1] A threat model is a maintained design artifact, not a one-time certificate and not a list of attack recipes.

Start with four questions:

1. **What are we working on?** Describe the system, users, data, components, and trust boundaries.
2. **What can go wrong?** Identify plausible failures or unwanted effects in this system’s context.
3. **What are we going to do about it?** Choose mitigations that reduce likelihood or impact.
4. **Did we do a good enough job?** Review assumptions, evidence, residual risk, and what must be revisited.

A small model names:

| Element | Meaning in the fictional note app |
|---|---|
| Asset | A private note and the learner’s chosen display name. |
| Actor | The learner, the local app, and an untrusted imported file. |
| Data flow | The learner saves a note; the app stores it locally. |
| Trust boundary | The point where imported text enters the app’s parser. |
| Assumption | The learner controls the device and the fixture contains no real personal data. |
| Control | Validate imported size and structure before storing it. |

A data-flow diagram can use plain text arrows. It does not need a special tool:

```text
learner -> note editor -> local database
fictional import -> size/schema check -> note editor
```

Threat modeling should describe unwanted outcomes without teaching how to cause them. For example, “an imported file might disclose information outside its intended scope” leads to “minimize fields, validate the schema, show a preview, and require explicit save.” The lesson does not need a payload, a target, or a bypass technique.

Risk prioritization is contextual. A useful beginner table records impact, likelihood as a qualitative judgment, affected people, existing controls, proposed mitigation, and remaining uncertainty. It should not pretend that a numeric score is objective when the evidence is weak.

## Worked example

The fictional **Pocket Notes** app accepts a local text fixture. The model identifies one concern: the fixture may contain an unexpected field called `location`. The safe response is to reject fields outside the documented schema, show the learner which fields were ignored, and keep the example data fictional. The review question is: “Can a test confirm that an unexpected field is not stored or exported?”

Another concern is accessibility: an error may be shown only by color. The control is to provide a text message and a semantic status announcement. The review question is: “Can a learner understand the error without relying on color or sound?”

These controls improve safety without exploring a real app, sending data, or discussing steps for bypassing a control.

## Common mistakes

Do not begin with a tool or a technique list before defining the system. Do not copy real credentials, private logs, or a public URL into a model. Do not confuse an asset with an attack method. Do not treat STRIDE or another mnemonic as a complete answer for every system. Do not rank risk with false precision. Do not publish a threat model that contains sensitive architecture details without considering its audience. Finally, do not use a fictional exercise as permission to test a real service.

## Offline practice

Create six paper cards for **Pocket Notes**: two assets, one actor, one data flow, one trust boundary, and one assumption. Add two fictional concerns and pair each with a defensive control and a testable review question. Mark each card **known**, **assumed**, or **needs evidence**.

Use the four OWASP-style questions to review your model. If a proposed action would contact an external service, replace it with a local fixture and write the safer boundary.

## Knowledge check

1. What is the first threat-modeling question? **What are we working on?** A model needs the system, data, actors, flows, and boundaries before risks can be judged.
2. What is a trust boundary? **A point where data, authority, or assumptions change between parts of a system.** In the fictional app, imported text crossing into the parser is a useful boundary to inspect.
3. What should a mitigation include? **A defensive control connected to a specific concern and a review or test question.** “Make it secure” is too vague.
4. Is a threat model a one-time guarantee? **No.** It should be reviewed and updated as the system, assumptions, evidence, and controls change.

## Project or application

Prepare a bounded threat-model card for a fictional offline study timer. Include its assets, actors, data flows, trust boundary, two concerns, two mitigations, one accessibility question, one privacy question, and three items of missing evidence. Do not include a real target, exploit, credential, or network action.

## Accessibility notes

The activity can be completed with labeled cards, a plain-text arrow diagram, large print, speech-to-text, or a notebook. No color-coded risk score, mouse gesture, timed task, or visual diagramming tool is required. The example includes a non-color accessibility question as part of the model.

## Safety and responsible use

This is an S1 defensive modeling lesson. Use only fictional local systems and synthetic data. Do not scan, probe, exploit, bypass, impersonate, collect credentials, or contact a public or third-party target. Do not treat a threat-modeling worksheet as authorization. If a real system is later assessed, written authorization, scope, stop conditions, and a controlled environment are required.

## Further reading

The [OWASP Threat Modeling Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Threat_Modeling_Cheat_Sheet.html) describes system modeling, threat identification, mitigations, and review and validation. The page contains operational references; this lesson intentionally uses only its high-level defensive process.

## Change log

- 1.0.0 — Initial Stage 3 draft.
