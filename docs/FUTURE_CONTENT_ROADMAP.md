# Future content roadmap and source plan

**Status:** Stage 1 is approved and implemented as a three-lesson slice; the original `mvp-20` baseline remains stable and later stages remain gated.

AetherLearn’s original MVP curriculum is intentionally frozen at 20 modules. It provides a coherent foundation in digital literacy, Python, basic algorithms, and developer practice. The approved Stage 1 expansion adds three bundled, offline history/systems lessons without renumbering or replacing that baseline. It does not attempt to cover every programming language, operating system, organization, security topic, or career path. This document records what could come next so future contributors do not expand the product ad hoc or turn historical and security material into unsafe operational instruction.

## 1. Coverage assessment

| Area | Current coverage | Evidence-based gap | Safe future direction |
|---|---|---|---|
| Digital literacy | Devices, files, Android settings, browsers, privacy, passwords, phishing | No deeper systems or network mental model | Add operating systems, processes, memory, DNS, HTTP, and data storage as conceptual lessons |
| Programming | Python fundamentals, tracing, functions, collections, conditions, loops | No comparative view of language design or older ecosystems | Add history and comparison rather than a full second language track initially |
| Algorithms | Data structures, searching, sorting, complexity, recursion, trees, graphs | No implementation testing or profiling practice | Add paper tracing, benchmark interpretation, and test-design exercises using bounded local data |
| Developer practice | Terminal, Git, debugging | No collaboration, testing, packaging, APIs, dependency, or release practice | Add local-only software-engineering foundations with reproducible examples |
| Web and data | Browser use and offline PWA experience | No HTML/CSS/HTTP, databases, accessibility engineering, or data modeling | Add browser-platform and data-literacy tracks with static/local examples |
| Security and society | Privacy, phishing awareness, safe updates, bounded Termux wrappers | No ethical-security history, threat modeling, disclosure, or defensive frameworks | Add non-operational ethics, incident analysis, secure-design, and coordinated-disclosure material |
| Organizations and community | Open-source project behavior is implied by Git and contribution docs | Learners lack a map of standards bodies, foundations, research groups, and career roles | Add an organization-literacy lesson with roles, governance, evidence, and contribution paths |
| Career orientation | No explicit role map | Learners may not know how skills connect to real work | Add role-based pathways using skills and outcomes, not job promises or salary claims |

The gap analysis is a planning assessment, not evidence that the MVP was incomplete. Stage 1 is deliberately limited and remains draft until human pedagogical and technical review; later releases must wait for their own stage decisions and checkpoints.

## 2. Recommended future strands

### A. Computing and language history

A short historical strand should show why languages exist and how constraints shape design. Suggested coverage is machine code and assembly as a hardware-near model; FORTRAN for scientific computing; COBOL for business systems; BASIC for accessibility to beginners; Pascal for teaching structured programming; C for systems and portability; Lisp and Smalltalk for symbolic and object-oriented ideas; SQL as a declarative data language; and shell languages as command composition. The goal is comparison—data model, execution model, readability, portability, ecosystem, and maintenance—not memorizing dates or installing obsolete toolchains.

The Computer History Museum’s timeline provides a useful primary-history starting point for Plankalkül, A-0, FORTRAN, COBOL, BASIC, Simula, LOGO, UNIX, Pascal, C, C++, Perl, and other milestones.[1] Historical claims should be checked against original manuals, archival material, or reputable institutional histories before publication. A lesson should label what is documented fact, what is an interpretation, and what remains debated.

A safe offline exercise can ask learners to translate the same tiny task—such as counting words in a fixed text—into pseudocode, a Python solution, and a historical-language-style description. It should not require downloading an unmaintained compiler or executing unknown binaries. Any example syntax must be clearly labeled as illustrative unless it has been tested against a pinned, trusted implementation.

### B. Systems, networks, and the Web

A second strand could connect the existing digital-literacy foundation to programming practice through CPU and memory models, processes and files, permissions, DNS, HTTP, browsers, databases, serialization, and client/server boundaries. The exercises should use diagrams, fixed local files, and loopback-only examples. They should not ask learners to scan public hosts, intercept traffic, collect credentials, or connect to infrastructure they do not own.

