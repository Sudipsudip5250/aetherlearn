---
id: web-02-css-layout-responsive-design
title: CSS layout and responsive presentation
strand: web-data
level: beginner
version: 1.0.0
prerequisites:
  - web-01-semantic-html-accessibility
estimated_minutes: 55
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain the difference between document structure and CSS presentation.
  - Predict how a simple layout may change when the available width changes.
  - Identify a responsive-design risk in a fictional page without relying on a particular device or browser.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can describe how CSS rules select and present HTML content, reason about a small layout at two viewport widths, and write a responsive review note that separates an observation from an assumption.

## Prerequisites

You should understand semantic HTML, headings, controls, and the difference between a document’s structure and its presentation. No browser developer tools, editor, network connection, or CSS runtime is required.

## Availability

This lesson is fully offline. It uses fixed layout cards and short illustrative CSS fragments. The app does not load external stylesheets or ask the learner to publish a page.

## Explanation

CSS, or Cascading Style Sheets, describes how HTML content is presented. MDN describes CSS as a language for styling and laying out web pages, including choices about font, color, size, spacing, columns, and other visual features.[1] A CSS rule commonly has a selector, which identifies elements, and declarations, which provide property/value choices.

The **cascade** is the process by which competing rules are considered. A rule’s selector, source order, importance, and inheritance can affect the result. For beginner reasoning, it is useful to keep selectors simple and to state the intended rule before discussing which rule wins.

The **box model** treats an element as content surrounded by padding, border, and margin. A layout also depends on the available width, the size of text, the length of words, and whether items are allowed to wrap. A page can look comfortable at one width and become crowded or unreadable at another.

Responsive presentation means that content remains usable as the available space and user settings change. A responsive design does not mean that every screen must look identical. It means that structure, reading order, controls, and essential information remain understandable. A fixed-width card, a long unbroken string, or a control row that cannot wrap can create problems on a narrow screen.

A paper exercise can predict likely behavior, but it cannot measure every browser, font, zoom level, writing system, or assistive-technology combination. Real review should include representative devices and text settings.

## Worked example

Suppose a fictional lesson page contains a title, a main explanation, and a related-links panel. The design has these rules:

```css
.page {
  display: flex;
  gap: 16px;
}

.related {
  width: 240px;
}
```

At a wide viewport, the main explanation and the related panel may fit side by side. At a narrow viewport, the fixed panel can take too much room. A safer design question is whether the items can wrap or stack while preserving the document order:

| Available width | Likely concern | Review question |
|---:|---|---|
| 900 px | Enough room for two regions in this fictional example | Can a reader reach the main explanation in a sensible order? |
| 420 px | The fixed panel may crowd the explanation | Can the regions stack or wrap without clipping text? |
| 320 px | Long labels and controls may become crowded | Can text resize and can every control still be reached? |

These are predictions, not measured results. The exercise is to identify what should be tested and why.

## Common mistakes

Do not treat CSS as a replacement for meaningful HTML structure. Do not use a fixed pixel width as if every device, zoom level, or font were identical. Do not hide overflow merely to make a screenshot look tidy. Do not assume that a visually attractive layout remains usable when text is enlarged. Do not use color alone to communicate a change. Finally, do not copy a layout rule from a live site and treat it as a complete responsive solution.

## Offline practice

Use the fictional page with four regions: `Title`, `Lesson`, `Practice`, and `Notes`. At 900 units of available width, place `Lesson` beside `Practice` and keep `Notes` below. At 360 units, propose a reading order and decide which regions should stack. For each decision, write the content purpose, the likely CSS behavior, and one thing to test.

Then review this fictional problem: “The `Save note` control is placed in a horizontal row with a long label and never wraps.” Write two possible layout responses and one question about keyboard access. Do not run a browser or use real user notes.

## Knowledge check

1. What is the main job of CSS in a Web page? **To describe the presentation and layout of structured content.** HTML still provides the document’s meaning and structure.
2. What parts are commonly found in a simple CSS rule? **A selector and one or more declarations containing properties and values.** The selector identifies what the rule applies to.
3. Why can a layout that fits at 900 units fail at 360 units? **The available space, text wrapping, and control relationships change.** Fixed widths and unbroken rows can crowd, clip, or obscure content.
4. Does a paper layout prediction prove that a page is responsive and accessible? **No.** It identifies likely risks and test questions; real evaluation still needs appropriate browsers, settings, and user-centered checks.

## Project or application

Create a responsive review card for a fictional offline study page. Draw or describe the page at a wide and narrow width, specify the reading order, identify one region that may stack, and list three acceptance checks: readable text, reachable controls, and no essential content hidden by clipping. State what your review card does not test.

## Accessibility notes

The exercise can be completed with a paper diagram, a table, or short labeled sentences. It does not require color, animation, a particular screen size, a mouse, or an internet connection. Learners may use “wide” and “narrow” instead of numeric widths if numbers are difficult to interpret.

## Safety and responsible use

This is an S0 offline Web-literacy lesson. Use fictional page content only. Do not upload screenshots containing personal data, install untrusted browser extensions, or treat this exercise as a full accessibility or usability certification. Avoid collecting device information merely to demonstrate responsive behavior.

## Further reading

The [MDN CSS styling basics module](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Styling_basics) introduces CSS syntax, selectors, values, the box model, sizing, overflow, and layout.[1] The [W3C WCAG 2.2 Recommendation](https://www.w3.org/TR/WCAG22/) includes success criteria such as resize text and reflow that are useful review vocabulary, but conformance requires appropriate evaluation of the complete page.[2]

## Change log

- 1.0.0 — Initial Stage 4 draft.

## References

[1]: https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Styling_basics "MDN: CSS styling basics"
[2]: https://www.w3.org/TR/WCAG22/ "W3C: Web Content Accessibility Guidelines 2.2"
