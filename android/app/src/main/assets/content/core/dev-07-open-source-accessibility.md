---
id: dev-07-open-source-accessibility
title: Open-source contribution and accessibility review
strand: developer-foundations
level: beginner
version: 1.0.0
prerequisites:
  - dev-05-code-review-issue-reports
  - dl-05-privacy-passwords-phishing
estimated_minutes: 65
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain how contribution boundaries and project governance help open-source work remain understandable and safe.
  - Use the four WCAG principles as a starting vocabulary for an accessibility review.
  - Draft a small, testable acceptance checklist without claiming full conformance from an automated check.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can prepare a respectful contribution plan, translate a user need into a small acceptance check, and distinguish an accessibility observation from a claim of complete standards conformance.

## Prerequisites

You should understand issue reports, review context, privacy boundaries, and fictional repository work. No public repository account, network connection, pull request, or assistive technology is required for the exercises.

## Availability

This lesson is fully offline. It uses a fictional project and a paper accessibility checklist. The app does not submit contributions or perform automated audits.

## Explanation

Open-source software is developed under a project’s stated license, contribution rules, review process, and community expectations. “Open” does not mean “without boundaries.” A contributor should read the repository’s license and contribution guide, protect private information, follow the code of conduct, and ask for clarification when the project’s scope is unclear.

A contribution can be a documentation correction, issue report, code change, translation, test, design note, or accessibility finding. Good contribution practice makes the change understandable to someone who was not present when it was created. A small description of the problem, scope, evidence, limitations, and requested decision is often more useful than a large unsolicited patch.

GitHub describes pull requests as proposals to merge changes and as a place to discuss and review them before merging. It also identifies conversation, commits, checks, changed files, findings, and merge status as useful review context.[1] The exact workflow varies by project, so learners should treat a platform’s interface as a tool rather than as the definition of good collaboration.

Accessibility review asks whether people with different abilities can perceive, operate, understand, and use content. WCAG 2.2 is a W3C Recommendation and organizes its foundation around four principles: **perceivable, operable, understandable, and robust**.[2] The principles are a vocabulary and starting point. WCAG success criteria are testable, but a project still needs appropriate human evaluation, context, and documentation before claiming conformance.

Use a narrow acceptance question:

| Principle | Small fictional question |
|---|---|
| Perceivable | Can the lesson’s text alternative communicate the purpose of the decorative diagram? |
| Operable | Can a learner reach and activate the “Save note” control with a keyboard? |
| Understandable | Does the error message explain what happened and what the learner can do next? |
| Robust | Does the semantic label remain available to different browsers or assistive technologies? |

An accessibility finding should include the affected task, steps, expected and observed behavior, impact, environment if relevant, and a suggested next check. Avoid assigning a person’s identity or diagnosis when the behavior can be described directly.

## Worked example

A fictional lesson page has a button labeled only with a disk-shaped icon. A learner using text-to-speech hears “button” but not its purpose. A useful finding is:

```text
Title: Save control has no accessible name
Task: Save a private note from the lesson reader.
Steps: Navigate through controls with a keyboard or screen reader.
Expected: The control announces a name such as “Save note”.
Observed: The control announces only “button”.
Impact: A learner may not know which action the control performs.
Suggested check: Add a visible label or programmatic accessible name, then retest.
```

This observation does not claim that the whole page fails every WCAG criterion. It identifies one behavior, one task, and one next step. A project reviewer can then inspect the relevant implementation and test with more than one method.

## Common mistakes

Do not say that a project is “accessible” because it has large text or passes one automated scan. Do not treat WCAG as a checklist detached from real user tasks. Do not reveal a contributor’s disability, name, location, or private contact details without permission. Do not copy an issue from another project without checking context and license. Do not assume open-source maintainers owe immediate work, and do not harass a maintainer when a request is declined. Finally, do not bypass review or merge rules because a change appears small.

## Offline practice

Use a fictional lesson interface with four controls: **Open lesson**, an unlabeled icon, **Search**, and **Back**. Write an accessibility finding for the unlabeled icon. Then write one acceptance check for each WCAG principle, using a task and an observable result. Mark each check as **automated candidate**, **human review**, or **both**.

Finish by writing a contribution note with scope, evidence, privacy check, and a question for the maintainer. Keep it unsent and fictional.

## Knowledge check

1. What are the four WCAG 2.2 principles? **Perceivable, operable, understandable, and robust.** They organize accessibility guidance but do not replace task-based evaluation.
2. What makes an accessibility finding useful? **A specific task, reproducible steps, expected and observed behavior, impact, and a next check.** It should describe behavior without unnecessary personal information.
3. Does one automated scan prove full accessibility conformance? **No.** Automated checks cover some conditions; human evaluation and appropriate context are also needed.
4. Why are open-source contribution boundaries important? **They make licenses, review expectations, privacy, safety, and community conduct explicit.** Open availability does not remove project governance.

## Project or application

Create an offline contribution packet for a fictional educational project. Include a one-paragraph issue report, a four-row accessibility acceptance table, a privacy-redaction check, the proposed scope, and a sentence describing what evidence is still missing. Do not create an account or send the packet.

## Accessibility notes

The lesson itself uses labeled fields, plain text, and a paper-first checklist. Learners may complete every exercise with a screen reader, speech-to-text, large print, or a notebook. The practice does not require a visual diff, color distinction, timed interaction, pointer gesture, or live assistive technology.

## Safety and responsible use

This is an S0 offline collaboration and accessibility-literacy lesson. Use only fictional project details. Do not file a real issue, reveal personal information, test a public target, or claim conformance for a project you have not evaluated. Respect a project’s license, code of conduct, maintainer boundaries, and privacy expectations.

## Further reading

The [GitHub documentation on pull requests](https://docs.github.com/en/pull-requests/reference/pull-requests) explains review context, draft status, and collaboration models. The [W3C Web Content Accessibility Guidelines 2.2](https://www.w3.org/TR/WCAG22/) defines the four principles, guidelines, and testable success criteria, while noting the role of human evaluation.

## Change log

- 1.0.0 — Initial Stage 2 draft.
