---
id: dev-06-dependency-provenance-builds
title: Dependencies, provenance, and reproducible builds
strand: developer-foundations
level: beginner
version: 1.0.0
prerequisites:
  - dev-02-git-local-repositories-history
  - dev-03-debugging-error-messages
estimated_minutes: 65
availability: offline
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain why a build record should identify important inputs and the produced artifact.
  - Distinguish a checksum from a statement about who or what produced an artifact.
  - Read a small fictional provenance record and identify missing verification information.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can describe why software builds need traceable inputs, compare a checksum with provenance, and identify questions that a reviewer should ask before trusting a local artifact.

## Prerequisites

You should understand commits, file hashes, and error evidence from DEV-02 and DEV-03. No package manager, build service, account, network connection, or downloaded artifact is required.

## Availability

This lesson is fully offline. It uses a fictional build manifest and paper verification table. The app never executes a dependency or builds learner-provided code.

## Explanation

A software artifact is an output such as an application package, archive, or library. A build has inputs: source files, configuration, tools, and dependencies. If a later reviewer cannot tell which inputs were used, reproducing or investigating the output becomes harder.

**Provenance** is a record about where, when, and how an artifact was produced. The SLSA Build Provenance specification describes provenance as verifiable information about software production and models inputs, resolved dependencies, the builder, and output subjects.[1] Provenance is evidence to inspect; it is not a magic label that makes an artifact safe.

A **checksum** is a digest calculated from bytes. If the same file is hashed with the same algorithm, the digest can show whether the bytes changed. A checksum does not, by itself, say who created the file, whether the source was reviewed, whether the build environment was honest, or whether the contents are appropriate. A **signature** can bind a statement to a signing identity, but a reviewer still needs to decide whether that identity and statement are trustworthy.

A useful verification record keeps these questions separate:

| Question | Evidence that may help | What it does not prove alone |
|---|---|---|
| Are these the expected bytes? | SHA-256 or another declared digest | That the bytes are good or approved |
| What source was used? | Commit identifier or immutable source reference | That the source has no defects |
| What dependencies were resolved? | Locked versions and dependency digests | That every dependency is harmless |
| Which builder produced it? | Builder identity and build record | That the builder was configured correctly |
| Was the change reviewed? | Review record, checks, and decision | That future changes will be reviewed |

A reproducible-build goal means that people can rebuild from sufficiently specified inputs and compare outputs. Reproducibility can expose unexpected differences, but it depends on recording inputs, tool versions, configuration, timestamps or other variable data, and the exact comparison method. Do not promise bit-for-bit equality when a project has not defined it.

## Worked example

Consider this fictional local manifest for `study-card-1.2.0.zip`:

```text
artifact: study-card-1.2.0.zip
sha256: 1111111111111111111111111111111111111111111111111111111111111111
source_commit: 7f00-example
builder: local-review-script
resolved_dependency: cards-format 2.1.0
```

The manifest gives a byte digest, a source label, a builder label, and one dependency label. A careful reviewer still asks: Is the digest calculated over the file that was received? Is `7f00-example` an immutable source reference? Where is the dependency’s own record? Is `local-review-script` a known builder? Was the manifest itself protected from accidental editing? The correct response is to record the questions, not to invent answers.

## Common mistakes

Do not call a checksum a signature or provenance. Do not assume a familiar package name identifies the exact bytes used. Do not copy a lock file from an untrusted source and treat it as independent evidence. Do not install a dependency to “see whether it works” in this lesson. Do not claim that reproducible output proves secure source or secure behavior. Finally, do not publish a private path, token, or internal build log when a redacted fictional record is enough.

## Offline practice

Create a verification table for a fictional `notes-export-1.0.0.zip`. Include an artifact name, a fictional SHA-256 digest, source revision, builder description, two resolved dependencies, and an expected output path. Add one column called **unanswered question** and write at least three questions a reviewer should resolve before accepting the record.

Then mark each field as **byte identity**, **source identity**, **build context**, **dependency context**, or **missing evidence**. Do not calculate a real digest or download any dependency.

## Knowledge check

1. What does a checksum primarily tell you? **Whether bytes produce the expected digest under the declared algorithm.** It does not identify the producer or prove the contents are safe.
2. What is provenance? **Verifiable information about where, when, and how an artifact was produced.** It can include source, dependencies, builder, and output information.
3. Why record resolved dependencies? **So a reviewer can see important build inputs and investigate or reproduce the artifact more precisely.** A package name without an exact version or digest may be insufficient.
4. Does reproducible output prove that a build is secure? **No.** It can provide useful evidence about repeatability, but source review, builder trust, dependency review, and other controls remain necessary.

## Project or application

Design a one-page “artifact evidence card” for the fictional AetherLearn sample pack. Include the archive name, schema version, module count, checksum field, source revision field, builder field, review field, and three explicit limitations. Keep the digest fictional and label the card as an educational example.

## Accessibility notes

The lesson separates concepts in a labeled table and uses fixed-width text only for a short fictional record. Learners can complete the verification card in plain text, large print, speech-to-text, or a paper worksheet. No color, animation, timed response, or tool installation is required.

## Safety and responsible use

This is an S1 offline supply-chain literacy lesson. Use only fictional manifests and local text. Never execute, install, or upload a dependency because a manifest mentions it. Never expose credentials, private repository URLs, internal logs, or personal data. A provenance record supports investigation and verification; it does not authorize access to a system or imply that an artifact is safe without human review.

## Further reading

The [SLSA Build Provenance v1.2 specification](https://slsa.dev/spec/v1.2/build-provenance) defines provenance fields and explains how build inputs, resolved dependencies, builders, and artifact subjects support verification. The repository release handoff separately documents checksum practice without creating a signing key; this offline lesson does not depend on that local file.

## Change log

- 1.0.0 — Initial Stage 2 draft.
