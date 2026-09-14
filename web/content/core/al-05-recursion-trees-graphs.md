---
id: al-05-recursion-trees-graphs
title: Recursion, trees, and graph vocabulary
strand: algorithms
level: beginner
version: 1.0.0
prerequisites:
  - al-04-complexity-growth
estimated_minutes: 75
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain the base case and smaller subproblem in a recursive description.
  - Identify nodes, edges, roots, leaves, and paths in simple trees and graphs.
  - Trace a finite traversal while recording visited items and avoiding cycles.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - algorithms
  - recursion
  - trees
  - graphs
sources:
  - https://open.umn.edu/opentextbooks/textbooks/an-open-guide-to-data-structures-and-algorithms
  - https://docs.python.org/3/tutorial/controlflow.html
---

# Recursion, trees, and graph vocabulary

## Objectives

By the end of this lesson, you can identify the parts of a tree or graph, describe a recursive process with a base case, and trace a finite traversal without revisiting a node forever.

## Prerequisites

Complete [AL-04](al-04-complexity-growth.md), including growth patterns, rough operation counts, and the need for explicit stopping rules.

## Availability

This lesson and every exercise are fully offline. No programming runtime or network is needed.

## Explanation

**Recursion** is a way to describe a problem using a smaller version of the same problem. A recursive function needs two parts: a **base case** that can be answered directly and a recursive step that moves toward that base case. Without progress toward the base case, a call can continue until a runtime limit or other failure is reached.

A **tree** is a connected structure with a hierarchy. Its **root** is the starting node, a parent can have children, and a **leaf** has no children. A path is a sequence of connected nodes. A **graph** is a collection of nodes (also called vertices) and edges that describe relationships. Graphs may have branches, cycles, or disconnected parts, so a traversal usually records which nodes it has visited.

Trees are a special mental model for hierarchical information such as folders or lesson prerequisites. Graphs are useful for general relationships such as roads or links between concepts. These are models: the safety and correctness of an application still depend on what data it stores and who can access it.

## Worked example

Recursive countdown pseudocode:

```text
count_down(n):
    if n == 0:
        return "done"
    print(n)
    return count_down(n - 1)
```

For `count_down(3)`:

| Call | Test | Action | Next call |
|---:|---|---|---|
| 3 | `3 == 0` false | print `3` | `count_down(2)` |
| 2 | `2 == 0` false | print `2` | `count_down(1)` |
| 1 | `1 == 0` false | print `1` | `count_down(0)` |
| 0 | true | return `done` | none |

Now consider this tree:

```text
        Study
       /     \
   Python   Algorithms
     /          \
  Loops        Search
```

`Study` is the root. `Python` and `Algorithms` are its children. `Loops` and `Search` are leaves. A graph could connect `Loops` back to `Search` or add another relationship; a traversal would need a visited set to avoid following a cycle forever.

## Common mistakes

The most serious recursion mistake is omitting a base case or failing to make the input smaller. Another is confusing a tree’s parent-child direction with an arbitrary graph relationship. A graph traversal can revisit a node through a cycle unless it records visited nodes. Finally, drawing an arrow does not prove that a relationship is two-way; direction must be stated.

## Offline practice

1. Trace the recursive function for `count_down(2)` and write the order of printed values.
2. Draw a three-level tree for fictional topics and label the root, one parent, and two leaves.
3. Draw a graph with nodes `A`, `B`, `C`, and `D`, including a cycle `A → B → C → A`. Starting at `A`, list the nodes visited if a traversal skips any already-visited node.

State the base case and the progress rule for every recursive design. Do not run a recursive process with unknown depth.

## Knowledge check

1. What is a base case? **A directly answerable stopping case in a recursive definition.** It prevents every call from creating another call.
2. What makes recursion move toward completion? **Each recursive step creates a smaller or simpler subproblem.** The process must eventually reach the base case.
3. What is a leaf in a tree? **A node with no children.** It can still be connected to its parent.
4. Why track visited nodes in a graph traversal? **To avoid processing a cycle forever.** A graph can contain a path back to a node already seen.

## Project or application

Design a paper map of five fictional learning topics. Use a tree for prerequisite hierarchy and a graph for cross-topic relationships. Mark the root, leaves, edges, and one path. Write a finite traversal order and a rule for handling a repeated topic.

## Accessibility notes

The tree and graph can be represented with labeled cards and tactile strings rather than relying on a visual diagram. Describe every relationship in words, identify arrows as directional when needed, and provide the recursion trace as a linear table.

## Safety and responsible use

This is an S0 offline reasoning lesson using fictional topics. It does not access live maps, accounts, or services. When representing real people or relationships, minimize identifying details and check access controls before sharing a graph or diagram.

## Further reading

[An Open Guide to Data Structures and Algorithms](https://open.umn.edu/opentextbooks/textbooks/an-open-guide-to-data-structures-and-algorithms) includes introductory material on recursion, trees, and graphs. The [Python control-flow tutorial](https://docs.python.org/3/tutorial/controlflow.html) explains that recursive calls create a new local symbol table for each call.

## Change log

- 1.0.0 — Initial MVP draft.
