---
id: sec-03-coordinated-disclosure
title: Coordinated vulnerability disclosure
strand: security-ethics
level: beginner
version: 1.0.0
prerequisites:
  - sec-01-ethics-scope-and-harm
  - sec-02-threat-modeling-defensive-controls
  - dev-05-code-review-issue-reports
estimated_minutes: 65
availability: offline
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain the distinct roles of a finder, vendor or maintainer, coordinator, and affected user in a disclosure process.
  - Identify the minimum useful fields in a private, redacted fictional report.
  - Choose a safe next step when ownership, policy, impact, or evidence is uncertain.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can explain why vulnerability disclosure is coordinated, distinguish the roles involved, and draft a private fictional handoff that gives maintainers useful evidence without exposing secrets or publishing operational details.

## Prerequisites

You should understand scope and permission, defensive threat modeling, expected versus observed behavior, and respectful issue reports. No real system, account, vendor contact, scanner, exploit, or network connection is required.

## Availability

This lesson is fully offline. It uses a redacted fictional report and a paper handoff form. The exercise is never submitted to a maintainer, coordinator, or public database.

## Explanation

A vulnerability is a weakness that may allow a security control to be defeated or may create an unwanted security consequence. A disclosure process exists to reduce harm while the people responsible for a product or service investigate, mitigate, and communicate the issue.

CISA describes its Coordinated Vulnerability Disclosure process through five broad steps: **collection**, **analysis**, **mitigation coordination**, **application of mitigations**, and **disclosure**.[1] The CERT Guide to Coordinated Vulnerability Disclosure describes CVD as gathering information from vulnerability finders, coordinating its sharing among relevant stakeholders, and disclosing vulnerabilities and mitigations to appropriate stakeholders, including the public.[2]

The names and timelines vary by program. The roles are commonly distinct:

| Role | Responsibility in a fictional process |
|---|---|
| Finder or reporter | Notices a possible issue and records bounded, relevant evidence. |
| Vendor or maintainer | Understands the product, confirms scope, assesses impact, and develops a fix or mitigation. |
| Coordinator | Helps relevant parties communicate when direct coordination is difficult or when a program assigns that role. |
| Deployer or affected user | Applies available mitigations and reports operational effects through the published channel. |
| Public reader | Uses an advisory to understand impact and available mitigations after disclosure. |

A report should be useful without being dangerous. It can include the affected component or version, a concise description, expected and observed behavior, a minimal safe reproduction using an authorized local fixture, impact as currently understood, evidence location, redactions, and a request for a secure response channel. It should not include passwords, tokens, private keys, personal data, weaponized code, public-target addresses, or more detail than is necessary to reproduce and fix the issue.

**Coordinated** does not mean every party agrees on every date. It means relevant stakeholders communicate about investigation, mitigation, and disclosure so that users have accurate information and a reasonable chance to reduce risk. A reporter should follow the affected project’s published vulnerability disclosure policy. If the policy or owner is unknown, do not guess a contact or publish sensitive details; preserve only the minimum fictional or authorized evidence and ask a responsible maintainer through a legitimate channel in real work.

## Worked example

A fictional local note importer accepts a record with an unexpected field. A paper test shows that the field appears in an exported preview even though the design says unknown fields are ignored. The safe report says:

```text
Title: Fictional importer preview includes an undeclared field
Scope: local fixture notes-importer 1.0, supplied for this exercise
Expected: fields outside the documented schema are ignored and not shown in preview
Observed: the synthetic field “private_tag” appears in the preview
Impact hypothesis: an export may reveal data outside the stated schema
Evidence: fixture-07.txt and paper comparison table; no real data
Request: confirm the correct private reporting route and advise whether the fixture is sufficient
```

The report does not test a public service, identify a real vendor, include a payload, or claim a final severity. The maintainer would need to confirm the issue, assess impact, decide on a mitigation, and communicate next steps.

## Common mistakes

Do not publish a sensitive report merely to force a response. Do not use a real account, target, credential, private log, or personal data for a beginner exercise. Do not claim a vulnerability is confirmed when the evidence is only a hypothesis. Do not promise a disclosure date on behalf of a maintainer or coordinator. Do not send a report to a guessed or unrelated address. Do not demand a reward, threaten public disclosure, or harass a maintainer. Finally, do not include exploit instructions when a concise behavior description is enough to fix the issue.

## Offline practice

Complete a private handoff form for the fictional importer example. Include title, scope, expected behavior, observed behavior, safe evidence, impact hypothesis, missing evidence, redactions, and the next responsible role. Label every statement **observed**, **reported**, **hypothesis**, or **unanswered**.

Then create a five-step process map using the CISA vocabulary: collection, analysis, mitigation coordination, application of mitigations, and disclosure. Add one sentence explaining why the exercise stops before contacting anyone.

## Knowledge check

1. Why coordinate vulnerability disclosure? **To help relevant stakeholders investigate, mitigate, and communicate a possible weakness while reducing avoidable harm.**
2. What is the difference between an observation and an impact hypothesis? **An observation records what the bounded evidence showed; an impact hypothesis proposes what that behavior might affect and still needs assessment.**
3. What does CISA list after analysis? **Mitigation coordination, followed by application of mitigations and disclosure.** The exact program timeline depends on context and policy.
4. Should a fictional learner submit the practice report to a real vendor? **No.** The activity stops at a local, redacted handoff; real disclosure requires authorization, a legitimate policy or contact, and appropriate handling of evidence.

## Project or application

Create a local disclosure worksheet for a fictional offline application. Include role names, a private report template, an evidence-redaction checklist, a stop condition, and a table that separates what the finder knows from what the maintainer must verify. Keep the report unsent and do not add real contact information.

## Accessibility notes

The report template uses labeled fields and can be completed in plain text, large print, speech-to-text, or a notebook. The process map can be written as a numbered list. The practice does not require color, a live form, a timed response, or a visual exploit demonstration.

## Safety and responsible use

This is an S1 defensive disclosure lesson. Work only with the supplied fictional fixture. Do not scan, probe, exploit, exfiltrate, impersonate, publish, or contact a real target. Do not include credentials, personal data, private keys, weaponized code, or unnecessary technical detail. A real report requires exact authorization and the affected project’s published process; this lesson provides neither.

## Further reading

The [CISA Coordinated Vulnerability Disclosure Program](https://www.cisa.gov/resources-tools/programs/coordinated-vulnerability-disclosure-program) describes CISA’s roles and five broad process steps. The [CERT Guide to Coordinated Vulnerability Disclosure](https://certcc.github.io/CERT-Guide-to-CVD/) explains finder, vendor, coordinator, and related stakeholder roles. These sources describe their own programs and guidance; they do not authorize activity against any target.

## Change log

- 1.0.0 — Initial Stage 3 draft.
