# Stage 5 browser smoke evidence

**Date:** 2026-08-26
**Environment:** Chromium sandbox browser, local static server at `http://localhost:4173/`, desktop viewport.

## Initial v8 shell and stale-pack observation

The refreshed shell loaded from the local server and visibly reported `Private by default · 37 current lessons · MVP baseline 20`. The page showed the privacy note and the message that Termux integration is Android-only. The existing browser-local IndexedDB cache still reported `Cached 35 core lessons available offline` and `35 modules · cached core pack`; this is expected because the prior Stage 4 pack remained active in browser storage. The next step is to clear only the `packs` object store, preserve the `state` store, reload, and explicitly cache the current Stage 5 pack.

## Packs-only reset

The browser database `aetherlearn-web` exposed separate `packs` and `state` object stores. A controlled reset targeted only `packs`; no database deletion or `state` mutation was requested. The first diagnostic serialized IndexedDB count requests as empty objects, so exact before/after record counts are intentionally not claimed here; the reset will be verified through the application’s uncached/recached lesson counts.

## Updated cache and catalog

After the packs-only reset, the application showed `Core lesson pack not loaded. Cache it for offline use.` while still rendering the updated shell with `37 current lessons` and `37 modules · shared Markdown source`. Selecting **Cache core content** completed successfully and changed the status to `Cached 37 core lessons in this browser.` The Learn view then showed `37 modules · cached core pack`; the visible catalog included module 36 `Morris worm: history, impact, and response` and module 37 `Cybersecurity role families and learning paths`. The stale 35-lesson pack was therefore replaced without deleting the browser-local state store.

The cached catalog DOM contained a lesson card numbered 36 with `data-open-lesson="sec-05-morris-worm-history-and-response"` and a lesson card numbered 37 with `data-open-lesson="sec-06-cybersecurity-career-role-families"`. Both Stage 5 lessons also appeared in the offline practice-card list. This confirms discovery and stable route wiring for both new IDs in the refreshed browser cache.

## SEC-05 reader smoke

The cached route `#/lesson/sec-05-morris-worm-history-and-response` opened successfully. The reader displayed the S0/offline metadata, objectives, prerequisites, explanation, stakeholder/timeline tables, fictional offline practice, four knowledge-check inputs, accessibility notes, safety boundary, and four source links. The page explicitly stated that it does not teach malware creation, spreading, hiding, or investigation and did not expose operational instructions. Console inspection after the route load reported no console output.

## SEC-06 reader smoke

The cached route `#/lesson/sec-06-cybersecurity-career-role-families` opened successfully. The reader displayed the S0/offline metadata, objectives, prerequisites, NIST NICE explanation, broad role-lens tables, fictional task-card practice, four knowledge-check inputs, accessibility notes, privacy safeguards, and the explicit statement that the lesson does not promise employment, compensation, legal outcomes, or regional portability. Console inspection after the route load reported no console output.
