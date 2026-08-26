# Network content packs

AetherLearn’s core learning path remains bundled and offline-first. Network packs are optional and are downloaded only after the learner opens **Settings → Storage & content packs**, enters a source URL, and taps **Download pack**. Progress, notes, bookmarks, quiz attempts, search queries, and Termux commands are never sent with a pack request.

## Pack format

The Android client accepts an HTTPS ZIP created by `scripts/build_pack.py`. The archive contains `manifest.json` at its root and Markdown lesson files under `modules/`. The manifest must use schema version `1`, declare `pack_id`, `pack_version`, `module_count`, `total_module_bytes`, and a `modules` array. Each module entry declares an approved stable ID, filename, byte size, and lowercase SHA-256 digest. Lesson frontmatter and required sections are parsed again on the device before activation.

The repository’s deterministic local example is created with:

```text
python scripts/build_pack.py --content-dir content/core --output-dir build/core-pack
```

The resulting ZIP is a test artifact, not an approved public distribution URL. This repository does not currently publish a public pack host, so the URL field is intentionally user-provided rather than silently pointed at an unreviewed server.

## Download behavior

Only HTTPS URLs without embedded credentials or fragments are accepted. Cleartext HTTP, URL credentials, and fragments are rejected before a request starts. Requests use bounded connection and read timeouts, do not follow redirects, and send no cookies or authentication headers. Downloads are streamed into app-private storage and are capped at 25 MiB overall and 512 KiB per lesson.

If the same URL is retried after a connection interruption, the client uses the retained partial file and an HTTP `Range` request when the server supports it. If the server ignores the range request, the client safely restarts instead of appending incompatible bytes. While the foreground Settings screen is open, the learner can pause, resume, or cancel. A failed or interrupted transfer never replaces the active pack.

## Validation and recovery

The client extracts into a temporary app-private staging directory. It rejects unexpected ZIP entries, unsafe paths, duplicate IDs or paths, unsupported schema, invalid IDs, IDs outside the explicit approved remote-pack allowlist, core-pack replacement attempts, missing required sections, size mismatches, SHA-256 mismatches, duplicate modules, malformed Markdown, and module-count or total-size mismatches. The Stage 1, Stage 2, and Stage 3 lessons are bundled content and are intentionally not added to that remote allowlist; adding a lesson to the canonical registry does not authorize network distribution. The active pack is renamed to a backup only after the downloaded pack passes structural validation; activation and metadata update occur inside the existing last-known-good rollback boundary.

A successful installation refreshes the Android lesson catalog after the current Settings operation completes. Deleting an optional pack leaves the core pack and learning data untouched. If activation or metadata persistence fails, the previous active directory is restored and the new staging directory is removed.

## Current limitations

The downloader is foreground and user-controlled; it does not run an automatic background scheduler or silently check for updates. HTTP transport security and per-file SHA-256 validation are implemented, but release signing and public-key rotation are not yet implemented because no project release key or approved distribution host has been established. Android device/emulator tests for interrupted radio, low storage, process death during transfer, and actual remote servers remain open evidence gates.

## Primary references

[1]: https://developer.android.com/develop/connectivity/network-ops/connecting "Android Developers: Connect to the network"
[2]: https://developer.android.com/privacy-and-security/risks/unsafe-download-manager "Android Developers: Unsafe Download Manager"
[3]: https://developer.android.com/topic/architecture/data-layer/offline-first "Android Developers: Build an offline-first app"
