---
id: dl-03-android-settings-permissions-apps
title: Android phone settings, permissions, and apps
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
  - Distinguish an app feature, a device setting, and a permission request.
  - Apply least-privilege reasoning to fictional Android app situations.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - digital-literacy
  - android
  - permissions
sources:
  - https://developer.android.com/guide/topics/permissions/overview
  - https://support.google.com/android/answer/9431959?hl=en
---

# Android phone settings, permissions, and apps

## Objectives

By the end of this lesson, you can separate what an app does from what the phone controls, identify when a permission is relevant, and ask whether access is necessary for the action you chose.

## Prerequisites

No previous Android experience is required. You only need to compare a requested action with the data or capability it needs.

## Availability

This lesson is fully offline. It uses fictional app names and does not ask you to change a real setting, grant a permission, or install an application.

## Explanation

An **app** is a program that provides features. A **device setting** is a control that changes how the phone or its services behave, such as brightness, sound, language, or connectivity. A **permission** is an authorization boundary that can protect restricted data or actions, such as contacts, location, the camera, or the microphone.

A permission is not proof that an app is trustworthy. Ask four questions: What action did I choose? What data or capability does it need? Is the request expected now? What happens if I deny it? A cautious app asks close to the feature that needs access, explains why, and provides an alternative when possible.

Android has different kinds of permissions and access. Some access is granted at install time, some requires a runtime decision, and some operations are controlled through special settings. Screen names can vary by Android version and manufacturer. Read the system prompt, check the app and permission category in Settings, and do not grant broad access merely to dismiss a prompt.

**Least privilege** means giving only the access needed for a specific task, for only as long as it is needed. A flashlight app asking to read contacts has a mismatch worth questioning. A voice recorder asking for microphone access after recording is selected has a clearer connection. If a feature does not work after denial, look for a lower-access alternative or decide not to use it.

## Worked example

A fictional app called `Pocket Sketch` offers two features:

| Feature | Requested access | First question |
|---|---|---|
| Draw and save a picture inside the app | None in this example | Can the feature work without broader access? |
| Take a new photo for reference | Camera | Does the request appear only after choosing the camera feature? |

The second request matches the chosen action, but the learner still reads the prompt and checks the app identity. A contacts request while drawing does not match the task and should be denied or investigated.

## Common mistakes

A common mistake is treating all permission prompts as identical. Denying a permission usually limits a feature; it does not damage the phone. Learners may also confuse notification settings with access to private data. Finally, access granted in the past is still worth reviewing if the app’s purpose or the user’s needs change.

## Offline practice

For each fictional request, choose **allow**, **deny**, or **ask for more information**, and explain why:

1. A calculator asks for microphone access before showing `2 + 2`.
2. A camera app asks for camera access after you select “take a photo.”
3. A notes app asks for contacts access when you select “export this note as text.”

Then write one lower-access alternative for a feature that cannot proceed after denial.

## Knowledge check

1. What is a permission? **An authorization to access restricted data or perform a restricted action.** It is a boundary, not a guarantee of trustworthiness.
2. What does least privilege mean? **Give only the access needed for the chosen task.** Avoid unrelated broad access.
3. Should a calculator need microphone access to add two numbers? **No, not for that calculation.** The request does not match the action.

## Project or application

Make a fictional permission review table for a calculator, photo viewer, and study timer. For each app, list one feature, minimum access, an unrelated request, and what the user can do if access is denied.

## Accessibility notes

The lesson uses a text table and written decision questions. The practice can be completed without a real permission dialog, and choices are words rather than color-only signals.

## Safety and responsible use

This is an S0 foundation lesson. Do not grant permissions to an unfamiliar app merely to continue. Review the app, requested capability, timing, and alternatives. Permission categories and screen labels vary by Android version, so use the current system prompt and help when making a real decision.

## Further reading

The [Android permissions overview](https://developer.android.com/guide/topics/permissions/overview) explains permission types, runtime decisions, and privacy practices. Google’s [Android permission settings guidance](https://support.google.com/android/answer/9431959?hl=en) describes how users can review an app’s permissions.

## Change log

- 1.0.0 — Initial MVP draft.
