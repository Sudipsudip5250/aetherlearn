---
id: dev-02-git-local-repositories-history
title: Git concepts, local repositories, and useful history
strand: developer-foundations
level: beginner
version: 1.0.0
prerequisites:
  - dev-01-terminal-command-line
estimated_minutes: 70
availability: termux-optional
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain the difference between a working folder, the staging area, and a Git commit.
  - Trace a small local change from editing through status, staging, and history.
  - Use descriptive history questions without changing a repository unexpectedly.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - git
  - version-control
  - history
  - developer-foundations
termux:
  exercise_id: dev-02-local-git-status
  fallback: Complete the local repository state table in the lesson using paper labels; do not run commands in a real repository.
sources:
  - https://git-scm.com/docs/gittutorial
  - https://docs.github.com/en/get-started/git-basics
---

# Git concepts, local repositories, and useful history

## Objectives

By the end of this lesson, you can describe the main local Git states, trace a small change into a commit, and choose read-only history commands for answering basic questions.

## Prerequisites

Complete [DEV-01](dev-01-terminal-command-line.md), including paths, working directories, and the difference between viewing and changing a command-line environment.

## Availability

The lesson and in-app practice are offline. The optional Termux exercise reads status from a dedicated local practice directory, uses no network or shared storage, and is not required for completion. The examples assume a recent Git installation but do not require one for the written exercise.

## Explanation

**Git** is a version-control system that records snapshots of a project and the relationships between those snapshots. A **repository** contains the project files and Git’s local history. The **working tree** is the version of files currently visible for editing. The **staging area**, also called the index, is where you prepare the exact changes for the next snapshot. A **commit** is a named snapshot with a message and a parent relationship in the history.

A careful local workflow is: inspect status, edit a small change, inspect the diff, stage only the intended files, inspect the staged diff, and commit with a meaningful message. `git log` reads history, while `git show` reads a chosen commit. These commands help answer “what changed?” without changing the project.

Git does not make a bad change safe merely by recording it. A commit can contain a secret, an accidental large file, or a misleading message. Review the diff and avoid staging credentials, private exports, generated clutter, or files outside the intended project.

## Worked example

Imagine a fictional repository with one file, `notes.md`:

| Stage | Repository state | Learner question |
|---|---|---|
| 1 | Working tree matches the latest commit | Is anything changed? |
| 2 | `notes.md` edited but unstaged | What does `git diff` show? |
| 3 | `notes.md` staged | What exact change will the next commit contain? |
| 4 | Commit created | What message explains the snapshot? |
| 5 | Later history exists | What did the project look like before? |

A cautious command sequence for a local practice repository is:

```text
git status
git diff
git add notes.md
git diff --cached
git commit -m "Describe the notes update"
git log --oneline
```

The commands are shown for understanding. Never run `git add` or `git commit` in a real project until the path and staged diff have been checked.

## Common mistakes

A common mistake is assuming that editing a file automatically puts it in the next commit. Another is staging everything with a broad command without reviewing the file list. `git diff` and `git diff --cached` answer different questions, and a commit message should describe the change rather than merely say “update.” Do not confuse a local commit with publishing: sharing with a remote repository is a separate action that should be deliberate.

## Offline practice

Use four paper cards labeled `working tree`, `staged`, `commit`, and `history`. Start with `notes.md` in the commit card. Then simulate:

1. Edit one line and move a copy of the change to the working-tree card.
2. Inspect the difference and stage only that line.
3. Write a commit message that explains the change.
4. Add the snapshot to the history card.
5. Add an unrelated fictional secret on a separate card and decide why it must not be staged.

Write the read-only question answered by `status`, `diff`, `diff --cached`, and `log`.

## Knowledge check

1. What is the staging area for? **Preparing the exact changes for the next commit.** It lets a learner review the intended snapshot before recording it.
2. What does a commit represent? **A recorded project snapshot with history metadata and a message.** It is not automatically published anywhere.
3. What does `git diff --cached` help inspect? **Changes already staged for the next commit.** It is different from the unstaged working-tree diff.
4. Why review before committing? **To avoid recording unintended files, secrets, or unrelated changes.** Version control preserves mistakes as well as good work.

## Project or application

Create a paper history for a fictional lesson repository with three snapshots: initial notes, a corrected example, and an accessibility improvement. For each snapshot, write a short message, the changed file, and one question that `git show` could answer. Mark which files must remain local and uncommitted.

## Accessibility notes

The workflow is represented as a sequence of labeled states and can be read linearly. Explain “working tree,” “staged,” and “commit” before using their Git names, and permit paper cards or a spoken diff review instead of terminal interaction.

## Safety and responsible use

This is an S1 local developer lesson. The optional exercise reads a dedicated local directory and changes no files. Never stage passwords, tokens, private notes, or exported learning data. Do not use broad destructive commands such as reset or clean until their effects and recovery options are understood.

## Further reading

The [Git tutorial](https://git-scm.com/docs/gittutorial) explains repositories, the index, commits, status, and history. [Git basics on GitHub Docs](https://docs.github.com/en/get-started/git-basics) provides introductory guidance for local Git and remote-repository concepts.

## Change log

- 1.0.0 — Initial MVP draft.
