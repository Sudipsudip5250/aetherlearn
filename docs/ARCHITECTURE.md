# AetherLearn MVP Architecture

## 1. Architecture decision

The MVP uses a **native Android application written in Kotlin with Jetpack Compose** as its primary client. A static PWA is a secondary reading and practice client built from the same content source, but it is not required to provide full Termux integration or full parity in v1.0.

This decision is driven by the phone-first and Termux requirements. Native Android provides direct access to Android intents, app links, private app storage, the Android Keystore, file pickers, share sheets, lifecycle-aware background work, and accessibility APIs. A PWA can provide excellent offline reading, but it cannot be the primary integration surface for controlled Android-to-Termux command handoff without a native bridge. React Native or Flutter could support the bridge through platform code, but they would add a cross-platform abstraction before iOS is an MVP requirement. Native Android minimizes the first release’s trust boundaries and testing surface.

| Criterion | Native Android/Kotlin | PWA | React Native/Flutter |
|---|---|---|---|
| Termux intents and permissions | Direct and first-class | Not reliable as a web capability | Requires native module or plugin |
| Offline local storage | Strong private app storage and SQLite options | Strong with IndexedDB, but browser quotas and eviction vary | Strong through native libraries |
| Android accessibility and lifecycle | Direct platform control | Depends on browser and installed mode | Good, with framework-specific testing |
| App size and distribution | APK/AAB and sideload/mirror possible | Easy URL distribution | Native distribution and bridge complexity |
| Desktop/web reuse | Low in client UI; shared content remains high | High | Medium |
| MVP maintenance scope | Smallest for Android-first product | Smallest for web-only product | Larger due framework plus native integration |
| Decision | **Selected** | Secondary client | Deferred |

The repository should keep the content schema, lesson identifiers, localization approach, and validation tools platform-neutral. The Android client and PWA may have separate presentation layers while consuming the same signed content-pack format.

## 2. Proposed technology stack

| Layer | MVP choice | Reason |
|---|---|---|
| Android language | Kotlin | Strong Android tooling, null-safety, and maintainable platform integration |
| UI | Jetpack Compose with Material 3 | Declarative, testable UI suited to responsive phone layouts and theme support |
| Navigation | AndroidX Navigation Compose | Stable in-app routes and deep-link handling |
| Local state | Room/SQLite for user data | Structured local persistence without a backend |
| Search | SQLite FTS4 index generated from content packs | Fully offline, deterministic, and smaller than an on-device semantic model |
| Content authoring | Markdown with YAML frontmatter | Human-readable, reviewable, diff-friendly, and community-contributable |
| Content build | Kotlin or Python validation/build CLI | Compiles lessons, metadata, search fields, asset manifests, and pack checksums |
| Background work | WorkManager only for user-visible pack downloads and validation | Resilient work under Android lifecycle constraints |
| Secure key storage | Android Keystore | Device-protected key material for future encrypted backup support |
| Testing | Kotlin unit tests, Compose UI tests, Android instrumentation, manual device matrix | Covers content, offline behavior, navigation, and native bridge behavior |
| Web fallback | Static TypeScript/React PWA sharing content-pack contracts | Provides desktop/web reading without introducing a backend |

The MVP should avoid a server, database, authentication system, analytics SDK, remote code runner, or mandatory third-party content API. A public release site and repository may exist, but the application’s core learning path must not depend on them.

## 3. Runtime layers

The app has four runtime layers. The **presentation layer** renders lessons, quizzes, progress, notes, search, storage status, and availability labels. The **learning-state layer** writes progress, attempts, notes, bookmarks, and settings to local storage. The **content layer** reads the currently active signed pack and exposes stable module IDs, prerequisites, metadata, and assets. The **integration layer** handles file export, optional content downloads, Android App Links, and explicit Termux handoff.

No layer may transmit learning data by default. Network access is limited to user-initiated content-pack or application update actions. The update client should not send search queries, lesson history, notes, or quiz data.

