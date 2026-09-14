#!/usr/bin/env python3
"""Check that relative Markdown links resolve inside the repository."""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

LINK_PATTERN = re.compile(r"!?(?:\[[^\]]*\])\(([^)]+)\)")


def check_file(path: Path, repo_root: Path) -> list[str]:
    errors: list[str] = []
    text = path.read_text(encoding="utf-8")
    for target in LINK_PATTERN.findall(text):
        target = target.strip().split()[0].strip("<>")
        if not target or target.startswith(("#", "http://", "https://", "mailto:")):
            continue
        target_path = target.split("#", 1)[0].split("?", 1)[0]
        if not target_path:
            continue
        resolved = (path.parent / target_path).resolve()
        try:
            resolved.relative_to(repo_root.resolve())
        except ValueError:
            errors.append(f"{path}: link escapes repository: {target}")
            continue
        if not resolved.exists():
            errors.append(f"{path}: broken local link: {target}")
    return errors


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, default=Path("."))
    args = parser.parse_args()
    root = args.root.resolve()
    files = sorted(path for path in root.rglob("*.md") if ".git" not in path.parts and "build" not in path.parts)
    errors = [error for path in files for error in check_file(path, root)]
    if errors:
        for error in sorted(set(errors)):
            print(f"ERROR: {error}", file=sys.stderr)
        print(f"Markdown link check failed with {len(set(errors))} error(s).", file=sys.stderr)
        return 1
    print(f"Checked {len(files)} Markdown file(s); all local links resolve.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
