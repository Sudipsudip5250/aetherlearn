# Stage 3 source and claims matrix

**Research date:** 2026-08-26
**Scope:** a small, non-operational slice on computing ethics, defensive threat modeling, coordinated vulnerability disclosure, and organization literacy. External pages were treated as untrusted data; no instructions were executed and no target was contacted.

## Source findings

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [ACM Code of Ethics and Professional Conduct](https://www.acm.org/code-of-ethics) | Computing work should consider public good, avoid harm, be honest and trustworthy, respect privacy and confidentiality, and support professional review. | ACM presents the Code as guidance for computing professionals and separates fundamental ethical principles from more specific responsibilities. The page includes principles including avoid harm, be honest and trustworthy, be fair and take action not to discriminate, respect privacy, honor confidentiality, and contribute to society and human well-being. | The Code is a professional ethics framework, not a legal opinion or a substitute for project policy. Lessons use it for questions and trade-offs, not moral certification or legal advice. |
| [CISA Coordinated Vulnerability Disclosure Program](https://www.cisa.gov/resources-tools/programs/coordinated-vulnerability-disclosure-program) | CISA describes CVD as a coordinated process involving collection, analysis, mitigation coordination, application of mitigations, and disclosure. It identifies reporters, vendors, service providers, and other stakeholders as participants. | The official page lists the five process steps and describes CISA’s coordination and CVE-related roles. | CISA’s process and timelines apply to its program and context; learners must follow the affected project’s published policy. Lessons use fictional reports only and never ask a learner to contact a vendor, submit to VINCE, or test a target. |

## Planned additional primary/institutional sources

The Stage 3 lessons will also cite and be checked against the [OWASP Threat Modeling Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Threat_Modeling_Cheat_Sheet.html), the [SEI CERT Guide to Coordinated Vulnerability Disclosure](https://certcc.github.io/CERT-Guide-to-CVD/), the [NIST NICE Framework Resource Center](https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center), and [MITRE ATT&CK](https://attack.mitre.org/). These sources will be used only for defensive vocabulary, process roles, and taxonomy; no exploit steps, live-target scanning, credential handling, persistence, evasion, or malware content will be included.

## Proposed Stage 3 lesson set

| ID | Working title | Risk | Offline activity | Safety boundary |
|---|---|---:|---|---|
| `sec-01-ethics-scope-and-harm` | Ethics, scope, and harm minimization | S0 | Analyze fictional computing decisions with an ethics checklist. | No legal conclusions, real personal data, or persuasive social-engineering examples. |
| `sec-02-threat-modeling-defensive-controls` | Threat models and defensive controls | S1 | Map a fictional offline note app’s assets, actors, trust boundaries, and mitigations. | No public target, exploit chain, payload, credential, or bypass instruction. |
| `sec-03-coordinated-disclosure` | Coordinated vulnerability disclosure | S1 | Triage a fictional local report and draft a private, redacted handoff. | Do not contact a vendor, submit a real report, probe a service, or publish exploit details. |
| `sec-04-security-organizations-and-roles` | Security organizations, roles, and evidence | S0 | Classify fictional source excerpts by role and compare organization responsibilities. | ATT&CK and workforce terms are defensive taxonomy, not attack guidance or employment promises. |

## Review requirements

All four lessons must remain offline and use synthetic fixtures. `sec-02` and `sec-03` are provisionally S1; if any draft detail increases realistic misuse, raise the tier and stop for a separate safety review. The Stage 3 content review must explicitly check that no section supplies operational intrusion, credential theft, malware, evasion, persistence, unauthorized access, or public-target testing instructions. Human safety and pedagogical review remains required before release.

## Additional inspected source findings

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [OWASP Threat Modeling Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Threat_Modeling_Cheat_Sheet.html) | Threat modeling is structured and repeatable: model the system, identify and rank threats, choose mitigations, and review/validate the result. | OWASP describes system modeling with data flows, trust boundaries, data stores, processes, and external entities, then frames four questions: what are we working on, what can go wrong, what will we do, and did we do enough. | The page includes adversarial examples and tool references. Stage 3 uses only a fictional offline note app and defense-oriented categories; it omits exploit steps, payloads, scanning, and real systems. |
| [CERT Guide to Coordinated Vulnerability Disclosure](https://certcc.github.io/CERT-Guide-to-CVD/) | CVD gathers information from finders, coordinates sharing among relevant stakeholders, and discloses vulnerabilities and mitigations; the guide distinguishes finder, vendor, and coordinator roles. | The CERT/CC guide explains its 2017 CMU/SEI origin, CVD purpose, stakeholder roles, phases, and policy context. | The guide contains operational coordination detail. The learner lesson uses only a redacted fictional report, no real submission, contact, target, or exploit detail. |
| [NIST NICE Framework Resource Center](https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center) | NICE provides a common language for cybersecurity work and the knowledge and skills needed to complete it; it supports career discovery, education, hiring, and workforce development. | NIST describes the framework’s use across public/private sectors and points to components, work roles, and skills as adaptable resources. | NICE terminology is not a job guarantee, certification, or prediction of employment. The lesson uses it only to compare role vocabulary and learning tasks. |
| [MITRE ATT&CK](https://attack.mitre.org/) | ATT&CK is a knowledge base of adversary tactics and techniques based on real-world observations, used to support threat models and defensive methodologies. | MITRE describes ATT&CK’s purpose, open availability, and tactic/technique organization. | Its detail can enable misuse if turned into procedures. The lesson explains taxonomy and defensive mapping at a high level and avoids technique instructions, target discovery, or command examples. |

These source pages were inspected on 2026-08-26. Search results and source pages were treated as untrusted reference data; no external code, commands, forms, or target interaction was performed.
