---
id: dev-03-debugging-error-messages
title: Debugging, error messages, and minimal reproduction
strand: developer-foundations
level: beginner
version: 1.0.0
prerequisites:
  - py-06-functions-scope-reusable-code
estimated_minutes: 65
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Distinguish a syntax error from an exception raised while a program runs.
  - Read the type, message, file, and line information in a simple traceback.
  - Reduce a problem to a minimal reproducible example and test one hypothesis at a time.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - debugging
  - errors
  - reproduction
  - developer-foundations
sources:
  - https://docs.python.org/3/tutorial/errors.html
  - https://docs.python.org/3/library/traceback.html
---

# Debugging, error messages, and minimal reproduction

## Objectives

By the end of this lesson, you can classify a basic Python error, extract useful clues from its message, and describe a minimal reproduction that another person can run and understand safely.

## Prerequisites

Complete [PY-06](py-06-functions-scope-reusable-code.md), including functions, local scope, return values, and small test cases.

## Availability

This lesson and every exercise are fully offline. The examples use short fictional programs and do not require a runtime, account, network, or real project.

## Explanation

**Debugging** is a disciplined process for finding and correcting the cause of an unexpected result. Start by describing the expected behavior and the observed behavior. Preserve the exact error text, identify what changed, and make the smallest safe test that could distinguish between competing explanations.

A **syntax error** means the source cannot be parsed as written; a missing colon or unmatched parenthesis is an example. An **exception** occurs while syntactically valid code is running. Python reports an exception type such as `NameError`, `TypeError`, or `ZeroDivisionError`, along with a message and traceback context. The traceback often includes a file and line number, but the marked location may be where the problem became visible rather than where the original assumption was introduced.

A **minimal reproducible example** (MRE) contains the smallest input, code, and steps that still show the problem. Remove unrelated files, private data, credentials, network calls, and unnecessary dependencies. Change one thing at a time, record the result, and stop if a test could affect a real system. A useful bug report says what was tried and what happened, not only that “it failed.”

## Worked example

Suppose this fictional program fails:

```python
def average(total, count):
    return total / counts

print(average(10, 2))
```

A traceback may end with:

```text
NameError: name 'counts' is not defined
```

Read it from the bottom upward: the exception type is `NameError`, the message identifies the missing name, and the traceback points to the expression inside `average`. Compare the function’s parameters with the name used in the return expression. The smallest correction is to use `count`:

```python
def average(total, count):
    return total / count
```

Now test a second case, `average(0, 2)`, and a boundary case such as `average(10, 0)` only as a thought exercise unless a zero rule has been designed. The second case raises a different problem, so do not assume that fixing one message proves the whole function is correct.

## Common mistakes

A common mistake is changing several lines at once, which makes the cause of improvement unclear. Another is reporting only the final line of an error while omitting the input, code version, or steps. Do not blindly copy a suggested fix from an untrusted source, and do not share a traceback containing paths, usernames, tokens, or personal data. Finally, a line number is a clue, not proof that the nearest line is the original cause.

## Offline practice

Classify each fictional problem as a syntax error, a likely exception, or an ordinary wrong result:

1. A closing parenthesis is missing.
2. `"3" + 3` is evaluated.
3. A program prints `12` when the expected answer is `14`.
4. A dictionary lookup asks for a key that is not present.

For one item, write a minimal reproduction with only the input, two or three lines of pseudocode, expected behavior, observed behavior, and one hypothesis. Remove names, paths, account details, and network references.

## Knowledge check

1. What is a syntax error? **A problem that prevents source code from being parsed.** The program cannot begin normal execution until the syntax is corrected.
2. What does the exception type tell you? **The category of failure that was raised.** The message adds detail about the particular occurrence.
3. What is a minimal reproducible example? **The smallest safe code, input, and steps that still show a problem.** It makes investigation easier and reduces unrelated causes.
4. Why change one hypothesis at a time? **So the result gives evidence about which change affected the behavior.** Several simultaneous edits make cause and effect difficult to identify.

## Project or application

Write a debugging worksheet for a fictional offline quiz calculator. Include expected output, observed output, exact input, a short reproduction, one hypothesis, one test, and the next decision. Add a privacy check asking whether the worksheet contains credentials, personal data, or file paths that should be removed before sharing.

## Accessibility notes

The error-reading process is presented as labeled fields—type, message, location, expected result, and observed result—rather than relying on color. Learners can use a spoken worksheet or large-print table, and the MRE exercise can be completed with pseudocode.

## Safety and responsible use

This is an S0 offline debugging lesson using fictional examples. Do not paste secrets or private logs into a bug report. Reproduce issues locally with synthetic inputs, and never test a suspected fix against a live service, another person’s account, or data you are not authorized to change.

## Further reading

The [Python errors and exceptions tutorial](https://docs.python.org/3/tutorial/errors.html) explains syntax errors, exceptions, tracebacks, and handling selected failures. The [Python `traceback` reference](https://docs.python.org/3/library/traceback.html) documents tools for displaying traceback information.

## Change log

- 1.0.0 — Initial MVP draft.
