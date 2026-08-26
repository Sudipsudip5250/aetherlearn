# Stage 4 Web browser smoke evidence

**Date:** 2026-08-26
**Environment:** Chromium sandbox browser against `http://localhost:4173/`, serving the repository `web/` directory.

## Initial catalog and explicit cache

The updated shell displayed `Private by default · 35 current lessons · MVP baseline 20`, the local privacy note, and the message that Termux integration is available only in the native Android app. The Learn view displayed `35 modules` and 35 lesson controls, including the four Stage 4 cards: `web-01-semantic-html-accessibility`, `web-02-css-layout-responsive-design`, `web-03-javascript-events-and-state`, and `web-04-data-modeling-and-json`.

Selecting **Cache core content** succeeded and displayed `Cached 35 core lessons in this browser.`; the catalog status changed to `35 modules · cached core pack`. This confirms the explicit local IndexedDB cache flow for the Stage 4 payload. Further storage-preserving reset, reader-open, and offline-network checks remain to be recorded below.

## Limitations

This evidence does not yet claim Android-browser runtime behavior, assistive-technology validation, or a network-disabled reload. The local server remains available for the next smoke steps.

## Storage-preserving reset

The browser database `aetherlearn-web` reported separate object stores named `packs` and `state`. Before reset it contained one pack record and no state records in this fresh browser profile. Clearing only `packs` completed successfully, leaving `packs: 0` and `state: 0`; the state store was not deleted or modified by the reset operation.

After reloading following the packs-only reset, the shell still displayed 35 lesson controls and `35 modules · shared Markdown source`, while the cache status returned to the uncached state. Selecting **Cache core content** again succeeded and restored `Cached 35 core lessons in this browser.` with `35 modules · cached core pack`.

## Stage 4 reader smoke

The cached catalog opened `#/lesson/web-01-semantic-html-accessibility`. The reader rendered the Stage 4 lesson metadata (`web-data`, `beginner`, `offline`, 55 minutes, `S0`), the required lesson sections, source links, four knowledge-check prompts with answer inputs and feedback controls, and browser-local bookmark, note, and completion controls. The lesson was marked `in progress` on open, as expected for the local learning-state flow.

## Cached reload with server stopped

The local server was stopped after the core pack had been cached. Reloading `#/lesson/web-01-semantic-html-accessibility` still delivered the shell and opened WEB-01 from the service-worker/IndexedDB cache. The full reader, Stage 4 metadata, lesson sections, knowledge-check inputs, and local study controls remained visible. A subsequent console inspection reported no console output. This is evidence of cached use with the local server unavailable; it is not Android-browser or assistive-technology evidence.

The cached `#/lesson/web-04-data-modeling-and-json` route also opened with the server stopped. Its S1 offline metadata, JSON/data-model explanation, privacy cautions, four knowledge checks, and local study controls rendered. Console inspection again reported no console output.
