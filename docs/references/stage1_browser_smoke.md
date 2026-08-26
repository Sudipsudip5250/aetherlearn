
## Reader smoke

The browser opened `#/lesson/dl-06-computing-language-history` successfully. The shared reader rendered the title, offline/S0 metadata, Objectives, Prerequisites, Availability, Explanation, Worked example, Common mistakes, Offline practice, Project or application, Accessibility notes, Safety and responsible use, Further reading, and Change log. The Knowledge check rendered four local text inputs, a Check answers button, and the local attempt/best-result summary. Bookmark, private note, save-note, and Mark lesson complete controls were present. Opening the lesson changed its local state to `in progress`, as expected. This smoke test did not claim offline reload, Android runtime, accessibility technology, or human content approval.

## Cache flow

From the Web shell, selecting **Cache core content** completed successfully and displayed `Cached 23 core lessons in this browser.` The catalog status changed to `23 modules · cached core pack`. This confirms the explicit local cache flow can stage the current 23-lesson manifest in the sandbox browser; it does not claim Android-browser parity or a network-disabled reload in this run.