## 4. Content-pack format and update strategy

The source repository stores lessons as Markdown files with frontmatter. A build step validates required fields, checks links and identifiers, computes plain-text search fields, records asset sizes, and emits a versioned content pack.

A pack contains a manifest, lessons, quiz definitions, lightweight assets, search data, and checksums. The manifest includes a schema version, pack version, minimum app version, creation timestamp, module count, total sizes, and a SHA-256 digest for each payload. The app verifies the manifest and all payload checksums before activation.

The update process is staged. The app keeps the active pack while downloading a new pack into temporary storage. It validates the new pack, activates it atomically, and retains the previous known-good pack until the new one has passed a startup and index check. If validation fails or storage is insufficient, the app leaves the active pack untouched and explains the failure.

Core content is bundled and remains available offline. Optional lesson packs are independently downloadable and deletable from Android Settings after the learner supplies an HTTPS URL. The Android downloader streams into app-private partial storage, resumes with HTTP Range when supported, exposes foreground pause/resume/retry/cancellation, rejects unsafe or oversized archives, verifies the existing manifest and per-file hashes, and activates only after parser/startup checks. A new app version must remain able to open the last compatible core pack. The PWA uses a versioned service-worker cache for its application shell and a separate IndexedDB content-pack store; service-worker caching alone is not treated as proof that the curriculum is offline-available. Transport and checksums are implemented; signed pack metadata and an approved public distribution host remain release follow-ups.

## 5. Local data and privacy

User learning data is stored in the Android application’s private storage. The local schema includes module state, quiz attempts, notes, bookmarks, preferences, and imported exercise results. Each record includes a schema version so migrations can be tested and rolled back where practical.

The MVP supports user-initiated Markdown and JSON export. Encrypted backup is a post-MVP feature because it requires careful key ownership and recovery design. When implemented, the app should generate a random data-encryption key, protect it with the Android Keystore, and require an explicit user-controlled export password or recovery mechanism for portability to another device. The app must never imply that a device-bound Keystore key alone creates a portable backup.

Deletion controls should allow the user to delete individual notes, optional packs, imported results, or all local learning data. The app should explain that exported files outside the app’s private storage are under the user’s control and may persist in other apps or backups.

## 6. Termux integration design

### 6.1 Supported scope

The MVP supports a small allowlist of beginner exercises. Each exercise is represented by a stable ID and a versioned wrapper contract. The app does not send arbitrary shell strings, lesson content, or user-entered commands through the bridge. It does not silently install packages, open SSH connections, or grant permissions.

A future advanced exercise may document user-owned SSH or VPS workflows, but the MVP treats that as a network-dependent, user-controlled activity and does not automate credentials, hosts, or remote commands.

### 6.2 Native handoff flow

1. The lesson displays a `Termux optional` label, prerequisites, expected package names, required storage, and a safety note.
2. The learner taps **Open in Termux**.
3. The app detects whether the Termux package is available and whether the Android integration is supported.
4. The app explains that Termux may require the user to grant the `com.termux.permission.RUN_COMMAND` permission and enable Termux’s external-command setting. It links to setup instructions rather than changing settings silently.
5. The app shows the exact versioned wrapper ID, working directory, arguments, and expected outputs. User confirmation is required.
6. The app sends an explicit Android intent to the Termux `RunCommandService` for the allowlisted wrapper. The command runs in foreground mode unless the exercise is explicitly designed as a background task.
7. The learner performs the exercise in Termux. The app does not trust terminal text, exit output, or an external process as proof of completion.
8. The learner returns to the app through the Android back stack or a controlled deep link. Progress is updated only after explicit confirmation or validated import of a small result file.

The Termux wiki documents that third-party apps can use the RUN_COMMAND interface, that Java-based invocation can receive results in supported versions, and that permissions, external-app settings, package visibility, storage access, and device battery behavior can affect the flow. Therefore the app must expose a manual copy-and-run fallback and test several Termux installation versions rather than assuming a universal callback.[1]

