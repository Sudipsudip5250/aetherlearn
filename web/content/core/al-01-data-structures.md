---
id: al-01-data-structures
title: What makes a good data structure?
strand: algorithms
level: beginner
version: 1.0.0
prerequisites:
  - py-07-lists-dictionaries-strings-data
estimated_minutes: 45
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Describe a data structure as an organized way to store and access values.
  - Choose a simple structure by comparing the operations a task needs.
  - Explain why speed, memory, clarity, and correctness are practical trade-offs.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - algorithms
  - data-structures
  - tradeoffs
sources:
  - https://open.umn.edu/opentextbooks/textbooks/an-open-guide-to-data-structures-and-algorithms
---

# What makes a good data structure?

## Objectives

By the end of this lesson, you can explain what a data structure organizes, identify common operations, and choose a simple structure for a small fictional task.

## Prerequisites

Complete [PY-07](py-07-lists-dictionaries-strings-data.md), including lists, dictionaries, strings, and simple summaries.

## Availability

This lesson and every exercise are fully offline. No programming runtime or network is needed.

## Explanation

A **data structure** is a way to organize values so a program can store, find, update, or process them. A structure is not automatically good or bad in isolation. Its usefulness depends on the operations a task needs, the amount of data, the memory available, and how clearly people can maintain the code.

Typical operations include adding an item, removing an item, finding an item, reading by position, and visiting every item. A list is often a clear choice for a small ordered sequence. A dictionary is often a clear choice for looking up a value by a unique key. A queue models first-in, first-out work, while a stack models last-in, first-out work. These models help a reader predict what the program should do.

Good choices make the important operation easy to express and keep the rules visible. A fast-looking choice that is confusing or does not preserve the required order can still be wrong. Start with a simple structure, describe the needed operations, and change the structure only when evidence shows that the current choice is inadequate.

## Worked example

Suppose a fictional study app needs to store three lessons and support two operations: show lessons in the learner’s chosen order and look up the minutes for a lesson ID.

| Need | Suitable structure | Reason |
|---|---|---|
| Preserve display order | List of lesson records | Lists retain sequence and are easy to iterate. |
| Look up minutes by ID | Dictionary keyed by ID | A key expresses the lookup directly. |
| Process every lesson | List or dictionary iteration | Both can be visited deliberately. |

A small design might use both:

```text
ordered_lessons = ["py-01", "py-02", "py-03"]
minutes_by_id = {"py-01": 40, "py-02": 50, "py-03": 55}
```

The list answers “what order should I display?” and the dictionary answers “how many minutes belong to this ID?” One structure is not forced to perform every job.

## Common mistakes

A common mistake is choosing a structure because it is familiar rather than because it matches the needed operation. Another is treating a dictionary as if it were a numeric list or assuming that every structure preserves the same ordering rules. Learners may also optimize before measuring; for a small dataset, clarity is usually more valuable than a complicated design. Finally, changing the structure without updating the invariants can silently break behavior.

## Offline practice

Choose a structure for each fictional requirement and explain one reason:

1. A playlist must keep the order in which songs were added.
2. A small directory must find a phone extension from a person’s fictional ID.
3. A line of three learners should be served from the front first.
4. A browser’s Back action should return to the most recently visited page first.

Then write one operation that would be awkward for your choice. This second answer matters because every structure has trade-offs.

## Knowledge check

1. What is a data structure? **An organized way to store and access values.** It gives data a shape that supports particular operations.
2. Why is a list useful for an ordered sequence? **It keeps items in sequence and supports iteration or position-based access.**
3. Why use a dictionary for an ID-to-value lookup? **The key states what identifies the value.** It models named lookup directly.
4. What should guide a structure choice? **The required operations, constraints, and clarity of the design.** A structure should serve the task rather than be selected by habit.

## Project or application

Design the data layout for a paper-based offline reading queue. List the operations it must support, such as add to end, remove the next lesson, revisit a lesson, and find a lesson by ID. Choose one or two simple structures and draw how three fictional lesson IDs move through them. State one invariant, such as “the next item is always at the front.”

## Accessibility notes

The comparison table presents one need per row and can be read without color or animation. Use the same fictional IDs throughout the exercise, and allow the learner to draw boxes and arrows instead of writing code.

## Safety and responsible use

This is an S0 conceptual lesson using fictional learning records. It does not access devices, files, accounts, or networks. In real applications, minimize retained data and document why a structure stores each field before adding personal information.

## Further reading

[An Open Guide to Data Structures and Algorithms](https://open.umn.edu/opentextbooks/textbooks/an-open-guide-to-data-structures-and-algorithms) introduces data structures, algorithms, and the trade-offs between speed, memory, and implementation complexity.

## Change log

- 1.0.0 — Initial MVP draft.
