# AetherLearn device and browser test checklist

**Purpose:** verify the first usable Android and Web/PWA release on real environments without weakening the frozen 20-module MVP baseline or the local-first privacy boundary.

**Audience:** a non-expert tester working with a release owner. Follow the steps in order, record the result of each test, and stop when a step is unsafe or differs materially from the expected result. A test marked **Not tested** is an open release gate, not a pass.

**Scope:** Android app, optional Termux handoff, Android browser/PWA, desktop browser/PWA, offline learning, local persistence, exports, optional packs, and accessibility basics. This checklist does not authorize public distribution, key handling, account creation, analytics, or testing against systems that you do not own or have permission to use.

## 1. Test record and prerequisites

Create one test record before starting. Do not include real passwords, personal notes, private URLs, or learning data in screenshots or bug reports.

| Field | Record |
|---|---|
| Tester and date |  |
| Android device/model or emulator |  |
| Android version and API level |  |
| App APK filename and SHA-256 |  |
| Web browser and version |  |
| Screen reader / text scale used |  |
| Network conditions | Wi-Fi, mobile data, airplane mode, or local test server |
| Termux installed | Yes / No / Not applicable |
| Result owner |  |

Before testing, obtain the unsigned debug APK or an authorized signed build, its checksum sidecar, and the test pack materials from the release owner. Verify the checksum before installation. Do not install an artifact whose checksum does not match. The repository’s release build is currently unsigned; it must not be presented as a production-signed release until an authorized release operator signs it.

Prepare two fictional lesson values for testing: `Offline functions` and `Review algorithms`. Use the fictional note `Practice return values tomorrow.` Do not use a real person’s name, credential, account number, contact, or private location.

## 2. Fresh install and privacy screen

1. On a test device or clean emulator, uninstall any prior AetherLearn build. If the device contains learning data that must be preserved, do not uninstall it; use a separate test profile or record the test as **Not tested**.
2. Install the verified APK through the normal Android installer or the release owner’s approved device-install procedure.
3. Launch AetherLearn with the network disabled if possible. The first screen should say **Welcome to AetherLearn** and describe the app as private and offline-first.
4. Confirm that the screen says no account is required and that progress, notes, bookmarks, and scores stay on the device for the core experience.
5. Confirm that the privacy card says the core app does not require a name, email address, location, contacts, advertising identifier, or personal learning data.
6. Confirm that optional downloads and exports are user-controlled and that nothing is uploaded automatically.
7. Tap **Continue**. The offline learning shell should open without requiring a login, network connection, or external app.
8. Close and reopen the app. The privacy welcome screen should not appear again for this test installation.

**Pass evidence:** privacy wording is visible, no account or network is required for the core path, and the shell opens offline. **Fail or stop:** a login, unexplained permission, automatic upload, or required network appears.

## 3. Android navigation and 37-lesson discovery

1. On the main shell, verify the four bottom destinations: **Learn**, **Practice**, **Search**, and **Progress**. Verify **Settings** is available from the top bar.
2. Open **Learn**. Confirm that the catalog contains 37 lessons: the stable 20-module MVP baseline, three Stage 1 lessons, four Stage 2 lessons, four Stage 3 security-ethics lessons, four Stage 4 Web/data foundations lessons, and two Stage 5 historical-security/career lessons.
3. Confirm that the previously completed content is visible: PY-06, PY-07, AL-01, AL-02, AL-03, AL-04, AL-05, DEV-02, and DEV-03. Also open DL-06, DL-07, DL-08, DEV-04, DEV-05, DEV-06, DEV-07, SEC-01, SEC-02, SEC-03, SEC-04, WEB-01, WEB-02, WEB-03, WEB-04, SEC-05, and SEC-06. For SEC-01 through SEC-06, confirm the safety and privacy text is visible and no lesson asks for a live target, external contact, credential, unsafe command, real incident data, or personal career data.
4. Confirm that the first five existing modules remain visible and that no duplicate stable IDs appear.
5. Open **Practice**, **Search**, and **Progress** in turn. Each destination should show a useful screen rather than a placeholder or crash.
6. Open **Settings** and return with **Back**. Confirm that returning does not reset the selected destination or learning state unexpectedly.
7. Change the theme to **Light**, **Dark**, and **Follow system** if the controls are available. Confirm that text and controls remain readable in each mode. Restore the preferred mode.

