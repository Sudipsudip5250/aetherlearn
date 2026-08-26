---
id: py-02-python-setup-expressions-values
title: Python setup, expressions, and values
strand: python-fundamentals
level: beginner
version: 1.0.0
prerequisites:
  - py-01-problems-algorithms-instructions
estimated_minutes: 50
availability: termux-optional
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain the difference between a value, an expression, and an output.
  - Predict the result of simple Python arithmetic and string expressions.
review_status: draft
last_reviewed: 2026-08-24
tags:
  - python
  - programming
termux:
  exercise_id: py-02-local-expressions
  fallback: Complete the in-app tracing exercise and record the predicted outputs in your notes.
sources:
  - https://docs.python.org/3/tutorial/introduction.html
---

# Python setup, expressions, and values

## Objectives

By the end of this lesson, you can distinguish values from expressions, read simple Python operators, and predict what a short expression will produce before running it.

## Prerequisites

Complete [PY-01](py-01-problems-algorithms-instructions.md). Termux is optional; the core lesson can be completed entirely inside the app.

## Availability

The explanation and in-app practice are fully offline. The optional terminal exercise uses a local Termux session and does not require a network connection.

## Explanation

A **value** is data such as the number `7` or the text `"hello"`. An **expression** is a piece of code that produces a value. In Python, `3 + 4` is an arithmetic expression and produces `7`. The expression `"a" + "b"` produces the text `"ab"`.

Operators have rules. Addition combines numbers and can also join strings of the same general kind. Division produces a quotient, while parentheses make grouping explicit. When an expression is printed, the program displays its resulting value; evaluating an expression and displaying it are related but not identical actions.

You do not need to memorize every Python feature at once. Read each expression from the inside out, identify the values, apply the operator rules, and write down the result. Predicting first makes errors easier to understand.

## Worked example

Evaluate `2 * (3 + 4)`. First evaluate the parentheses: `3 + 4` is `7`. Then multiply: `2 * 7` is `14`. Evaluate `"learn" + " " + "locally"` by joining the three strings to produce `"learn locally"`.

## Common mistakes

A common mistake is confusing text that looks like a number with a number: `"7"` is text, while `7` is an integer. Another mistake is assuming that every operator works with every value type. When Python reports a type error, read the values and operator together rather than guessing.

## Offline practice

Predict the result of `5 + 2 * 3`, `(5 + 2) * 3`, and `"A" + "B"`. Then explain why the first two expressions differ. Use the in-app checker to compare your reasoning with the expected explanation.

## Knowledge check

1. Is `4 + 5` a value or an expression? **An expression** that evaluates to the value 9.
2. What does `"4"` represent? **Text**, not the integer 4.
3. Why use parentheses? **To make grouping explicit and control the order in which parts are evaluated.**

## Project or application

Write a tiny “message builder” on paper or in the in-app editor using three text values: a greeting, a learner name, and a study goal. Show the expression that would join them with spaces. Optionally run the same expression in Termux after reviewing the command and expected output.

## Accessibility notes

All examples are written as text and paired with plain-language explanations. The in-app checker provides feedback in words rather than relying only on color or animation. Code samples remain usable with large text.

## Safety and responsible use

This is an S1 local programming lesson. The optional Termux exercise uses only a local expression and does not need network, shared storage, credentials, package installation, or access to a remote host.

## Further reading

Read the [Python tutorial introduction](https://docs.python.org/3/tutorial/introduction.html) for Python’s basic numerical, text, and expression examples.

## Change log

- 1.0.0 — Initial MVP draft.
