---
id: py-05-loops-repetition-tracing
title: Loops, repetition, and tracing
strand: python-fundamentals
level: beginner
version: 1.0.0
prerequisites:
  - py-04-conditions-boolean-logic
estimated_minutes: 60
availability: termux-optional
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Trace a bounded for or while loop and record each iteration.
  - Explain how a loop condition and update prevent unintended repetition.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - python
  - loops
  - tracing
termux:
  exercise_id: py-05-local-loop-trace
  fallback: Complete the loop-tracing table in the lesson and record the predicted output in your notes.
sources:
  - https://docs.python.org/3/tutorial/controlflow.html
---

# Loops, repetition, and tracing

## Objectives

By the end of this lesson, you can trace a bounded loop, distinguish `for` from `while`, and identify the update or stopping rule that keeps repetition safe and finite.

## Prerequisites

Complete [PY-04](py-04-conditions-boolean-logic.md), including comparisons and Boolean conditions.

## Availability

The lesson and in-app practice are fully offline. The optional Termux exercise uses a fixed local loop, does not need a network, and is not required for completion.

## Explanation

A **loop** repeats a block of instructions. A `for` loop is useful when iterating over a known sequence or range of items. A `while` loop repeats while a condition remains true, so the program must change something that can eventually make the condition false. Indentation identifies the loop body in Python.

`range(4)` produces four values: `0`, `1`, `2`, and `3`; the endpoint is not included. This makes a bounded `for` loop easy to count. A `while` loop needs a clear starting state, condition, and update. If the update never moves toward the stopping condition, the loop may continue indefinitely.

**Tracing** means recording the important state before or after each iteration. A trace table makes it easier to see which values change, when the body runs, and what output is produced. A loop is not automatically safer than repeated copy-and-paste: its bounds and effects still need review.

## Worked example

Trace this local counting loop:

```python
count = 1
while count <= 3:
    print(count)
    count = count + 1
```

| Iteration | `count` at test | Test result | Output | `count` after update |
|---:|---:|---|---:|---:|
| 1 | 1 | true | 1 | 2 |
| 2 | 2 | true | 2 | 3 |
| 3 | 3 | true | 3 | 4 |
| 4 | 4 | false | none | stops |

The update moves `count` upward, so the condition eventually becomes false. A `for` version, `for count in range(1, 4): print(count)`, produces the same three output values with the bounds made explicit.

## Common mistakes

A common mistake is forgetting that `range` stops before its endpoint. Another is changing the loop variable incorrectly or omitting the update in a `while` loop. Learners may also trace only the output and miss the state change that caused it. Finally, repeating a command many times can create more effects than intended, so practice should start with printing fixed fictional values.

## Offline practice

Complete a trace table for:

```python
for step in range(2, 5):
    print(step * 10)
```

Record each value of `step` and the output. Then inspect this loop and name the missing safety condition:

```python
ready = False
while not ready:
    print("check")
```

What change would let the loop stop in a small fictional example? Do not run an unbounded loop against a real service.

## Knowledge check

1. What values does `range(3)` provide? **0, 1, and 2.** The endpoint 3 is not included.
2. What must a while loop have so it can stop? **A condition and an update that can make it false.** Without progress toward the stopping condition, it may not finish.
3. What is tracing? **Recording state across iterations.** A trace table reveals how values and output change step by step.

## Project or application

Design a paper repetition plan for three fictional study reminders. Write a bounded loop in pseudocode, then make a trace table with the iteration number, current value, condition, and output. Include a maximum count and explain why the bound is appropriate.

## Accessibility notes

The trace table makes state changes explicit and can be read row by row. Code examples are short, and the exercise can be completed on paper or with enlarged text without running a program.

## Safety and responsible use

This is an S1 local programming lesson. The optional Termux exercise prints fixed values only and does not access files, credentials, shared storage, or a network. Never test an uncertain loop against a third-party service or a command that changes real data.

## Further reading

The [Python control-flow tutorial](https://docs.python.org/3/tutorial/controlflow.html) explains `for`, `while`, `range`, `break`, and `continue` behavior.

## Change log

- 1.0.0 — Initial MVP draft.
