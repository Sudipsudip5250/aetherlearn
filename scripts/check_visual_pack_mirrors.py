"""Check byte parity between the canonical visual pack and client asset mirrors."""

from __future__ import annotations

import argparse
import sys
from pathlib import Path


DEFAULT_ROOT = Path(__file__).resolve().parents[1]
SOURCE = Path("media/visuals/visual-foundations")
MIRRORS = (
    Path("android/app/src/main/assets/packs/visual-foundations"),
    Path("web/visuals/visual-foundations"),
)


def file_map(root: Path) -> dict[str, bytes]:
    return {
        file.relative_to(root).as_posix(): file.read_bytes()
        for file in root.rglob("*")
        if file.is_file()
    }


def check_mirrors(repo_root: Path = DEFAULT_ROOT) -> list[str]:
    source_root = repo_root / SOURCE
    expected = file_map(source_root)
    errors: list[str] = []
    for mirror in MIRRORS:
        mirror_root = repo_root / mirror
        actual = file_map(mirror_root) if mirror_root.is_dir() else {}
        for relative in sorted(set(expected) - set(actual)):
            errors.append(f"{mirror}: missing mirror file: {relative}")
        for relative in sorted(set(actual) - set(expected)):
            errors.append(f"{mirror}: unexpected mirror file: {relative}")
        for relative in sorted(set(expected) & set(actual)):
            if expected[relative] != actual[relative]:
                errors.append(f"{mirror}: byte mismatch: {relative}")
    return errors


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--repo-root", type=Path, default=DEFAULT_ROOT)
    args = parser.parse_args()
    errors = check_mirrors(args.repo_root.resolve())
    if errors:
        for error in errors:
            print(f"ERROR: {error}", file=sys.stderr)
        return 1
    print("Visual pack mirrors are byte-identical to the canonical source.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
