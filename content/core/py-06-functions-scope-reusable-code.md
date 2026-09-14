---
id: py-06-functions-scope-reusable-code
title: Functions, scope, and reusable code
strand: python-fundamentals
level: beginner
version: 1.0.0
prerequisites:
  - py-05-loops-repetition-tracing
estimated_minutes: 65
availability: termux-optional
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Define and call a small Python function with clear inputs and output.
  - Explain how local scope keeps a function’s working names separate from its caller.
  - Trace a function call and test a return value with a small example.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - python
  - functions
  - scope
  - reuse
termux:
  exercise_id: py-06-local-functions
  fallback: Complete the function-tracing table in the lesson and write the expected return values in your notes.
sources:
  - https://docs.python.org/3/tutorial/controlflow.html
  - https://peps.python.org/pep-0257/
---

# Functions, scope, and reusable code

## Objectives

By the end of this lesson, you can define a small function, pass it an argument, return a result, and explain why a local variable does not automatically change a caller’s variable.

## Prerequisites

Complete [PY-05](py-05-loops-repetition-tracing.md), including bounded loops, conditions, and trace tables.

## Availability

The lesson, examples, and practice are fully offline. The optional Termux exercise runs one fixed Python expression locally, uses no files or network, and is not required for completion.

## Explanation

A **function** is a named, reusable block of instructions. In Python, `def` starts a function definition. The indented lines form its body. A function can receive **parameters**, which are names for values supplied by the caller, and can send a result back with `return`.

A function call creates a new local working space. Names assigned inside the function normally belong to that function’s **local scope**. A name outside the function belongs to an outer or global scope. Keeping temporary names local makes a function easier to understand and reduces accidental changes elsewhere. A function can read an outer value in some cases, but beginners should prefer explicit parameters and return values.

Returning a value is different from printing a value. `print()` displays something for a person; `return` gives a value back to the next line of the program. A function without a `return` expression returns `None`. Small functions should have one understandable job and should be checked with ordinary example inputs, including a boundary case.

## Worked example

This function converts minutes into whole hours and leftover minutes:

```python
def split_minutes(total_minutes):
    hours = total_minutes // 60
    minutes = total_minutes % 60
    return hours, minutes

study_time = split_minutes(95)
print(study_time)
```

Trace the call with `total_minutes = 95`:

| Step | Name or expression | Result |
|---:|---|---|
| 1 | `95 // 60` | `1` |
| 2 | `95 % 60` | `35` |
| 3 | `return hours, minutes` | `(1, 35)` |
| 4 | `study_time = ...` | `study_time` refers to `(1, 35)` |

The names `hours` and `minutes` are local to the function. The caller receives the returned tuple through `study_time`. The function does not need to know the caller’s variable name.

## Common mistakes

A frequent mistake is defining a function but never calling it. Another is forgetting the colon or indentation after `def`. Printing a value and returning it are also confused: a printed value is not automatically available to the caller. Avoid hidden global state and mutable default arguments while learning; pass the needed value in and return the result out. Finally, test a function with a normal input and a boundary such as `0` before trusting it in a larger program.

## Offline practice

Trace this function by hand for `score = 7` and `score = 0`:

```python
def label_score(score):
    if score >= 5:
        label = "ready"
    else:
        label = "practice"
    return label
```

Make a table with the argument, condition result, local `label`, and returned value. Then write a second function in pseudocode named `double_minutes` that accepts one number and returns twice that number. State one test input and its expected result. Do not use personal data or a real account as test input.

## Knowledge check

1. What does `return` do? **It sends a value from the function back to its caller.** `print()` may display a value, but `return` makes the value available to the calling code.
2. What is local scope? **The set of names belonging to one function call.** A local name is normally created and used inside that function.
3. Why use a parameter instead of a hidden global value? **A parameter makes the function’s input explicit and easier to test.** The caller can see what the function needs.
4. What does a function with no returned expression produce? **`None`.** Falling off the end of a Python function also returns `None`.

## Project or application

Design a small offline study helper on paper. Define two functions in pseudocode: one that converts a number of pages into complete groups of five and a remainder, and one that labels the remainder as `"complete"` or `"continue"`. List the inputs, outputs, and two test cases for each function. Explain which names should remain local.

## Accessibility notes

The call trace presents one operation per row and can be completed without running code. Keep code examples short, use a monospaced font with adequate spacing, and read the returned value aloud separately from the printed display. The exercise works on paper or with enlarged text.

## Safety and responsible use

This is an S1 local programming lesson. The optional exercise prints fixed fictional values and does not access files, credentials, shared storage, or a network. Do not paste secrets into examples or use a function to automate changes to real data until its inputs, outputs, and stopping behavior have been reviewed.

## Further reading

The [Python control-flow tutorial](https://docs.python.org/3/tutorial/controlflow.html) documents function definitions, arguments, return values, and local symbol tables. [PEP 257](https://peps.python.org/pep-0257/) describes conventions for documenting functions with docstrings.

## Change log

- 1.0.0 — Initial MVP draft.