### 6.3 Deep links and return flow

The Android app registers a narrowly scoped custom URI or HTTPS App Link for returning to a known exercise-result route. The route contains only a non-sensitive exercise identifier and one-time nonce; it must not contain notes, tokens, shell commands, or personal learning data. The app validates the nonce and imports no result merely because a URI was opened.

If a public website later needs to route learners into the native app, verified Android App Links are preferred over unverified deep links. Android’s documentation distinguishes ordinary deep links, which may show a chooser, from verified App Links associated with the project’s website.[2]

## 7. Safe execution model

The MVP has no server-side code execution. In-app activities are either static examples, deterministic quiz logic, data visualizations, or narrowly scoped interpreters with no filesystem, network, reflection, or arbitrary native-code access. A general-purpose in-app Python interpreter is not part of v1.0.

Real Python and terminal exercises run only through the learner’s own Termux installation and are labeled as such. The app provides safe, beginner-oriented commands and local files, but the learner remains responsible for reviewing commands and permissions. Exercises must not target third-party systems, collect credentials, scan public ranges, download exploit kits, or bypass security controls.

## 8. Threat model summary

| Asset or boundary | Plausible threat | Primary control | Verification |
|---|---|---|---|
| Local notes and progress | Device user or another app reads exported or local data | Private app storage, minimal collection, explicit export, future encrypted backup | Inspect storage and export flows; verify no network transmission |
| Content pack | Tampered or incomplete lesson pack is activated | Manifest, per-file hashes, atomic activation, last-known-good rollback | Corrupt files and manifests in automated tests |
| Termux bridge | Malicious or malformed input causes unintended command execution | Allowlisted wrapper IDs, fixed executable paths, explicit confirmation, no arbitrary shell text | Negative tests for unknown IDs, altered arguments, missing permissions |
| Deep-link return route | Crafted URI causes false completion or data import | Narrow route, nonce validation, no automatic completion, schema validation | Fuzz URI parameters and replay one-time nonces |
| In-app exercise engine | Malicious lesson asset or exercise exhausts resources | Trusted reviewed content, bounded input sizes, no arbitrary code or network access | Resource-limit and malformed-content tests |
| Optional downloads | Large or malicious asset consumes storage or exploits parser | Declared size, checksum, pack validation, user confirmation, safe parsers | Oversize, truncated, and malformed pack tests |
| Update channel | Compromised mirror serves a modified release | Signed releases, checksums, reproducible build goals, multiple mirrors | Signature/checksum failure tests and release checklist |
| Privacy boundary | Accidental analytics or diagnostic upload leaks learning data | No analytics SDK, network allowlist, static analysis, privacy review | Inspect dependencies and proxy/network logs |

Residual risks include a compromised Android device, malicious software already granted powerful device permissions, unsafe commands the learner chooses to run directly in Termux, and vulnerabilities in the Android or Termux platform. The product must not claim to eliminate those risks.

## 9. Release and test matrix

The MVP must be tested on at least two modern Android phones from different manufacturers, one low-memory or battery-aggressive configuration, airplane mode, a fresh install, an interrupted content download, an invalid content pack, a missing Termux installation, a Termux installation with integration disabled, and a successful supported Termux handoff. The PWA fallback must be smoke-tested on a current Chromium-based desktop browser and Android browser with the network disabled after the core pack is cached.

## References

[1]: https://github.com/termux/termux-app/wiki/RUN_COMMAND-Intent "Termux: RUN_COMMAND Intent"
[2]: https://developer.android.com/training/app-links "Android Developers: About deep links"
[3]: https://developer.android.com/guide/components/intents-filters "Android Developers: Intents and intent filters"
[4]: https://web.dev/learn/pwa/service-workers "web.dev: Service workers"
