---
id: web-04-data-modeling-and-json
title: Data modeling and JSON fixtures
strand: web-data
level: beginner
version: 1.0.0
prerequisites:
  - dl-01-digital-information
  - web-01-semantic-html-accessibility
estimated_minutes: 60
availability: offline
risk_tier: S1
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Distinguish a data model from one concrete record stored in a format.
  - Identify JSON objects, arrays, strings, numbers, booleans, and null in small fixtures.
  - Design a minimal fictional record and state its validation and privacy boundaries.
review_status: draft
last_reviewed: 2026-08-26
---

## Objectives

By the end of this lesson, you can describe the purpose of a small data model, read a JSON object and array, and design a minimal local fixture with explicit required fields, validation questions, and privacy limits.

## Prerequisites

You should understand that digital information is represented using discrete states and that HTML gives content structure. No JSON parser, database, API, account, or network connection is required.

## Availability

This lesson is fully offline. It uses short fictional JSON records as reading material and paper practice. The app does not import, transmit, or execute learner-provided data.

## Explanation

A **data model** is a description of the entities, fields, relationships, and rules that a program uses for a particular purpose. A **data format** is a way to represent that information so that it can be stored or exchanged. JSON is one data format; it does not decide whether a field is necessary, whether a value is private, or whether a record is valid for an application.

RFC 8259 describes JSON as a lightweight, text-based, language-independent data-interchange format for structured data.[1] It defines six commonly taught value categories: strings, numbers, booleans, null, objects, and arrays. An object contains name/value pairs, and an array is an ordered sequence of values. JSON’s syntax is small, but an application still needs a separate model and validation policy.

A useful design sequence is:

```text
purpose → minimum fields → field types → validation rules → privacy and retention → example fixture
```

Start with the purpose, not with every piece of information that might be available. If an offline reader only needs to remember whether a lesson is bookmarked, a stable lesson ID and a Boolean may be enough. Storing a person’s name, location, contacts, or device identifiers would be unrelated data.

Validation asks whether a record has the right shape and acceptable values. A record can be valid JSON but still fail an application’s rules: a required `lesson_id` may be missing, a number may be outside a permitted range, or a string may contain more information than the feature needs. A parser checks syntax; it does not grant permission to use the data.

## Worked example

A fictional offline reading feature might use this record:

```json
{
  "lesson_id": "web-04-data-modeling-and-json",
  "bookmarked": true,
  "review_minutes": 20,
  "note": null
}
```

The model can be described as follows:

| Field | Type | Required? | Example rule | Privacy note |
|---|---|---|---|---|
| `lesson_id` | string | yes | Must be one known local lesson ID. | Identifies a lesson, not a person. |
| `bookmarked` | boolean | yes | Must be `true` or `false`. | A local preference. |
| `review_minutes` | number | no | Must be a non-negative whole number in the app’s chosen range. | Avoid unnecessary timing detail. |
| `note` | string or null | no | May be empty or omitted according to the feature contract. | Learner text may be private and needs local retention/deletion rules. |

The record is valid JSON, but it is not automatically valid for every application. A version of the model might require `review_minutes` to be omitted when no timing information is needed. The smallest useful model is often easier to review and protect.

Compare these fictional fragments:

```json
{"lesson_id":"web-04-data-modeling-and-json","bookmarked":false}
```

```json
{"lesson_id":"web-04-data-modeling-and-json","bookmarked":"false"}
```

Both are JSON objects. In the second, `bookmarked` is a string rather than a Boolean, so an application that requires a Boolean should reject it or normalize it according to a documented rule. The syntax alone does not settle that decision.

## Common mistakes

Do not confuse JSON syntax with a complete data model. Do not treat every available device field as necessary. Do not use a string such as `"false"` when a Boolean is required without documenting the conversion. Do not assume that valid JSON is safe to import, display, or store. Do not use real notes, addresses, credentials, or exported browser data as fixtures. Finally, do not promise that a small example proves an application’s privacy, security, or interoperability properties.

## Offline practice

Design a fictional local record for a “practice reminder.” Start with the purpose and choose no more than four fields. For each field, write its JSON type, whether it is required, one validation rule, and a retention/deletion rule. Then write one valid JSON fixture and one syntactically valid but model-invalid fixture.

Review your model for data minimization. Remove one field that is not necessary, and write why it should not be collected. Do not use a real person, account, note, device identifier, or external data source.

## Knowledge check

1. What is the difference between a data model and JSON? **A data model describes the information and rules an application needs; JSON is one syntax for representing structured data.** JSON does not define an application’s meaning or privacy policy.
2. Which value categories does RFC 8259 define for JSON? **Strings, numbers, booleans, null, objects, and arrays.** Objects and arrays are structured values.
3. Can a text such as `"false"` be the same type as the JSON Boolean `false`? **No.** The first is a string and the second is a Boolean; an application must follow its documented model.
4. Does valid JSON automatically make a record appropriate to store? **No.** The record still needs purpose, validation, privacy, retention, and deletion rules.

## Project or application

Create a paper data-model card for a fictional offline lesson reader. Include the purpose, three or four minimum fields, types, required/optional decisions, validation rules, one valid fixture, one model-invalid fixture, and a deletion rule. Add a sentence explaining what the card does not prove about a real parser, database, API, or user data.

## Accessibility notes

The practice can be completed using a table, plain-language sentences, or speech-to-text. Learners may read the JSON with line breaks and indentation or describe each field verbally. No color, code execution, timed response, database, or network access is required.

## Safety and responsible use

This is an S1 offline data-literacy lesson. Use only fictional records. Do not paste real personal data, credentials, browser exports, private notes, or API responses into the activity. Do not import or transmit a fixture, and do not treat valid JSON as authorization to process another person’s data. For real systems, obtain an appropriate purpose, permission, retention rule, and privacy review.

## Further reading

[RFC 8259](https://www.rfc-editor.org/rfc/rfc8259) is the IETF Internet Standard describing the JSON data-interchange format, its grammar, values, objects, arrays, strings, and interoperability considerations.[1] The [MDN Working with JSON guide](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting/JSON) provides a beginner-oriented Web perspective; examples should still be checked against the version and rules of the application using them.[2]

## Change log

- 1.0.0 — Initial Stage 4 draft.

## References

[1]: https://www.rfc-editor.org/rfc/rfc8259 "IETF RFC 8259: The JavaScript Object Notation (JSON) Data Interchange Format"
[2]: https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting/JSON "MDN: Working with JSON"