**Pass evidence:** all 37 current lessons are discoverable, the original 20-module baseline remains present, all Stage 1 through Stage 5 lessons open, the Stage 3 safety boundary, Stage 4 offline/privacy boundary, and Stage 5 historical/cultural/career-framing boundary are visible, all destinations open, and no crash or data reset occurs.

## 4. Complete one lesson fully offline

1. Enable airplane mode and confirm that Wi-Fi and mobile data are disabled. Do not use a network pack in this section.
2. From **Learn**, open `PY-06 — Functions, scope, and reusable code` and, if available, sample `SEC-05 — Morris worm: history, impact, and response` or `SEC-06 — Cybersecurity role families and learning paths`.
3. Confirm that the reader shows the title, strand, level, estimated time, availability, risk tier, objectives, prerequisites, explanation, worked example, common mistakes, offline practice, project/application, accessibility notes, safety guidance, further reading, and change log. For Stage 5, confirm the case-study or career-framing boundary is visible and remains offline/non-operational.
4. Scroll through the entire lesson. Confirm that code blocks, tables, links, and paragraphs remain readable and do not prevent reaching the quiz and local study tools.
5. In **Knowledge check**, answer the four prompts using the fictional lesson material. Tap **Check answers**. Confirm that feedback appears and explains the result in words rather than relying only on color.
6. Tap **Retry**, give at least one different answer, and check again. Confirm that the attempt count changes and the best score is retained locally.
7. Tap **Bookmark**. Confirm that the control changes to indicate the bookmarked state.
8. Enter `Practice return values tomorrow.` in **Private note**, tap **Save note on this device**, and confirm that a saved-state message appears if provided.
9. Tap **Mark complete**. Confirm that the lesson shows completed status.
10. Return to **Learn** and verify that the lesson’s status is completed or otherwise reflects the recorded state.

**Pass evidence:** a new lesson can be read, practiced, checked, bookmarked, noted, and completed with airplane mode enabled. **Fail or stop:** the app requests network access for core reading or loses the reader state.

## 5. Persistence after force-stop and restart

1. While still offline, note the completed lesson, quiz score, bookmark, and note from Section 4.
2. Open Android system App Info for AetherLearn and choose **Force stop**. Do not choose **Clear storage** or **Clear data**.
3. Launch AetherLearn again.
4. Open **Learn**, the same lesson, and **Progress**. Confirm that completion, quiz attempt/best score, bookmark, and note remain.
5. Open **Search** and search for `functions` and `algorithms`. Confirm that offline results are returned from lesson titles or bodies.
6. Restart the device or emulator if the test owner wants a stronger persistence check. Repeat the verification after boot.

**Pass evidence:** local learning state survives force-stop and restart. **Fail or stop:** state is uploaded, requires login, or disappears without an intentional data-clear action.

## 6. Practice and search coverage

1. Open **Practice** while offline. Confirm that exercises from the current catalog are listed or that the screen clearly explains any filtering.
2. Open an exercise from PY-06, AL-03, or DEV-03 and confirm that it is answerable using the lesson text and does not require a network or unsafe command.
3. Open **Search** and search for `scope`, `stack`, `binary`, `Git`, and `traceback`, one at a time.
4. Confirm that results identify the matching lesson and can open the reader.
5. Search for a nonsense term such as `no-such-fictional-term`. Confirm that the empty state is clear and does not crash.
6. Confirm that search input and results remain usable with the keyboard or screen reader if available.

## 7. Notes, bookmarks, and progress review

1. Bookmark a second fictional lesson, such as `AL-03 — Searching and sorting by example`.
2. Add a different short fictional note to that lesson and save it.
3. Open **Progress**. Confirm that completed, in-progress, bookmarked, and noted lessons are distinguishable using text, labels, or controls rather than color alone.
4. Open each bookmarked lesson from Progress and confirm that the reader opens the correct stable ID.
5. Remove one bookmark and verify that only that bookmark changes.
6. Edit or clear one note, save it, and verify that the updated local value is shown after leaving and reopening the lesson.

## 8. Export Markdown and JSON

Use only the fictional learning data created in this checklist.

1. Open **Settings** and locate **Export**.
2. Choose **Markdown**. Read the warning. It should explain that the export may contain personal learning notes, that the user chooses the destination, and that AetherLearn will not upload it automatically.
3. Choose a test destination in a location controlled by the tester. Do not export to a shared public folder.
4. Confirm that the export completes and contains only the intended learning data: progress, quiz attempts, notes, and bookmarks. It should not contain network credentials, unrelated device data, or hidden analytics fields.
5. Repeat with **JSON**.
6. Delete the test exports after inspection unless the release owner needs them as sanitized evidence.
7. Cancel one export at the warning or file-picker stage. Confirm that cancellation does not create an unexpected file or change learning state.

