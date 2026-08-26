---
id: web-03-javascript-events-and-state
title: JavaScript events and local state
strand: web-data
level: beginner
version: 1.0.0
prerequisites:
  - web-01-semantic-html-accessibility
  - dl-07-how-programs-run
estimated_minutes: 60
availability: offline
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Trace a fictional user event from an input to a bounded state change.
  - Distinguish document structure, event handling, and stored application state.
  - Identify a privacy or reliability question before a local state change is implemented.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can describe an event as something that happens in an interface, trace a small state transition on paper, and explain why local state needs clear ownership, validation, and privacy limits.

## Prerequisites

You should understand how a computer runs a program, semantic HTML controls, and the difference between a local activity and a network service. No JavaScript runtime, browser console, account, or network connection is required.

## Availability

This lesson is fully offline. It uses a fictional event trace and pseudocode-like fragments for reading only. The app does not execute learner-provided JavaScript or save the fictional data outside the lesson.

## Explanation

JavaScript is a programming language commonly used to add behavior to Web pages. MDN’s beginner scripting curriculum treats events, DOM scripting, JSON, network requests, and debugging as related but distinct topics.[1] The **Document Object Model**, or DOM, is a browser-provided representation of a document that scripts can inspect or change.

An **event** is a notification that something happened, such as a user activating a button, changing a field, or moving focus. An event handler is behavior associated with that event. A useful mental model is:

```text
event happens → handler checks the input → state changes → interface is updated
```

**State** is information that can change while an application is running, such as whether a fictional lesson is bookmarked. A state transition describes the old state, the event, the checks, and the new state. Keeping this chain visible helps a reviewer ask whether the change is expected, reversible, and private.

Local state is not automatically safe merely because it stays in a browser or app. A design still needs to say what is stored, for how long, who can read it, and how a person can remove it. A state value should be no broader than the feature requires. A note-taking feature should not collect unrelated device details just because a script can access them.

This lesson intentionally stops before network requests and arbitrary code. A real JavaScript program also needs testing, error handling, and accessibility review. A paper trace is a design aid, not evidence that an implementation is correct or secure.

## Worked example

Imagine a fictional offline reader with a `Bookmark lesson` button. Its state is either `not bookmarked` or `bookmarked`.

| Step | Event or check | State before | State after | Review question |
|---:|---|---|---|---|
| 1 | Page opens | unknown | not bookmarked | What is the safe default if no saved value exists? |
| 2 | Learner activates the button | not bookmarked | bookmarked | Is the action available by keyboard and clearly announced? |
| 3 | The interface updates its text | bookmarked | bookmarked | Does the text communicate the new state without color alone? |
| 4 | Learner activates the button again | bookmarked | not bookmarked | Is the action reversible and local? |

A written handler description might be:

```text
on bookmark activation:
  if the current lesson ID is known:
    switch bookmarked between true and false
    update the button’s text and state description
  otherwise:
    show a local error and change nothing
```

This is not executable JavaScript. It makes preconditions and outcomes visible. It also shows why a stable lesson ID is safer than using an arbitrary text label as the storage key.

## Common mistakes

Do not confuse an event with the state it changes. Do not update the interface before deciding whether the input is valid. Do not store an entire document when a stable identifier and a small boolean are enough. Do not assume browser-local state is invisible to every other person who can use the device. Do not add a network request to a feature that can be taught locally. Finally, do not use a custom clickable element without checking keyboard behavior and state announcements.

## Offline practice

Trace this fictional interaction on paper:

```text
Initial state: reminder = empty
Event 1: learner enters “Review tables”
Event 2: learner activates Save
Event 3: learner opens the lesson again
Event 4: learner clears the reminder
```

For each event, write the state before and after, the validation check, the interface message, and whether the value should remain stored. Add one privacy question and one accessibility question. Then identify one failure case, such as an empty input or an unknown lesson ID, and describe a safe no-change outcome.

## Knowledge check

1. What is an event in a Web interface? **A notification that something happened, such as a button activation or field change.** A handler can respond to that event.
2. What is application state? **Information that can change while the application runs, such as whether a lesson is bookmarked.** State is not the same thing as the event that caused the change.
3. What should a bounded state transition describe? **The old state, event, checks, and new state.** This makes behavior reviewable and helps expose missing conditions.
4. Does local browser state automatically mean that no privacy review is needed? **No.** A local value still needs a clear purpose, retention rule, access expectation, and removal path.

## Project or application

Design a paper state machine for a fictional offline “practice reminder” control. Include three states, at least four events, one invalid-input path, the text a user should hear or read after each change, and a deletion action. State that the design does not test a real browser, network request, persistence store, or assistive technology.

## Accessibility notes

The lesson can be completed with a state table, spoken answers, or a notebook diagram. No code execution, animation, color change, or timed interaction is required. When describing an event, always name the action and the resulting state in text so the practice does not depend on visual changes alone.

## Safety and responsible use

This is an S1 offline Web/data lesson. Use only fictional lesson IDs and notes. Do not run copied scripts, paste personal data into a console, inspect another person’s browser storage, add tracking, or create a network request for this exercise. The paper design is not permission to execute code against a browser, account, device, or service.

## Further reading

The [MDN Dynamic scripting with JavaScript module](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting) introduces beginner topics including events, DOM scripting, JSON, network requests, and debugging.[1] The [MDN introduction to events](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting/Events) provides a focused explanation of event handling; use it as reference material, not as a reason to run unreviewed code.

## Change log

- 1.0.0 — Initial Stage 4 draft.

## References

[1]: https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting "MDN: Dynamic scripting with JavaScript"
