#!/usr/bin/env python3
"""Fail on common accidentally committed secret patterns in tracked text files."""

from __future__ import annotations

import argparse
import re
import sys
from pathlib import Path

PATTERNS = [
    re.compile(r"gh[pousr]_[A-Za-z0-9_]{20,}"),
    re.compile(r"-----BEGIN (?:RSA|OPENSSH|EC|PGP) PRIVATE KEY-----"),
    re.compile(r"(?:OPENAI_API_KEY|AWS_SECRET_ACCESS_KEY|GOOGLE_API_KEY)\s*[:=]"),
]
SKIP_PARTS = {".git", "build", "__pycache__"}
SKIP_SUFFIXES = {".png", ".jpg", ".jpeg", ".gif", ".webp", ".zip", ".pyc"}


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, default=Path("."))
    args = parser.parse_args()
    root = args.root.resolve()
    findings: list[str] = []
    for path in sorted(root.rglob("*")):
        if not path.is_file() or any(part in SKIP_PARTS for part in path.parts) or path.suffix.lower() in SKIP_SUFFIXES:
            continue
        try:
            text = path.read_text(encoding="utf-8")
        except (UnicodeDecodeError, OSError):
            continue
        for pattern in PATTERNS:
            if pattern.search(text):
                findings.append(f"{path}: matched secret pattern {pattern.pattern}")
    if findings:
        for finding in findings:
            print(f"ERROR: {finding}", file=sys.stderr)
        return 1
    print("No configured secret patterns found in repository text files.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
