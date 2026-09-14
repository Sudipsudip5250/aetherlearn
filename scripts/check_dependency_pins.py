#!/usr/bin/env python3
"""Reject floating dependency versions in the repository's release inputs."""

from __future__ import annotations

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
DYNAMIC = re.compile(r"(?:[=:])(?:latest|release|snapshot|[+*])", re.IGNORECASE)
COORDINATE = re.compile(r"[\"']([A-Za-z0-9_.-]+:[A-Za-z0-9_.-]+:[^\"']+)[\"']")


def main() -> int:
    files = [ROOT / "requirements-dev.txt", ROOT / "android" / "app" / "build.gradle.kts", ROOT / "android" / "gradle" / "libs.versions.toml"]
    errors: list[str] = []
    for path in files:
        text = path.read_text(encoding="utf-8")
        if DYNAMIC.search(text):
            errors.append(f"{path}: dynamic dependency version detected")
        for coordinate in COORDINATE.findall(text):
            if coordinate.rsplit(":", 1)[-1].strip().lower() in {"", "+", "latest", "release", "snapshot"}:
                errors.append(f"{path}: unpinned Maven coordinate {coordinate}")
    for line_number, line in enumerate((ROOT / "requirements-dev.txt").read_text(encoding="utf-8").splitlines(), 1):
        stripped = line.strip()
        if stripped and not stripped.startswith("#") and "==" not in stripped:
            errors.append(f"requirements-dev.txt:{line_number}: requirement is not pinned with ==")
    if errors:
        for error in errors:
            print(f"ERROR: {error}", file=sys.stderr)
        return 1
    print("Dependency pin checks passed for Android and development requirements.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
