---
id: py-04-conditions-boolean-logic
title: Conditions and Boolean logic
strand: python-fundamentals
level: beginner
version: 1.0.0
prerequisites:
  - py-03-variables-types-input-output
estimated_minutes: 55
availability: offline
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Evaluate comparisons as Boolean values in small Python examples.
  - Trace an if/elif/else decision and explain why one branch runs.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - python
  - conditions
  - boolean-logic
sources:
  - https://docs.python.org/3/tutorial/controlflow.html
---

# Conditions and Boolean logic

## Objectives

By the end of this lesson, you can read comparisons, combine Boolean conditions, and trace which branch of a small Python decision executes.

## Prerequisites

Complete [PY-03](py-03-variables-types-input-output.md), including the difference between text and numeric values.

## Availability

This lesson is fully offline. It uses deterministic code-reading exercises and does not require a runtime, files, or network access.

## Explanation

A **condition** is an expression that can be evaluated as true or false. Python represents these results with the Boolean values `True` and `False`. Comparisons include `<`, `>`, `<=`, `>=`, `==`, and `!=`. The single equals sign `=` assigns a value; the double equals sign `==` compares values.

An `if` statement runs its indented block when its condition is true. `elif` provides another condition if earlier conditions were false, and `else` provides a fallback. In an `if`/`elif`/`else` chain, the first matching branch runs and the remaining branches are skipped.

Boolean logic combines conditions. `and` requires both sides to be true. `or` requires at least one side to be true. `not` reverses a Boolean result. Parentheses make the intended grouping visible. A condition should describe an observable rule rather than a vague instruction such as “do the usual thing.”

## Worked example

Trace this fictional ticket rule:

```python
age = 12
has_ticket = True

if age < 8:
    price = 0
elif has_ticket and age < 16:
    price = 5
else:
    price = 10
```

The first condition is false because `12 < 8` is false. The second condition is true because both `has_ticket` and `age < 16` are true. Therefore `price` becomes `5`, and the `else` block does not run.

## Common mistakes

A common mistake is using `=` where a comparison needs `==`. Another is forgetting that `and` needs both conditions while `or` needs at least one. Learners may also read every branch as if it runs; in a chain, only the first true branch runs. Finally, a condition involving text and numbers may be invalid or misleading unless types are clear.

## Offline practice

Evaluate each condition as `True` or `False`, using `score = 72` and `submitted = False`:

1. `score >= 60`
2. `submitted and score >= 60`
3. `not submitted or score == 72`

Then trace which message is printed:

```python
if score >= 90:
    print("excellent")
elif score >= 60:
    print("ready to review")
else:
    print("try again")
```

## Knowledge check

1. What does `==` do? **It compares two values.** It produces a Boolean result instead of assigning.
2. When does `a and b` become true? **When both conditions are true.** If either side is false, the combined condition is false.
3. In an if/elif/else chain, how many branches run? **The first matching branch.** Later branches are skipped once a match is found.

## Project or application

Write a paper decision tree for a fictional library reminder. It should use a due-date category and whether the book was returned to choose one of three messages. Write each condition precisely, test it with three fictional cases, and identify any boundary case such as “due today.”

## Accessibility notes

Every code block is followed by plain-language tracing. Conditions are presented as text and the practice does not depend on color, animation, or timed interaction.

## Safety and responsible use

This is an S1 local programming lesson. Examples use fictional scores and library states. Do not connect a first conditional program to real accounts, messages, payments, or access-control decisions without review and testing.

## Further reading

The [Python control-flow tutorial](https://docs.python.org/3/tutorial/controlflow.html) documents `if`, `elif`, `else`, comparison operators, and Boolean behavior.

## Change log

- 1.0.0 — Initial MVP draft.
