# AetherLearn MVP Product Specification

## 1. Decision summary

The MVP is a **phone-first native Android application** for self-taught beginners and intermediate learners who want a private, structured introduction to computing and programming. The core product works offline after a one-time installation and core-content download. Optional Termux integration enables real terminal practice but never blocks the learning path.

The MVP is intentionally not a general-purpose learning-management system, cloud classroom, multi-language IDE, or complete computer-science encyclopedia. Its purpose is to prove one high-quality loop: **learn a concept, practice it safely, record progress locally, and optionally apply it in a real Android terminal**.

## 2. Primary audience

The primary audience is **self-taught beginners and early intermediate learners using a modern Android phone**, including learners with limited or intermittent connectivity. They may have no prior programming experience, or they may know basic Python and want a structured foundation in computing, development tools, and practical problem solving.

The MVP is not optimized first for university instructors, enterprise training managers, advanced penetration testers, research programmers, or children in formal classrooms. These groups may use later releases, but their requirements must not distort the first release.

## 3. Problem statement

Many computing resources assume reliable connectivity, a desktop computer, multiple accounts, or prior knowledge. Learners need a coherent progression that remains useful offline and gives them a safe path from visual explanations to practical terminal work. AetherLearn addresses this by bundling a compact, reviewable curriculum with local progress, offline search, practice exercises, and an explicit optional bridge to Termux.

## 4. MVP boundary

### Included in v1.0

The first public release includes an installable Android application, a complete offline app shell, a versioned core content pack, local progress and notes, bookmarks, quizzes, full-text search, light and dark themes, accessible navigation, export to Markdown and JSON, safe in-app exercises, and a Termux handoff for a small number of explicitly supported beginner exercises.

The core curriculum contains four strands: digital literacy and phone computing; computational thinking and Python fundamentals; data structures and basic algorithms; and developer foundations covering the terminal, Git, debugging, and basic web concepts. The exact module list is defined in [`CURRICULUM.md`](CURRICULUM.md).

### Explicitly out of v1.0

The MVP does not include user accounts, cloud synchronization, social features, chat, public user-generated content, advertising, analytics, remote code execution, arbitrary shell-command execution, multi-language native compilation, live blockchain explorers, live security scanning, exploit kits, credential collection, offensive-security automation, on-device semantic search, iOS distribution, or a complete desktop-native client.

The MVP also excludes advanced AI-model training, model-refusal reduction techniques, dark-web navigation, reverse-engineering labs, quantum-computing simulators, large video libraries, and 3D-heavy content. These can be considered only after the core release has stable content governance and security review.

## 5. Core user journeys

### Journey A: First install and offline setup

A learner installs the Android application, sees a plain-language privacy summary, selects an optional starting level, and downloads the signed core content pack. The application displays the pack size, available storage requirement, and whether the download is complete. Once installation finishes, the learner can enable airplane mode and continue through the starter path.

The application must not require an account, contact information, advertising identifier, or analytics consent in order to use the core learning path. If the download fails, the app retains the previous valid content pack and offers retry, cancellation, and a clear recovery message.

### Journey B: Lesson to practice to progress

The learner opens a lesson containing objectives, explanation, an example, and an interactive exercise. The exercise runs inside the application using deterministic, browser-safe or native-local logic. The learner submits an answer, receives an explanation rather than only a score, and may retry. Completion is stored locally and appears in the learner’s progress view.

A module is not marked complete merely because it was opened. The default completion rule is that the learner reads the required sections and achieves the module’s configured assessment threshold, with an explicit option to mark a lesson as reviewed when no assessment is present.

### Journey C: Lesson to Termux exercise

A lesson marked `termux-optional` explains what Termux is, what will happen, which package prerequisites are needed, and what permissions or settings may be required. The learner chooses **Open in Termux**. The app checks whether Termux is installed and supported, presents the exact command or wrapper that will be launched, and requires an explicit confirmation.

The preferred native flow invokes a versioned, allowlisted helper command through the Termux RUN_COMMAND interface. The MVP never forwards arbitrary lesson text as a shell command and never silently installs packages. Package installation instructions are shown for user review, and the learner may copy them manually or open a documented setup helper. The app treats Termux output as untrusted text.

