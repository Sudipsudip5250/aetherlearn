"""Verify Android bundled lesson assets and the explicit catalog mirror canonical content."""

from __future__ import annotations

import re
import sys
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]
SOURCE_DIR = ROOT / "content" / "core"
ANDROID_DIR = ROOT / "android" / "app" / "src" / "main" / "assets" / "content" / "core"
CATALOG_PATH = ROOT / "android" / "app" / "src" / "main" / "java" / "com" / "aetherlearn" / "app" / "data" / "ModuleCatalog.kt"


def main() -> int:
    errors: list[str] = []
    source_files = {path.name: path for path in SOURCE_DIR.glob("*.md")}
    android_files = {path.name: path for path in ANDROID_DIR.glob("*.md")}
    if set(source_files) != set(android_files):
        missing = sorted(set(source_files) - set(android_files))
        extra = sorted(set(android_files) - set(source_files))
        if missing:
            errors.append(f"missing Android lesson assets: {', '.join(missing)}")
        if extra:
            errors.append(f"unexpected Android lesson assets: {', '.join(extra)}")
    for name in sorted(set(source_files) & set(android_files)):
        if source_files[name].read_bytes() != android_files[name].read_bytes():
            errors.append(f"Android asset differs from canonical source: {name}")

    catalog_text = CATALOG_PATH.read_text(encoding="utf-8")
    catalog_files = set(re.findall(r'"([A-Za-z0-9._-]+\.md)"', catalog_text))
    if catalog_files != set(source_files):
        missing = sorted(set(source_files) - catalog_files)
        extra = sorted(catalog_files - set(source_files))
        if missing:
            errors.append(f"ModuleCatalog is missing canonical lessons: {', '.join(missing)}")
        if extra:
            errors.append(f"ModuleCatalog references unknown lessons: {', '.join(extra)}")

    if errors:
        for error in errors:
            print(f"ERROR: {error}", file=sys.stderr)
        return 1
    print(f"Android content mirrors {len(source_files)} canonical lesson(s).")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
