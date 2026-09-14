---
id: py-03-variables-types-input-output
title: Variables, types, input, and output
strand: python-fundamentals
level: beginner
version: 1.0.0
prerequisites:
  - py-02-python-setup-expressions-values
estimated_minutes: 55
availability: termux-optional
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain what a variable name refers to in a simple Python program.
  - Predict output while distinguishing text input from numeric values.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - python
  - variables
  - types
  - input-output
termux:
  exercise_id: py-03-local-variables-output
  fallback: Complete the variable-tracing exercise in the lesson and record each predicted output in your notes.
sources:
  - https://docs.python.org/3/tutorial/introduction.html
  - https://docs.python.org/3/tutorial/inputoutput.html
---

# Variables, types, input, and output

## Objectives

By the end of this lesson, you can trace assignments, distinguish common Python value types, and predict what a short program prints after receiving fictional input.

## Prerequisites

Complete [PY-02](py-02-python-setup-expressions-values.md). Termux is optional; the core lesson and practice can be completed in the app or on paper.

## Availability

The lesson and in-app practice are fully offline. The optional Termux exercise uses a fixed local script, does not need a network, and is not required for completion.

## Explanation

A **variable** is a name that refers to a value in a program. In `minutes = 25`, the expression on the right is evaluated and the name `minutes` is assigned to that value. A later assignment can make the same name refer to a different value. The equals sign in an assignment is an instruction to store a reference, not a claim that two sides are being tested for equality.

A value has a **type**, which describes what kind of value it is and which operations make sense. Beginner examples include `int` for whole numbers, `float` for numbers with a fractional part, `str` for text, and `bool` for `True` or `False`. Quotes matter: `4` is an integer, while `"4"` is text.

`input()` reads a line of user input and returns text. If a program expects a number, it can convert the text explicitly with `int()` or `float()`, after deciding how invalid input should be handled. `print()` writes values as output. Input is what a program receives; output is what it presents or records. They are related, but neither one automatically changes the other.

## Worked example

Trace this fictional program after the learner enters `30` at the prompt:

```python
name = "Ari"
minutes_text = input("Study minutes: ")
minutes = int(minutes_text)
print(name)
print(minutes + 5)
```

The input first becomes the text value `"30"`. `int()` converts that text to the integer `30`. The output is `Ari` followed by `35`. If the learner entered text that was not a valid integer, the conversion would need a separate error-handling decision; the program should not silently pretend the value was numeric.

## Common mistakes

A common mistake is assuming `input()` returns a number because the user typed digits. Another is confusing a variable name with the value currently referred to by that name. Learners may also forget that `print()` displays a value but does not permanently change the variable. Finally, changing a value’s type without an explicit conversion can produce an error or an unintended result.

## Offline practice

Complete a trace table for this program with fictional input `7`:

```python
label = "days"
text = input("How many? ")
count = int(text)
print(count)
print(label)
```

Write the type of `text`, the type of `count`, and the two output lines. Then predict what would happen if the input were `seven` and explain why.

## Knowledge check

1. What does a variable name refer to? **A value used by the program.** Assignment makes a name refer to the evaluated value.
2. What type does `input()` return before conversion? **Text, or `str`.** Digits typed by a user are still text until converted.
3. What does `print()` do? **It writes values as output.** It presents information and does not by itself change the stored variable.

## Project or application

Design a paper “study session receipt.” Choose fictional values for a learner name and planned minutes, show the variable assignments, and write the output lines that a program would print. Include one invalid input and describe a friendly message rather than running code against real personal data.

## Accessibility notes

Code is short, line-numberable, and paired with a trace table that can be read without color or animation. The practice can be completed with a text editor, paper, or enlarged system text.

## Safety and responsible use

This is an S1 local programming lesson. The optional exercise uses fixed fictional values and does not access files, credentials, shared storage, or a network. Do not paste personal information into an unknown interpreter or script.

## Further reading

The [Python tutorial introduction](https://docs.python.org/3/tutorial/introduction.html) introduces assignment, types, strings, `input()`, and `print()`. Python’s [input and output chapter](https://docs.python.org/3/tutorial/inputoutput.html) explains ways to present values and work with text files.

## Change log

- 1.0.0 — Initial MVP draft.
