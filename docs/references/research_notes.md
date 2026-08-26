# Research notes for MVP specification

## Verified external findings

- Android Intents are messaging objects used to request actions from app components; they can start activities, services, or deliver broadcasts. Explicit intents identify a specific component; implicit intents allow another app to handle a general action. Android documentation cautions that services should be started explicitly rather than through implicit intents. Source: https://developer.android.com/guide/components/intents-filters
- The Termux RUN_COMMAND wiki states that third-party apps can run commands in Termux context through `RunCommandService` or the Termux Tasker plugin client. Java-based invocation can receive command results; `am startservice` cannot receive results in the same way. The wiki says Termux app version `>= 0.109` is required for result handling via Java.
- Termux RUN_COMMAND requires the sender to request `com.termux.permission.RUN_COMMAND`, user grant of that permission, and the Termux `allow-external-apps` property set to true. Depending on the flow, Draw Over Apps, shared-storage permission, or battery-optimization changes may be needed. The wiki also notes package-visibility considerations for apps targeting Android 11/API 30 or later.
- RUN_COMMAND accepts an absolute executable path, argument array, optional stdin, working directory, foreground/background mode, and session metadata. This supports a controlled helper-command approach, but the app should not send arbitrary shell text by default.

## Design implications

- The MVP should make Termux integration optional and user-mediated, not a hidden background execution path.
- The first integration should launch a versioned, app-provided exercise wrapper or clearly documented command with fixed arguments; arbitrary command execution should be excluded from MVP.
- The app should detect Termux availability, explain required permissions and settings, show the exact command or package prerequisites before launch, and provide a manual fallback when integration is unavailable.
- Returning exercise results should not be assumed to work across all Termux installation methods, versions, or invocation modes. MVP progress should be updated by explicit user confirmation or a local import/result file rather than treating the Termux process as a trusted completion signal.
- Use Android App Links/deep links only for navigation back into the app; do not encode secrets or sensitive learning data in URIs.

## Additional verified external findings

- Android's current deep-link guide distinguishes standard deep links, which use intents and may show a disambiguation dialog, from verified Android App Links, which associate a website with an app and can open matching content directly. The guide says App Links are supported on Android 6 and later on devices with Google services; standard deep links are available on all Android versions. Source: https://developer.android.com/training/app-links
- The web.dev PWA service-worker guide lists service-worker registration, scope, lifecycle, updates, lifespan, and capabilities as core concerns. The PWA navigation also separates caching, offline data, installation, and update topics. Source: https://web.dev/learn/pwa/service-workers

## Additional design implications

- A native Android shell can use an internal app/deep-link route such as `learnapp://exercise/<id>` for controlled navigation, while a verified HTTPS App Link is preferable for public website-to-app routing if a domain is available.
- PWA offline behavior should be designed around a versioned service-worker cache plus an explicit content-pack layer; caching the application shell alone is not equivalent to making all curriculum content available offline.
- Updates should be staged and reversible: retain the last known-good content pack, validate manifest and checksums before activation, and allow the user to defer optional media downloads.
