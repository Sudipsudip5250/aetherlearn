---
id: dl-02-files-folders-storage-backups
title: Files, folders, storage, and backups
strand: digital-literacy
level: beginner
version: 1.0.0
prerequisites:
  - dl-01-digital-information
estimated_minutes: 40
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Distinguish a file, a folder, device storage, and a backup.
  - Choose a safe local action for organizing or protecting fictional files.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - digital-literacy
  - file-management
  - backups
sources:
  - https://developer.android.com/training/data-storage
  - https://support.google.com/android/answer/2819582?hl=en
---

# Files, folders, storage, and backups

## Objectives

By the end of this lesson, you can tell a file from a folder, describe why storage location matters, and explain why a backup is a separate recoverable copy.

## Prerequisites

Complete [DL-01](dl-01-digital-information.md), or be comfortable thinking about stored digital information as patterns interpreted by software.

## Availability

This lesson is fully offline. It uses fictional filenames and does not ask you to open, move, delete, or upload a real file.

## Explanation

A **file** is a named piece of stored information, such as a photograph, text document, or saved configuration. A **folder** is a container that organizes files and sometimes other folders. A folder is not the same thing as the file inside it: renaming a folder changes organization, not the contents of every file.

**Storage** is the space where information is kept for later use. A phone may provide app-specific storage, shared files, removable storage, or other areas depending on its operating system. The useful questions are both “where is the file?” and “which app or user can access it?”

A **backup** is an additional copy kept so information can be recovered after loss, damage, accidental deletion, or a device change. A second copy in the same fragile location is not strong protection. A useful backup has a known location, a date or version, and a way to restore or check it. Cloud backup can be convenient, but it sends data to a service and is therefore also a privacy and account decision.

Organizing and protecting are different actions. Renaming `notes-june.txt` changes its label. Copying it to a separate location creates another copy. Moving it changes its location. Deleting it removes or marks the original for removal. Pause before destructive actions, especially when a filename or folder is unfamiliar.

## Worked example

Imagine a fictional phone with this structure:

```text
Study/
  python-notes.txt
  diagrams/
    bits.png
  backup-2026-08/
    python-notes-copy.txt
```

`python-notes.txt` is a file. `Study/` and `diagrams/` are folders. `python-notes-copy.txt` is a separate copy whose folder records when it was made. The structure does not prove that the copy is current or restorable, so a careful learner also records what was copied and when.

## Common mistakes

A common mistake is calling every location a file. Another is assuming that renaming changes the information inside a file. Synchronization is also not always the same as backup: synchronization may copy an unwanted deletion, while a backup is intended to preserve a recoverable version. Finally, an unchecked backup may fail when it is needed.

## Offline practice

Use the fictional structure above. Label each item as a file or folder. Then write three safe actions for `python-notes.txt`: one that changes its name, one that creates a second copy, and one that changes only its folder. Finish with one question you would ask before deleting the original.

## Knowledge check

1. What is the difference between a file and a folder? **A file contains data; a folder organizes entries.** They have different roles in storage.
2. What makes a backup useful? **A separate, recoverable copy with a known location or version.** A duplicate that cannot be found or restored is not useful protection.
3. Does renaming a file normally change its contents? **No, it changes the file name.** Renaming changes identification, not the stored content.

## Project or application

Create a paper inventory for a fictional study folder. Include five filenames, two subfolders, and a backup date. For every item, record whether it is a file or folder and write one non-destructive action. Do not include real names, addresses, credentials, or personal documents.

## Accessibility notes

The lesson uses text labels and an indented tree rather than relying on color or icons. The tree can be read line by line, and the exercise can be completed on paper or with enlarged text.

## Safety and responsible use

This is an S0 foundation lesson. Examples are fictional and do not require device storage, cloud accounts, personal files, or destructive operations. When practicing on a real device later, confirm the target, prefer reversible actions, and understand the destination before copying private data.

## Further reading

The [Android data and file storage overview](https://developer.android.com/training/data-storage) describes app-specific, shared, preference, and database storage. Google’s [Android backup and restore guidance](https://support.google.com/android/answer/2819582?hl=en) explains that backup behavior varies by device and Android version.

## Change log

- 1.0.0 — Initial MVP draft.
