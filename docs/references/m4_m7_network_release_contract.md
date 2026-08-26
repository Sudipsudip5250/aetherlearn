# M4 network packs and M7 release hardening

Date: 2026-08-25

## Scope

This slice adds a real, user-initiated Android network content-pack path and the feasible repository-side M7 hardening gates. The core learning path remains bundled and offline-first. Network access is never used for progress, notes, bookmarks, quiz attempts, search, analytics, or automatic synchronization.

## Network-pack contract

The Android client accepts an explicitly entered HTTPS URL for a deterministic AetherLearn ZIP pack. The pack format is the existing `build_pack.py` output: `manifest.json` at the archive root and lesson payloads under `modules/`. The manifest must use the existing schema, contain a pack ID, version, module count, total size, and per-module SHA-256/size metadata. Every extracted lesson is validated against the current parser and curriculum registry before activation.

The client downloads only into app-private storage. It uses a streaming `HttpsURLConnection` request, bounded connect/read timeouts, an HTTP `Range` request when a valid partial file exists, and a local partial file that can be paused, resumed, retried, or cancelled by the learner. A server that ignores a range request causes a safe restart rather than appending incompatible bytes. The client does not use Android `DownloadManager`, shared storage, credentials, cookies, arbitrary headers, or user-authored commands.

A successful transfer is extracted into a staging directory, validated, checked for declared sizes and hashes, and startup-parsed before atomic activation. The current active pack and installed-pack metadata remain unchanged on any failure, cancellation, malformed archive, checksum mismatch, unsupported schema, path traversal attempt, or insufficient-storage error. ZIP entries are restricted to `manifest.json` and safe `modules/*.md` paths; extraction is bounded by declared and implementation limits.

The initial UI is foreground and explicit: the learner enters the source URL, sees the network-required label, starts the transfer, can pause/resume/cancel, and receives a success or recovery message. There is no background auto-update, no silent first-run network request, and no public default URL because this private repository does not yet have an approved public pack host. A release operator may provide a repository or release URL in the settings field.

## M7 gates implemented in this slice

Repository and CI gates include content/link/secret checks, Android build/unit-test/lint, web syntax and JSON checks, pack generation and verification, byte parity, dependency review, release APK checksum generation, and manifest/network-permission inspection. Browser smoke checks cover desktop responsive behavior, keyboard/focus semantics, offline PWA reload, and no-console-error inspection where the environment permits.

M7 device-matrix, Android-browser, accessibility-assistive-technology, interrupted-radio, low-memory, and Termux runtime tests remain evidence-limited until physical devices or emulators are attached. The release documentation must not claim those gates are complete from static checks alone.

## Primary references

[1]: https://developer.android.com/develop/connectivity/network-ops/connecting "Android Developers: Connect to the network"
[2]: https://developer.android.com/privacy-and-security/risks/unsafe-download-manager "Android Developers: Unsafe Download Manager"
[3]: https://developer.android.com/topic/architecture/data-layer/offline-first "Android Developers: Build an offline-first app"
[4]: https://docs.python.org/3/library/zipfile.html "Python documentation: zipfile"

## Browser checkpoint

On 2026-08-25, Chromium loaded the web shell with 11 modules and the current M7 client after the service-worker cache update. The browser console showed no output/errors on initial load. The catalog displayed all eleven lessons and the cache status remained explicit and user-controlled.

Keyboard smoke: Chromium exposed the skip link as the first tab stop; activating it moved the URL to `#main-content` and focused the main region without runtime errors.
