# AetherLearn web fallback

This directory contains the secondary, client-only M6 web reader. It reuses the five validated Markdown lesson files from `content/core/` and does not introduce a second lesson schema. The copied files under `web/content/core/` are the static web payload for this initial shell; update them from the canonical source when content changes.

## Serve locally

A browser should load the client through a local HTTP server rather than directly from `file://`, because the shared lesson loader uses `fetch()`.

From the repository root:

```bash
python3 -m http.server 4173 --directory web
```

Then open <http://localhost:4173/>. No build step or package installation is required for this initial plain JavaScript shell.

## Current slice

The shell loads the shared `web/content/manifest.json`, fetches all five Markdown files, parses the existing YAML frontmatter and level-two sections, and renders a responsive lesson catalog plus a full lesson reader. The UI includes a privacy note, a visible Android-only Termux limitation, a theme toggle, accessible landmarks, keyboard focus styles, a skip link, and reduced-motion support.

The service worker, explicit IndexedDB content-pack cache, local progress, notes/bookmarks, search, and practice state are the next M6 slices. The current cache button is intentionally a status placeholder until the cache lifecycle is implemented; it does not claim that offline use is ready.

## Boundaries

The web client is fully client-side. It has no backend, accounts, analytics SDK, tracking, Termux bridge, network content marketplace, or native export parity. The core lesson source remains the repository’s validated Markdown contract in [`../content/README.md`](../content/README.md).
