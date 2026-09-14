---
id: al-03-searching-sorting
title: Searching and sorting by example
strand: algorithms
level: beginner
version: 1.0.0
prerequisites:
  - al-02-arrays-lists-stacks-queues
estimated_minutes: 75
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Trace a linear search and report whether an item is found.
  - Explain why binary search requires sorted data and a clear midpoint rule.
  - Trace a simple selection-style sort and describe the output order.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - algorithms
  - search
  - sorting
  - tracing
sources:
  - https://open.umn.edu/opentextbooks/textbooks/an-open-guide-to-data-structures-and-algorithms
  - https://docs.python.org/3/library/functions.html
---

# Searching and sorting by example

## Objectives

By the end of this lesson, you can trace a linear search, explain the precondition that makes binary search meaningful, and follow a simple sorting process on a small list.

## Prerequisites

Complete [AL-02](al-02-arrays-lists-stacks-queues.md), including ordered sequences and operation tracing.

## Availability

This lesson and every exercise are fully offline. No programming runtime or network is needed.

## Explanation

**Searching** asks whether a target exists and, often, where it occurs. A linear search checks items from left to right until it finds the target or reaches the end. It works even when the values are not ordered, but it may inspect many items.

Binary search uses a different idea: compare the target with the middle item of a **sorted** sequence, then keep only the half that could still contain the target. If the sequence is not sorted, discarding half can discard the answer. A careful implementation defines the low and high boundaries and stops when the remaining range is empty.

**Sorting** rearranges values into an order, such as smallest to largest or alphabetical order. A selection-style sort repeatedly finds the smallest remaining value and places it at the next position. It is easy to trace, although it may perform many comparisons. A short, clear algorithm is useful for learning before choosing a more efficient implementation for larger data.

## Worked example

Linear search for `7` in `[4, 9, 7, 2]`:

| Check | Position | Value | Decision |
|---:|---:|---:|---|
| 1 | 0 | 4 | Not the target; continue. |
| 2 | 1 | 9 | Not the target; continue. |
| 3 | 2 | 7 | Found at position 2. |

For binary search, first sort the values: `[2, 4, 7, 9]`. The middle value is `4`. Because `7` is larger, keep the right half `[7, 9]`; the next comparison finds `7`. The sorted precondition made that discard safe.

A selection-style sort of `[3, 1, 2]` is:

| Pass | Remaining values | Smallest chosen | Output prefix |
|---:|---|---:|---|
| 1 | `[3, 1, 2]` | 1 | `[1]` |
| 2 | `[3, 2]` | 2 | `[1, 2]` |
| 3 | `[3]` | 3 | `[1, 2, 3]` |

## Common mistakes

Searching and sorting are different operations: a search answers a question, while sorting changes order. Binary search is often applied to unsorted data by mistake. Off-by-one boundary errors can skip the first or last item, and stopping after the first failed comparison is not the same as completing a linear search. When sorting, distinguish the values still under consideration from the values already placed.

## Offline practice

1. Trace a linear search for `5` in `[8, 5, 3, 5]`. Record the first position found and the number of checks.
2. For binary search of `12` in `[2, 5, 8, 12, 15, 19]`, record each midpoint and remaining range.
3. Use the selection-style process on `[4, 2, 5, 1]`. Record the chosen minimum after each pass.

Complete the tables on paper. Do not assume binary search is valid until you have confirmed the input is sorted.

## Knowledge check

1. What does a linear search do? **It checks items in sequence until it finds the target or reaches the end.** It does not require sorted input.
2. What precondition does binary search need? **The sequence must be sorted according to the same comparison rule.** Otherwise a discarded half may contain the target.
3. What is sorting? **Rearranging values into a chosen order.** The order might be ascending, descending, or alphabetical.
4. Why is a simple selection-style sort useful in a beginner lesson? **Its repeated choice and placement are easy to trace.** Clarity helps learners understand the invariant before studying faster methods.

## Project or application

Design a paper search-and-sort plan for five fictional lesson durations. First write a linear-search trace for a target duration. Then sort the durations from shortest to longest using the selection-style process. State an invariant such as “the output prefix is already sorted after each pass.”

## Accessibility notes

Each algorithm is represented by a table with one comparison or pass per row. Learners can move labeled cards instead of reading code, and the target, midpoint, and remaining range should be announced in words as well as shown visually.

## Safety and responsible use

This is an S0 offline reasoning lesson using fictional numbers. It does not access real records or services. In real systems, confirm that search and sort rules do not expose private data or change records unexpectedly.

## Further reading

[An Open Guide to Data Structures and Algorithms](https://open.umn.edu/opentextbooks/textbooks/an-open-guide-to-data-structures-and-algorithms) introduces search and sorting concepts. The [Python built-in functions reference](https://docs.python.org/3/library/functions.html) documents useful operations such as `sorted()` that return ordered results without requiring a custom algorithm for small programs.

## Change log

- 1.0.0 — Initial MVP draft.
