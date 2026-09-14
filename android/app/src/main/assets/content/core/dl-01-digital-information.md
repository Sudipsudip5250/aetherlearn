---
id: dl-01-digital-information
title: How digital devices represent information
strand: digital-literacy
level: beginner
version: 1.0.0
prerequisites: []
estimated_minutes: 35
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain that digital devices represent information using discrete states.
  - Distinguish the meaning of information from the physical representation used to store it.
review_status: draft
last_reviewed: 2026-08-24
tags:
  - foundations
  - digital-literacy
sources:
  - https://www.nist.gov/glossary-term/bit
---

# How digital devices represent information

## Objectives

By the end of this lesson, you can explain what a bit represents, describe why groups of bits can represent many values, and connect text, images, and sound to stored digital representations.

## Prerequisites

No previous computing experience is required. You only need to be comfortable comparing quantities and following a short example.

## Availability

This lesson is fully offline. The exercises use only the text included in the core content pack.

## Explanation

A digital device stores and processes information using distinguishable states. A **bit** is a small unit that can be in one of two states, commonly written as 0 or 1. A group of bits can represent more than two possibilities: two bits have four possible patterns, three bits have eight, and so on.

The pattern is not the meaning by itself. A device and a program agree on a rule for interpreting the pattern. The same number can represent a color channel, a character, a length, or part of a sound sample depending on the format. This is why file formats and encoding rules matter.

Text is represented through agreed character encodings. Images are represented through pixels and color values. Audio is represented through measurements taken over time. In each case, the device stores patterns, while software supplies the rules that turn those patterns into something meaningful to a person.

## Worked example

Suppose a toy system uses three bits to represent a shelf number. The patterns `000` through `111` represent shelf numbers 0 through 7. The pattern `101` has the numerical value 5 in this rule. If the same pattern is used in a different format, it could represent a color component or a character instead.

## Common mistakes

A bit is not the same as a byte. A byte is a group of bits, while a bit has only two possible states. Another common mistake is assuming that a stored pattern has one universal meaning. Meaning comes from the agreed encoding or file format.

## Offline practice

Write the eight three-bit patterns in order from `000` to `111`. Then circle the pattern that represents 6 in the toy shelf-number system. Finally, invent a different meaning for the pattern `101` and explain why the pattern itself did not change.

## Knowledge check

1. How many patterns can two bits represent? **Four.** Each bit has two possible states, so the combinations are `00`, `01`, `10`, and `11`.
2. Can the same stored pattern have different meanings? **Yes.** The interpreting format or program determines the meaning.
3. What is one example of information represented digitally? **Text, an image, or sampled sound** are all valid examples when encoded using an agreed format.

## Project or application

Create a one-page “digital representation map” in your notes. Choose text, an image, and sound. For each, record what is measured or encoded, what the stored values might look like, and what rule a program needs in order to display or play it.

## Accessibility notes

All concepts are explained in text, and no exercise depends on color, animation, or audio. Code-like patterns are presented as text and should remain readable when enlarged.

## Safety and responsible use

This is an S0 foundation lesson. It uses fictional values and does not access files, devices, networks, or personal information.

## Further reading

The [NIST Glossary entry for bit](https://www.nist.gov/glossary-term/bit) provides a concise technical definition.

## Change log

- 1.0.0 — Initial MVP draft.