**Pass evidence:** both formats can be created through the user-controlled file picker, the warning is visible, and no automatic upload occurs.

## 9. Optional local content-pack install and delete

Run this section only if the build includes an available local optional pack. This is distinct from the network-pack flow.

1. In **Settings**, open **Storage & content packs**.
2. Record the core module count and confirm that the core pack is marked always available offline and protected from deletion.
3. For a listed optional pack, tap **Install local pack**. Confirm that the UI reports installation and that the pack’s lessons become discoverable.
4. Open one optional-pack lesson and confirm that it parses using the shared reader without changing the original 20 bundled MVP lessons or the approved Stage 1, Stage 2, Stage 3, and Stage 4 bundled lessons.
5. Verify that existing progress, notes, bookmarks, and quiz attempts for core lessons remain.
6. Tap **Delete optional pack**. Confirm that only the optional content is removed, while the core pack and all learning data remain.
7. If installation fails, confirm that the prior valid content remains available and that the error is understandable.

Do not treat the absence of a local optional pack as a failure; record **Not applicable** if the build intentionally contains none.

### 9.1 Optional visual foundations pack

This is a separate proof-of-concept check. It must not add lessons or alter learner records.

1. In **Settings**, install **Visual Foundations** if the build lists it. Confirm the status says it is installed and verified locally, and record the displayed size.
2. Open DL-01, DL-02, and WEB-01. Confirm each associated reader shows an **Optional visual aid**, the static diagram, a meaningful caption, a text equivalent, and license/attribution text. Confirm a non-associated lesson such as DL-03 has no visual-aid section.
3. Confirm the visual pack is supplementary: the lesson body, practice, knowledge check, note, bookmark, and completion controls remain usable if the visual is ignored or unavailable.
4. Delete **Visual Foundations**. Confirm only the optional visual aid disappears; the 37 core lessons, progress, notes, bookmarks, quiz attempts, and core pack remain. If deletion or rendering fails, record the exact message and do not treat source-level checks as device evidence.
5. Record the Android version, device, font-size setting, and whether the SVG appeared in the restricted WebView. Check that JavaScript, external navigation, and file access are not exposed by the visual surface.

The current visual fixture is unsigned development metadata and is bundled locally. Do not test it through the network-pack URL field; signing, public hosting, audio/video, and large-media behavior are separate future decisions.

## 10. Explicit network-pack download controls

This section requires an authorized HTTPS test host containing a pack generated by the repository’s `scripts/build_pack.py`. Do not use a random public ZIP, a URL containing credentials, or a server you do not control. The core path must remain usable if this section is skipped.

### 10.1 URL and preflight behavior

1. Ensure the device has a controlled network connection and leave airplane mode.
2. Open **Settings → Network content pack**. Confirm the text says the action is user initiated and that the core path stays available offline.
3. Enter a valid authorized HTTPS test URL. Confirm the field label is **HTTPS pack URL**.
4. Try a clearly invalid URL such as `http://example.invalid/pack.zip`. Confirm it is rejected before a download starts.
5. Try a URL with a fragment or embedded user information only if the release owner has prepared the exact test strings. Confirm rejection and do not submit real credentials.

### 10.2 Download, pause, resume, and cancel

1. Tap **Download pack** once. Confirm that a foreground status and progress indicator appear.
2. Tap **Pause**. Confirm that the status changes to paused and that the URL field cannot be edited during the active transfer.
3. Tap **Resume**. Confirm that progress continues. If the server does not support HTTP Range, record that the transfer restarted rather than failing; this is an expected limitation.
4. Start another controlled download only if the first one completed or was cancelled. Tap **Cancel** during transfer. Confirm that the status reports cancellation and that the next attempt can start cleanly.
5. Complete a valid download. Confirm that the app reports validation/activation success and that the optional lesson becomes available. Stage 5 lessons are bundled-only and must not be treated as optional network-pack content.
6. Reopen the same URL and download again. Confirm that re-download does not duplicate stable IDs or destroy existing core learning state.
7. Submit an authorized malformed, oversized, tampered, or incompatible test pack if the release owner provides one. Confirm that it is rejected and the previously active valid pack remains available.

