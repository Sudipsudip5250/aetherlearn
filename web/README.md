# AetherLearn web fallback

This directory contains the secondary, client-only M6 web reader. It reuses the eleven validated Markdown lesson files from `content/core/` and does not introduce a second lesson schema. The copied files under `web/content/core/` are the static web payload; `scripts/check_web_content.py` verifies that every copy remains byte-identical to its canonical source.

## Serve locally

A browser should load the client through a local HTTP server rather than directly from `file://`, because the client uses `fetch()` and a service worker.

From the repository root:

```bash
python3 -m http.server 4173 --directory web
```

Then open <http://localhost:4173/>. No build step, package installation, backend, or account is required.

## Offline use

Open the app while the eleven core lessons are available, then select **Cache core content**. The client downloads the manifest and all eleven Markdown files, validates the manifest-to-lesson IDs and titles, and writes the complete `core` pack to IndexedDB. The web manifest is versioned as `1.1.0` for this expanded content batch.

Pack replacement uses a staging record and an active record in one read/write transaction. A failed update therefore reports an error while retaining the previous active pack. The visible status reports whether the pack is loaded, updating, cached, or unavailable. The user must explicitly start caching; the client does not silently create a network content subscription.

The service worker caches the static app shell and uses cache-first responses for same-origin resources. IndexedDB remains the authoritative browser-local store for the active lesson pack and learning state. After the first successful cache, the shell, catalog, reader, practice list, and search index can be reopened with the network disabled. The browser must support service workers, IndexedDB, and an origin served over HTTP(S); private browsing modes, storage eviction, or clearing site data can limit persistence. The web fallback does not expose the Android network-pack URL field or a remote marketplace; its network boundary remains the explicit core-cache action.

## Local learning features

The fallback stores a versioned learning-state record in this browser only. It includes per-lesson progress (`not started`, `in progress`, or `completed`), private notes, bookmarks, and knowledge-check attempt/best-score data. Opening a lesson marks it in progress; completion, note saving, bookmark changes, and quiz attempts are explicit local actions. Search scans the cached lesson titles and bodies in memory, and Practice exposes the `Offline practice` section from every lesson. No learning state is synchronized, uploaded, or shared automatically.

The new batch is immediately usable by both clients because it follows the existing frontmatter and section contract. The native Android catalog now discovers eleven bundled assets. The browser manifest and copied payload cover the same eleven canonical files. Existing progress, notes, bookmarks, and quiz state are keyed by stable lesson ID, so adding modules does not rewrite state for the original five lessons.

## Privacy and boundaries

The web client is fully client-side. It has no backend, accounts, analytics SDK, tracking pixels, remote content marketplace, or automatic network reporting. **Termux is Android-only:** the browser fallback shows the lesson and offline practice for `termux-optional` modules but does not provide the native terminal handoff.

This fallback does not claim feature parity with the Android client. Native export flows, optional-pack management, device storage controls, Termux execution, and Android lifecycle behavior remain Android-specific. Browser storage is device- and origin-local; clearing site data or browser eviction removes the cached pack and learning state. The web client also does not provide encrypted backup, sync, semantic search, or a general-purpose Python runtime. M7 adds a same-origin restrictive CSP, `no-referrer`, HTTPS-only external Markdown links with `noopener noreferrer`, route-heading focus restoration, visible focus outlines, 44-pixel minimum controls, forced-color safeguards, and reduced-motion support. These source-level safeguards do not replace screen-reader, Android-browser, or assistive-technology testing.

## Verification

Run the content and payload checks from the repository root:

```bash
python3 scripts/validate_content.py
python3 scripts/check_web_content.py
python3 scripts/build_pack.py
python3 scripts/check_android_manifest.py
node --check web/app.js
node --check web/idb.js
node --check web/sw.js
```

The browser evidence log is recorded in [`../docs/references/m6_browser_notes.md`](../docs/references/m6_browser_notes.md). The content-batch source note is [`../docs/references/content_batch_2026-08-25.md`](../docs/references/content_batch_2026-08-25.md). M4 network-pack setup and recovery are documented in [`../docs/NETWORK_PACKS.md`](../docs/NETWORK_PACKS.md), and M7 gates and limitations are in [`../docs/M7_RELEASE_NOTES.md`](../docs/M7_RELEASE_NOTES.md).

The core content source remains the repository’s validated Markdown contract in [`../content/README.md`](../content/README.md).