When the exercise ends, the learner returns to AetherLearn using the app’s deep link or the Android back stack. Because result callbacks are not guaranteed across all Termux versions and invocation modes, progress is updated through an explicit **Mark exercise complete** action or by importing a small locally generated result file whose schema and checksum are validated. If Termux is unavailable, the same lesson provides an in-app simulation or written alternative.

### Journey D: Notes, export, and device-only backup

The learner adds a note or bookmark while offline. The data is stored locally and remains available after restarting the app. The learner can export selected notes and progress to Markdown or JSON. The export is generated locally and clearly warns that the resulting file may contain personal learning information. No export is uploaded automatically.

### Journey E: Optional content or media pack

The learner views an optional pack such as diagrams or additional exercises. The app labels it as optional, shows its size and offline availability, and permits download, pause, deletion, and re-download. Removing an optional pack does not delete progress, notes, or the core curriculum.

## 6. Hybrid offline/online capability model

| Feature | Classification | MVP rule |
|---|---|---|
| App shell and navigation | Fully offline after install | No network required after installation |
| Core text curriculum | Fully offline after core pack download | Versioned content pack retained locally |
| Core diagrams and lightweight assets | Fully offline after core pack download | Avoid remote image dependencies |
| Local progress, notes, bookmarks, scores | Fully offline | Never sent to a server by default |
| Quizzes and flashcards | Fully offline | Deterministic local assessment |
| Full-text search | Fully offline | Search index bundled with or generated from core pack |
| Safe visual exercises | Fully offline | No network, filesystem, or arbitrary code access |
| Markdown/JSON export | Fully offline | User-initiated local file creation or share sheet |
| Optional media packs | Offline after optional download | Download is explicit and deletable |
| Termux beginner exercises | Requires local Termux integration | Optional; manual fallback always available |
| SSH to a user-owned VPS | Requires network and Termux | Out of core path; labeled advanced and user-controlled |
| Package installation in Termux | Requires Termux and usually network | Instructions only in MVP; no silent installation |
| Fresh content discovery | Requires network | Not required for core use; opt-in repository or release page |
| Content-pack update check | Requires network | No user learning data transmitted; check may be manually triggered |
| App update | Requires network or sideload | Existing installed version continues to work offline |
| Cloud sync and accounts | Not in MVP | Deliberately excluded |
| Semantic search using an on-device model | Future optional download | Not in MVP because of storage, battery, and model-maintenance cost |

The user interface must display a small, consistent availability badge on any action that is not fully offline. The badge must use both text and a non-color cue. For example: **Offline**, **Download required**, **Termux required**, or **Network required**. Network-required features must never be presented as prerequisites for completing the core path.

## 7. MVP information architecture

The bottom-level navigation contains **Learn**, **Practice**, **Search**, and **Progress**. Settings contains privacy, storage, content packs, export, accessibility, Termux integration, and diagnostic information. A lesson page contains objectives, prerequisites, estimated time, availability label, content sections, practice, notes, bookmark, and completion state.

A content module is addressable by a stable identifier such as `python/01-values-and-expressions`. Identifiers remain stable across content revisions so that bookmarks, progress, and imported results do not break when wording changes.

## 8. Measurable acceptance criteria

| Area | v1.0 target |
|---|---|
| Supported Android | Officially test Android 8.0/API 26 and later on modern Android phones; support is best-effort on newer supported versions. |
| Reference device | Test at minimum on a representative 4 GB RAM Android phone with 64 GB storage and a current Chromium-based browser for the web fallback. |
| Base app size | Target no more than 35 MB compressed download for the Android base application, excluding optional media and the core content pack. |
| Core content size | Target no more than 120 MB installed for the complete MVP text, diagrams, quizzes, and lightweight exercises. |
| Optional packs | Every pack declares compressed and installed size; no single MVP optional pack exceeds 500 MB without a later decision record. |
| Offline launch | After initial setup, the home screen and previously downloaded core lessons open in airplane mode. A smoke test must pass after a cold start. |
| Offline search | Search returns results for the bundled core index in airplane mode, with no request to a remote service. |
| Startup | On the reference device, cold launch to an interactive home screen is at or below 3 seconds after the first-run setup has completed. This is a design target to be measured, not a claim about an unbuilt app. |
| Local data | Progress, notes, bookmarks, and scores remain readable and writable in airplane mode and survive process termination. |
| Export | A user can export notes and progress without an account; exported files are human-readable and validated before writing. |
| Termux fallback | If Termux is missing or integration is disabled, every MVP Termux lesson remains completable through an in-app or written alternative. |
| Termux safety | No MVP control executes arbitrary user-provided shell text through the bridge; all launched commands are allowlisted and versioned. |
| Accessibility | All core journeys are keyboard-operable where a keyboard exists, have labeled controls, support text scaling without clipped essential content, provide visible focus, and respect reduced-motion preferences where supported. |
| Content integrity | The app rejects an invalid or incomplete content-pack manifest and keeps the last known-good pack active. |
| Privacy | No account, advertising identifier, personal-learning telemetry, or automatic upload is required for core use. |
| Recovery | Interrupted downloads can resume or restart without deleting the last valid content pack or local learning data. |

