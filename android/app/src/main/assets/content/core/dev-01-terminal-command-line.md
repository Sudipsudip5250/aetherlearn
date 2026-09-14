---
id: dev-01-terminal-command-line
title: The terminal and command-line mental model
strand: developer-foundations
level: beginner
version: 1.0.0
prerequisites:
  - dl-02-files-folders-storage-backups
  - py-02-python-setup-expressions-values
estimated_minutes: 55
availability: termux-optional
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain the relationship between a terminal, a shell, a command, an argument, and a working directory.
  - Read a safe local command and predict which local information it will display or change.
review_status: draft
last_reviewed: 2026-08-24
tags:
  - terminal
  - developer-tools
  - termux
termux:
  exercise_id: dev-01-safe-navigation
  fallback: Use the in-app terminal simulator to inspect a fictional directory tree and predict command results.
sources:
  - https://termux.dev/en/
---

# The terminal and command-line mental model

## Objectives

By the end of this lesson, you can describe the main parts of a command-line session, distinguish a command from its arguments, and decide whether a proposed beginner command is appropriate for a local practice directory.

## Prerequisites

Complete DL-02 and PY-02. Termux is optional.

## Availability

The lesson, simulator, and safety checks are offline. The optional practical uses a local Termux directory. It does not require package installation, shared storage, network access, SSH, or elevated privileges.

## Explanation

A terminal is an interface for interacting with a computer through text. A shell reads a command line and asks the operating system or another program to perform an action. The command is the program or built-in operation being requested. Arguments provide additional values, such as a filename or a directory. The working directory is the location that relative paths are interpreted from.

A safe mental model is to identify the command, every argument, the working directory, and the expected effect before pressing Enter. Start with read-only inspection in a dedicated practice directory. Avoid commands that delete, overwrite, change permissions, or access shared storage until you understand their consequences.

Termux provides a local terminal environment on Android. Its exact behavior and integration permissions depend on the installed version and the user’s device. AetherLearn’s optional handoff therefore shows the intended wrapper and prerequisites, asks for confirmation, and always provides an in-app alternative.

## Worked example

In a fictional practice directory, `pwd` asks the shell to print the current working directory. `ls` asks for a listing of that directory. `cd notes` changes the shell’s working directory to a child directory named `notes`. Before running `cd notes`, check that the directory exists and that the exercise is using the intended local practice folder.

## Common mistakes

A terminal window is not itself the shell, and a shell prompt is not proof that a command is safe. Another mistake is assuming that a relative path starts in the device’s shared storage. The working directory determines what the path means, so inspect it first and use a dedicated practice directory.

## Offline practice

Use the simulator’s fictional tree: `/practice`, containing `notes/`, `examples/`, and `README.txt`. Predict the output category for `pwd`, `ls`, `cd notes`, and `ls` after changing directories. Then mark which of these commands only reads or navigates and which could change data.

## Knowledge check

1. What does a working directory do? **It provides the starting location for relative paths and commands that operate on the current directory.**
2. What is an argument? **An additional value supplied to a command**, such as a filename or directory name.
3. What should you do before running an unfamiliar command? **Read the command, arguments, working directory, and expected effects; then use a safe local alternative or do not run it.**

## Project or application

Create a fictional practice tree for a small Python project and write a safe inspection sequence: show the current directory, list its files, and read a README. Do not use destructive commands. Optionally repeat the exercise inside a dedicated local Termux directory after reviewing the exact wrapper information.

## Accessibility notes

The simulator provides text descriptions for directory changes and command effects. Users may complete the exercise with a keyboard or touch input. The lesson does not rely on timed typing, color, or audio.

## Safety and responsible use

This is an S1 local developer-tools lesson. The optional Termux exercise is restricted to read-only navigation and inspection in a user-controlled practice directory. It does not install packages, access shared storage, use network tools, connect to SSH, or target another system.

## Further reading

Visit the [Termux project website](https://termux.dev/en/) for project information. Use the in-app setup guidance before attempting any optional local exercise.

## Change log

- 1.0.0 — Initial MVP draft.
