#!/usr/bin/env python3
"""Build a deterministic AetherLearn core content pack."""

from __future__ import annotations

import argparse
import json
import shutil
import sys
import zipfile
from pathlib import Path

from validate_content import (
    load_curriculum_registry,
    load_lessons,
    validate_graph,
    validate_lesson,
    write_manifest,
)


def build_pack(content_dir: Path, output_dir: Path) -> None:
    repo_root = Path(__file__).resolve().parents[1]
    content_root = content_dir.resolve()
    known_ids, registry_errors = load_curriculum_registry(repo_root)
    lessons, errors = load_lessons(content_root)
    errors.extend(registry_errors)
    for lesson in lessons:
        errors.extend(validate_lesson(lesson, content_root, repo_root))
    errors.extend(validate_graph(lessons, known_ids))
    if errors:
        for error in sorted(set(errors)):
            print(f"ERROR: {error}", file=sys.stderr)
        raise SystemExit(f"Build stopped: {len(set(errors))} validation error(s).")

    output_dir = output_dir.resolve()
    if output_dir.exists():
        shutil.rmtree(output_dir)
    output_dir.mkdir(parents=True)
    manifest_path = output_dir / "manifest.json"
    write_manifest(manifest_path, lessons, content_root)

    zip_path = output_dir.parent / f"{output_dir.name}.zip"
    if zip_path.exists():
        zip_path.unlink()
    files: list[tuple[str, bytes]] = [("manifest.json", manifest_path.read_bytes())]
    files.extend(
        (f"modules/{lesson.path.name}", lesson.path.read_bytes())
        for lesson in lessons
    )
    with zipfile.ZipFile(zip_path, "w", compression=zipfile.ZIP_DEFLATED, compresslevel=9) as archive:
        for name, data in sorted(files):
            info = zipfile.ZipInfo(name, date_time=(1980, 1, 1, 0, 0, 0))
            info.compress_type = zipfile.ZIP_DEFLATED
            info.external_attr = 0o100644 << 16
            archive.writestr(info, data)

    print(f"Built {len(lessons)} lesson(s).")
    print(f"Directory: {output_dir}")
    print(f"Archive: {zip_path}")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--content-dir", type=Path, default=Path("content/core"))
    parser.add_argument("--output-dir", type=Path, default=Path("build/core-pack"))
    args = parser.parse_args()
    build_pack(args.content_dir, args.output_dir)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
