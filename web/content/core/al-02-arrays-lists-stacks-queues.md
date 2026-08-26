---
id: al-02-arrays-lists-stacks-queues
title: Arrays, lists, stacks, and queues
strand: algorithms
level: beginner
version: 1.0.0
prerequisites:
  - al-01-data-structures
estimated_minutes: 65
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Distinguish position-based sequences from stack and queue access rules.
  - Trace push, pop, enqueue, and dequeue operations on a small structure.
  - Select a structure that preserves the order required by a fictional task.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - algorithms
  - arrays
  - stacks
  - queues
sources:
  - https://open.umn.edu/opentextbooks/textbooks/an-open-guide-to-data-structures-and-algorithms
  - https://docs.python.org/3/tutorial/datastructures.html
---

# Arrays, lists, stacks, and queues

## Objectives

By the end of this lesson, you can compare position-based sequences with stacks and queues, trace their core operations, and choose an order-preserving structure for a small problem.

## Prerequisites

Complete [AL-01](al-01-data-structures.md), including operations, trade-offs, and invariants.

## Availability

This lesson and every exercise are fully offline. No programming runtime or network is needed.

## Explanation

An **array** is a sequence whose elements are stored at positions, often with a fixed or predictable layout. A Python list provides a flexible sequence interface: it keeps order, supports zero-based indexing, and can grow. For this lesson, focus on the access rule rather than the details of a particular language implementation.

A **stack** follows last-in, first-out (LIFO). Add an item with **push** and remove the most recently added item with **pop**. Browser Back history and nested work often resemble a stack. A **queue** follows first-in, first-out (FIFO). Add an item at the back with **enqueue** and remove the oldest item from the front with **dequeue**. A waiting line is a useful mental model.

Each structure has an invariant: a rule that should remain true after every operation. For a stack, only the top item is removed. For a queue, the oldest waiting item leaves first. Naming the invariant before coding makes an incorrect operation easier to notice.

## Worked example

Trace a stack and a queue separately:

```text
stack = []
push A, push B, pop, push C

queue = []
enqueue A, enqueue B, dequeue, enqueue C
```

| Step | Stack after operation | Queue after operation |
|---:|---|---|
| 1 | `[A]` | `[]` |
| 2 | `[A, B]` | `[]` |
| 3 | `[A]`, removed `B` | `[]` |
| 4 | `[A, C]` | `[]` |
| 5 | `[A, C]` | `[A]` |
| 6 | `[A, C]` | `[A, B]` |
| 7 | `[A, C]` | `[B]`, removed `A` |
| 8 | `[A, C]` | `[B, C]` |

The stack’s next removal would be `C`; the queue’s next removal would be `B`. The structures contain similar labels but answer “who leaves next?” differently.

## Common mistakes

A common mistake is popping the oldest stack item or dequeuing the newest queue item. Another is using the word “array” as if it always means a stack or queue; an array describes position-based storage, while stack and queue describe access discipline. Empty-structure operations need a defined response rather than an assumed item. Also, drawing only the final state can hide a wrong middle operation, so trace one row per step.

## Offline practice

Start with empty structures and trace these operations:

```text
stack: push 1, push 2, push 3, pop, push 4, pop
queue: enqueue 1, enqueue 2, enqueue 3, dequeue, enqueue 4, dequeue
```

Write the removed value and final contents for each. Then answer: which structure would you choose for an undo history, and which for print jobs that should be handled in arrival order? Explain the invariant in one sentence for each choice.

## Knowledge check

1. What rule does a stack follow? **Last-in, first-out.** The most recently pushed item is removed first.
2. What rule does a queue follow? **First-in, first-out.** The earliest enqueued item is removed first.
3. Why trace every operation? **The order of intermediate states can reveal an incorrect operation.** The final contents alone may not show where the mistake occurred.
4. What is an invariant? **A rule that should remain true after each valid operation.** It helps test whether a design is still behaving as intended.

## Project or application

Design a paper “offline task desk” for four fictional tasks. Choose a queue for incoming work and a stack for a temporary undo history. Draw the state after each add and removal, and write the invariant beside each drawing. Include a rule for what happens when either structure is empty.

## Accessibility notes

The trace table separates each state and removed item into readable columns. Learners can use labeled cards or sticky notes instead of code, and the operation words should be spoken as “add,” “remove newest,” “add to back,” and “remove oldest” before using the shorter names.

## Safety and responsible use

This is an S0 offline reasoning lesson using fictional labels. It does not access real queues, accounts, devices, or services. When modeling real work, avoid placing personal or sensitive details on shared cards or screenshots.

## Further reading

The [Python data-structures tutorial](https://docs.python.org/3/tutorial/datastructures.html) shows lists used as stacks and explains why a `deque` is suitable for queues. [An Open Guide to Data Structures and Algorithms](https://open.umn.edu/opentextbooks/textbooks/an-open-guide-to-data-structures-and-algorithms) provides broader coverage of stacks and queues.

## Change log

- 1.0.0 — Initial MVP draft.