### 10.3 Interrupted-transfer recovery

Only the release owner should coordinate this test because force-stopping an app can lose unsaved foreground state.

1. Start a valid controlled download.
2. Force-stop the app or disable the test network during transfer.
3. Relaunch the app and retry the same URL.
4. If the server supports ranges, inspect whether the transfer resumes from the retained partial file. If it restarts, record the server behavior.
5. Confirm that no partial or invalid pack becomes active and that the core pack remains available.

Record the URL host, server Range support, interruption method, status messages, and result. Do not claim process-death recovery as verified until this device test passes.

## 11. Termux present, absent, denied, and fallback paths

Termux is optional. The core lesson, in-app practice, and written fallback must work without it. The app’s Termux bridge uses fixed allowlisted wrappers and learner-confirmed completion; it does not accept arbitrary commands or use terminal output as proof.

### 11.1 Termux absent

1. On a test device without Termux, open a termux-optional lesson such as PY-06 or DEV-02.
2. Confirm the card says **Optional Termux exercise**, identifies the exercise as local-only and no network, shows a fallback, and reports **Termux status: not detected**.
3. Tap **Setup guidance**. Confirm that guidance is shown without automatically installing an app or changing settings.
4. Complete the in-app or written fallback and mark it complete. Confirm that completion is learner-confirmed.

### 11.2 Termux present and permitted

1. Use a Termux installation from a source approved by the release owner.
2. Open the termux-optional lesson and tap **Review and open**.
3. Read the confirmation dialog. Confirm it shows the wrapper ID, contract version, executable, fixed arguments, dedicated working directory, prerequisites, expected effects, and the statement that no network, shared storage, credentials, package installation, or remote host is used.
4. Confirm the action. The fixed local wrapper should open or be handed off according to the configured Termux service.
5. Return to AetherLearn and tap **I finished it in Termux** only after inspecting the expected local result. Confirm that completion is recorded by the learner, not inferred from terminal output.

### 11.3 Permission denied or unsupported service

1. Deny the AetherLearn-to-Termux integration permission, leave external-app execution disabled, or use an unsupported service configuration in a controlled test profile.
2. Try **Review and open**.
3. Confirm that the app reports a permission/setup/service problem, shows guidance, and keeps the fallback available.
4. Confirm that no arbitrary command field, editable executable, user argument, network tool, package installer, or shared-storage path is presented.

The actual Termux package, permission, service, and Android-version matrix remains an open human runtime gate until tested on representative devices.

## 12. Desktop Web/PWA offline path

Use a desktop browser that supports service workers and IndexedDB over an HTTP(S) origin. A local development server is sufficient for a controlled test.

1. Serve the `web/` directory using the repository instructions, or open the authorized deployed test origin.
2. Open the Web client and confirm that it presents **37 current lessons** and the message that Termux is Android-only. The original 20-module MVP baseline remains part of the catalog.
3. Open **Search**, **Practice**, and **Progress**. Confirm that they work without an account.
4. Select **Cache core content**. Confirm a status such as `Cached 37 core lessons in this browser.` and `37 modules · cached core pack`.
5. Open DL-06, DL-07, DL-08, PY-06, AL-03, DEV-03, WEB-01, WEB-02, WEB-03, WEB-04, SEC-05, and SEC-06 from the cached catalog. Confirm the full reader and quiz controls render. Sample at least one original MVP lesson, all three Stage 1 lessons, all four Stage 4 lessons, and both Stage 5 lessons. For SEC-05 and SEC-06, verify the historical/cultural and career/privacy boundaries described in Sections 3 and 4.
6. Add a fictional note, bookmark, quiz attempt, and completion state in the browser.
7. Disable the network using the browser’s offline mode or operating-system network controls. Reload the application and reopen the cached lessons and local state.
8. Confirm that the old state remains local and that the cache failure state does not replace a previous valid cache.
9. Clear site data only after recording the results. Confirm that the UI can explain that clearing browser storage removes the cached pack and browser-local learning state.

### 12.1 Optional visual foundations pack

