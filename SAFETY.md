# AetherLearn Safety and Responsible-Use Policy

## Purpose

AetherLearn teaches computing, programming, and selected security concepts for education, research, and authorized practice. Safety is implemented through content selection, exercise design, sandbox boundaries, user-visible consent, and review controls. A disclaimer is not considered an adequate control by itself.

This document governs MVP content and establishes the approval path for future dual-use modules.

## Risk classification

| Tier | Description | MVP status | Required controls |
|---|---|---|---|
| S0 | General digital literacy, privacy hygiene, safe computing, and benign history | Included | Normal technical and pedagogical review |
| S1 | Benign programming, algorithms, local files, Git, debugging, and toy simulations | Included | Offline execution where possible; bounded inputs; standard review |
| S2 | Defensive security and controlled dual-use concepts that cannot affect third parties | Limited inclusion | Local-only examples, synthetic data, no credentials, no public targets, safety review |
| S3 | Material that could materially enable intrusion, credential theft, malware, evasion, unauthorized access, or bypassing safeguards | Excluded from MVP | Requires a future policy decision, specialist review, controlled lab design, and release approval |
| S4 | Requests or artifacts intended to facilitate unauthorized harm or provide ready-to-use attack capability | Prohibited | Do not include, execute, distribute, or accept as a contribution |

A module’s risk tier is determined by the most dangerous realistic misuse of its exercises, not by its title. If the risk is uncertain, the module receives the higher provisional tier until reviewed.

## Dual-use content rules

MVP security content may explain concepts, terminology, threat models, defensive controls, secure coding, incident-response reasoning, responsible disclosure, and the operation of intentionally simplified local examples. It may use fictional data and toy protocols to teach recognition and defense.

MVP content must not provide ready-to-run exploit kits, credential-capture workflows, malware, persistence mechanisms, stealth or evasion procedures, destructive payloads, public-target scanning instructions, unauthorized access methods, or instructions for bypassing authentication, monitoring, licensing, or safety controls. It must not ask a learner to test an external system or person.

Content about phishing and social engineering is limited to recognition, prevention, safe reporting, and analysis of clearly fictional examples. It must not include persuasive templates intended for real targets or instructions for collecting credentials.

Content about anonymity networks is limited to architecture, history, privacy trade-offs, lawful use, and personal-safety risks. The MVP does not provide dark-web discovery workflows, hidden-service targeting, or evasion guidance.

Content about AI model behavior is limited to evaluation, interpretability, robustness, red-team methodology in controlled settings, alignment trade-offs, and safety measurement. The MVP does not teach users how to weaken safety controls, evade refusal mechanisms, or operationalize harmful outputs. The phrase “uncensoring models” is not used as a learning objective.

## Lab safety

Every practical lab must state its environment, inputs, expected outputs, cleanup steps, and boundaries. The default environment is a local simulation or a learner-owned device. A lab may not depend on a public IP range, third-party account, real credential, personal data, or external target.

Labs must be deterministic where feasible. They should use synthetic data, prebuilt local fixtures, intentionally limited inputs, and explicit resource ceilings. Any exercise that needs network access is labeled `network-optional`, describes the destination and data flow in advance, and has an offline alternative. Network access is never silently enabled by the application.

The app does not include a remote code runner in the MVP. In-app activities are bounded visualizations, static examples, quiz logic, or constrained interpreters without filesystem, network, reflection, or native-code access. General-purpose arbitrary code execution inside the app is out of scope.

## Termux-specific controls

Termux is an optional local runtime controlled by the learner. AetherLearn does not treat Termux as a trusted sandbox and does not claim that a user’s device is safe merely because an exercise is educational.

The MVP Termux bridge follows these rules:

1. Only versioned, allowlisted exercise wrappers may be launched.
2. The app does not pass arbitrary shell text, lesson text, or unvalidated user input as a command.
3. The app does not silently install packages, change Termux settings, grant permissions, modify SSH keys, or select a remote host.
4. The user sees the executable path, wrapper ID, arguments, working directory, prerequisites, and expected effects before confirmation.
5. Commands run locally and use a lesson-specific working directory. Shared storage is avoided unless the user explicitly chooses it.
6. Any network-dependent step is labeled and requires a separate confirmation. No MVP exercise targets a third-party system.
7. Terminal output, deep-link parameters, imported files, and exit statuses are treated as untrusted input.
8. Completion is never inferred solely from a process callback. The learner confirms completion, or the app validates a narrowly scoped result file against a schema and size limit.
9. When the required Termux permissions, settings, or version are unavailable, the app provides manual instructions and an in-app alternative.
10. The app never stores Termux commands containing secrets, personal hosts, tokens, private keys, or credentials in progress data.

## Content and contribution review

A contributor must provide the module’s risk tier, intended learner, prerequisites, environment, source references, expected misuse cases, and proposed mitigations. The contributor must show that examples use synthetic data and that the exercise can be completed without contacting an external target.

S0 and S1 contributions require technical and pedagogical review. S2 contributions require those reviews plus a safety review by a maintainer familiar with the subject. S3 material is not scheduled merely because it has educational value; it requires a separate decision record, specialist review, controlled lab design, and explicit release approval. S4 material is rejected.

Reviewers should ask whether the learning objective can be achieved with a safer abstraction, simulation, defensive example, or local fixture. They should remove operational detail that is not necessary for the objective. A module is returned to draft when a new dependency, exercise, or example changes its risk profile.

## Privacy and learner protection

No lesson should request real passwords, private keys, personal identifiers, location data, or sensitive files. Examples use fictional identities and generated data. Notes and quiz data remain local by default. Export flows warn learners that exported files may be copied or backed up by other applications.

The product must not shame learners for mistakes or require public performance. Safety warnings must be concise, understandable, and placed before the risky action rather than hidden in a footer. The app must support reduced motion, text scaling, and non-color safety cues so that warnings remain usable.

## Reporting and response

The project repository must provide a private security contact for reporting vulnerabilities in the application, content-pack parser, update path, or Termux integration. A report should include the affected version, reproduction context, impact, and whether any personal data was exposed. Maintainers should avoid requesting secrets or unnecessary personal information.

If a released module is found to contain unsafe instructions, the project should mark it unavailable in the next content-pack release, preserve the prior known-good pack for unrelated learning, publish a concise correction, and record the review decision. If an application vulnerability affects local data or command execution, the release should be paused until the issue is assessed and mitigated.

## Safety acceptance criteria for v1.0

| Criterion | Required evidence |
|---|---|
| Risk labeling | Every MVP module declares a risk tier and availability label |
| Safe examples | No exercise requires a real credential, third-party target, public scan, or personal data |
| Termux restrictions | Unknown exercise IDs and altered arguments are rejected |
| Network disclosure | Network-dependent actions are visibly labeled and separately confirmed |
| Fallback path | Every Termux lesson has an in-app or written alternative |
| Import validation | External result files are size-limited, schema-validated, and cannot directly mark unrelated modules complete |
| Content review | S2 content, if any, has a recorded safety review; S3 and S4 content are absent from the MVP |
| Privacy | No sensitive learner data is inserted into commands, URIs, logs, or automatic requests |
| Recovery | Unsafe or invalid content packs are rejected without replacing the last known-good pack |

## Decision rule

When educational value and operational risk conflict, AetherLearn chooses the safer abstraction. The project should teach learners to understand systems, recognize failure modes, build defenses, and practice only in authorized environments. It should not maximize realism at the expense of user safety or the safety of others.
