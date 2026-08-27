# Android M2 build notes

Checked 2026-08-24.

- Android's official Compose setup guide recommends the Compose Compiler Gradle plugin with Kotlin 2.0+ and shows Kotlin 2.3.21 in its current version-catalog example. It recommends the Compose BOM `2026.08.00` and notes that recent Compose releases may require `compileSdk 37` and AGP 9. Source: https://developer.android.com/develop/ui/compose/setup-compose-dependencies-and-compiler
- Android Gradle Plugin 9.2.0's official release notes state that it supports a maximum API level of 37.0, uses Gradle 9.4.1 as its minimum/default in that release, uses SDK Build Tools 36.0.0, and requires JDK 17. Source: https://developer.android.com/build/releases/agp-9-2-0-release-notes
- The sandbox has Java 21 but no installed Android SDK, Gradle executable, or adb. The project therefore needs a Gradle wrapper and the Android SDK/command-line tools must be installed by a developer or CI environment before a local APK build can be verified.

## Implementation decision

Use AGP 9.2.0, Gradle 9.4.1 wrapper, Kotlin 2.3.21, Compose Compiler plugin 2.3.21, Compose BOM 2026.08.00, compileSdk 37, targetSdk 37, and minSdk 26. Pin versions in `gradle/libs.versions.toml`. Keep dependencies limited to AndroidX Activity Compose, Compose UI/Material3, Navigation Compose, Lifecycle ViewModel Compose, and Room runtime/ktx/compiler only if needed for the storage foundation.