1. From **About → Optional visual pack**, install the local `visual-foundations` 1.1.0 fixture. Confirm the status reports 37 diagrams available offline and inspect browser Cache Storage if available; no external host should be involved.
2. Open at least one lesson in each approved strand, including DL-01, PY-01, AL-03, DEV-03, SEC-02, WEB-01, and SEC-06. Confirm the associated static SVG, alt text, caption, text equivalent, attribution, and **Try it** observe-and-trace activity render. Confirm an unassociated lesson is not possible after the full-pack expansion, and verify every current lesson has exactly one association through the manifest validator.
3. Stop the local server or use browser offline mode, reload, and reopen DL-01. Confirm the cached SVG and full core lesson still render. This tests desktop Chromium only; it does not prove Android-browser support.
4. Delete the visual pack through the About control and accept the confirmation. Confirm the visual cache and active visual pointer are removed while the core shell/cache and browser-local learning state remain.
5. If installation, reload, or deletion fails, retain the previous valid core state, capture the status text, and record the step as **Failed** rather than inferring success from source inspection.

The Web visual flow accepts only the same-origin unsigned development fixture. It does not provide remote visual downloads or a media marketplace. The 37 optional activities are supplementary; core lesson reading, existing short-answer checks, notes, bookmarks, and completion must remain usable if the visual pack is absent.

The Web fallback does not expose the Android network-pack URL field or a Termux handoff. Do not treat their absence in the Web client as a failure.

## 13. Android-browser PWA path

Run this section on an Android phone browser if available. It is separate from the native app.

1. Open the authorized Web/PWA origin in the Android browser.
2. Confirm the page displays 37 current lessons, readable navigation, the local privacy note, and the Android-only Termux message. The original MVP baseline remains identifiable in project documentation.
3. Cache the core content and confirm the 37-lesson cache status.
4. Open and complete a short lesson, add a fictional note/bookmark, and reload.
5. Enable airplane mode or browser offline mode. Reload and reopen the cached lesson, search, practice, and progress routes.
6. Check for horizontal scrolling, clipped controls, blocked keyboard focus, unreadable code, and inputs hidden below the viewport.
7. If “install to home screen” is offered, test it only as an optional convenience. The browser cache and the native Android app remain separate local stores.
8. From **About → Optional visual pack**, install the local fixture if it is available. Open DL-01, DL-02, and WEB-01 and check the diagram, alt text, caption, text equivalent, and responsive layout. Delete it and confirm the core lesson still works.

If no Android browser/device is available, record **Not tested** for the complete section, including the visual-pack steps. Desktop browser evidence does not prove Android-browser behavior.

## 14. Accessibility basics

Automated checks and one browser do not prove accessibility. Record the platform and assistive technology used. WCAG is a baseline; manual testing is still required.[1]

### Text size, zoom, and reflow

1. On Android, increase system font size to a large setting. In the Web client, test browser zoom or text scaling at 200% where supported.
2. Reopen Learn, a full reader, Practice, Search, Progress, Settings, and the export warning.
3. Confirm that text is not clipped, controls remain reachable, tables and code can be read, and no essential action depends on hover or animation.
4. Rotate the device if orientation testing is in scope. Record any overflow or lost focus.

### Keyboard and focus

1. On the Web client, navigate with `Tab`, `Shift+Tab`, `Enter`, and `Space` where applicable.
2. Confirm that the skip link moves focus to the main content, route changes move focus to the route heading, and the focused control is visibly outlined.
3. On Android, use a hardware keyboard or switch-access method if available. Confirm that Learn cards, bottom destinations, Settings, quiz fields, note field, and buttons are reachable in a logical order.

### TalkBack or another screen reader

1. Enable TalkBack only if the tester knows how to operate it and can safely disable it afterward.
2. Swipe through the first-run privacy screen, navigation destinations, one lesson, quiz fields, note field, bookmark, completion control, and Settings.
3. Confirm that controls have meaningful spoken labels, selected navigation state is announced, the quiz fields identify their questions, progress messages are understandable, and decorative symbols are not read as essential content.
4. Confirm that the screen reader can reach the Termux fallback and that the confirmation dialog exposes the fixed wrapper details.
5. Record any label, focus-order, live-region, or dialog problem with the exact screen and steps.

### Contrast, forced colors, and motion

1. Test light and dark themes and any system high-contrast or forced-color mode available.
2. Confirm that status is not conveyed only by color and that text remains legible.
3. Enable reduced-motion preferences where available. Confirm that essential content and actions remain available.

Do not mark accessibility complete from source inspection alone. Manual screen-reader, Android-browser, and representative device testing remain release gates.[2]

## 15. Privacy and network-boundary checks