## 9. Privacy and data model

The MVP stores only what is needed for local learning: module state, quiz attempts, notes, bookmarks, settings, and optional local exercise results. The app does not collect names, emails, contacts, location, advertising identifiers, or browsing history for the core experience.

Local data is stored in the app’s private storage. Exports are user-initiated. The MVP may provide an optional encrypted backup file in a later milestone; until that implementation is reviewed, the product should describe plain JSON/Markdown export as portable export rather than secure backup.

## 10. Sustainability

The official project remains free to users and does not create paid features. Sustainability may come from donations, sponsorships, grants, educational institutions, and volunteer contributions. Funding must not require user accounts or change the offline learning experience. The first release should document the cost of release signing, hosting, mirrors, accessibility review, and content maintenance so that the project does not confuse “free” with “costless to operate.”

## 11. Definition of done for v1.0

The first public release is done when a new user can install the Android app, download the core pack, complete the first curriculum strand in airplane mode, search lessons offline, take a quiz, save a note and bookmark, view progress, export local data, and complete at least one safe optional Termux exercise or its fallback. The release must pass the acceptance criteria above, include the companion documents in this specification set, publish build and contribution instructions, and document known limitations and recovery steps.

## 12. Long-term vision and principles

AetherLearn’s long-term mission is to make rigorous computer and digital-technology education available without advertising, surveillance, forced accounts, or continuous connectivity. The product should help learners move from digital literacy through programming, systems understanding, responsible security research, and advanced computer-science study without becoming an encyclopedic content dump.

The non-negotiable principles are free access, open source, local-first privacy, offline continuity, phone-first practice, Termux as optional rather than required, human-readable content, responsible dual-use education, and accessibility by design. Progress, notes, bookmarks, quiz scores, and learning preferences remain on the device by default, and sensitive topics use safer abstractions, explicit authorization boundaries, controlled labs, and fictional examples.

The long-term product shape has separable layers: a coherent curriculum, an offline-capable learning client, browser-safe visualizations, optional real-world terminal practice, and transparent community contribution. Future topics may include computing history, programming, algorithms, operating systems, networks, databases, distributed systems, computer architecture, AI, security, scientific computing, graphics, HCI, embedded systems, robotics, and other technologies. This is a roadmap, not approval for first-release scope; any expansion requires a new decision record and review.

The project’s sustainability direction is community-funded infrastructure through donations, sponsorships, grants, educational partnerships, and volunteer maintenance. Funding must not create a paid learning tier or make the core path dependent on an account. If official hosting stops, downloaded applications and content should continue to work and community mirrors should remain possible.

The success condition is a learner installing the Android app, downloading a modest core pack once, completing a coherent path privately and offline, taking notes and quizzes, exporting work, and optionally moving from explanation to an authorized local terminal exercise without losing context or being forced into a cloud account.

## References

[1]: https://developer.android.com/guide/components/intents-filters "Android Developers: Intents and intent filters"
[2]: https://github.com/termux/termux-app/wiki/RUN_COMMAND-Intent "Termux: RUN_COMMAND Intent"
[3]: https://developer.android.com/training/app-links "Android Developers: About deep links"
[4]: https://web.dev/learn/pwa/service-workers "web.dev: Service workers"
