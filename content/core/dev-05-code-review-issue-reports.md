---
id: dev-05-code-review-issue-reports
title: Code review and useful issue reports
strand: developer-foundations
level: beginner
version: 1.0.0
prerequisites:
  - dev-02-git-local-repositories-history
  - dev-03-debugging-error-messages
estimated_minutes: 60
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain the purpose of a pull request and the context a reviewer needs.
  - Write a clear, privacy-aware issue report for a fictional local project.
  - Distinguish a respectful review comment from an unexplained preference or personal attack.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can describe what a change proposal asks reviewers to decide, write an issue report that another person can reproduce, and give review feedback that is specific, kind, and connected to the project’s requirements.

## Prerequisites

You should know the difference between a local Git repository, a commit, expected behavior, observed behavior, and a minimal reproduction. No GitHub account, remote repository, branch publication, or network connection is required.

## Availability

This lesson is fully offline. It uses a fictional repository and paper review forms. The exercises never publish a branch or send a report.

## Explanation

A code review is a structured conversation about a proposed change. GitHub describes a pull request as a proposal to merge code changes and a place to discuss and review them before merging.[1] The proposal is not only a diff: reviewers also need the problem statement, intended behavior, tests or checks, limitations, and any decisions that affect users.

A useful review context often includes:

| Context | Question it answers |
|---|---|
| Problem | What behavior or need motivated this change? |
| Scope | What files, cases, or users are intentionally included or excluded? |
| Evidence | What tests, examples, or manual checks support the change? |
| Risk | What could regress, expose private data, or become harder to undo? |
| Follow-up | What is deliberately deferred, and who will revisit it? |

GitHub’s documentation identifies conversation, commits, checks, changed files, findings, and merge status as parts of the pull-request context. It also distinguishes a **draft** pull request from work ready for review.[1] These ideas generalize to other collaboration tools, but their labels and permissions vary.

An issue report records a problem or request so another person can understand and prioritize it. A strong report separates **expected behavior** from **observed behavior**, includes safe reproduction steps, identifies the smallest relevant input, and states the environment only when it matters. It should not include passwords, private names, access tokens, unredacted screenshots, or a public service address when a fictional or local description is enough.

Review comments should discuss the change or its effect, not the author’s character. “This branch does not handle an empty record; could we add a boundary test?” is actionable. “This is careless” is not. A reviewer can request a change, ask a question, approve, or explain why the evidence is sufficient. A project may have its own review rules, so learners should read its contribution and code-of-conduct documents before participating.

## Worked example

A fictional project called **Pocket Ledger** stores a local list of expenses. A proposed change claims to add a monthly total. The review card says:

- **Problem:** The summary screen cannot show the total for a selected month.
- **Scope:** This change only calculates totals from the local fixture file; it does not add accounts, sync, or cloud storage.
- **Evidence:** The author checked an empty month, a one-entry month, and two entries with the same date.
- **Open question:** The requirement does not say whether a refunded entry is negative or excluded.
- **Reviewer request:** Define the refund rule and add one example before merging.

The review request is specific because it points to an ambiguous requirement and a missing example. It does not assume bad intent, and it does not ask the author to disclose unrelated project data.

A matching issue report might say:

```text
Title: Monthly total is unclear for a month with no entries
Expected: The summary explains whether an empty month displays 0 or “no data”.
Observed: The fictional design note shows a blank field.
Steps: Open the supplied empty-month fixture and select April.
Safe evidence: fixture-empty-april.txt; no personal data or network address.
Question: Which display should the requirement define?
```

## Common mistakes

Do not treat a pull request as an automatic approval or a code-quality guarantee. Do not report a vague “does not work” without expected and observed behavior. Do not paste secrets or private logs into an issue because they seem useful. Do not turn a style preference into a blocking defect without connecting it to a stated rule. Do not review only the changed line while ignoring its tests, surrounding behavior, accessibility, privacy, or rollback implications. Finally, do not assume that a draft or a green check replaces human judgment.

## Offline practice

Use the fictional **Pocket Ledger** example to complete two forms. First, write an issue report for a missing empty-month rule. Second, write three review comments: one blocking question tied to a requirement, one non-blocking suggestion, and one approval statement that names the evidence you checked. Remove names, paths, credentials, and external URLs from your forms.

As a final check, label each comment **requirement**, **evidence**, **question**, **suggestion**, or **approval**. If a comment cannot receive a clear response or action, rewrite it.

## Knowledge check

1. What is a pull request? **A proposal to merge a set of code changes that can be discussed and reviewed before merging.** It is a collaboration and decision space, not proof that a change is correct.
2. What belongs in a useful issue report? **Expected behavior, observed behavior, safe reproduction steps, and relevant evidence or environment details.** The report should omit secrets and unrelated private data.
3. Why should review feedback focus on the change rather than the person? **Change-focused feedback is actionable and lowers the chance that a technical disagreement becomes a personal attack.** It also gives the author a clear next step.
4. Does a passing check eliminate the need for review? **No.** Automated checks provide evidence for what they cover; people still assess requirements, scope, privacy, accessibility, and trade-offs.

## Project or application

Create a review checklist for a fictional offline feature. Include problem statement, scope, expected behavior, normal and boundary examples, test evidence, privacy check, accessibility question, rollback or follow-up note, and a decision field. The checklist must work without a GitHub account or network.

## Accessibility notes

The lesson presents review context as labeled fields and allows issue reports to be completed as plain text, speech-to-text, or a paper form. Learners do not need to interpret color-coded statuses, hover states, timed notifications, or a side-by-side diff. A reviewer may request a text summary when a visual diff is difficult to inspect.

## Safety and responsible use

This is an S0 offline collaboration lesson. Use only the fictional repository and supplied local fixture. Never include credentials, personal data, private logs, or unredacted screenshots in a report. Do not publish an issue, open a pull request, or contact a maintainer as part of the exercise. Respect a project’s access rules and code of conduct before contributing.

## Further reading

The [GitHub documentation on pull requests](https://docs.github.com/en/pull-requests/reference/pull-requests) explains proposals, review context, draft status, and collaboration models. The repository’s contribution rules should be read before making a real contribution; this offline lesson intentionally does not link to or submit a live contribution.

## Change log

- 1.0.0 — Initial Stage 2 draft.