1. With the core Android app offline, read lessons, search, practice, complete a quiz, save a note, and bookmark a lesson. Confirm that all work remains available.
2. If a network monitor or router log is available to the authorized tester, inspect only the test device and test origin. Confirm that no learning-data request is made during the core path.
3. In Settings, confirm that the only network workflow is the explicit HTTPS pack URL action. There should be no automatic polling, marketplace, account sync, analytics, or background subscription.
4. Confirm that the Android app requests no broad external-storage permission and rejects cleartext network URLs.
5. Confirm that exports occur only after the user chooses a destination and that an export is not uploaded automatically.

A network inspection result is stronger evidence than UI inspection alone. If no authorized monitor is available, record the boundary as **source/automated checks passed; physical inspection not tested**.

## 16. Defect recording and release decision

For every failure, record:

| Field | What to write |
|---|---|
| Test ID and screen | Exact checklist section and app/browser screen |
| Preconditions | Device, network, account state, Termux state, text scale |
| Steps | Numbered actions that reproduce the issue |
| Expected | The expected result from this checklist |
| Observed | Exact text, status, crash, visual issue, or data loss |
| Severity | Blocker, high, medium, or low |
| Evidence | Sanitized screenshot, log, or screen recording; no secrets or personal data |
| Owner and disposition | Fix, retest, accepted limitation, or defer |

A first public usable release should not be approved if a core offline path loses learning data, requires an account or unexplained permission, executes an arbitrary command, replaces a valid pack with invalid content, exports or uploads private data unexpectedly, crashes in a core route, or has an unresolved accessibility blocker in a required flow.

A **release candidate** may be called repository-ready only when automated checks, content review, and the relevant device/browser checks have passed. It may be called **production-signed** only after an authorized release operator signs the artifact and verifies the release metadata. It may be called **publicly distributed** only after the approved distribution and pack-host process is configured and tested.

## 17. Reset and cleanup

At the end of the test, remove only test artifacts and test accounts created for this checklist. Do not clear app storage until persistence evidence has been recorded. If the release owner requests a clean rerun, use a separate test profile or uninstall/reinstall after exporting no private data. Remove test ZIPs from any server and revoke temporary URLs when the authorized test window ends.

## 18. Stage 5 content and public-distribution review

This section is the specialized human review for the two Stage 5 lessons. It supplements Sections 1–16, [`CONTENT_REVIEW.md`](CONTENT_REVIEW.md), [`SAFETY.md`](SAFETY.md), [`SIGNING.md`](SIGNING.md), and [`RELEASE_HANDOFF.md`](RELEASE_HANDOFF.md). Automated checkpoint D-039 is complete, but every human gate below remains open until a named reviewer records evidence. Record **Pass**, **Fail**, **Not tested**, or **Needs revision**; “Not tested” is not a pass.

### 18.1 Review record and lesson matrix

Record the release candidate commit, content version (expected: 37 lessons), review lead, technical, pedagogical, accessibility, safety/privacy, historical/cultural, career-framing, device/browser, and signing owners, review environments, and final decision. Use fictional learner data only. Never place private notes, passwords, private URLs, identity documents, keystore details, or personal career information in evidence.

Read the canonical files and Android/Web mirrors side by side: [`sec-05-morris-worm-history-and-response.md`](../content/core/sec-05-morris-worm-history-and-response.md) and [`sec-06-cybersecurity-career-role-families.md`](../content/core/sec-06-cybersecurity-career-role-families.md). Render both lessons in each client; Markdown inspection alone is insufficient.

| Gate | SEC-05: Morris worm | SEC-06: career role families |
|---|---|---|
| Frontmatter, title, objectives, prerequisites, body sections, review date, and draft status agree |  |  |
| Objectives are observable, answerable, and beginner-appropriate |  |  |
| Explanation separates documented evidence from interpretation |  |  |
| Examples are fictional and do not imply authorization |  |  |
| Practice is paper/text-based, offline, and uses no personal data |  |  |
| Knowledge checks have defensible answers and accurate explanations |  |  |
| Accessibility alternatives support text, audio, large print, speech-to-text, and non-visual completion |  |  |
| Safety wording appears before sensitive interpretation and no operational capability is added |  |  |
| Further-reading links are current, relevant, reachable, and accurately described |  |  |

