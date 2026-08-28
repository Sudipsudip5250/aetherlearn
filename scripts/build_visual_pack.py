#!/usr/bin/env python3
"""Build a deterministic AetherLearn optional visual pack."""

from __future__ import annotations

import argparse
import hashlib
import sys
import zipfile
from pathlib import Path

from validate_visual_pack import validate_directory, validate_zip


def build(source_dir: Path, output_zip: Path) -> None:
    source_dir = source_dir.resolve()
    errors = validate_directory(source_dir)
    if errors:
        for error in sorted(set(errors)):
            print(f"ERROR: {error}", file=sys.stderr)
        raise SystemExit(f"Build stopped: {len(set(errors))} validation error(s).")

    output_zip = output_zip.resolve()
    sidecar = output_zip.with_name(output_zip.name + ".SHA256SUMS")
    output_zip.parent.mkdir(parents=True, exist_ok=True)
    if output_zip.exists():
        output_zip.unlink()
    if sidecar.exists():
        sidecar.unlink()
    files = sorted(file for file in source_dir.rglob("*") if file.is_file())
    with zipfile.ZipFile(output_zip, "w", compression=zipfile.ZIP_DEFLATED, compresslevel=9) as archive:
        for file in files:
            name = file.relative_to(source_dir).as_posix()
            info = zipfile.ZipInfo(name, date_time=(1980, 1, 1, 0, 0, 0))
            info.compress_type = zipfile.ZIP_DEFLATED
            info.external_attr = 0o100644 << 16
            archive.writestr(info, file.read_bytes())

    archive_errors = validate_zip(output_zip)
    if archive_errors:
        output_zip.unlink(missing_ok=True)
        sidecar.unlink(missing_ok=True)
        for error in sorted(set(archive_errors)):
            print(f"ERROR: {error}", file=sys.stderr)
        raise SystemExit(f"Build stopped: generated archive failed validation.")

    digest = hashlib.sha256(output_zip.read_bytes()).hexdigest()
    sidecar.write_text(f"{digest}  {output_zip.name}\n", encoding="utf-8")
    print(f"Built visual pack: {output_zip}")
    print(f"Checksum sidecar: {sidecar}")
    print(f"Files: {len(files)}")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--source-dir", type=Path, default=Path("media/visuals/visual-foundations"))
    parser.add_argument("--output", type=Path, default=Path("docs/sample-pack/aetherlearn-visual-foundations-1.1.0.zip"))
    args = parser.parse_args()
    build(args.source_dir, args.output)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
