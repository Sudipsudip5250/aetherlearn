---
id: dev-04-testing-pure-functions
title: Testing small programs with examples and expected results
strand: developer-foundations
level: beginner
version: 1.0.0
prerequisites:
  - dev-03-debugging-error-messages
estimated_minutes: 55
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain how a test case connects an input to an expected result.
  - Distinguish a pure function’s output from setup, fixtures, and reporting.
  - Design a small set of normal, boundary, and invalid-input examples on paper.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can turn an expected behavior into a small test case, choose examples that cover important boundaries, and explain why a passing test is evidence rather than a guarantee that every possible input is correct.

## Prerequisites

You should understand expected versus observed behavior, hypotheses, and minimal reproductions from DEV-03. No test framework, account, package installation, or programming runtime is required.

## Availability

This lesson is fully offline. Its examples use paper tables and fictional functions. The app does not execute learner-provided code.

## Explanation

A test is a repeatable question about a program. It supplies an input, observes a result, and compares that result with an expectation. The expectation should be written before the observation so that the test is evidence instead of a post-hoc description.

Python’s standard `unittest` documentation describes a **test case** as checking a specific response to particular inputs. It also describes fixtures as preparation and cleanup, suites as collections of cases, and runners as components that coordinate execution and report outcomes.[1] These concepts appear in many testing systems even when the names differ.

A **pure function** is a useful teaching model: the same input produces the same output, and the function does not change unrelated state or depend on a live service. Pure functions are easier to reason about because a failing result can be compared with a small input without first reconstructing a device, account, clock, or network.

A good first test set usually includes:

| Kind of example | Purpose | Fictional example for `label_count(text)` |
|---|---|---|
| Normal | Checks a representative case. | `"red blue"` should produce `2`. |
| Boundary | Checks an edge near a rule’s limit. | `""` should produce `0` if empty text is allowed. |
| Repeated or unusual | Checks assumptions about duplicates or spacing. | `"red  red"` should follow the stated spacing rule. |
| Invalid input | Checks a documented rejection. | A non-text value should be rejected if the contract forbids it. |

A test name should describe the behavior, not the implementation detail. “Empty text has zero labels” is more useful than “test line 14.” When a test fails, record the input, expected result, observed result, and a short interpretation. Do not silently change the expected result just to make a failure disappear; first decide whether the program or the requirement is wrong.

Tests can be too narrow. Four passing examples do not prove that every string, number, device, or user will behave correctly. Tests are selected evidence. They become more useful when their cases are connected to a clear rule and when the suite is updated as the rule changes.

## Worked example

Imagine a fictional offline function named `shipping_band(weight_kg)` with this contract:

- weights from `0` through `2` kilograms use band `A`;
- weights greater than `2` through `5` kilograms use band `B`;
- negative weights are rejected;
- values above `5` kilograms use band `C`.

A paper test table might be:

| Input | Expected result | Reason |
|---:|---|---|
| `0` | `A` | Lowest allowed boundary. |
| `2` | `A` | Upper boundary of the first band. |
| `2.01` | `B` | Just above the first boundary. |
| `5` | `B` | Upper boundary of the second band. |
| `-1` | Reject | Invalid negative weight. |
| `6` | `C` | Above the second boundary. |

The table does not run a program. It makes the rule visible and gives a future implementation a set of questions. If a reviewer notices that `2.01` was not defined, the contract needs clarification before coding.

## Common mistakes

Do not test only the easiest normal case. Do not confuse “the function returned a value” with “the value was correct.” Do not omit units, case rules, empty values, or rejection behavior when those details affect the contract. Do not use a live API, real customer data, or a copied log as a fixture for a beginner exercise. Finally, do not treat a green test report as proof that security, accessibility, performance, and every untested path are correct.

## Offline practice

Create a six-row test table for a fictional function `study_minutes(day_record)` that returns the total minutes in a record. Define one normal case, one empty case, two boundary cases, one malformed record, and one repeated-entry case. For every row, write the input, expected result, and reason. Then mark which cases are **normal**, **boundary**, or **invalid**.

Review your own table. Is the result defined for missing data? Are the units clear? Could two people interpret the same row differently? Revise the contract before revising any imagined implementation.

## Knowledge check

1. What three pieces connect in a basic test case? **An input, an observed result, and an expected result.** The comparison gives the test its meaning.
2. Why are pure functions useful for introductory testing? **They make the same input easier to compare because they avoid unrelated state and live dependencies.** This reduces setup and ambiguity; it does not make every program pure.
3. What is a boundary example? **An input at or just beside a rule’s limit.** Boundaries often reveal unclear conditions such as whether a value is included.
4. Does a passing test prove that every possible input is correct? **No.** It is evidence for the cases and behavior covered by that test; broader confidence requires more relevant cases and review.

## Project or application

Create a paper “test-design card” for one fictional local utility, such as converting minutes to hours, counting words in a note, or labeling a study session. Include its contract, four test cases, one boundary, one invalid-input rule, and a sentence explaining what the card does not prove. Keep every value fictional and local.

## Accessibility notes

The lesson uses plain-language definitions, a text table, and a paper-first practice that can be completed with speech-to-text or a screen reader. Learners may replace numeric tables with labeled sentences. No color, animation, timed response, code font, or mouse gesture is required.

## Safety and responsible use

This is an S0 offline quality lesson. Use fictional inputs and local notes only. Do not paste credentials, personal data, private logs, or live-service addresses into a test case. Do not install an unverified testing package or run copied code as part of this lesson. A test table documents expected behavior; it does not authorize testing a device, account, or service.

## Further reading

The [Python `unittest` documentation](https://docs.python.org/3/library/unittest.html) defines test cases, fixtures, suites, runners, assertions, and test organization. The [Python testing documentation](https://docs.python.org/3/library/test.html) provides the official documentation index for Python’s test-related tools.

## Change log

- 1.0.0 — Initial Stage 2 draft.
