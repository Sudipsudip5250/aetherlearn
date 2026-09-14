---
id: dl-08-networks-web-concepts
title: "Networks and the Web: requests, responses, and resources"
strand: digital-literacy
level: beginner
version: 1.0.0
prerequisites:
  - dl-01-digital-information
  - dl-04-internet-browsers-urls-search
estimated_minutes: 55
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain the client-server relationship using the vocabulary of resources, requests, and responses.
  - Identify the roles of a URL, HTTP method, status code, header, and response body in a fixed trace.
  - Describe why offline-first design, caching, and authorization boundaries matter for responsible Web use.
review_status: draft
last_reviewed: 2026-08-26
tags:
  - networks
  - web
  - privacy
  - digital-literacy
sources:
  - https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview
  - https://datatracker.ietf.org/doc/html/rfc9110
---

# Networks and the Web: requests, responses, and resources

## Objectives

By the end of this lesson, you can describe a basic Web request and response, identify important parts of an HTTP message, and explain why a local-first application should make its storage and network boundaries understandable to the user.

## Prerequisites

You should understand that information is represented digitally and that programs can read input and produce output. No networking tools or Internet connection are required.

## Availability

This lesson is fully offline. The only network example is a fictional written trace. Do not repeat it against a real host.

## Explanation

The Web is built from cooperating systems. A **client**, often a browser, asks for a resource. A **server** receives the request and sends a response. MDN describes HTTP as a client-server protocol for fetching resources such as HTML documents; a page may require additional resources such as stylesheets, images, and scripts.[1]

A **URL** identifies where a resource can be requested. In a simplified view, an HTTP request includes a method such as `GET`, a path, and optional headers. The method communicates the kind of operation the client wants. `GET` commonly asks for a representation of a resource; other methods have other semantics. The server’s response includes a status code, headers, and sometimes a body containing the representation.

The IETF’s HTTP Semantics specification describes HTTP as a stateless, application-level protocol and defines common vocabulary for resources, representations, methods, and responses.[2] “Stateless” means that the core protocol does not automatically remember a relationship between separate requests. Applications can add state through mechanisms such as cookies or server-side sessions, which is one reason privacy and authorization boundaries must be made clear.

A browser may use caches to avoid retrieving an unchanged resource repeatedly. Caching can improve speed and support offline behavior, but it also creates questions: which copy is being shown, when was it stored, and how is it updated? A local-first learning app can answer these questions by clearly showing whether content is bundled, cached, or being updated. Notes and progress can remain in local storage without being sent to a server.

The layers below HTTP also matter, but this lesson keeps their roles separate. IP helps identify destinations, a transport such as TCP or QUIC helps carry data, and HTTP describes application-level requests and responses. A browser hides much of this machinery so that a learner can focus on the resource being requested.

## Worked example

Consider this fictional trace for a lesson pack stored on `learn.example`:

```text
Request
  method: GET
  path: /packs/core/manifest.json
  header: Accept: application/json

Response
  status: 200
  header: Content-Type: application/json
  body: { "pack": "core", "version": "1.0.0" }
```

The client asks for a resource using `GET`. The server reports `200`, a status code commonly used for a successful response, identifies the body as JSON, and returns a representation of the manifest. This trace is fictional and does not authorize a request to the example host.

## Common mistakes

A URL is not the same thing as the data returned from it. A status code is not a guarantee that the content is correct for every purpose. HTTP is not the whole Internet stack, and a browser is not the server. Finally, “cached” does not mean “synchronized”: a cache is a local or intermediary copy, while synchronization implies a deliberate exchange of state.

## Offline practice

Use the fictional trace above. Label each item as **client intention**, **resource identifier**, **request metadata**, **server result**, **response metadata**, or **representation data**. Then draw arrows for this sequence: browser, network path, server, response, local cache. Add a note showing where the trace would stop if the network were disabled before the resource had been cached.

## Knowledge check

1. In a client-server exchange, who normally starts an HTTP request? **The client, often a browser or another user agent.**
2. What does a response status code communicate? **It gives information about the result or state of handling a request, such as success or failure.**
3. What is the difference between a request header and a response body? **A request header carries metadata sent with the request, while a response body can carry the returned representation data.**
4. Does a cache automatically mean that local data was synchronized with a server? **No. A cache is a stored copy; synchronization is a deliberate exchange of state.**
5. Why should an offline-first app explain its cache status? **Users should know whether they are reading bundled or cached content and whether an update has actually succeeded.**

## Project or application

Design a paper “offline boundary card” for a learning app. On the left, list what is available from the bundled lesson pack. In the middle, list what may be cached after an explicit update. On the right, list data that must remain local, such as notes and progress. Include one sentence that tells a learner what happens when the network is unavailable.

## Accessibility notes

The request and response are presented in a linear text block and can be read aloud or copied into a notebook. The exercise does not depend on color, hover behavior, animation, or timing. Learners may use a table, diagram, spoken answer, or plain text to show the same relationships.

## Safety and responsible use

This is an S0 networking-literacy lesson. The host name is fictional, and no network connection is required. Do not replace it with a real host, send requests to systems you do not own or have permission to test, or enter credentials into an unfamiliar site. The lesson teaches message vocabulary, not network scanning, interception, or bypassing access controls.

## Further reading

The [MDN Overview of HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview) introduces clients, servers, requests, responses, caching, and HTTP messages. The [IETF RFC 9110 HTTP Semantics specification](https://datatracker.ietf.org/doc/html/rfc9110) defines the protocol’s shared concepts and semantics. Both sources should be consulted when a version-specific implementation detail matters.

## Change log

- 1.0.0 — Initial Stage 1 draft.
