# Stage 4 source and claims matrix

**Research date:** 2026-08-26
**Scope:** a small offline Web/data foundation slice covering semantic HTML and accessibility, CSS responsive presentation, JavaScript events/state, and JSON/data modeling. External pages were treated as untrusted reference data; no code was copied into the app, no forms were submitted, and no external target was contacted.

## Sources inspected

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [MDN: HTML: A good basis for accessibility](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Accessibility/HTML) | Semantic HTML uses elements for their intended purpose and gives browsers built-in accessibility hooks; native controls such as buttons provide keyboard behavior. Labels, useful link text, table headings, plain language, and source order support accessibility. | MDN’s learning page states these learning outcomes and explains semantic elements, native controls, labels, links, tables, and keyboard access. | MDN is practical developer guidance, not a conformance certificate. Lessons use static fictional markup and state that an example is not a complete accessibility audit. |
| [W3C Web Content Accessibility Guidelines 2.2](https://www.w3.org/TR/WCAG22/) | WCAG 2.2 is a W3C Recommendation with testable, technology-neutral success criteria covering web content on desktop and mobile devices. | The Recommendation identifies its publication date as 12 December 2024 and describes success criteria, supporting documents, and the relationship to WCAG 2.0/2.1. | WCAG success criteria require context and testing; the lesson must not claim conformance from a paper exercise or a few checks. |

## Planned additional primary sources

The remaining lessons will cite [MDN CSS basics](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Styling_basics), [MDN JavaScript scripting](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting), and [RFC 8259: The JavaScript Object Notation (JSON) Data Interchange Format](https://www.rfc-editor.org/rfc/rfc8259). These sources will support only stable, high-level concepts and fixed offline fixtures.

## Proposed Stage 4 lesson set

| ID | Working title | Risk | Offline activity | Safety/privacy boundary |
|---|---|---:|---|---|
| `web-01-semantic-html-accessibility` | Semantic HTML and accessible structure | S0 | Label a fictional page outline and replace generic containers with appropriate elements. | No live site, user data, or claim of WCAG conformance. |
| `web-02-css-layout-responsive-design` | CSS layout and responsive presentation | S0 | Predict layout changes at two fixed viewport widths using a static style card. | No tracking, remote assets, or browser automation. |
| `web-03-javascript-events-and-state` | JavaScript events and local state | S1 | Trace a fictional button event and state transition on paper; no code execution in the app. | No arbitrary execution, storage of personal data, network calls, or third-party scripts. |
| `web-04-data-modeling-and-json` | Data modeling and JSON fixtures | S1 | Design a bounded local record schema and identify valid/invalid JSON fixture cases. | No real personal data, credentials, imports, or external API calls. |

## Review requirements

All four lessons must remain offline, use synthetic examples, and state that the exercises are educational practice rather than standards conformance or production security review. `web-03` and `web-04` are provisionally S1 because state and data-format misunderstandings can lead to unsafe implementations; they must not become arbitrary code execution, network, credential, or real-data exercises. Human technical, pedagogical, accessibility, and privacy review remains required before release.

## Source freshness

MDN pages are living documentation and may change; lesson claims should remain generic and be rechecked at each content release. WCAG 2.2 is a versioned W3C Recommendation and should be cited by version. RFC 8259 is the normative JSON format reference; examples in lessons must remain small and clearly marked as fictional fixtures.

## Additional inspected source findings

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [MDN: CSS styling basics](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Styling_basics) | CSS styles and lays out web pages; its fundamentals include syntax, selectors, values, sizing, box-model behavior, and layout. | MDN describes CSS as the styling and layout language applied to HTML and organizes learning around selectors, the box model, sizing, overflow, tables, and debugging. | Examples are instructional and version-sensitive. Stage 4 uses fixed style cards and viewport predictions, not a promise of identical rendering across browsers. |
| [MDN: Dynamic scripting with JavaScript](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting) | JavaScript can manipulate the DOM through browser APIs; beginner scripting includes events, DOM scripting, JSON, network requests, and debugging as distinct topics. | MDN lists events and DOM scripting in its core curriculum and explains DOM manipulation as changing document structure through built-in browser APIs. | The page also covers network requests and broader APIs. Stage 4 deliberately excludes network access, arbitrary execution, third-party scripts, and personal data. |

These pages were inspected on 2026-08-26. The planned lesson examples remain high-level and local so they do not inherit the operational breadth of the full MDN curriculum.

## JSON standard finding

| Source | Claim supported | Evidence inspected | Limitation and authoring rule |
|---|---|---|---|
| [RFC 8259: The JavaScript Object Notation (JSON) Data Interchange Format](https://www.rfc-editor.org/rfc/rfc8259) | JSON is a lightweight, text-based, language-independent format for exchanging structured data. RFC 8259 describes strings, numbers, booleans, null, objects, and arrays, and defines JSON text as a serialized value. | The RFC Editor page identifies RFC 8259 as Internet Standard STD 90, published December 2017, obsoleting RFC 7159. Its grammar and values sections define the core syntax and data types. | JSON syntax does not define an application’s meaning, validation policy, privacy policy, or authorization. Stage 4 uses tiny fictional fixtures and teaches schema decisions separately from syntax. |

The RFC page was inspected on 2026-08-26. No external data was submitted and no code or network operation was performed from the source.
