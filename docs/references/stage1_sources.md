# Stage 1 source findings

**Purpose:** source record for the first approved post-MVP curriculum slice. This note is research evidence, not a lesson and does not by itself change the curriculum registry.

## Proposed Stage 1 lessons

| Proposed ID | Topic | Intended risk | Primary sources |
|---|---|---|---|
| `dl-06-computing-language-history` | How programming languages reflect constraints and communities | S0 | Computer History Museum language timeline |
| `dl-07-how-programs-run` | Source text, interpreter/compiler concepts, processes, memory, and errors | S0/S1 | Python interpreter documentation; existing AetherLearn Python lessons |
| `dl-08-networks-web-concepts` | Client/server, HTTP request/response, URLs, caching, and safe network boundaries | S0/S1 | MDN HTTP overview; IETF RFC 9110; existing AetherLearn browser/privacy lessons |

## Inspected source claims

The Computer History Museum timeline presents historical milestones including Plankalkül, A-0, FORTRAN, COBOL, BASIC, Simula, LOGO, UNIX, Pascal, C, C++, and Perl. It is suitable for a high-level history lesson, but claims such as “first” and claims about current usage require careful wording and source-date review. The lesson will use comparison and design constraints rather than a comprehensive chronology.

The Python documentation explains that the interpreter can operate interactively from a terminal or execute a script supplied as a file; it also distinguishes command-line options such as `-c` and `-m`. The lesson will use this only to explain the source-to-execution concept and will not add arbitrary execution to AetherLearn.

MDN describes HTTP as a client-server protocol in which clients send requests and servers return responses. It explains that HTTP messages include methods, paths, headers, status codes, and optional bodies, and that browsers may request multiple resources to build a page. The lesson will use a fixed written trace and will not contact live hosts.

RFC 9110 defines HTTP as a stateless application-level protocol and describes request/response semantics, resources, representations, and the evolution of HTTP/1.1, HTTP/2, and HTTP/3. The lesson will present these as conceptual vocabulary and avoid version-sensitive implementation promises.

## Safety and maintenance boundaries

Stage 1 exercises will use paper diagrams, fixed fictional traces, and local text. They will not require an obsolete compiler, download unknown binaries, scan public hosts, intercept traffic, collect credentials, connect to third-party infrastructure, or run a network service. Each lesson will include an offline path, expected answer shape, accessibility notes, and a source/date note. A human technical and pedagogical review remains required before release status changes from draft.

## References

[1]: https://www.computerhistory.org/timeline/software-languages/ "Computer History Museum: Timeline of Computer History — Software & Languages"
[2]: https://docs.python.org/3/tutorial/interpreter.html "Python Documentation: Using the Python Interpreter"
[3]: https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview "MDN Web Docs: Overview of HTTP"
[4]: https://datatracker.ietf.org/doc/html/rfc9110 "IETF: RFC 9110 — HTTP Semantics"