The Web portion should distinguish the browser’s document model, styling, scripting, network requests, caching, and accessibility. A static local page and a small local dataset are enough to teach the concept. A later optional exercise may use a local-only server with explicit permissions, but the default remains offline-first.

### C. Software engineering and open-source practice

The next practical strand should cover tests, readable interfaces, error handling, code review, issue reports, semantic versioning, dependency provenance, reproducible builds, accessibility acceptance criteria, and release notes. It should extend DEV-02 and DEV-03 rather than introduce a new tool-heavy workflow.

Suggested exercises are a local test plan for a small pure function, a review of a deliberately flawed patch, a dependency inventory using a fixture project, and a release checklist that verifies checksums and documents limitations. No exercise should require publishing a branch, exposing private notes, or copying secrets into a repository.

### D. Ethical security and responsible research

A future security strand should begin with permission, scope, harm minimization, privacy, evidence handling, and coordinated vulnerability disclosure. The ACM Code of Ethics frames computing responsibility around the public good and explicitly discusses avoiding harm, honesty, fairness, privacy, confidentiality, and professional review.[2] The CERT Guide to Coordinated Vulnerability Disclosure describes finders, vendors, coordinators, phases, and operational problems; CISA’s program describes collection, analysis, mitigation coordination, mitigation application, and disclosure.[4] [5]

OWASP’s Web Security Testing Guide can provide a structured vocabulary for defensive review, but its versioned references should be pinned because the project itself warns that identifiers and latest content change over time.[3] A learner-facing lesson should use a local toy application or a written fixture, not a live target. It should teach how to write a bounded finding, reproduce a problem locally, assess impact without exploiting real users, and route a report through the owner’s published policy.

MITRE ATT&CK can be used as a defensive taxonomy for discussing adversary behavior, detections, mitigations, and incident timelines. It should not be turned into a checklist for attacking real systems.[6] Examples should be high-level and mapped to prevention or detection outcomes. The curriculum should avoid step-by-step intrusion, credential theft, persistence, evasion, malware construction, or instructions for targeting named organizations.

### E. Organizations, groups, and the human side of computing

Learners would benefit from an organization-literacy section that explains what different groups do and how to evaluate their authority and source quality:

| Organization or community | Role to teach | Safe learner outcome |
|---|---|---|
| ACM | Professional society and ethics/community resource | Apply a professional principle to a design decision |
| OWASP | Community-led application-security projects and testing guidance | Use a versioned defensive checklist against a local fixture |
| CISA / CVE ecosystem | Public-sector coordination, advisories, and vulnerability information | Read an advisory and identify affected versions, mitigations, and uncertainty |
| CERT/CC / SEI | Vulnerability coordination and operational guidance | Draft a non-sensitive disclosure timeline |
| MITRE | Knowledge bases and structured defensive/adversary modeling | Map a fictional incident to a defensive tactic and mitigation |
| NIST NICE | Common language for cybersecurity work and skills | Compare a learner’s skills with role descriptions without promising employment |
| IETF and W3C | Open standards processes for Internet protocols and the Web | Distinguish a standard, a recommendation, an implementation, and an opinion |
| Free and open-source foundations | Stewardship, licenses, governance, and contribution norms | Choose a license-compatible contribution path |

The NIST NICE Framework is appropriate for career discovery because it provides a common language for cybersecurity work and the knowledge and skills needed for roles across sectors.[7] It should be presented as a framework, not as a guarantee of employment or a substitute for regional labor-market research.

### F. Historical security case studies without glorification

Historical “hacker” material can motivate learners when it focuses on curiosity, consequences, responsible choices, and defenders rather than celebrity or operational technique. The recommended format is:

1. State the source, date, and uncertainty level.
2. Describe the technical idea only at the level needed to understand the impact.
3. Explain who was affected and what harm or risk resulted.
4. Identify the defensive response, governance change, or lesson for builders.
5. Give the learner a safe local exercise, such as writing a timeline, threat model, disclosure report, or mitigation checklist.
6. End with permission boundaries and a route for reporting concerns.

