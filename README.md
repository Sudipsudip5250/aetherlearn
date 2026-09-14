# AetherLearn

A **free, open-source, privacy-first** computer-science learning app. It runs mostly offline on a phone, with **no ads, no accounts, no analytics, and no tracking**.

- **Primary client:** native Android (Kotlin, Jetpack Compose, Material 3)
- **Secondary client:** static Web/PWA (service worker + IndexedDB)
- **Content:** 37 bundled lessons (frozen 20-lesson MVP baseline + approved Stages 1–5)
- **License:** MIT

This is the public product repository. It is not a specification stub.

## Download (testing builds)

These files are **debug/testing** artifacts. They are **not** Play-signed production releases. Install the APK only on a device you control, via **Install unknown apps**, at your own risk. Turn that permission off afterwards. A checksum confirms the file was not corrupted in transit; it does not prove a store identity.

| File | What it is |
|---|---|
| [AetherLearn-debug.apk](https://github.com/Sudipsudip5250/aetherlearn/releases/download/testing/AetherLearn-debug.apk) | Installable debug APK |
| [AetherLearn-debug.apk.SHA256SUMS](https://github.com/Sudipsudip5250/aetherlearn/releases/download/testing/AetherLearn-debug.apk.SHA256SUMS) | APK checksum |
| [aetherlearn-visual-foundations-1.1.0.zip](https://github.com/Sudipsudip5250/aetherlearn/releases/download/testing/aetherlearn-visual-foundations-1.1.0.zip) | Optional 37-diagram visual pack |
| [aetherlearn-core-pack-1.0.0.zip](https://github.com/Sudipsudip5250/aetherlearn/releases/download/testing/aetherlearn-core-pack-1.0.0.zip) | Sample core lesson ZIP (for pack-install tests) |

Release page: [testing](https://github.com/Sudipsudip5250/aetherlearn/releases/tag/testing)

After download:

```bash
sha256sum -c AetherLearn-debug.apk.SHA256SUMS
```

The same pack ZIPs also live in [`docs/sample-pack/`](docs/sample-pack/) in this repository.

## Try the web reader

Serve `web/` over HTTP (not `file://`):

```bash
python3 -m http.server 4173 --directory web
```

Then open the local URL, cache the core pack once, and you can keep reading offline in that browser.

## What stays local

Progress, notes, bookmarks, quiz attempts, reading palettes, and learning goals stay **on the device or in this browser**. There is no login and no cloud learner dashboard. Optional Termux exercises are Android-only and allowlisted.

## Docs

Start at [`docs/KNOWLEDGE_GRAPH.md`](docs/KNOWLEDGE_GRAPH.md) (session resume) or the [documentation index](docs/README.md).

| Doc | Use |
|---|---|
| [`docs/PRODUCT_SPEC.md`](docs/PRODUCT_SPEC.md) | Product boundary and non-goals |
| [`docs/SAFETY.md`](docs/SAFETY.md) | Dual-use / lab / Termux rules |
| [`docs/CURRICULUM.md`](docs/CURRICULUM.md) | Lesson catalog |
| [`docs/RELEASE_HANDOFF.md`](docs/RELEASE_HANDOFF.md) | Build, APK, packs, human test |
| [`docs/SIGNING.md`](docs/SIGNING.md) | How a human signs a real release (no keystore in git) |

## Contribute

Read [`CONTRIBUTING.md`](CONTRIBUTING.md) and [`docs/SAFETY.md`](docs/SAFETY.md). Do not send credentials, personal data, exploit kits, or commands aimed at systems you do not own. No Stage 6 curriculum tracks without a decision-log entry.

CI: content/parity validators run on every push (~1 minute). Full Android APK assembly is **manual or tag-only** so the GitHub Free 2,000-minute budget is not burned on docs edits.
