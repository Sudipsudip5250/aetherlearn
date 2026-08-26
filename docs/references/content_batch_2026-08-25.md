# Next curriculum batch research note

The selected batch is exactly six modules from the validator-backed `mvp-20` registry: DL-02, DL-03, DL-04, PY-03, PY-04, and PY-05. DL-02, DL-03, and DL-04 complete Digital Literacy after DL-01 and DL-05. PY-03, PY-04, and PY-05 continue the approved prerequisite chain from PY-02 through loops. DEV-04 and DEV-05 are not in `content/curriculum.yml` and are deferred.

| Module | Prerequisite | Availability | Risk | Primary sources |
|---|---|---|---|---|
| DL-02 | DL-01 | offline | S0 | https://developer.android.com/training/data-storage; https://support.google.com/android/answer/2819582?hl=en |
| DL-03 | none | offline | S0 | https://developer.android.com/guide/topics/permissions/overview; https://support.google.com/android/answer/9431959?hl=en |
| DL-04 | DL-01 | offline | S0 | https://developer.mozilla.org/en-US/docs/Learn_web_development/Howto/Web_mechanics/What_is_a_URL; https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview |
| PY-03 | PY-02 | termux-optional | S1 | https://docs.python.org/3/tutorial/introduction.html; https://docs.python.org/3/tutorial/inputoutput.html |
| PY-04 | PY-03 | offline | S1 | https://docs.python.org/3/tutorial/controlflow.html |
| PY-05 | PY-04 | termux-optional | S1 | https://docs.python.org/3/tutorial/controlflow.html |

The primary pages were retrieved on 2026-08-25. Android’s storage documentation distinguishes app-specific, shared, preference, and database storage; Android’s permission overview covers restricted data/actions and least-privilege practices; MDN documents URL parts and HTTP client/server exchange; Python’s official tutorial documents assignment, types, input/output, comparisons, Boolean logic, `for`, `while`, and `range`.

## Browser checkpoint

Chromium loaded the current web client after the prior M6 IndexedDB database and caches were cleared. The catalog showed exactly eleven lessons in stable order, including DL-02, DL-03, DL-04, PY-03, PY-04, and PY-05. Status reported that core lessons were loaded and available to cache; no stale five-lesson catalog remained. The temporary local server was `http://127.0.0.1:4173/`.

DL-02 browser smoke checkpoint: direct navigation to `#/lesson/dl-02-files-folders-storage-backups` rendered the title, objectives, prerequisites, availability, explanation, worked example, common mistakes, offline practice, project, accessibility, safety, further reading, knowledge check with three inputs, and browser-local study tools. The route opened with `in progress`, confirming the existing reader state behavior works for a new stable ID.

PY-05 browser smoke checkpoint: direct navigation to `#/lesson/py-05-loops-repetition-tracing` rendered the termux-optional metadata, explicit `Termux is Android-only` notice, prerequisites, loop explanation, trace-table example, offline practice, three quiz inputs, and local study tools. The browser did not expose a native terminal handoff.

Expanded web cache checkpoint: after selecting **Cache core content**, Chromium reported `Cached 11 core lessons in this browser.` and the catalog status changed to `11 modules · cached core pack`. This verifies the new manifest, all eleven Markdown payloads, and the existing IndexedDB cache flow work together.

Network-disabled browser checkpoint: the temporary HTTP server was stopped and `curl` could not connect. Reloading `#/learn` still rendered `11 modules · cached core pack`, all eleven lesson cards, and `Cached 11 core lessons in this browser.` This confirms the expanded web pack is recoverable from the service-worker shell and IndexedDB while offline.

Offline PY-05 checkpoint: with the local HTTP server still stopped, direct navigation to `#/lesson/py-05-loops-repetition-tracing` rendered the full new lesson from the cached pack, including `Termux is Android-only`, loop practice, quiz controls, and local study tools.

## Final verification

The eleven lessons passed `scripts/validate_content.py`; the deterministic core pack built and its generated manifest verified. `scripts/check_web_content.py` passed, and `cmp` confirmed byte identity for every canonical lesson against both Android assets and web payload copies. Markdown links, secret-pattern checks, five Python unit tests, JavaScript syntax checks, JSON validation, and `git diff --check` passed. Android `assembleDebug`, `testDebugUnitTest`, and `lintDebug` passed with the project’s Java 17 toolchain. No Android device/emulator runtime evidence was claimed.


## Content expansion batch 2 browser checkpoint

The first browser reload after copying the twenty-module payload still displayed the prior eleven-module shell because the persistent test browser retained the earlier M6 service-worker/page state. A direct repository check confirmed `web/content/manifest.json` contains twenty IDs and no hard-coded eleven count remains in `web/app.js`; stale present-state text was then corrected in `web/index.html` and `web/README.md`. A fresh cache-busting reload will be used for the final Web smoke check.


After clearing the stale browser state and selecting **Cache core content**, the current Web client reported `Cached 20 core lessons in this browser.` and `20 modules · cached core pack`. The Learn view rendered PY-06, PY-07, AL-01 through AL-05, DEV-02, and DEV-03 in canonical order. The browser still contained prior batch-1 learning-state examples, which remained keyed by stable ID and did not block the new content cache.


The twenty-module Learn view also rendered the newly authored lesson cards in the browser, including PY-06, PY-07, all five algorithm modules, DEV-02, and DEV-03. Existing batch-1 state remained visible only for the earlier stable IDs; no schema or state migration was required.


Direct route smoke test for `py-06-functions-scope-reusable-code` rendered the title, objectives, Termux-is-Android-only notice, prerequisites, availability, explanation, worked example table, offline practice, four quiz inputs, local study tools, accessibility, safety, and source links. The browser console had no output/errors after the new reader loaded.


## Batch-2 final verification

The completed batch contains twenty canonical lesson files. `scripts/validate_content.py` passed for all twenty lessons and the generated deterministic core pack; `scripts/check_web_content.py` confirmed twenty Web payload mirrors; and byte comparison confirmed every canonical lesson matches both Android assets and Web payload copies. The full Python test suite passed all six tests, including the twenty-lesson repository assertion, along with Markdown links, secret-pattern, dependency-pin, Android-manifest, Node syntax, JSON, and whitespace checks.

The Android regression matrix passed after synchronization: `assembleDebug`, `assembleRelease`, `testDebugUnitTest`, and `lintDebug` completed successfully with the Java 17 toolchain. A cache-busted Chromium smoke test refreshed the browser pack to twenty lessons, rendered the new lesson cards, opened PY-06 through the shared reader, showed the Android-only Termux boundary, and produced no browser-console errors. No physical-device, emulator, Android-browser, assistive-technology, or final human pedagogical/safety review evidence is claimed.