Suitable case-study categories include the history of early worms and software failures, the evolution of bug-bounty and coordinated-disclosure practices, major public incidents described through official advisories, and biographies of engineers, researchers, educators, and defenders whose work improved computing. Avoid glamorizing criminal groups, reproducing payloads, naming targets unnecessarily, or treating public notoriety as a career model. Every case study should pass a separate human safety and historical-accuracy review before inclusion.

## 3. Source and freshness policy

Future lessons should prefer primary sources and stable institutional documentation. Each external source should be recorded with title, publisher, URL, access date, version or publication date when available, and the exact claim it supports. A link should not be used merely because it appears authoritative; the lesson author must inspect the source and distinguish source-reported fact from interpretation.

| Source class | Preferred use | Maintenance rule |
|---|---|---|
| Official standards and documentation | Language semantics, protocols, APIs, security guidance | Pin versions where identifiers or behavior change |
| Museums, archives, universities | Computing and language history | Cross-check dates and avoid unsupported “first” claims |
| Professional societies | Ethics, professional practice, community norms | Recheck policy wording before each major content release |
| Government advisories and catalogs | Public incidents, vulnerability status, mitigations | Record publication/update date and avoid stale severity claims |
| Open-source project documentation | Governance, contribution, implementation guidance | Link to tagged/versioned documents where possible |
| News and biographies | Discovery and context only | Never use as the sole source for technical or safety claims |
| Community forums and social media | Leads and learner perspectives | Do not treat as authoritative evidence without independent confirmation |

The current research set supports the roadmap with the Computer History Museum language timeline, the ACM Code of Ethics, OWASP WSTG, CISA CVD, the CERT CVD Guide, MITRE ATT&CK, and the NIST NICE Framework.[1]–[7] These links are starting points, not permission to copy large portions of copyrighted material or to skip project review.

## 4. Proposed content-gate process

Before any future strand becomes an authored module, the contributor should submit a scope note identifying the learner outcome, prerequisites, offline/online requirement, risk tier, exercise boundary, source list, accessibility considerations, and maintenance owner. A reviewer should then check technical accuracy, citation support, answerability, reading level, cultural and historical framing, and whether the exercise can be completed without unauthorized access or personal data.

A release candidate should keep unapproved content outside `content/curriculum.yml`. Once a batch is explicitly approved, it must use the versioned registry, existing validator, Android/Web byte-parity process, and human pedagogical/safety review. This preserves the stable `mvp-20` baseline and prevents a roadmap from silently becoming product scope. Stage 1 is the first recorded exception under decision D-031.

## 5. Recommended order after release gates

The first post-MVP content increment should be a small, non-operational **computing history and systems vocabulary** set. The second should be **software engineering and open-source practice**. The third should be **ethical security, disclosure, and organization literacy**. Historical security case studies and career pathways should follow only after the project has a named human reviewer for source quality, safety framing, and local-law sensitivity.

The immediate content priority is to complete Stage 1’s technical and pedagogical review without claiming that repository checks replace human approval. Contributors may begin Stage 2 only after Stage 1’s validation, parity, documentation, commit, and review checkpoint is recorded. The roadmap is successful when each increment remains bounded, source-backed, safety-reviewed, and reversible.

## References

[1]: https://www.computerhistory.org/timeline/software-languages/ "Computer History Museum: Timeline of Computer History — Software & Languages"
[2]: https://www.acm.org/code-of-ethics "ACM: Code of Ethics and Professional Conduct"
[3]: https://owasp.org/www-project-web-security-testing-guide/ "OWASP: Web Security Testing Guide"
[4]: https://www.cisa.gov/resources-tools/programs/coordinated-vulnerability-disclosure-program "CISA: Coordinated Vulnerability Disclosure Program"
[5]: https://certcc.github.io/CERT-Guide-to-CVD/ "CERT/CC: Guide to Coordinated Vulnerability Disclosure"
[6]: https://attack.mitre.org/ "MITRE: ATT&CK Knowledge Base"
[7]: https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center "NIST: NICE Framework Resource Center"
