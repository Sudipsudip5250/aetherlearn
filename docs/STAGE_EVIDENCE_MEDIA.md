# Media proof-of-concept evidence

Date: 2026-08-27

## Foundation-prompt reconciliation

The attached artifact-only prompt was checked against the actual checkout. Its statement that the prior visual implementation was absent is not true for this repository: `HEAD` and `origin/main` are at `f748fc8`, with the visual-pack implementation in `aab375f` and the Android import correction in `f748fc8`. The current task therefore does not recreate or broaden the existing client rendering/install behavior. In particular, the Android restricted WebView, Web Cache Storage flow, and pack-install UI are pre-existing published code and are outside the artifact-foundation delta requested here. This prompt explicitly forbids adding WebView, remote delivery, audio/video, streaming, Media3, PAD, OBB, or a new curriculum stage; no new implementation in those areas is being added.

## Browser smoke evidence

Environment: local Chromium browser against `http://127.0.0.1:4173`, served from the repository `web/` directory. No remote host or production browser was used.

- Opened `#/about`; the optional visual-pack controls were present and the initial status read that the pack was not installed.
- Activated **Install visual diagrams**. The UI reported: `Installed locally · 3 diagrams available offline.`
- Returned to the Learn catalog. The UI reported `37 lessons · cached core pack`; the catalog still listed the frozen 37-lesson set.
- Opened `#/lesson/dl-01-digital-information`. The reader rendered the optional visual-aid section, the static SVG, its descriptive alt text, caption, text equivalent, MIT attribution, and the unchanged lesson body, quiz, notes, bookmark, and completion controls.

Remaining browser checks: DL-02 and WEB-01 associations, delete behavior, reload/offline cache persistence, and console/error review.

## Explicit limitations

No Android SDK/device/emulator, Android browser, TalkBack, network interruption, signing, public hosting, or human accessibility review was performed in this sandbox. Android’s current proof-of-concept path is a local WebView-rendered SVG with text alternative and requires device verification.

- Opened `#/lesson/dl-02-files-folders-storage-backups`; the file-lifecycle SVG, caption, text equivalent, license, and full core lesson rendered.
- Opened `#/lesson/web-01-semantic-html-accessibility`; the semantic-structure SVG and optional visual-aid heading rendered, with the reader retaining its lesson content and local quiz/tools.

- Opened unassociated `#/lesson/dl-03-android-settings-permissions-apps`; the full core lesson rendered, and a keyword check confirmed no `Optional visual aid` section was present.

- The deletion test was initiated from About, but the browser automation timed out on the app’s native confirmation dialog and the browser session became unavailable. Post-delete state is therefore **not verified** here; no claim is made that the browser cache was deleted by this test.

- A fresh browser session confirmed the visual pack was still installed. A page-context test overrode only the in-page `confirm` function, clicked Delete, and completed without a thrown browser error; the execution wrapper returned no structured result, so the post-delete cache list is not claimed until independently inspected.

- The controlled post-delete inspection reported `activeKey: null`, no `aetherlearn-visuals-v1-*` caches, and the `aetherlearn-shell-v15` core shell cache still present. Opening DL-01 afterward showed the full core reader, quiz, notes, bookmark, and completion controls with no optional visual section. This verifies the intended deletion invariant in the local browser session.

- With the final Web code loaded at v15, reinstalling from About again reported `Installed locally · 3 diagrams available offline.`

- Before the offline reload, Cache Storage contained the active visual manifest plus all three SVG URLs. After stopping the temporary local HTTP server, reloading DL-01 still served the full reader and rendered the installed SVG, confirming the tested service-worker/Cache Storage offline path in Chromium. This does not establish Android-browser or production-host behavior.

- After the no-store service-worker fix, the final v17 shell loaded from the local server and About initially reported the visual pack as not installed, ready for a clean final install test.

- Final v17 browser pass: About installed the fixture successfully, and DL-01 rendered the optional visual aid with caption, alt text, text equivalent, attribution, and the unchanged knowledge-check/study controls.

- Final v17 offline reload: after stopping the temporary local server, Chromium reloaded DL-01 from the service-worker shell and rendered the cached optional SVG, caption, and text alternative. The browser console view reported no output, so no uncaught console errors were observed in this run.

## Android build limitation

A final `./gradlew :app:compileDebugKotlin --no-daemon` attempt exited with status 1 because this sandbox has no configured Android SDK (`ANDROID_HOME` and `ANDROID_SDK_ROOT` are unset). The failure occurred before compilation; no Android build, emulator, device, WebView, or TalkBack result is claimed.

## Expanded visual-practice pass (2026-08-27)

A local v18 Web shell was served from `127.0.0.1:4173`. After installing the expanded `visual-foundations` fixture, the About route reported `Installed locally · 37 diagrams available offline.` The Practice route displayed `Trace the idea` with `37 local activities` and an `Open visual aid` control for each lesson, alongside the existing core practice cards. The DL-01 reader rendered the optional SVG, caption, alt text, text equivalent, and `Try it: trace the idea` disclosure with three steps and a self-check. The SEC-02 reader likewise rendered its associated diagram and practical disclosure. These checks were performed in sandbox Chromium only; Android device, Android browser, TalkBack, and human visual/accessibility review remain open.
