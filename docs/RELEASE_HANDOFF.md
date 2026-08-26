# AetherLearn release artifact and human-test handoff

**Purpose:** give a new tester one place to build or obtain an APK, install it safely, prepare the sample content pack, run the human device checklist, and understand which release gates are still open.

**Release posture:** the repository contains the complete 20-module MVP and an unsigned release build path. It does **not** contain a production signing key, signed metadata, a private key, an automatic updater, a public pack registry, or verified physical-device results. Do not treat the unsigned release APK or sample core ZIP as a production release.

## 1. Build or obtain an APK

Prerequisites for a local build are Android Studio or Android command-line tools, JDK 17 or newer, Android SDK Platform 37, Android SDK Build Tools 36.0.0 or newer, and a cloned repository checkout.

From the repository root:

```bash
cd android
chmod +x gradlew
./gradlew --no-daemon --max-workers=1 assembleDebug assembleRelease testDebugUnitTest lintDebug
```

The outputs are:

| Artifact | Path | Meaning |
|---|---|---|
| Debug APK | `android/app/build/outputs/apk/debug/app-debug.apk` | Development/test APK for a controlled device or emulator |
| Release APK | `android/app/build/outputs/apk/release/app-release-unsigned.apk` | Minified/shrunk but **unsigned**; it is not installable as a release artifact until an authorized operator signs it |

Generate checksums before transferring an APK:

```bash
sha256sum android/app/build/outputs/apk/debug/app-debug.apk
sha256sum android/app/build/outputs/apk/release/app-release-unsigned.apk
```

### Download from CI

Open the repository’s **Actions** tab, select the **Quality** workflow, open a successful run for the exact commit under test, and download the `aetherlearn-apks` artifact from the run summary. Confirm the run’s commit SHA and both job conclusions before downloading. Extract it without renaming its internal directories. It contains the debug APK, the unsigned release APK, and their `.SHA256SUMS` sidecars. Do not use an artifact from an untrusted fork, an unknown workflow, or an unrelated commit.

From the extracted artifact directory, verify the sidecars before installation:

```bash
sha256sum -c build/release/app-debug.SHA256SUMS
sha256sum -c build/release/app-release-unsigned.SHA256SUMS
```

A checksum confirms file-transfer integrity; it does not prove publisher identity. The release APK remains unsigned until the authorized process in [`SIGNING.md`](SIGNING.md) is completed.[3] For ordinary device testing, install `app-debug.apk`; do not try to install `app-release-unsigned.apk` directly. Android package installation will reject an unsigned APK. A signed release must be verified separately before installation.

## 2. Install safely on Android

### ADB

Enable **Developer options** and **USB debugging** only on a test device you control. Connect it, accept the debugging prompt, and check the device:

```bash
adb devices
```

Install the debug APK:

```bash
adb install -r android/app/build/outputs/apk/debug/app-debug.apk
```

The `-r` update path works only when the installed application has a compatible signing identity. A debug-signed installation and a release-signed installation normally cannot update one another. If ADB reports a signature or certificate mismatch, stop and decide whether the existing app data must be preserved; uninstalling to resolve the mismatch deletes local learning data. Use separate test devices or explicitly exported fictional data for clean-install comparisons.

The `-r` option updates an existing installation and normally retains app data. Do not use it for a clean-install test. For a clean test, record only fictional data first, then uninstall intentionally:

```bash
adb uninstall com.aetherlearn.app
adb install android/app/build/outputs/apk/debug/app-debug.apk
```

Never uninstall an installation containing learning data that must be preserved.[2] If the phone already has an app with the same package name signed by another publisher, do not remove it merely to force installation; use a dedicated test device or obtain the authorized matching artifact.

### File transfer

Copy the verified APK to the phone using a USB cable, private local transfer, or another channel approved by the device owner. Open it with the Android file manager and follow the package-installer prompt. Keep the file private and delete it afterward unless it is part of the controlled evidence record.

If Android displays **Install unknown apps**, grant the permission only to the specific file manager or transfer application being used, only for the duration of the test. Install the verified APK, then return to **Settings → Apps → Special app access → Install unknown apps**, select that source, and turn the permission off again. Do not enable installation from unknown sources globally or grant it to an unrelated browser. Menu names vary by Android version.[1]

### First launch

Begin with [`DEVICE_TEST_CHECKLIST.md`](DEVICE_TEST_CHECKLIST.md). The first launch should show the local privacy screen and then open the core learning shell without an account or network. Installation success alone does not prove device, accessibility, Termux, or offline persistence readiness.

## 3. Sample core-pack artifact

The committed sample artifact is [`sample-pack/aetherlearn-core-pack-1.0.0.zip`](sample-pack/aetherlearn-core-pack-1.0.0.zip), with checksum sidecar [`sample-pack/aetherlearn-core-pack-1.0.0.zip.SHA256SUMS`](sample-pack/aetherlearn-core-pack-1.0.0.zip.SHA256SUMS). It contains the 27 current bundled lessons—the original 20-module MVP baseline plus the three approved Stage 1 lessons and four Stage 2 drafts—along with a root `manifest.json` and `modules/<filename>` entries.

Regenerate it from source:

