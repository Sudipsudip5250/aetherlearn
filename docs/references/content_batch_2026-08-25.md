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
