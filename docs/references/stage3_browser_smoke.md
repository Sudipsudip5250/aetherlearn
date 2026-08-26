# Stage 3 Web browser smoke evidence

**Date:** 2026-08-26
**Environment:** Chromium sandbox browser, local HTTP server at `http://127.0.0.1:4173/`

The Stage 3 shell loaded with the updated visible copy: `Private by default · 31 current lessons · MVP baseline 20`, the privacy note, and the message that Termux integration is available only in the native Android app. The page rendered 31 lesson controls, including the new Stage 3 cards in the catalog.

The first load still reported `Cached 27 core lessons available offline` and `27 modules · cached core pack`. This was not a repository mismatch: the service-worker shell was v6 and the live manifest was 1.5.0, but the browser’s IndexedDB active pack still contained the prior Stage 2 payload. `indexedDB.databases()` identified `aetherlearn-web` version 1 with stores `packs` and `state`. Only the `packs` store was cleared; the `state` store was preserved. The reset returned `cleared packs; preserved stores: state`.

A fresh reload and explicit cache action are still required to confirm the Stage 3 pack status and reader flow. This evidence does not claim Android-browser behavior, a network-disabled reload, assistive-technology validation, or human content/safety approval.

After the storage-preserving reset, the fresh reload displayed `31 modules · shared Markdown source`, all 31 lesson cards, the four Stage 3 cards in positions 28–31, the privacy copy, and the Android-only Termux message. Clicking the cache control completed successfully and displayed `Cached 31 core lessons in this browser.` with `31 modules · cached core pack`. Existing browser-local learning state remained visible on previously progressed lessons, confirming that only the pack store had been cleared. This confirms the local Web catalog and explicit cache flow in the sandbox browser; it does not claim a network-disabled reload, Android-browser runtime, accessibility-technology validation, or human content/safety approval.

The local reader opened `sec-01-ethics-scope-and-harm`. It rendered the `security-ethics · beginner` metadata, offline/S0 labels, objectives, prerequisites, availability, explanation, worked example, common mistakes, offline practice, project/application, accessibility notes, safety guidance, further reading, and change log. The reader exposed four local quiz inputs with **Check answers**, a bookmark control, private note textarea/save control, and **Mark lesson complete**. The page clearly states that notes and bookmarks remain in the browser and are never synced. This is a browser rendering smoke result only and does not substitute for human pedagogical or safety review.
