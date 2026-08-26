# Security Policy

## Scope

AetherLearn is currently a documentation and content-contract repository. Security reports are welcome for the content validator, content-pack parser and updater, build and release workflow, local-data handling, PWA behavior, Android integration, and Termux bridge planned or implemented in this repository.

## Supported versions

The project is pre-release. Until a release policy is published, the `main` branch and the latest tagged release, when one exists, are the supported versions for security triage.

## Private reporting

Please do not disclose a suspected vulnerability in a public issue, pull request, discussion, or social-media post. Use GitHub’s **private vulnerability reporting** or security-advisory flow for this repository if it is enabled. If that flow is unavailable, contact the repository maintainer through the private contact method listed in the repository owner’s GitHub profile and include the repository name `Sudipsudip5250/aetherlearn-mvp-spec`.

Do not include passwords, private keys, access tokens, personal learning data, or unnecessary personal information in a report. Redact logs and use synthetic data wherever possible.

## What to include

A useful report contains the affected version or commit, the affected file or component, a concise description of the security impact, safe reproduction steps using a local or synthetic fixture, and any suggested mitigation. If the issue involves the Termux bridge or command execution, state whether the behavior can launch an unallowlisted command, bypass explicit confirmation, access shared storage, or transmit data.

If a report requires a proof of concept, keep it non-destructive and local. Do not target a third-party system, account, device, network, or service. We may ask for additional details only when they are necessary to reproduce and fix the issue.

## Response and disclosure

Maintainers will acknowledge reports when practical, triage their severity and exploitability, and coordinate a fix or mitigation. A report may result in a code change, content-pack withdrawal, documentation correction, release pause, or advisory. Public disclosure should wait until affected users have a reasonable mitigation or until the maintainers and reporter agree that coordinated disclosure is no longer needed.

## Security boundaries

AetherLearn does not promise that a learner’s Android device, Termux environment, browser, operating system, or user-chosen remote host is secure. The project must not execute arbitrary app-provided shell text, silently install packages, collect credentials, or require a network target for the core learning path. These are product constraints as well as security expectations.

For dual-use content, follow [`docs/SAFETY.md`](docs/SAFETY.md). For contribution practices, follow [`CONTRIBUTING.md`](CONTRIBUTING.md).
