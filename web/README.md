# AetherLearn web fallback

This directory contains the secondary, client-only M6 web reader. It reuses the five validated Markdown lesson files from `content/core/` and does not introduce a second lesson schema. The copied files under `web/content/core/` are the static web payload; `scripts/check_web_content.py` verifies that each copy remains byte-identical to its canonical source.

## Serve locally

A browser should load the client through a local HTTP server rather than directly from `file://`, because the client uses `fetch()` and a service worker.

From the repository root:

```bash
python3 -m http.server 4173 --directory web
```

Then open <http://localhost:4173/>. No build step, package installation, backend, or account is required.

## Offline use

Open the app while the five core lessons are available, then select **Cache core content**. The client downloads the manifest and all five Markdown files, validates the manifest-to-lesson IDs and titles, and writes the complete pack to IndexedDB. Pack replacement uses a staging record and an active record in one read/write transaction, so a failed update does not intentionally replace the previous active pack. The hero status reports whether the pack is loaded, updating, cached, or unavailable.

The service worker caches the static app shell and uses cache-first responses for same-origin resources. IndexedDB remains the authoritative browser-local store for the active lesson pack and learning state. After the first successful cache, the shell, catalog, reader, practice list, and search index can be reopened with the network disabled. The browser must support service workers, IndexedDB, and an origin served over HTTP(S); private browsing modes and storage eviction can limit persistence.

## Local learning features

The fallback stores a versioned learning-state record in this browser only. It includes per-lesson progress (`not started`, `in progress`, or `completed`), private notes, bookmarks, and knowledge-check attempt/best-score data. Opening a lesson marks it in progress; completion, note saving, bookmark changes, and quiz attempts are explicit local actions. Search scans the cached five lesson titles and bodies in memory, and Practice exposes the existing `Offline practice` section from every lesson. No learning state is synchronized, uploaded, or shared automatically.

## Privacy and boundaries

The web client is fully client-side. It has no backend, accounts, analytics SDK, tracking pixels, remote content marketplace, Termux bridge, or automatic network reporting. The five core lessons are the only content in this slice. **Termux is Android-only**: the browser fallback shows the lesson and offline practice for termux-optional modules but does not provide the native terminal handoff.

This fallback does not claim feature parity with the Android client. Native export flows, optional-pack management, device storage controls, Termux execution, and Android lifecycle behavior remain Android-specific. Browser storage is device- and origin-local; clearing site data or browser eviction removes the cached pack and learning state. The web client also does not provide encrypted backup, sync, semantic search, or a general-purpose Python runtime.

## Verification

The repository check `python3 scripts/check_web_content.py` enforces exact five-module coverage and byte-level parity with `content/core/`. JavaScript syntax can be checked with `node --check web/app.js`, `node --check web/idb.js`, and `node --check web/sw.js`. The M6 browser evidence is recorded in [`../docs/references/m6_browser_notes.md`](../docs/references/m6_browser_notes.md), including cache, network-disabled reload, reader, progress, note, bookmark, quiz, practice, search, Termux-message, persistence, and storage checks.

The core content source remains the repository’s validated Markdown contract in [`../content/README.md`](../content/README.md).
