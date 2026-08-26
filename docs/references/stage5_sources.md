# Stage 5 source and claims matrix

**Research date:** 2026-08-26
**Implemented scope:** a bounded historical-security and career-orientation slice. The lessons remain offline, bundled-only, draft, and non-operational. Historical material focuses on impact, affected stakeholders, response, accountability, and defensive lessons; it omits attack construction, evasion, persistence, credential handling, and live-target activity. Career material describes role families and learning evidence, not jobs, salary, legal outcomes, or regional portability.

## Source findings

| Source | Observed claim | Lesson use | Limitation / safety boundary |
|---|---|---|---|
| Carnegie Mellon Software Engineering Institute, “Fostering Growth in Professional Cyber Incident Management” | SEI describes the 1988 Morris worm as a catalyst for DARPA asking SEI to establish CERT/CC. It describes CERT/CC as a neutral third party that reports vulnerabilities to vendors without revealing the reporter’s identity and publishes Vulnerability Notes with summaries, remediation information, and affected-vendor lists. | Historical response lesson: connect a major incident to institutional coordination, vulnerability communication, remediation, and neutral intermediaries. | This institutional-history page is a concise retrospective and does not provide a complete incident chronology or independent damage estimate. Do not reproduce operational worm behavior or treat the page as a complete technical account. |
| FBI, “The Morris Worm — 30 Years Since First Major Attack on the Internet” (2018-11-02) | The FBI reports the November 2, 1988 incident, describes widespread disruption and delayed email, explains that affected institutions used measures such as disconnection or system restoration, and records the subsequent investigation, conviction, and community-service sentence. | Historical case study: separate intent from impact, identify affected communities, compare immediate response with institutional learning, and discuss accountability without glorifying the actor. | This official retrospective has a law-enforcement perspective and includes technical descriptors. Stage 5 summarizes consequences and defensive lessons only; it does not include exploit paths, concealment, or replication details. |
| NIST, “NICE Framework Resource Center” | NIST states that the NICE Framework establishes a common language for cybersecurity work and the knowledge and skills needed to complete it. It describes uses including career discovery, education and training, work-role description, and workforce development. | Career orientation: map fictional learner evidence to broad role families and skills, then identify learning questions and safe local artifacts without treating a framework as a job guarantee. | The resource center describes a framework, not a promise of employment, salary, legal status, or regional portability. The lesson avoids labor-market predictions and states that role names and local requirements change. |
| Computer History Museum, “The Internet Comes From Behind” | The museum places the early network in an academic and publicly supported context, describes the 1988 Morris worm as affecting more than 6,000 computers, and frames the incident as part of the network’s social and institutional history. It also presents the later conviction and apology in a museum context. | Cultural framing: show that infrastructure history includes institutions, public investment, communities, affected users, and consequences—not only a technically skilled individual. | This curated retrospective uses broad summary language. Stage 5 cross-checks dates and consequences with CMU SEI/FBI, avoids hero/villain framing, and omits operational mechanisms. |
| ACM Code of Ethics and Professional Conduct | ACM presents principles including contributing to society and human well-being, avoiding harm, being honest and trustworthy, being fair and not discriminating, respecting privacy, and honoring confidentiality. It describes the Code as guidance for ethical decision-making rather than an algorithm that mechanically resolves every case. | Reflection framework for historical impact, affected stakeholders, intent-versus-consequence, privacy, and accountability. Learners apply the principles to fictional discussion cards rather than judge a real person or provide legal advice. | The Code is professional ethical guidance, not a complete legal or cultural framework. The lesson invites multiple perspectives and states that legal and organizational duties vary by place and context. |

## Source inspection status

The NIST NICE Framework Resource Center, Computer History Museum retrospective, and ACM Code of Ethics were inspected on 2026-08-26 and included in the matrix above. The matrix supports the D-038 scope and D-039 implementation review. Career claims remain limited to framework language and avoid employment or compensation promises. The matrix is not a substitute for human historical, cultural, pedagogical, accessibility, safety, or source-freshness review.

## Stage 5 lessons

1. `sec-05-morris-worm-history-and-response` — a high-level history-and-impact case study using CMU SEI, FBI, and Computer History Museum sources, with ACM ethics reflection. Risk: S0 because activities are fictional, historical, and non-operational.
2. `sec-06-cybersecurity-career-role-families` — a role-and-learning orientation using the NIST NICE Framework. Risk: S0 because it describes role families, skills, reflection, and portfolio evidence without employment, salary, legal, or regional-portability claims.

Stage 5 remains offline and bundled-only. No live targets, attack recipes, exploit or malware details, credentials, real personal data, external contact, arbitrary execution, network exercise, or Termux wrapper is permitted.

## References

[1]: https://www.sei.cmu.edu/history-of-innovation/fostering-growth-in-professional-cyber-incident-management/ "Carnegie Mellon Software Engineering Institute: Fostering Growth in Professional Cyber Incident Management"
[2]: https://www.fbi.gov/news/stories/morris-worm-30-years-since-first-major-attack-on-internet-110218 "FBI: The Morris Worm — 30 Years Since First Major Attack on the Internet"
[3]: https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center "NIST: NICE Framework Resource Center"
[4]: https://www.computerhistory.org/revolution/networking/19/378 "Computer History Museum: The Internet Comes From Behind"
[5]: https://www.acm.org/code-of-ethics "ACM: Code of Ethics and Professional Conduct"
