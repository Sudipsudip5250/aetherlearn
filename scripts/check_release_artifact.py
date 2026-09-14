#!/usr/bin/env python3
"""Verify a release artifact against a SHA-256 checksum sidecar."""

from __future__ import annotations

import argparse
import hashlib
import re
import sys
from pathlib import Path

SHA256 = re.compile(r"^[0-9a-f]{64}$")


def digest(path: Path) -> str:
    hasher = hashlib.sha256()
    with path.open("rb") as stream:
        for chunk in iter(lambda: stream.read(1024 * 1024), b""):
            hasher.update(chunk)
    return hasher.hexdigest()


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("artifact", type=Path)
    parser.add_argument("checksum_file", type=Path)
    args = parser.parse_args()
    if not args.artifact.is_file():
        print(f"ERROR: artifact does not exist: {args.artifact}", file=sys.stderr)
        return 1
    lines = [line.strip() for line in args.checksum_file.read_text(encoding="utf-8").splitlines() if line.strip()]
    if len(lines) != 1:
        print("ERROR: checksum sidecar must contain exactly one non-empty line", file=sys.stderr)
        return 1
    parts = lines[0].split(maxsplit=1)
    if len(parts) != 2 or not SHA256.fullmatch(parts[0]) or Path(parts[1].lstrip("*")) != args.artifact:
        print("ERROR: checksum sidecar format or artifact name is invalid", file=sys.stderr)
        return 1
    actual = digest(args.artifact)
    if actual != parts[0]:
        print(f"ERROR: checksum mismatch: expected {parts[0]}, got {actual}", file=sys.stderr)
        return 1
    print(f"Verified SHA-256 for {args.artifact}: {actual}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
