#!/usr/bin/env python3
"""Verify that the static web payload mirrors the canonical lesson source."""

from __future__ import annotations

import json
import re
import sys
from pathlib import Path

import yaml

ROOT = Path(__file__).resolve().parents[1]
SOURCE_DIR = ROOT / "content" / "core"
WEB_DIR = ROOT / "web"
MANIFEST_PATH = WEB_DIR / "content" / "manifest.json"
def frontmatter(path: Path) -> dict[str, str]:
    text = path.read_text(encoding="utf-8")
    match = re.match(r"^---\s*\n([\s\S]*?)\n---\s*\n", text)
    if not match:
        raise ValueError(f"{path}: missing frontmatter")
    try:
        fields = yaml.safe_load(match.group(1)) or {}
    except yaml.YAMLError as exc:
        raise ValueError(f"{path}: invalid frontmatter: {exc}") from exc
    if not isinstance(fields, dict) or not isinstance(fields.get("id"), str) or not isinstance(fields.get("title"), str):
        raise ValueError(f"{path}: frontmatter needs string id and title")
    return {"id": fields["id"], "title": fields["title"]}


def main() -> int:
    errors: list[str] = []
    if not MANIFEST_PATH.exists():
        print(f"Missing {MANIFEST_PATH}", file=sys.stderr)
        return 1
    manifest = json.loads(MANIFEST_PATH.read_text(encoding="utf-8"))
    entries = manifest.get("lessons")
    if manifest.get("schema_version") != 1 or not isinstance(entries, list):
        print("Web manifest must use schema_version 1 with a lessons list", file=sys.stderr)
        return 1
    source_files = {path.name: path for path in SOURCE_DIR.glob("*.md")}
    if len(entries) != len(source_files):
        errors.append(f"expected {len(source_files)} web lessons, found {len(entries)}")
    manifest_files: set[str] = set()
    for entry in entries:
        path_value = entry.get("path", "")
        web_path = WEB_DIR / path_value
        source_name = Path(path_value).name
        manifest_files.add(source_name)
        source_path = source_files.get(source_name)
        if source_path is None:
            errors.append(f"manifest references unknown source file: {source_name}")
            continue
        if not web_path.exists():
            errors.append(f"missing web payload: {path_value}")
            continue
        source_meta = frontmatter(source_path)
        if entry.get("id") != source_meta["id"] or entry.get("title") != source_meta["title"]:
            errors.append(f"manifest metadata mismatch: {source_name}")
        if web_path.read_bytes() != source_path.read_bytes():
            errors.append(f"web payload differs from canonical source: {source_name}")

    if manifest_files != set(source_files):
        errors.append("manifest does not cover exactly the canonical source files")
    if errors:
        for error in errors:
            print(f"ERROR: {error}", file=sys.stderr)
        return 1
    print(f"Web content mirrors {len(entries)} canonical lesson(s).")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
