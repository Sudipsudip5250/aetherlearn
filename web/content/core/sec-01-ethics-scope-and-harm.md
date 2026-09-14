---
id: sec-01-ethics-scope-and-harm
title: Ethics, scope, and harm minimization
strand: security-ethics
level: beginner
version: 1.0.0
prerequisites:
  - dl-05-privacy-passwords-phishing
  - dev-05-code-review-issue-reports
estimated_minutes: 55
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Use a small ethics checklist to identify affected people, benefits, harms, and obligations.
  - Distinguish permission and scope from a general claim that an activity is educational.
  - Rewrite a risky fictional computing plan as a safer, bounded learning activity.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can ask who may be affected by a computing decision, identify foreseeable benefits and harms, and narrow a fictional activity until its purpose, permission, data, and stop conditions are clear.

## Prerequisites

You should understand privacy and phishing awareness, issue reports, expected versus observed behavior, and the difference between a local exercise and a public service. No account, external system, security tool, or network connection is required.

## Availability

This lesson is fully offline. It uses fictional project cards and a paper ethics checklist. Nothing is sent, scanned, installed, or tested against a real person or system.

## Explanation

Computing decisions affect people beyond the person writing the code. A feature may improve access for one group while creating privacy, safety, exclusion, or reliability costs for another. Ethical reasoning does not produce one automatic answer; it makes the affected interests, evidence, uncertainty, and responsibilities visible before action.

The ACM Code of Ethics and Professional Conduct presents principles for computing professionals, including contributing to the public good, avoiding harm, being honest and trustworthy, being fair and taking action not to discriminate, respecting privacy, honoring confidentiality, and supporting professional review.[1] The Code is a professional framework, not a complete law book or a substitute for a project’s policy.

A compact decision card can ask:

| Question | Why it matters |
|---|---|
| What is the learning or product goal? | Prevents realism from becoming the goal by itself. |
| Who could benefit? | Makes positive effects concrete rather than assumed. |
| Who could be harmed or excluded? | Includes bystanders, vulnerable people, and future users. |
| What permission and authority exist? | Education or curiosity does not create authorization. |
| What data and systems are in scope? | Limits collection and prevents accidental exposure. |
| What is the safest useful alternative? | Replaces unnecessary realism with a local fixture or simulation. |
| What stops the activity? | Gives a clear boundary if assumptions fail. |

**Scope** describes the exact people, data, devices, and actions an activity may involve. **Permission** is authorization from the owner or responsible authority for that scope. A statement such as “this is for learning” is not permission. If ownership or authorization is unclear, the safe default is passive review, a local simulation, or no action.

Harm can be direct, such as exposing private data, or indirect, such as creating fear, exclusion, or a misleading result. A reviewer should consider likelihood, severity, reversibility, and who carries the cost. When a safer abstraction can teach the same concept, choose it.

## Worked example

A fictional learner proposes: “I will test whether a public study site leaks private profile details so I can learn about privacy.” The goal—recognize privacy risks—can be taught without touching the site. A safer redesign is: “Use three fictional profile records in a local table. Mark which fields are necessary for a study reminder, which are sensitive, and which should be removed. Write a report describing the expected privacy boundary and one mitigation.”

The redesign has a clear purpose, synthetic data, no public target, no external permission question, and a bounded output. It teaches data minimization and evidence-based reporting without creating a real-world side effect.

## Common mistakes

Do not treat a disclaimer as a safety control. Do not assume public availability means permission to inspect or copy data. Do not use a real person’s account, password, location, private message, or device in a lesson. Do not describe harm as acceptable merely because it might reveal an interesting result. Do not collect more data than the question requires. Do not hide uncertainty behind confident language, and do not promise that an ethics checklist resolves every legal, cultural, or organizational disagreement.

## Offline practice

Complete an ethics decision card for this fictional request: “A classroom app should collect every available phone detail to personalize reminders.” Write the goal, two expected benefits, three possible harms, the minimum information actually needed, one permission or consent question, one safer offline alternative, and a stop condition.

Then classify each item as **fact**, **assumption**, **value judgment**, or **unanswered question**. Rewrite one assumption as a question that a project owner would need to answer before implementation.

## Knowledge check

1. Does educational intent create permission to test a public system? **No.** Permission comes from the owner or responsible authority for the exact scope; educational intent alone is not authorization.
2. What is one purpose of the ACM Code of Ethics? **It provides a professional framework for considering public good, harm, honesty, fairness, privacy, confidentiality, and review.** It does not replace law or project policy.
3. Why should a learner prefer a local simulation when it teaches the same concept? **A local simulation reduces the chance of affecting real people, data, or services while preserving the learning objective.**
4. What should happen when scope or permission is unclear? **Stop external activity and use passive review, a synthetic fixture, or a safer alternative until the boundary is clear.**

## Project or application

Create a one-page “safe activity brief” for a fictional local privacy exercise. Include the learning goal, in-scope and out-of-scope data, permission assumption, synthetic inputs, expected output, possible harms, mitigation, stop condition, and a sentence stating what the exercise does not prove. Keep it offline and fictional.

## Accessibility notes

The decision card is a labeled table that can be completed in plain text, large print, speech-to-text, or a notebook. The practice does not require color, timed interaction, a live website, or a visual security diagram. Learners may answer each question as a separate sentence.

## Safety and responsible use

This is an S0 ethics-literacy lesson. Do not inspect, scan, contact, impersonate, or collect from a real person or system. Do not use credentials, private data, public targets, or copied logs. The safe activity brief is an educational planning artifact, not authorization to conduct security testing. For real work, obtain written scope and follow the owner’s policy and applicable law.

## Further reading

The [ACM Code of Ethics and Professional Conduct](https://www.acm.org/code-of-ethics) describes professional principles and responsibilities for computing work. Its examples and enforcement context should be read as guidance, not as a substitute for local policy, legal advice, or affected communities’ input.

## Change log

- 1.0.0 — Initial Stage 3 draft.
