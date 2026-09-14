---
id: dl-07-how-programs-run
title: How a computer runs a program
strand: digital-literacy
level: beginner
version: 1.0.0
prerequisites:
  - dl-01-digital-information
  - py-01-problems-algorithms-instructions
estimated_minutes: 55
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Trace the path from source text to a running program at a high level.
  - Distinguish an interpreter, a compiler, a process, and a file without treating them as identical.
  - Use a small execution trace to locate whether a problem is in input, translation, or program logic.
review_status: draft
last_reviewed: 2026-08-26
tags:
  - systems
  - program-execution
  - digital-literacy
sources:
  - https://docs.python.org/3/tutorial/interpreter.html
---

# How a computer runs a program

## Objectives

By the end of this lesson, you can describe the main stages between a program file and its output, distinguish common program-execution terms, and use a written trace to reason about a failure without guessing.

## Prerequisites

You should know that a program is a precise set of instructions and have seen a simple algorithm. You do not need to install Python or use a terminal for this lesson.

## Availability

This lesson is fully offline. All traces and examples are fictional and included in the core content pack.

## Explanation

A program begins as source text written in a programming language. The source text is saved in a file so that it can be read, changed, copied, and checked. A tool then processes the source so the computer can carry out its instructions.

An **interpreter** is a program that reads instructions and performs their meaning during execution. Python’s documentation describes two common interpreter modes: interactive mode, where commands are entered and executed through a prompt, and script mode, where the interpreter reads a file and runs it.[1] A **compiler** translates source into another form before execution. Some modern language systems use several stages, such as translation to an intermediate form followed by a virtual machine or just-in-time process. For a beginner, the important idea is that source text must be understood by a tool before the requested work can happen.

When a program is running, the operating system gives that running instance resources such as memory and a place to report output. That running instance is called a **process**. A process may read input, calculate values, access allowed files, and produce output. A file is stored information; a process is an active computation. Confusing the two can make errors difficult to describe.

A failure can occur at several different stages. The source may be missing or saved under an unexpected name. The interpreter or compiler may reject the syntax. The program may start but receive input of an unexpected kind. Or the instructions may be valid but produce the wrong result because the logic is incorrect. Good debugging begins by identifying the stage instead of changing many things at once.

AetherLearn does not include a general-purpose code runner. Its examples are for reading and tracing, and its optional Termux exercises use fixed, allowlisted wrappers rather than arbitrary learner commands.

## Worked example

Imagine a fictional file named `count_notes.py` containing a short word-count program. A written execution trace can be described like this:

1. The file is found and opened.
2. The interpreter reads the source text.
3. The instructions are accepted as valid Python syntax.
4. The program receives the fixed input `red blue red`.
5. The program counts three words and displays `3`.

If the file is missing, the problem is file selection. If a colon is omitted from a Python statement, the problem is syntax. If the output is `2`, the program may have a logic or input-handling error. Each diagnosis suggests a different next question.

## Common mistakes

A compiler or interpreter is not the same thing as the operating system. A source file is not the same thing as a process. A program that starts successfully is not necessarily correct. Also avoid saying that an interpreter always executes one source line at a time; implementations may translate or optimize internally while preserving the language’s intended behavior.

## Offline practice

Use this trace table:

| Stage | Observation | Your diagnosis |
|---|---|---|
| File | `count_notes.py` is not in the lesson folder | ? |
| Translation | The file is found, but a closing parenthesis is missing | ? |
| Input | The program receives the fixed text `red blue red` | ? |
| Logic | The program runs and prints `2` instead of `3` | ? |

Fill the last column with **file problem**, **syntax problem**, **input observation**, or **logic problem**. Then write one careful question you would ask at each stage. Do not run commands or download a runtime.

## Knowledge check

1. What is source code? **Human-readable text written in a programming language that describes data and instructions.**
2. What is an interpreter used for? **It reads program instructions and carries out their meaning during execution; an implementation may use internal translation or optimization.**
3. What is a process? **A running instance of a program that receives operating-system resources while it computes.**
4. If a file cannot be found, should you first rewrite the algorithm? **No. First check the file name, location, and input assumptions because the failure is at the file-selection stage.**
5. Why is output that is syntactically valid still worth checking? **A program can run successfully and still contain a logic error that produces the wrong result.**

## Project or application

Draw a four-stage “from file to result” map for a fictional calculator program. Include source file, language tool, running process, and output. Add one possible failure and one diagnostic question at each stage. Keep the map conceptual and use no personal files or real commands.

## Accessibility notes

The lesson uses a text trace and a simple table rather than animation or timing. A screen reader can read the stages in order. The practice can be completed in a notebook, through dictation, or with a plain-text editor. No color or sound is required to distinguish outcomes.

## Safety and responsible use

This is an S0 systems-literacy lesson. It intentionally avoids arbitrary code execution, shell commands, external files, network access, and personal data. Learners should not download unknown interpreters or run unfamiliar programs to complete it. The lesson’s distinction between a process and a file is educational, not permission to inspect another person’s device.

## Further reading

The [Python documentation on using the interpreter](https://docs.python.org/3/tutorial/interpreter.html) explains interactive mode, script execution, command-line arguments, and source-file encoding. It is versioned documentation, so examples should be checked against the runtime version being discussed.

## Change log

- 1.0.0 — Initial Stage 1 draft.
