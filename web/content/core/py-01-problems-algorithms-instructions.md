---
id: py-01-problems-algorithms-instructions
title: Problems, algorithms, and precise instructions
strand: python-fundamentals
level: beginner
version: 1.0.0
prerequisites:
  - dl-01-digital-information
estimated_minutes: 40
availability: offline
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Break a familiar task into a finite sequence of unambiguous steps.
  - Trace an algorithm on a small input and identify an ambiguous or missing step.
review_status: draft
last_reviewed: 2026-08-24
tags:
  - algorithms
  - computational-thinking
sources:
  - https://www.nist.gov/glossary-term/algorithm
---

# Problems, algorithms, and precise instructions

## Objectives

By the end of this lesson, you can describe a problem using inputs and outputs, write a short algorithm with clear steps, and test the algorithm against more than one example.

## Prerequisites

Complete [DL-01](dl-01-digital-information.md), or be comfortable following an ordered list of instructions.

## Availability

This lesson is fully offline. It uses everyday fictional tasks and does not execute code.

## Explanation

A problem describes a goal and the constraints around reaching it. An algorithm is a finite, repeatable procedure for transforming inputs into an expected result. A good algorithm is precise enough that two people—or two programs—can follow it and obtain the same result for the same input.

A useful first step is to name the input and output. For example, the input might be a list of three temperatures and the output might be the largest temperature. The algorithm should also handle ordinary edge cases, such as an empty list, rather than quietly making an assumption.

Algorithms are tested with examples. A normal example checks the expected path, a boundary example checks the smallest or largest allowed input, and an unusual example checks whether an assumption has been exposed. Testing is part of thinking, not an activity reserved for after programming.

## Worked example

Goal: find the largest number in a non-empty list. Start with the first number as the current largest. Read the next number. If it is larger, replace the current largest. Continue until there are no numbers left. Return the current largest. For `[4, 9, 2]`, the current largest changes from 4 to 9 and remains 9.

## Common mistakes

“Find the largest” is not a complete procedure because it does not say where to start or how to compare the values. Another mistake is testing only one example. An algorithm that works for `[4, 9, 2]` may still fail for a one-item list or a list containing negative numbers.

## Offline practice

Write precise steps for sorting three fictional books by page count from smallest to largest. Test your steps on two different page-count lists. Circle any step that depends on an unstated assumption, such as all page counts being different.

## Knowledge check

1. What are the two useful parts of a problem description? **Its input and its expected output**, along with relevant constraints.
2. Why test a boundary case? **To reveal behavior at the smallest, largest, empty, or otherwise important limit.**
3. Is “do the obvious thing” a precise algorithm step? **No.** The step needs an observable action and a rule for deciding what happens next.

## Project or application

Create a paper-based algorithm for choosing the shortest route among three fictional bus routes. Define the inputs, the output, the comparison rule, and at least two test cases. Do not use real travel data or make a real-world decision from the exercise.

## Accessibility notes

The lesson is text-complete and does not depend on drawing or timed interaction. Learners may write steps in plain language, a numbered list, or a simple table.

## Safety and responsible use

This is an S1 benign computational-thinking lesson. It uses fictional data and does not interact with devices, accounts, networks, or external systems.

## Further reading

The [NIST Glossary entry for algorithm](https://www.nist.gov/glossary-term/algorithm) provides a concise technical definition.

## Change log

- 1.0.0 — Initial MVP draft.