```bash
python3 -m pip install --requirement requirements-dev.txt
python3 scripts/build_pack.py --content-dir content/core --output-dir build/core-pack
mkdir -p docs/sample-pack
cp build/core-pack.zip docs/sample-pack/aetherlearn-core-pack-1.0.0.zip
sha256sum docs/sample-pack/aetherlearn-core-pack-1.0.0.zip > docs/sample-pack/aetherlearn-core-pack-1.0.0.zip.SHA256SUMS
python3 scripts/check_release_artifact.py \
  docs/sample-pack/aetherlearn-core-pack-1.0.0.zip \
  docs/sample-pack/aetherlearn-core-pack-1.0.0.zip.SHA256SUMS
```

### Important limitation

The deterministic builder emits `pack_id: core`. The Android installer intentionally rejects a network attempt to replace the protected core pack. The committed ZIP is therefore suitable for checksum, transport, malformed-pack, and core-replacement rejection testing; it is **not** expected to activate as a successful optional network pack.

A successful optional-pack activation requires an authorized non-core pack containing only approved, not-yet-bundled IDs and a controlled HTTPS host. The repository does not generate or publish such a pack automatically.

### Temporary hosting

Android accepts HTTPS only. A plain local server is useful for Web/PWA testing but is correctly rejected by Android:

```bash
python3 -m http.server 8443 --directory docs/sample-pack
```

Do not disable cleartext protection or weaken certificate validation to make a local HTTP or self-signed-certificate test pass. For Android transport testing, use a temporary HTTPS host with a certificate trusted by the test device and controlled by the test owner.

After this commit is available on the public repository, the sample ZIP can be downloaded from:

```text
https://raw.githubusercontent.com/Sudipsudip5250/aetherlearn-mvp-spec/main/docs/sample-pack/aetherlearn-core-pack-1.0.0.zip
```

This is a public file URL, not an authenticated or signed pack registry. With the core sample, the expected result is safe validation rejection rather than core replacement.

## 4. Android Settings network-pack flow

Use [`NETWORK_PACKS.md`](NETWORK_PACKS.md) for protocol details and this sequence for the human test:

1. Open AetherLearn and tap **Settings**.
2. Scroll to **Network content pack** under **Storage & content packs**.
3. Confirm the screen says **Network required · user initiated** and that the core path stays available offline.
4. Enter an authorized HTTPS ZIP URL in **HTTPS pack URL**.
5. Tap **Download pack** once. Confirm a progress message and, when a length is known, a progress bar.
6. While it runs, tap **Pause**. Confirm the transfer pauses and the URL field remains disabled.
7. Tap **Resume**. Confirm progress continues. If the server does not support HTTP Range, record that the app safely restarts the transfer.
8. Start or retry a controlled transfer and tap **Cancel**. Confirm cancellation is reported and a later attempt starts cleanly.
9. For a valid non-core optional pack, wait for validation and activation, then confirm the new lesson is visible without duplicate core IDs.
10. For the committed core sample, record the expected validation rejection and confirm the existing core lessons remain available.
11. If a release owner supplies malformed, oversized, tampered, or incompatible fixtures, confirm rejection and preservation of the previous valid content.
12. Record host, URL class, Range support, device state, status text, and sanitized screenshots.

The downloader is foreground and user-controlled. It does not create an account, transmit learning data, use cookies or credentials, poll automatically, or expose a marketplace.

## 5. Required human verification

Complete [`DEVICE_TEST_CHECKLIST.md`](DEVICE_TEST_CHECKLIST.md) in order. It covers fresh install and privacy, navigation and discovery of the 27 current lessons, complete offline lessons, quizzes, progress, notes, bookmarks, force-stop persistence, Markdown/JSON exports, local packs, network-pack controls and interruption recovery, Termux variants, desktop and Android-browser PWA caching, accessibility basics, privacy boundaries, defect recording, and cleanup. The 20-lesson MVP baseline remains a useful comparison point; the three Stage 1 and four Stage 2 lessons must also be sampled.

Mark unavailable environments **Not tested**. Desktop source checks do not prove Android-device behavior, and a successful build does not prove accessibility, Termux, low-storage, process-death, or interrupted-network behavior.

## 6. Release decision boundary

The current repository is suitable for controlled human testing. The debug APK is the normal unsigned-by-project test artifact; the release APK is explicitly unsigned and must be signed before it can be installed or distributed. The repository is not production-signed or publicly approved. The release owner must still obtain human approval for all 27 current lessons, including the original 20-module baseline, the three draft Stage 1 lessons, and the four draft Stage 2 lessons, complete representative Android/emulator and Termux tests, complete Android-browser and assistive-technology tests, establish authorized signing and key rotation, verify signed metadata, and approve the distribution and pack-host process.

Do not commit a keystore, password, signing configuration containing secret values, private certificate material, or personal test data. Follow [`SIGNING.md`](SIGNING.md) for human-operated signing only.

## References

[1]: https://support.google.com/android/answer/9457058 "Google Android Help: Install unknown apps from other sources"
[2]: https://developer.android.com/tools/adb "Android Developers: Android Debug Bridge (adb)"
[3]: https://docs.github.com/en/actions/using-workflows/storing-workflow-data-as-artifacts "GitHub Docs: Store and share data with workflow artifacts"
