# Stage 5 human review and public-distribution checklist

**Purpose:** provide a repeatable human-owned review path for the two Stage 5 lessons and the AetherLearn release before public distribution. This checklist supplements, and does not replace, [`DEVICE_TEST_CHECKLIST.md`](DEVICE_TEST_CHECKLIST.md), [`CONTENT_REVIEW.md`](CONTENT_REVIEW.md), [`SAFETY.md`](SAFETY.md), [`SIGNING.md`](SIGNING.md), and [`RELEASE_HANDOFF.md`](RELEASE_HANDOFF.md).

**Current automated status:** Stage 5 automated checkpoint D-039 is complete. The repository contains 37 lessons, including the two draft Stage 5 lessons. Hosted Quality workflow [`32983760468`](https://github.com/Sudipsudip5250/aetherlearn-mvp-spec/actions/runs/32982750065) passed the validation and Android jobs. This document deliberately treats every human gate below as **open until a named person records evidence**.

> **Release rule:** automated validation proves repository invariants; it does not prove that the lessons are historically complete, culturally appropriate, pedagogically effective, accessible on real devices, safe for every learner, signed by the authorized publisher, or ready for public distribution.

## 1. Test record and decision ownership

Create one review record for each review stream. Use fictional learner data only. Do not place private notes, passwords, private URLs, identity documents, keystore details, or personal career information in issues, screenshots, or this checklist.

| Field | Record |
|---|---|
| Release candidate commit |  |
| Content version / catalog count | Expected: 37 lessons |
| Human review lead |  |
| Technical reviewer |  |
| Pedagogical reviewer |  |
| Accessibility reviewer |  |
| Safety/privacy reviewer |  |
| Historical/cultural reviewer |  |
| Career-framing reviewer |  |
| Device/browser test owner |  |
| Release/signing owner |  |
| Review window and environments |  |
| Final decision | Pending / approved with conditions / rejected |

A reviewer should record **Pass**, **Fail**, **Not tested**, or **Needs revision** for every gate. “Not tested” is an open gate, not a pass. Any blocker should include a reproduction, affected lesson or screen, severity, owner, fix, and retest result.

## 2. Stage 5 lesson-by-lesson content review

Read the canonical files and the Android/Web mirrors side by side. The canonical files are [`sec-05-morris-worm-history-and-response.md`](../content/core/sec-05-morris-worm-history-and-response.md) and [`sec-06-cybersecurity-career-role-families.md`](../content/core/sec-06-cybersecurity-career-role-families.md). Review the exact rendered versions in both clients; Markdown inspection alone is insufficient.

| Gate | SEC-05 result/evidence | SEC-06 result/evidence |
|---|---|---|
| Frontmatter has stable ID, title, strand, level, version, prerequisites, time, availability, risk, objectives, review status, and date |  |  |
| Title, objectives, prerequisites, and body sections agree |  |  |
| Objectives are observable, answerable, and appropriate for a beginner |  |  |
| Explanation distinguishes documented evidence from interpretation |  |  |
| Worked example uses fictional material and does not imply real authorization |  |  |
| Common-mistakes section prevents unsafe or misleading conclusions |  |  |
| Offline practice can be completed with paper/text and fictional data |  |  |
| Knowledge checks have one defensible answer or a clearly explained acceptable range |  |  |
| Knowledge-check explanations are accurate, respectful, and not merely keyword matches |  |  |
| Project/application output is safe, minimal, and does not request personal data |  |  |
| Accessibility alternatives support text, audio, large print, speech-to-text, and non-visual completion |  |  |
| Safety/responsible-use wording appears before any potentially sensitive interpretation |  |  |
| Further-reading links are current, relevant, reachable, and correctly described |  |  |
| Change log, review date, and draft status are accurate |  |  |

### 2.1 Morris worm history and response

The historical reviewer should verify that the lesson separates **intent, impact, response, accountability, and learning** rather than presenting a single heroic or villainous narrative. Cross-check the November 2, 1988 date, broad disruption, affected institutions, CERT/CC response history, and legal-accountability statements against the sources listed in [`STAGE_EVIDENCE.md`](references/STAGE_EVIDENCE.md). Record any disagreement between the FBI, CMU SEI, and Computer History Museum accounts instead of silently harmonizing it.

Confirm that the lesson does not turn the affected systems into a scorecard, treat an experiment as harmless merely because intent was described that way, or imply that one historical event explains all later security practice. Check that the language names affected communities and institutional consequences without exposing operational mechanisms, vulnerable-service names, commands, source code, concealment methods, replication steps, or real incident-response instructions.

The reviewer should also test whether a learner can complete the fictional stakeholder-card exercise without searching for malware artifacts or contacting an affected organization. The acceptable output is a reflective, source-labeled timeline and defensive learning brief—not a forensic report, legal conclusion, reproduction guide, or notoriety ranking.

### 2.2 Cybersecurity role families and learning paths

The career-framing reviewer should confirm that the lesson uses the NIST NICE Framework as a **vocabulary for work and skills**, not as a universal taxonomy, ranking system, qualification decision, employment prediction, or legal authority. Review for regional, cultural, disability, language, education, device-access, and socioeconomic assumptions. No single educational route should be presented as the only legitimate path.

Confirm that the fictional task cards and learning map request no résumé, identity document, employer contact, account, assessment answer, personal profile, or sensitive career goal. The lesson must not promise employment, salary, compensation, immigration or legal outcomes, professional certification, or regional portability. Any local labor-market or qualification claim must be removed or separately sourced and reviewed for the relevant jurisdiction and date.

Check that a learner can create a safe artifact from fictional data, understand the difference between a task, knowledge, skill, role lens, and evidence, and identify unanswered questions for a human mentor or local provider. The artifact must not be treated as proof of professional competence or production readiness.

## 3. Source, historical, cultural, and maintenance review

Use [`STAGE_EVIDENCE.md`](references/STAGE_EVIDENCE.md) as the claims inventory. For each material statement, record the source actually inspected, publication or update date when available, the reviewer’s confidence, and any uncertainty. A source’s presence in the lesson does not prove that it supports every nearby sentence.

| Source-review gate | Owner | Result/evidence |
|---|---|---|
| CMU SEI claims about CERT/CC and incident-response institution building are accurately summarized | Historical/source reviewer |  |
| FBI claims are clearly identified as a law-enforcement retrospective rather than a complete neutral history | Historical/source reviewer |  |
| Computer History Museum context is used for infrastructure and social history without treating curated exhibit language as exhaustive | Historical/cultural reviewer |  |
| ACM principles are presented as ethical guidance, not a complete legal or cultural framework | Safety/ethics reviewer |  |
| NIST NICE statements are current enough for the release date and limited to framework uses | Career/source reviewer |  |
| Differences in source purpose, perspective, uncertainty, and omitted communities are noted | Historical/cultural reviewer |  |
| Dates, names, institutions, and legal references are rechecked against the source pages | Technical/source reviewer |  |
| Source links resolve from the packaged clients or are clearly documented as external reading | Release owner |  |
| A named maintainer owns future source-freshness checks and update triggers | Project maintainer |  |
| A future correction path can mark a lesson unavailable without replacing unrelated valid content | Maintainer/release owner |  |

The historical reviewer should specifically check for **hero/villain framing, erasure of affected communities, Western or law-enforcement-only framing, ableist or class-based assumptions, and language that treats unauthorized experimentation as admirable**. Rewrite before release if the lesson’s educational point depends on sensationalism or technical notoriety.

## 4. Safety and privacy review

The safety reviewer should apply [`SAFETY.md`](SAFETY.md), not rely only on the S0 label. S0 is appropriate only if the realistic misuse of the lesson remains benign and the exercise does not provide operational security capability. If a reviewer believes a passage materially enables misuse, pause release and escalate the risk tier rather than weakening the concern through wording alone.

| Safety/privacy gate | Result/evidence |
|---|---|
| Both lessons remain `availability: offline`, `risk_tier: S0`, and `review_status: draft` until approval |  |
| No lesson includes malware, exploit construction, payloads, credentials, target lists, scanning steps, persistence, evasion, bypass, concealment, or arbitrary execution |  |
| SEC-05 focuses on impact, affected people, response institutions, accountability, and defensive learning |  |
| SEC-05 does not glorify an actor or use affected systems as a measure of cleverness or fame |  |
| SEC-06 does not request or retain résumé, identity, employer, contact, or sensitive career data |  |
| SEC-06 contains no job, salary, compensation, legal, immigration, certification, or regional-portability promise |  |
| Fictional fixtures are visibly fictional and contain no personal data or live indicators |  |
| No exercise requires a live target, external contact, account, network connection, or real incident data |  |
| No Stage 5 ID was added to the remote-pack allowlist; the lessons remain bundled-only |  |
| Android and Web privacy notes remain visible and understandable |  |
| Notes, quiz attempts, bookmarks, exports, and browser state remain local and are not silently transmitted |  |
| Any safety uncertainty has a written disposition from the safety reviewer |  |

Stop and reject the release candidate if a core path requests unexplained permissions, uploads private learning data, executes an arbitrary command, contacts a real organization, or presents operational security instructions.

## 5. Android device and native-app review

Follow the full [`DEVICE_TEST_CHECKLIST.md`](DEVICE_TEST_CHECKLIST.md), using a clean test profile where necessary. Hosted CI is not a substitute for device evidence. The test owner should record device model, Android version/API level, APK commit and checksum, network state, Termux state, text scale, and screen-reader configuration.

| Android gate | Result/evidence |
|---|---|
| Fresh install opens the privacy screen without account or required network |  |
| Learn discovers all 37 lessons with no duplicate IDs |  |
| SEC-05 and SEC-06 open, parse, and display all required sections |  |
| Practice, Search, Progress, Settings, export, and reader navigation do not crash |  |
| One Stage 5 lesson is read and completed with airplane mode enabled |  |
| Knowledge checks show explanations, allow retry, and persist best score |  |
| Notes and bookmarks persist after force-stop and restart |  |
| Search finds Stage 5 titles/body text offline |  |
| Android system font scaling keeps headings, tables, code, inputs, and controls usable |  |
| Back navigation and lifecycle changes do not lose state |  |
| Optional-pack install/delete cannot replace or delete bundled Stage 5 content |  |
| Export warnings are visible and files contain only intended local learning data |  |
| No unexpected account, analytics, advertising, storage, contact, or location permission appears |  |

### 5.1 Termux boundary

Stage 5 adds no Termux wrapper. Nonetheless, test the existing Termux paths because public release includes the shared Android surface. Verify missing Termux, present/permitted Termux, denied permission, unsupported service, and written fallback behavior using the fixed allowlist. Confirm that no Stage 5 lesson exposes a command field or suggests using Termux for its exercise.

## 6. Web/PWA and browser review

Use the desktop procedure in [`DEVICE_TEST_CHECKLIST.md`](DEVICE_TEST_CHECKLIST.md) and repeat it on at least one supported Android browser before calling the Web path release-ready. The Stage 5 desktop smoke already recorded a local-server-stopped reload for cached SEC-05 and SEC-06 routes; that evidence does not close Android-browser or assistive-technology gates.

| Web gate | Result/evidence |
|---|---|
| Shell reports 37 current lessons and preserves the local privacy note |  |
| Manifest version and service-worker cache key match the release payload |  |
| Explicit cache action stores all 37 lessons and shows a clear status |  |
| A failed update preserves the previous good cache |  |
| SEC-05 and SEC-06 are discoverable from Learn and Practice |  |
| Both Stage 5 reader routes render fully from IndexedDB with the network disabled |  |
| Notes, bookmarks, completion, quiz attempts, search, and practice remain local offline |  |
| Clearing only `packs` does not clear `state`; clearing site data explains both effects |  |
| No network request is required for cached reading, practice, or progress |  |
| Browser keyboard navigation, skip link, focus restoration, and visible focus work |  |
| Browser zoom/text scaling at 200% has no blocking clipping or horizontal overflow |  |
| Android-browser cache, reload, route, input, and responsive behavior pass separately |  |

Do not mark the Web path complete merely because Chromium desktop works. Record the browser name/version, viewport, cache state, offline method, and console/network evidence.

## 7. Accessibility and inclusive-education review

Manual accessibility review is required in addition to source inspection and automated checks. Use the relevant WCAG guidance as a test baseline, but do not claim formal conformance unless the project conducts the appropriate conformance evaluation.[1]

| Accessibility gate | Result/evidence |
|---|---|
| Large Android text and desktop browser zoom preserve reading order and essential actions |  |
| Headings, tables, lists, links, labels, inputs, buttons, and status messages have meaningful names |  |
| Keyboard-only Web use reaches every route, reader control, quiz field, note field, and action |  |
| Focus moves predictably after route changes and returns from the reader |  |
| Screen reader/TalkBack announces navigation state, quiz questions, status, warnings, and dialogs |  |
| No critical status is conveyed by color alone |  |
| Forced-colors/high-contrast mode remains usable where supported |  |
| Reduced-motion setting preserves all essential content and controls |  |
| Stage 5 practice supports non-visual, audio, large-print, speech-to-text, and plain-language alternatives |  |
| Cultural, language, disability, and access assumptions do not turn one format into a competence test |  |
| Any blocker has a reproducible issue and retest record |  |

## 8. Network, export, pack, and recovery review

The core learning path must remain local-first. If physical network inspection is available, the authorized tester should inspect only the test device and controlled test origin. Follow [`NETWORK_PACKS.md`](NETWORK_PACKS.md) for HTTPS, URL, range, size, ZIP, checksum, staging, rollback, and core-pack-protection behavior.

| Operational gate | Result/evidence |
|---|---|
| Core reading, search, practice, quiz, notes, and bookmarks work offline |  |
| No automatic polling, analytics, account sync, marketplace, cookies, or learning-data request fields occur |  |
| Only explicit user-initiated HTTPS pack actions can use the network |  |
| Stage 5 IDs are rejected if presented as an unapproved remote optional pack |  |
| Protected `pack_id: core` sample is rejected for optional activation without replacing core content |  |
| Valid non-core optional pack activates only after complete validation |  |
| Malformed, oversized, tampered, incompatible, duplicate, or unsafe-path pack is rejected |  |
| Interrupted transfer leaves the prior valid pack active and recovery behavior is recorded |  |
| Markdown and JSON exports require a user-selected destination and show a personal-data warning |  |
| Export files contain no credentials, analytics fields, hidden network data, or unrelated device data |  |

## 9. Signing, artifact identity, and distribution

The repository’s debug and release artifacts are unsigned. Use [`SIGNING.md`](SIGNING.md) for human-operated signing. Never create or store a real keystore, password, private key, or signing secret as part of this review.

| Release gate | Owner | Result/evidence |
|---|---|---|
| Release owner and approved custodians are recorded privately | Release owner |  |
| Keystore is outside the repository and encrypted at rest | Release owner |  |
| Passwords are stored only in an approved password manager or protected secret store | Release owner |  |
| Encrypted backup was restored and recovery was tested | Release owner |  |
| Package identity is confirmed as `com.aetherlearn.app` | Release owner |  |
| Version code is monotonic and update compatibility is tested | Release owner |  |
| Release APK is signed by the authorized identity | Release owner |  |
| `apksigner verify --verbose --print-certs` passes | Release owner |  |
| Signer fingerprint matches the private release inventory | Release owner |  |
| Exact signed APK checksum is recorded | Release owner |  |
| CI/public workflows cannot access signing secrets from untrusted changes | Release owner |  |
| Debug and production identities cannot be confused during installation | Release owner |  |
| Distribution channel, public pack host, TLS, access controls, and rollback process are approved | Release owner |  |
| Public artifact page identifies commit, content version, checksum, and release status | Release owner |  |

A checksum proves transfer integrity, not publisher identity. Do not call an artifact production-signed until the signer and metadata have been verified by the authorized release operator.

## 10. Final decision and post-release readiness

The release owner should approve public distribution only after every required gate is **Pass** or has a documented, explicitly accepted non-blocking limitation. The following conditions are release blockers unless the project’s governance records a justified exception before distribution:

- Any loss of local progress, notes, bookmarks, quiz attempts, or exports in a required core path.
- Any required account, unexplained permission, automatic upload, analytics path, or silent network access.
- Any arbitrary command execution, live-target activity, real credential handling, malware reproduction detail, or unreviewed dual-use expansion.
- Any historical claim that cannot be supported, culturally harmful framing, actor glorification, or omission of affected communities that changes the lesson’s meaning.
- Any career promise, discriminatory assumption, privacy-invasive learner exercise, or presentation of a worksheet as professional qualification.
- Any crash, invalid-pack activation, broken rollback, signer mismatch, artifact ambiguity, or unresolved accessibility blocker in a required flow.
- Any missing Android-device, Android-browser, screen-reader, signing, distribution, or authorized network evidence that the release owner has designated mandatory.

After approval, retain the signed release record, checksums, source-review date, reviewer identities, sanitized test evidence, known limitations, rollback path, and correction owner. If a post-release safety or factual problem is reported, pause distribution as appropriate, preserve the last-known-good artifact, mark or correct affected content, and record the decision.

## References

[1]: https://www.w3.org/WAI/standards-guidelines/wcag/ "W3C Web Content Accessibility Guidelines overview"
[2]: https://developer.android.com/guide/topics/ui/accessibility/testing "Android Developers: Test your app's accessibility"
[3]: https://www.nist.gov/itl/applied-cybersecurity/nice/nice-framework-resource-center "NIST: NICE Framework Resource Center"
[4]: https://www.acm.org/code-of-ethics "ACM: Code of Ethics and Professional Conduct"
