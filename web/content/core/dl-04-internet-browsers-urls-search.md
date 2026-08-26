---
id: dl-04-internet-browsers-urls-search
title: Internet basics, browsers, URLs, and search
strand: digital-literacy
level: beginner
version: 1.0.0
prerequisites:
  - dl-01-digital-information
estimated_minutes: 45
availability: offline
risk_tier: S0
core_asset_bytes: 0
optional_asset_bytes: 0
objectives:
  - Explain the roles of a browser, a web server, a URL, and a search engine.
  - Evaluate a fictional search result and URL before opening or sharing it.
review_status: draft
last_reviewed: 2026-08-25
tags:
  - digital-literacy
  - internet
  - browsers
  - search
sources:
  - https://developer.mozilla.org/en-US/docs/Learn_web_development/Howto/Web_mechanics/What_is_a_URL
  - https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview
---

# Internet basics, browsers, URLs, and search

## Objectives

By the end of this lesson, you can describe the path from a browser to a web resource, identify important URL parts, and judge a fictional search result before opening it.

## Prerequisites

Complete [DL-01](dl-01-digital-information.md), or be comfortable treating a web page as information retrieved and interpreted by software.

## Availability

This lesson is fully offline. URLs and search results in the exercises are fictional; do not open an unfamiliar link as part of the lesson.

## Explanation

The **internet** is a network of networks that lets devices and services exchange data. The **web** is one system that uses the internet: browsers request resources such as HTML pages, stylesheets, images, and scripts, and web servers respond. A browser is the app that requests, receives, and displays web content. A search engine helps find indexed pages; it is not the same thing as the browser.

A **URL** is a Uniform Resource Locator, an address that tells software where a resource is located and how to request it. In `https://learn.example/course/week1?mode=offline#practice`, the scheme is `https`, the host is `learn.example`, the path is `/course/week1`, the query is `mode=offline`, and the fragment is `practice`. The fragment identifies a place within a resource and is not normally sent to the server.

`https` helps protect the connection between browser and server, but it does not make every page honest or every download safe. The host matters because similar-looking words can belong to different domains. Read the domain carefully when a page asks for a password, payment, verification code, or urgent action.

Search results are clues, not proof. A title can mislead, an advertisement can appear above useful results, and a page can become outdated. Compare the question with the author, date, evidence, and URL. For important information, prefer a known official or primary source, open it deliberately, and do not enter private information merely because a page asks for it.

## Worked example

Consider these fictional results for “How do I update my phone?”

| Result | What to inspect |
|---|---|
| `Phone maker help — System updates` at `https://support.phone.example/update` | Domain, HTTPS scheme, and whether it matches the phone maker |
| `You won a phone — install this urgent file` at `http://free-prize.example/download` | Unexpected claim, urgency, download request, and untrusted context |
| `Community post — I changed this setting` | Whether it is an experience report rather than official, current guidance |

The first may be the best starting point, but still check that it matches the device and does not request unrelated credentials. Do not open the second merely because it appeared in search.

## Common mistakes

A common mistake is thinking a browser and search engine are the same. Another is reading only the first words of a URL and ignoring the actual host. `https` describes connection protection, not honesty. Finally, copying a search snippet without checking the page can spread an incomplete or outdated answer.

## Offline practice

Rank these fictional results from **best starting point** to **do not open**, giving one reason for each:

1. A public library page at `https://library.example/books?topic=python` that names its author and review date.
2. A result at `https://unknown.example/free-book` that asks for a password before showing a preview.
3. A shortened link with no visible destination that promises an urgent prize.

Then label the scheme, host, path, query, and fragment in:

```text
https://library.example/books?topic=python#chapter-2
```

Finish by writing one type of information you should never enter into an unfamiliar page.

## Knowledge check

1. What does a browser do? **It requests and displays web resources.** It retrieves and interprets web content.
2. What is the host in `https://library.example/books`? **library.example.** The host identifies the server or domain requested.
3. Does `https` prove a page is trustworthy? **No, it mainly protects the connection in transit.** You must still judge source, request, and context.

## Project or application

Create a paper “search before trust” checklist: What am I trying to learn? Who published this? Is the date relevant? Does the URL match the expected organization? Is the requested action safe and necessary? Test it on two fictional results.

## Accessibility notes

URLs appear in code blocks and are explained by name, so learners need not distinguish tiny icons or colors. Tables have text labels, and the practice works on paper or with enlarged text.

## Safety and responsible use

This is an S0 foundation lesson. Do not open unfamiliar links, download unexpected files, or enter credentials and verification codes during practice. Treat search results and pages as untrusted until their source and purpose are clear.

## Further reading

MDN’s [What is a URL?](https://developer.mozilla.org/en-US/docs/Learn_web_development/Howto/Web_mechanics/What_is_a_URL) explains URL parts, addresses, and fragments. Its [HTTP overview](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview) introduces how clients and servers exchange web resources.

## Change log

- 1.0.0 — Initial MVP draft.
