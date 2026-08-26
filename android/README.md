# AetherLearn Android shell

This directory contains the native Android client for AetherLearn. It uses Kotlin, Jetpack Compose, Material 3, and an app-private SQLite storage boundary. The app has four bottom destinations—Learn, Practice, Search, and Progress—plus Settings. It reads the five validated lessons from local assets, supports offline learning state, exports, local optional-pack management, and a safe optional Termux pilot.

## Requirements

Install Android Studio or the Android command-line tools, JDK 17 or newer, Android SDK Platform 37, and Android SDK Build Tools 36.0.0 or newer. The project targets Android API 37 and supports Android API 26 or newer. The Gradle wrapper pins Gradle 9.4.1. These version choices follow the current Android Compose and Android Gradle Plugin documentation; see [`../docs/references/android_m2_build_notes.md`](../docs/references/android_m2_build_notes.md).

## Build

From this directory, run:

```bash
./gradlew assembleDebug
```

The debug APK is produced at `app/build/outputs/apk/debug/app-debug.apk`. Install it on a connected device or emulator with:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

The first launch displays the local privacy screen. After continuing, the app opens the offline learning shell. Progress, quiz attempts, notes, bookmarks, pack status, first-run state, and theme preference are stored in app-private SQLite storage. No network permission is requested by the app manifest. The Termux pilot additionally declares only `com.termux.permission.RUN_COMMAND` and does not grant it automatically.

## Checks

From the repository root, the content and repository checks remain:

```bash
python3 scripts/check_markdown_links.py
python3 scripts/check_secrets.py
python3 scripts/build_pack.py --content-dir content/core --output-dir build/core-pack
python3 scripts/validate_content.py --content-dir content/core --manifest build/core-pack/manifest.json
python3 -m unittest discover -s tests -v
```

M5 currently includes the contract, allowlist, package detection, explicit confirmation, fixed-argument `RUN_COMMAND` handoff, learner-confirmed completion, and in-app fallback for two local-only S1 exercises. It does not install packages, change Termux settings, use shared storage, accept arbitrary commands, receive terminal output as completion proof, or contact a network target. The remaining device/emulator checks are documented in `docs/TODO.md`; the product and safety boundaries are defined in [`../docs/PRODUCT_SPEC.md`](../docs/PRODUCT_SPEC.md), [`../docs/SAFETY.md`](../docs/SAFETY.md), and [`../docs/TERMUX_WRAPPERS.md`](../docs/TERMUX_WRAPPERS.md).