For SEC-05, verify that the lesson separates **intent, impact, response, accountability, and learning**; cross-check the November 2, 1988 date, broad disruption, affected institutions, CERT/CC response history, and legal-accountability statements against [`STAGE_EVIDENCE.md`](references/STAGE_EVIDENCE.md). Record disagreements among FBI, CMU SEI, and Computer History Museum accounts rather than silently harmonizing them. Reject any framing that turns affected systems into a scorecard, treats unauthorized experimentation as harmless, glorifies an actor, or exposes service names, commands, source code, concealment methods, replication steps, or real incident-response instructions. The acceptable learner output is a source-labeled timeline and defensive learning brief, not a forensic report, legal conclusion, reproduction guide, or notoriety ranking.

For SEC-06, verify that the NIST NICE Framework is presented as a vocabulary for work and skills—not a universal taxonomy, ranking system, qualification decision, employment prediction, or legal authority. Check for regional, cultural, disability, language, education, device-access, and socioeconomic assumptions, and ensure that no single educational route is presented as the only legitimate path. The exercise must request no résumé, identity document, employer contact, account, assessment answer, personal profile, or sensitive career goal, and must not promise employment, salary, compensation, immigration, legal, certification, or regional-portability outcomes. A learner artifact is evidence of reflection, not proof of competence or production readiness.

### 18.2 Source, historical, cultural, and maintenance review

Use [`STAGE_EVIDENCE.md`](references/STAGE_EVIDENCE.md) as the claims inventory. For every material statement, record the inspected source, publication/update date when available, confidence, uncertainty, and maintenance owner. Check the following before approval:

| Source/review gate | Owner | Result/evidence |
|---|---|---|
| CMU SEI, FBI, and Computer History Museum claims are accurately scoped and their differing perspectives are visible | Historical/source reviewer |  |
| ACM principles are treated as ethical guidance, not a complete legal or cultural framework | Safety/ethics reviewer |  |
| NIST NICE statements use a current version and are limited to framework purposes | Career/source reviewer |  |
| Dates, names, institutions, and legal references are rechecked against the source pages | Technical/source reviewer |  |
| Historical, cultural, disability, language, and access assumptions are reviewed | Historical/cultural reviewer |  |
| Source links resolve from the packaged clients or are clearly labeled as external reading | Release owner |  |
| A maintainer owns source-freshness checks, correction triggers, and future updates | Project maintainer |  |

Rewrite before release if the lesson depends on hero/villain framing, erases affected communities, uses only a law-enforcement or Western perspective, contains ableist/class-based assumptions, or treats unauthorized experimentation as admirable.

### 18.3 Stage 5 safety, privacy, and distribution gates

Both lessons must remain `availability: offline`, `risk_tier: S0`, and `review_status: draft` until human approval. Reject the candidate if either lesson contains malware, exploit construction, payloads, credentials, target lists, scanning steps, persistence, evasion, bypass, concealment, arbitrary execution, live targets, external contact, real incident data, or personal career data. Confirm that fictional fixtures are visibly fictional, Stage 5 IDs are not in the remote-pack allowlist, Android/Web privacy notes remain visible, and notes, quiz attempts, bookmarks, exports, and browser state remain local.

Follow the Android, Web, accessibility, network, export, signing, and distribution procedures in Sections 2–16 and record the exact device/browser, text scale, screen-reader configuration, network state, artifact commit, and checksum. The release owner must additionally confirm that the release APK is signed by the authorized identity, `apksigner verify --verbose --print-certs` passes, the signer fingerprint matches the private inventory, and the public artifact page identifies the commit, content version, checksum, release status, distribution channel, TLS, access controls, and rollback path. Never create or store a real key, password, or private signing material in this repository.

Automated validation proves repository invariants; it does not prove historical completeness, cultural appropriateness, pedagogical effectiveness, device accessibility, safety for every learner, authorized signing, or public-distribution readiness. Keep the last-known-good artifact and record a correction owner if any factual or safety issue is found after review.

## References

[1]: https://www.w3.org/WAI/standards-guidelines/wcag/ "W3C Web Content Accessibility Guidelines overview"
[2]: https://developer.android.com/guide/topics/ui/accessibility/testing "Android Developers: Test your app's accessibility"
[3]: https://developer.android.com/topic/architecture/data-layer/offline-first "Android Developers: Build an offline-first app"
[4]: https://developer.android.com/privacy-and-security/risks/unsafe-download-manager "Android Developers: Unsafe Download Manager"
[5]: https://github.com/termux/termux-app/wiki/RUN_COMMAND-Intent "Termux: RUN_COMMAND Intent"
