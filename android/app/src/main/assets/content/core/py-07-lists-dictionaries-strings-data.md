---
id: py-07-lists-dictionaries-strings-data
title: Lists, dictionaries, strings, and simple data processing
strand: python-fundamentals
level: beginner
version: 1.0.0
prerequisites:
  - py-06-functions-scope-reusable-code
estimated_minutes: 75
availability: termux-optional
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Choose between a list and dictionary for a small data-recording task.
  - Use indexing, iteration, and safe string methods to transform local example data.
  - Build a small summary from a collection without changing the original unexpectedly.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - python
  - lists
  - dictionaries
  - strings
  - data
termux:
  exercise_id: py-07-local-data-summary
  fallback: Complete the data-summary table in the lesson and calculate the final counts by hand.
sources:
  - https://docs.python.org/3/tutorial/datastructures.html
  - https://docs.python.org/3/library/stdtypes.html
---

# Lists, dictionaries, strings, and simple data processing

## Objectives

By the end of this lesson, you can store a sequence in a list, look up named values in a dictionary, clean a short string, and produce a small summary using local fictional data.

## Prerequisites

Complete [PY-06](py-06-functions-scope-reusable-code.md), including parameters, return values, and local variables.

## Availability

The lesson, examples, and practice are fully offline. The optional Termux exercise prints a fixed summary from fictional values, uses no files or network, and is not required for completion.

## Explanation

A **list** stores an ordered sequence of values. It is useful when position and iteration matter, such as a short sequence of study topics. List indexes start at zero, so the first item is at index `0`. Lists are mutable: methods such as `append()` change the list. A slice such as `topics[:2]` creates a selected part without changing the original list.

A **dictionary** stores key-value pairs. It is useful when a name should identify a value, such as `{"topic": "loops", "minutes": 20}`. Keys are unique within one dictionary. `record.get("minutes")` is a safe way to ask for a possibly missing key without immediately raising `KeyError`.

A **string** is a sequence of text characters. Methods such as `strip()`, `lower()`, and `split()` can clean or divide local text. Processing usually follows a repeatable pattern: inspect the input, transform one item, and add the result to a new collection or counter. Creating a new result is often easier to reason about than changing a collection while iterating over it.

## Worked example

This function counts fictional study labels after removing surrounding spaces and ignoring case:

```python
def count_labels(labels):
    counts = {}
    for label in labels:
        clean = label.strip().lower()
        counts[clean] = counts.get(clean, 0) + 1
    return counts

labels = ["Python", " python ", "Algorithms"]
print(count_labels(labels))
```

The trace is:

| Input item | Clean value | Previous count | New count |
|---|---|---:|---:|
| `"Python"` | `python` | 0 | 1 |
| `" python "` | `python` | 1 | 2 |
| `"Algorithms"` | `algorithms` | 0 | 1 |

The result is `{'python': 2, 'algorithms': 1}`. The original `labels` list remains available, while `counts` is a separate summary dictionary.

## Common mistakes

Remember that list indexing starts at zero, and an index outside the list raises `IndexError`. A dictionary key is not the same thing as a list position. Calling `record["missing"]` can raise `KeyError`; use `get()` when absence is expected. String methods usually return a new string rather than changing the original. Do not modify a list while iterating over it unless the effect and ordering have been deliberately checked; prefer a new result list for beginner exercises.

## Offline practice

Use this local, fictional data:

```python
minutes = [15, 20, 15, 30]
status = {"reviewed": 3, "remaining": 1}
labels = [" short ", "LONG", "short"]
```

1. What is `minutes[0]`, and what is `sum(minutes)`?
2. What value does `status.get("reviewed")` return? What does `status.get("missing", 0)` return?
3. Write the cleaned version of each string in `labels` after `strip().lower()`.
4. Design a function named `summarize_minutes` that returns a dictionary with `count` and `total`. State the expected result for `minutes` without running code.

Keep all practice values fictional and local.

## Knowledge check

1. When is a list a good choice? **When values form an ordered sequence that you will access or iterate over.** A list keeps order and supports numeric indexing.
2. When is a dictionary a good choice? **When named keys should look up associated values.** The key expresses what the value means.
3. What does `strip()` usually do to a string? **It returns a copy with surrounding whitespace removed.** The original string is not changed because strings are immutable.
4. Why use `get("missing", 0)`? **It supplies a safe default when a key may not exist.** Direct indexing would raise `KeyError` for a missing key.

## Project or application

Create a paper data card for three fictional lessons. Each card has a title, minutes, and status. Decide which parts belong in a dictionary and which belong in a list. Then write pseudocode that counts how many cards have each status and calculates total minutes. Include one empty-list case and explain the expected result.

## Accessibility notes

The tables show transformations one row at a time and can be read linearly. Use clear labels for keys and values, avoid relying on color to distinguish data, and complete the exercise with a paper table or enlarged text if code is difficult to read.

## Safety and responsible use

This is an S1 local programming lesson. The optional exercise uses only fixed fictional values and does not read files, access shared storage, send data, or use a network. Do not copy private contact lists or account data into practice dictionaries. Review what a program reads and retains before processing real information.

## Further reading

The [Python data-structures tutorial](https://docs.python.org/3/tutorial/datastructures.html) covers lists, dictionaries, comprehensions, and looping techniques. The [Python built-in types reference](https://docs.python.org/3/library/stdtypes.html) documents string and collection behavior.

## Change log

- 1.0.0 — Initial MVP draft.
