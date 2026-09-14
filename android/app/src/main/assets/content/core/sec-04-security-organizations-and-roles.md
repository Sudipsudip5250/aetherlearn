---
id: sec-04-security-organizations-and-roles
title: Security organizations, roles, and evidence
strand: security-ethics
level: beginner
version: 1.0.0
prerequisites:
  - sec-01-ethics-scope-and-harm
  - dev-07-open-source-accessibility
estimated_minutes: 55
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Distinguish an ethics framework, a technical community, a government program, a workforce framework, and a defensive knowledge base.
  - Match a fictional question to an organization or source type without treating any source as universal authority.
  - Separate evidence, interpretation, and recommendation in a short security note.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can describe what different security organizations and frameworks are for, choose a source type for a fictional question, and write a cautious note that separates observed evidence from interpretation and recommendation.

## Prerequisites

You should understand open-source contribution boundaries, accessibility review, privacy, scope, and the difference between a fact and a hypothesis. No account, search engine, security tool, public target, or network connection is required.

## Availability

This lesson is fully offline. It uses fictional source cards and a paper evidence table. The activity does not browse, submit, scan, or contact an organization.

## Explanation

Security work is distributed across communities, standards bodies, government programs, professional societies, maintainers, educators, and employers. These groups do different jobs. A source can be authoritative for its own framework without being the final authority for every technical, legal, cultural, or organizational decision.

The following roles provide a useful beginner vocabulary:

| Source or organization type | What it can provide | What a learner should not assume |
|---|---|---|
| Professional ethics body | Principles for responsible computing decisions | A complete legal answer or a single correct moral choice |
| Technical community or foundation | Standards, guidance, tools, and shared terminology | That every project uses the same workflow |
| Government program | Public guidance, coordination, advisories, or sector responsibilities | That its process automatically applies to every project or country |
| Workforce framework | A common language for work roles, knowledge, and skills | A job guarantee, certification, or personal career prediction |
| Defensive knowledge base | A structured taxonomy of observed behaviors or mitigations | A permission slip, attack manual, or proof that a specific event occurred |

NIST describes the NICE Framework as a common language for cybersecurity work and the knowledge and skills needed to complete it. It is used for career discovery, education, hiring, and workforce development.[1] A framework can help a learner name a direction—such as analysis, development, governance, or response—without promising employment or requiring one fixed route.

MITRE ATT&CK describes itself as a knowledge base of adversary tactics and techniques based on real-world observations, used as a foundation for specific threat models and methodologies.[2] In a defensive context, a team may use a taxonomy to organize detections, mitigations, or coverage questions. The taxonomy is not authorization to reproduce a technique, and a label is not evidence that a particular incident happened.

A careful security note separates three layers:

1. **Evidence:** What was directly observed, with source, date, scope, and redactions.
2. **Interpretation:** What the evidence may mean, including uncertainty and alternative explanations.
3. **Recommendation:** A bounded next action, owner, and review condition.

For example: “The fictional fixture records three failed local validation checks” is evidence. “The parser may be rejecting an unexpected field” is an interpretation. “Add a local regression case and ask the maintainer to review the schema” is a recommendation. None of these statements claims a real incident or assigns blame.

## Worked example

A fictional project wants to answer three questions:

- “What principles should guide a decision about collecting learner data?” → consult an ethics framework and the project’s privacy policy.
- “What skills might a defensive analyst practice?” → consult a workforce framework such as NICE, while treating the result as vocabulary rather than a promise.
- “How can a team organize defensive coverage questions?” → consult a defensive knowledge base such as ATT&CK, without copying technique instructions or testing a target.

The best source depends on the question. A single source card cannot replace the project owner, affected users, legal counsel, or a safety reviewer when those perspectives are needed.

## Common mistakes

Do not call every organization a standards body. Do not treat a taxonomy as a command list. Do not cite a framework for a claim it does not make. Do not turn a role description into an employment promise. Do not hide the source date, scope, or uncertainty. Do not assign blame to a person or group based on a category label. Finally, do not use an organization’s public website as permission to test a system, submit a report, or collect information.

## Offline practice

Make five fictional source cards labeled **ethics framework**, **technical community**, **government program**, **workforce framework**, and **defensive knowledge base**. For each card, write one question it can help answer and one question it cannot answer. Add a source date and a limitation.

Then write a three-sentence security note about the fictional Pocket Notes app: one evidence sentence, one interpretation sentence, and one recommendation sentence. Mark which sentence would need a human reviewer before release.

## Knowledge check

1. What does the NICE Framework provide? **A common language for cybersecurity work and the knowledge and skills needed to complete it.** It does not guarantee a job or prescribe one career.
2. What is MITRE ATT&CK? **A knowledge base of adversary tactics and techniques based on real-world observations that can support threat models and defensive methodologies.** It is not permission or an instruction manual.
3. How should a security note separate evidence and interpretation? **Evidence states what was observed and where; interpretation explains what it may mean and names uncertainty.** Mixing them makes review harder.
4. Can an organization’s public guidance authorize testing any system? **No.** Authorization must come from the responsible owner or an explicitly applicable program and scope; public information alone is not permission.

## Project or application

Create a local source-selection matrix for a fictional privacy review. Include four questions, the source type that best fits each question, one limitation, and the human role that should review the answer. Keep all source names fictional or use only the source descriptions in this lesson; do not browse or contact anyone.

## Accessibility notes

The source-selection matrix is a labeled table that can be completed in plain text, large print, speech-to-text, or a notebook. The practice does not require color, a visual taxonomy, a live website, or a timed response. Learners may represent the evidence/interpretation/recommendation layers as three labeled paragraphs.

## Safety and responsible use

This is an S0 defensive organization-literacy lesson. Do not use an organization’s name as permission to scan, probe, exploit, impersonate, or contact a real target. Do not copy operational attack procedures, credentials, private data, or unredacted incident records. Treat public frameworks as bounded sources whose scope and limitations must be checked.

## Further reading

The [NIST NICE Framework Resource Center](https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center) explains the framework’s common language for cybersecurity work, roles, knowledge, and skills. [MITRE ATT&CK](https://attack.mitre.org/) describes its defensive knowledge-base purpose and tactic/technique organization. Both sources should be interpreted within their stated scope.

## Change log

- 1.0.0 — Initial Stage 3 draft.
