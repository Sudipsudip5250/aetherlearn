---
id: web-01-semantic-html-accessibility
title: Semantic HTML and accessible structure
strand: web-data
level: beginner
version: 1.0.0
prerequisites:
  - dl-04-internet-browsers-urls-search
estimated_minutes: 55
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Identify semantic HTML elements that match the purpose of page content.
  - Explain why native controls, labels, headings, and source order help people use a page.
  - Review a fictional page outline for a small set of accessibility risks without claiming full conformance.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can choose meaningful HTML elements for a small document, connect a form label to its field in a written example, and explain why a paper review is useful evidence but not a complete accessibility audit.

## Prerequisites

You should understand that a browser reads a URL and presents a document, and that different people may use different input methods. No HTML editor, browser extension, account, or network connection is required.

## Availability

This lesson is fully offline. It uses fictional page outlines and short markup fragments as reading material. The app does not load, publish, or test a live webpage.

## Explanation

HTML, or HyperText Markup Language, describes the structure and meaning of content. CSS can change presentation, and JavaScript can add behavior, but those layers do not remove the importance of a meaningful document structure.

MDN’s accessibility guidance recommends using semantic HTML: the element should match the job it performs. A native `<button>` has built-in keyboard behavior, while a generic container with a click handler needs extra work to behave like a control. MDN also highlights useful link text, form labels, table headings, plain language, and logical source order as practical accessibility foundations.[1]

A heading communicates the organization of a page. A `<nav>` identifies navigation. A `<main>` identifies the primary content. A `<button>` represents an action, while an `<a>` represents a link to another resource or location. These meanings help browsers, assistive technologies, search tools, and people who scan a page understand what is available.

A semantic choice is not a guarantee of accessibility. The final result also depends on text, language, contrast, keyboard behavior, dynamic updates, timing, content alternatives, device settings, and testing with representative users. WCAG 2.2 expresses accessibility requirements as testable, technology-neutral success criteria and covers content on desktop and mobile devices.[2] A small lesson checklist can support review, but it cannot claim that a page conforms to WCAG.

## Worked example

Consider this fictional course-page outline:

| Content purpose | Weak label | Better structural choice | Reason |
|---|---|---|---|
| Page title | `big purple text` | One descriptive `<h1>` | The heading states what the page is about. |
| List of lessons | `box with links` | `<nav>` or a labeled list of links | Navigation is identified as navigation. |
| Search action | Clickable `<div>` | A labeled `<button>` beside a search field | The native control has an expected keyboard and control meaning. |
| Email field | `text box` | A `<label>` connected to an input | The purpose is available in text, not only by position. |
| Main lesson | `everything` | `<main>` containing sections and headings | The primary content has a recognizable landmark. |

The table does not require an HTML runtime. It asks whether each element’s meaning matches its purpose. If the fictional page later gains color, styling, or scripts, the review must also check those changes rather than assuming that the original structure is enough.

A small written fragment might look like this:

```html
<main>
  <h1>Offline study plan</h1>
  <label for="topic">Topic</label>
  <input id="topic" name="topic">
  <button type="button">Save topic</button>
</main>
```

The `for` value and the input `id` connect the label to the field. The button states its type explicitly. This fragment is illustrative; it has not been presented as a complete page or a complete accessibility test.

## Common mistakes

Do not choose an element only because it is easy to style. Do not use a heading merely to make text large, or a link merely to trigger an in-page action. Do not rely on placeholder text as the only label. Do not describe a control only by color or position. Do not assume that one keyboard check, automated scan, or screen-reader pass proves complete conformance. Finally, do not put real names, contact details, or private notes into a copied practice fragment.

## Offline practice

Review this fictional outline for a local study page:

```text
A large title: “Three ways to review a lesson”
A row of items: Read, Practice, Notes
A field described by nearby text: “Reminder”
A rectangle that changes the reminder when tapped
A two-column list comparing “Before review” and “After review”
```

Write a semantic element or structure for each item. Identify one place where a label, heading, table relationship, or keyboard behavior needs clarification. Then write one question that a human reviewer should test with a keyboard or assistive technology. Do not claim that your written review proves WCAG conformance.

## Knowledge check

1. What does semantic HTML try to match? **The element’s meaning and purpose to the content or interaction it represents.** This gives the browser and assistive technologies useful structure.
2. Why is a native button usually preferable to a clickable generic container for an action? **A native button has built-in control semantics and keyboard behavior.** A custom control may be possible, but it requires more careful implementation and testing.
3. Why should a form field have a visible or programmatically connected label? **The label communicates the field’s purpose in text and can be associated with the control.** Position alone may not be clear to every user.
4. Does a paper checklist prove that a page conforms to WCAG 2.2? **No.** WCAG success criteria are testable, and a small review is only limited evidence; conformance requires appropriate evaluation of the complete content and context.

## Project or application

Create a one-page semantic outline for a fictional offline lesson reader. Include a page heading, navigation, the main lesson, one form field, one action, and a results list. For each part, write its purpose, proposed element, keyboard expectation, and one question that still needs testing. Keep all names and values fictional.

## Accessibility notes

The practice is paper-first and can be completed as labeled sentences, a table, speech-to-text, or a notebook drawing. No color distinction, timed response, mouse gesture, live page, or code execution is required. Learners may describe markup in words if angle brackets are difficult to enter or read.

## Safety and responsible use

This is an S0 offline Web-literacy lesson. Use only fictional content. Do not paste real personal data into a form example, upload a page, contact a site owner, or claim that an educational checklist is a legal or technical accessibility certification. For a real project, follow the project’s accessibility process and include people with relevant lived experience in review.

## Further reading

The [MDN guide “HTML: A good basis for accessibility”](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Accessibility/HTML) explains semantic HTML, native controls, labels, links, tables, and keyboard accessibility.[1] The [W3C Web Content Accessibility Guidelines 2.2 Recommendation](https://www.w3.org/TR/WCAG22/) defines testable, technology-neutral success criteria for accessible Web content.[2]

## Change log

- 1.0.0 — Initial Stage 4 draft.

## References

[1]: https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Accessibility/HTML "MDN: HTML: A good basis for accessibility"
[2]: https://www.w3.org/TR/WCAG22/ "W3C: Web Content Accessibility Guidelines 2.2"
