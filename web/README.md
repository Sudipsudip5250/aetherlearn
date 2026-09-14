# AetherLearn web fallback

This directory contains the secondary, client-only M6 web reader. It reuses the 37 validated Markdown lesson files from `content/core/` and does not introduce a second lesson schema. The copied files under `web/content/core/` are the static web payload; `scripts/check_web_content.py` verifies that every copy remains byte-identical to its canonical source.

## Serve locally

A browser should load the client through a local HTTP server rather than directly from `file://`, because the client uses `fetch()` and a service worker.

From the repository root:

```bash
python3 -m http.server 4173 --directory web
```

Then open <http://localhost:4173/>. No build step, package installation, backend, or account is required.

Brand assets live with the client: `favicon.svg`, `icons/icon.svg`, `icons/icon-192.png`, `icons/icon-512.png`, `icons/apple-touch-icon.png`, and `og.jpg`. They are same-origin only and listed in the PWA manifest.

## Offline use

Open the app while the 37 current core lessons are available, then select **Cache core content**. The client downloads the manifest and all 37 Markdown files, validates the manifest-to-lesson IDs and titles, and writes the complete `core` pack to IndexedDB. The web manifest is versioned as `1.7.0` for the approved Stage 5 history/career batch.

Pack replacement uses a staging record and an active record in one read/write transaction. A failed update therefore reports an error while retaining the previous active pack. The visible status reports whether the pack is loaded, updating, cached, or unavailable. The user must explicitly start caching; the client does not silently create a network content subscription.

The service worker caches the static app shell and uses cache-first responses for same-origin resources. IndexedDB remains the authoritative browser-local store for the active lesson pack and learning state. After the first successful cache, the shell, catalog, reader, practice list, and search index can be reopened with the network disabled. The browser must support service workers, IndexedDB, and an origin served over HTTP(S); private browsing modes, storage eviction, or clearing site data can limit persistence. The web fallback does not expose the Android network-pack URL field or a remote marketplace; its network boundary remains the explicit core-cache action.

### Optional visual-pack proof of concept

The About route also provides an explicit local install/delete flow for `visual-foundations`, a version 1.1.0 pack containing one bundled static SVG diagram and trace prompt for each of the 37 current lessons. The client validates the schema, fixed pack identity, known module associations, asset paths, SVG MIME type, accessibility text, license/attribution metadata, declared sizes, SHA-256 digests, and forbidden active-content markers before copying the manifest and SVG bytes into a staging Cache Storage cache. The active cache name includes the manifest digest; a failed install leaves the prior active cache unchanged.

Visual resources are separate from IndexedDB learner state. Deleting the visual pack removes only its Cache Storage entries and local active-cache pointer; it does not clear lessons, progress, notes, bookmarks, quiz attempts, or the core content cache. The reader shows the diagrams only for associated modules and always includes their alt text, caption, text equivalent, and attribution. This proof of concept accepts only the same-origin unsigned development fixture. It has no remote visual catalog, HTTPS visual download, signing, audio/video, streaming, Media3, or Play Asset Delivery implementation. Browser quotas and eviction still apply.

## Local learning features

The fallback stores a versioned learning-state record in this browser only. It includes per-lesson progress (`not started`, `in progress`, or `completed`), private notes, bookmarks, knowledge-check attempt/best-score data, optional local goals, and an optional starting-level preference. Existing state is migrated non-destructively to state version 2; goals are an additive list and do not rewrite earlier keys. Opening a lesson marks it in progress; completion, note saving, bookmark changes, starting-level selection, quiz attempts, and goals are explicit local actions. Light/dark and reading palettes (Default, Soft paper, Cool contrast, High contrast, Soft pattern) persist in `localStorage` only. Learn provides a deterministic recommendation, strand grouping, and filters for strand, level, status, and availability; filters never lock the browse-all catalog. Search scans the cached lesson titles and bodies in memory, and Practice exposes the `Offline practice` section from every lesson with the bounded `short-answer-v1` exercise label. Quiz grading uses explicit normalized accepted-answer variants rather than arbitrary substring matching. Reader fenced code is escaped, horizontally scrollable, and copyable as text only. No learning state is synchronized, uploaded, or shared automatically.

The About route provides user-initiated plain JSON export and validated import for the browser-local learning state. Imports are limited to the versioned AetherLearn format, known lesson IDs, allowed progress values, bounded note sizes, and finite quiz results; replacing current state requires explicit confirmation. The export is not encrypted and should not be treated as a secure backup. Cached content can be cleared separately from learning data.

The new batch is immediately usable by both clients because it follows the existing frontmatter and section contract. The native Android catalog now discovers 37 bundled assets. The browser manifest and copied payload cover the same 37 canonical files. Stages 4 and 5 are bundled-only and are not implicitly optional remote-pack authorizations. Existing progress, notes, bookmarks, and quiz state are keyed by stable lesson ID, so adding modules does not rewrite state for earlier lessons.

## Privacy and boundaries

The web client is fully client-side. It has no backend, accounts, analytics SDK, tracking pixels, remote content marketplace, or automatic network reporting. **Termux is Android-only:** the browser fallback shows the lesson and offline practice for `termux-optional` modules but does not provide the native terminal handoff.

This fallback does not claim feature parity with the Android client. Native export flows, device storage controls, Termux execution, and Android lifecycle behavior remain Android-specific. Browser storage is device- and origin-local; clearing site data or browser eviction removes the cached pack and learning state. The web client also does not provide encrypted backup, sync, semantic search, or a general-purpose Python runtime. M7 adds a same-origin restrictive CSP, `no-referrer`, HTTPS-only external Markdown links with `noopener noreferrer`, route-heading focus restoration, visible focus outlines, 44-pixel minimum controls, forced-color safeguards, and reduced-motion support. These source-level safeguards do not replace screen-reader, Android-browser, or assistive-technology testing.

## Verification

Run the content and payload checks from the repository root:

```bash
python3 scripts/validate_content.py
python3 scripts/check_web_content.py
python3 scripts/validate_visual_pack.py media/visuals/visual-foundations
python3 scripts/validate_visual_pack.py docs/sample-pack/aetherlearn-visual-foundations-1.1.0.zip
python3 scripts/check_visual_pack_mirrors.py
python3 scripts/build_pack.py
python3 scripts/check_android_manifest.py
node --check web/app.js
node --check web/idb.js
node --check web/sw.js
```

Current browser evidence is recorded in [`../docs/STAGE_EVIDENCE_MEDIA.md`](../docs/STAGE_EVIDENCE_MEDIA.md) for this visual-pack slice and in [`../docs/references/`](../docs/references/) for the Stage 1–5 smoke logs and source matrices. Early M6 and content-batch notes are summarized in [`../docs/references/ARCHIVE.md`](../docs/references/ARCHIVE.md) for provenance. M4 network-pack setup and recovery are documented in [`../docs/NETWORK_PACKS.md`](../docs/NETWORK_PACKS.md); release, Stage 5 review, and human limitations are in [`../docs/RELEASE_HANDOFF.md`](../docs/RELEASE_HANDOFF.md) and [`../docs/DEVICE_TEST_CHECKLIST.md`](../docs/DEVICE_TEST_CHECKLIST.md).

The core content source remains the repository’s validated Markdown contract in [`../content/README.md`](../content/README.md).
