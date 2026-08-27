#!/usr/bin/env python3
"""Validate AetherLearn optional visual packs without executing pack content."""

from __future__ import annotations

import argparse
import hashlib
import json
import re
import sys
import zipfile
from pathlib import Path, PurePosixPath
from typing import Any, Iterable

import yaml


SCHEMA_VERSION = 1
PACK_KIND = "visuals"
DISTRIBUTION_STATUSES = {"unsigned-development", "signed"}
ALLOWED_MIME = {"image/svg+xml", "image/png", "image/webp"}
MAX_ASSETS = 24
MAX_ASSET_BYTES = 256 * 1024
MAX_INSTALLED_BYTES = 2 * 1024 * 1024
MAX_ARCHIVE_MEMBERS = MAX_ASSETS + 1
MAX_ARCHIVE_UNCOMPRESSED_BYTES = MAX_INSTALLED_BYTES + 64 * 1024
SEMVER = re.compile(r"^\d+\.\d+\.\d+(?:-[0-9A-Za-z.-]+)?$")
SHA256 = re.compile(r"^[0-9a-f]{64}$")
PACK_ID = re.compile(r"^[a-z0-9]+(?:-[a-z0-9]+)+$")


def sha256_bytes(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def nonempty(value: Any) -> bool:
    return isinstance(value, str) and bool(value.strip())


def safe_relative_path(value: Any) -> str | None:
    if not nonempty(value):
        return None
    path = PurePosixPath(str(value))
    if path.is_absolute() or ".." in path.parts or "." in path.parts:
        return None
    normalized = path.as_posix()
    if normalized != str(value).replace("\\", "/"):
        return None
    return normalized


def load_known_module_ids(repo_root: Path) -> set[str]:
    registry = repo_root / "content" / "curriculum.yml"
    data = yaml.safe_load(registry.read_text(encoding="utf-8")) or {}
    ids: set[str] = set()
    for item in data.get("modules", []):
        if isinstance(item, dict) and nonempty(item.get("id")):
            ids.add(str(item["id"]))
    return ids


def validate_svg(data: bytes, label: str) -> list[str]:
    text = data.decode("utf-8", errors="strict")
    lowered = text.lower()
    errors: list[str] = []
    if not lowered.lstrip().startswith("<svg") and "<svg" not in lowered[:512]:
        errors.append(f"{label}: SVG must contain an SVG root element")
    for marker in ("<script", "javascript:", "onload=", "onclick=", "<foreignobject"):
        if marker in lowered:
            errors.append(f"{label}: SVG contains forbidden active-content marker: {marker}")
    return errors


def validate_manifest_and_files(
    manifest: Any,
    files: dict[str, bytes],
    repo_root: Path,
    source_label: str,
) -> list[str]:
    errors: list[str] = []
    if not isinstance(manifest, dict):
        return [f"{source_label}: manifest must be a JSON object"]

    required = {
        "schema_version", "pack_id", "pack_version", "pack_kind", "minimum_app_version",
        "name", "description", "created_at", "expires_at", "module_ids", "compressed_bytes", "installed_bytes", "assets",
        "distribution_status", "revocation_status", "signature",
    }
    errors.extend(f"{source_label}: missing manifest field: {field}" for field in sorted(required - manifest.keys()))
    if manifest.get("schema_version") != SCHEMA_VERSION:
        errors.append(f"{source_label}: schema_version must be {SCHEMA_VERSION}")
    if not PACK_ID.fullmatch(str(manifest.get("pack_id", ""))):
        errors.append(f"{source_label}: pack_id must be lowercase kebab-case")
    for field in ("pack_version", "minimum_app_version"):
        if not SEMVER.fullmatch(str(manifest.get(field, ""))):
            errors.append(f"{source_label}: {field} must be semantic-version shaped")
    if manifest.get("pack_kind") != PACK_KIND:
        errors.append(f"{source_label}: pack_kind must be {PACK_KIND}")
    for field in ("name", "description", "created_at"):
        if not nonempty(manifest.get(field)):
            errors.append(f"{source_label}: {field} must be non-empty")
    if manifest.get("expires_at") is not None and not nonempty(manifest.get("expires_at")):
        errors.append(f"{source_label}: expires_at must be null or non-empty")
    if manifest.get("distribution_status") not in DISTRIBUTION_STATUSES:
        errors.append(f"{source_label}: distribution_status must be unsigned-development or signed")
    if manifest.get("revocation_status") != "not-revoked":
        errors.append(f"{source_label}: revocation_status must be not-revoked for the local proof of concept")
    if manifest.get("distribution_status") == "signed":
        signature = manifest.get("signature")
        if not isinstance(signature, dict) or not all(nonempty(signature.get(key)) for key in ("algorithm", "key_id", "value")):
            errors.append(f"{source_label}: signed packs require algorithm, key_id, and value signature metadata")
    elif manifest.get("signature") is not None:
        errors.append(f"{source_label}: unsigned-development packs must have a null signature")

    module_ids = manifest.get("module_ids")
    known_ids = load_known_module_ids(repo_root)
    if not isinstance(module_ids, list) or not module_ids or not all(nonempty(item) for item in module_ids):
        errors.append(f"{source_label}: module_ids must be a non-empty list of stable lesson IDs")
    else:
        unknown = sorted(set(module_ids) - known_ids)
        errors.extend(f"{source_label}: unknown module ID: {module_id}" for module_id in unknown)

    assets = manifest.get("assets")
    if not isinstance(assets, list) or not assets:
        errors.append(f"{source_label}: assets must be a non-empty list")
        assets = []
    if len(assets) > MAX_ASSETS:
        errors.append(f"{source_label}: asset count exceeds {MAX_ASSETS}")

    declared_paths = {"manifest.json"}
    asset_ids: set[str] = set()
    asset_paths: set[str] = set()
    installed_total = 0
    compressed_total = 0
    for index, asset in enumerate(assets):
        label = f"{source_label}: asset[{index}]"
        if not isinstance(asset, dict):
            errors.append(f"{label} must be an object")
            continue
        for field in ("asset_id", "module_id", "path", "kind", "mime", "compressed_bytes", "installed_bytes", "sha256", "required", "alt_text", "caption", "text_equivalent", "license", "attribution", "source_url", "author", "locale", "reduced_motion_alternative"):
            if field not in asset:
                errors.append(f"{label} missing field: {field}")
        module_id = asset.get("module_id")
        if not isinstance(module_id, str) or module_id not in known_ids:
            errors.append(f"{label}.module_id must identify a known lesson")
        asset_id = str(asset.get("asset_id", ""))
        if not PACK_ID.fullmatch(asset_id):
            errors.append(f"{label}.asset_id must be lowercase kebab-case")
        if asset_id in asset_ids:
            errors.append(f"{label}: duplicate asset_id: {asset_id}")
        asset_ids.add(asset_id)
        path = safe_relative_path(asset.get("path"))
        if path is None or not path.startswith("assets/"):
            errors.append(f"{label}.path must be a safe relative path below assets/")
            path = str(asset.get("path", ""))
        if path in asset_paths:
            errors.append(f"{label}: duplicate asset path: {path}")
        asset_paths.add(path)
        declared_paths.add(path)
        mime = asset.get("mime")
        if mime not in ALLOWED_MIME:
            errors.append(f"{label}: unsupported MIME type: {mime!r}")
        for field in ("alt_text", "caption", "text_equivalent", "license", "attribution", "author", "locale", "reduced_motion_alternative"):
            if not nonempty(asset.get(field)):
                errors.append(f"{label}.{field} must be non-empty")
        source_url = asset.get("source_url")
        if source_url is not None and (not nonempty(source_url) or not str(source_url).startswith("https://")):
            errors.append(f"{label}.source_url must be null or an HTTPS URL")
        if not isinstance(asset.get("required"), bool):
            errors.append(f"{label}.required must be boolean")
        for field in ("compressed_bytes", "installed_bytes"):
            if not isinstance(asset.get(field), int) or asset.get(field, -1) < 0:
                errors.append(f"{label}.{field} must be a non-negative integer")
        data = files.get(path)
        if data is None:
            errors.append(f"{label}: missing declared asset file: {path}")
            continue
        actual_size = len(data)
        if actual_size > MAX_ASSET_BYTES:
            errors.append(f"{label}: asset exceeds {MAX_ASSET_BYTES} bytes: {path}")
        if asset.get("installed_bytes") != actual_size:
            errors.append(f"{label}: installed_bytes mismatch for {path}: declared {asset.get('installed_bytes')}, actual {actual_size}")
        if asset.get("compressed_bytes") != actual_size:
            errors.append(f"{label}: compressed_bytes must equal the deterministic source-payload size for this fixture: {path}")
        digest = asset.get("sha256")
        if not isinstance(digest, str) or not SHA256.fullmatch(digest):
            errors.append(f"{label}.sha256 must be lowercase hexadecimal SHA-256")
        elif digest != sha256_bytes(data):
            errors.append(f"{label}: SHA-256 mismatch for {path}")
        if mime == "image/svg+xml":
            try:
                errors.extend(validate_svg(data, f"{source_label}: {path}"))
            except UnicodeDecodeError:
                errors.append(f"{source_label}: {path}: SVG must be UTF-8")
        installed_total += actual_size
        compressed_total += actual_size

    if not isinstance(manifest.get("installed_bytes"), int) or manifest.get("installed_bytes") != installed_total:
        errors.append(f"{source_label}: installed_bytes total mismatch: declared {manifest.get('installed_bytes')}, computed {installed_total}")
    if not isinstance(manifest.get("compressed_bytes"), int) or manifest.get("compressed_bytes") != compressed_total:
        errors.append(f"{source_label}: compressed_bytes total mismatch: declared {manifest.get('compressed_bytes')}, computed {compressed_total}")
    if installed_total > MAX_INSTALLED_BYTES:
        errors.append(f"{source_label}: installed payload exceeds {MAX_INSTALLED_BYTES} bytes")

    extra = sorted(set(files) - declared_paths)
    errors.extend(f"{source_label}: undeclared file in pack: {path}" for path in extra)
    missing = sorted(declared_paths - set(files))
    errors.extend(f"{source_label}: required file missing from pack: {path}" for path in missing)
    return errors


def validate_directory(path: Path, repo_root: Path | None = None) -> list[str]:
    path = path.resolve()
    repo_root = (repo_root or Path(__file__).resolve().parents[1]).resolve()
    manifest_path = path / "manifest.json"
    if not manifest_path.is_file():
        return [f"{path}: missing manifest.json"]
    try:
        manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as exc:
        return [f"{manifest_path}: invalid JSON: {exc}"]
    files = {file.relative_to(path).as_posix(): file.read_bytes() for file in path.rglob("*") if file.is_file()}
    return validate_manifest_and_files(manifest, files, repo_root, str(path))


def validate_zip(path: Path, repo_root: Path | None = None) -> list[str]:
    path = path.resolve()
    repo_root = (repo_root or Path(__file__).resolve().parents[1]).resolve()
    try:
        with zipfile.ZipFile(path) as archive:
            infos = archive.infolist()
            if len(infos) > MAX_ARCHIVE_MEMBERS:
                return [f"{path}: archive has too many members"]
            files: dict[str, bytes] = {}
            total = 0
            errors: list[str] = []
            for info in infos:
                member = PurePosixPath(info.filename)
                if member.is_absolute() or ".." in member.parts or "." in member.parts:
                    errors.append(f"{path}: unsafe archive member: {info.filename}")
                    continue
                if info.is_dir():
                    errors.append(f"{path}: directory entries are not allowed: {info.filename}")
                    continue
                total += info.file_size
                if total > MAX_ARCHIVE_UNCOMPRESSED_BYTES:
                    errors.append(f"{path}: decompressed archive size exceeds safety limit")
                    break
                if info.filename in files:
                    errors.append(f"{path}: duplicate archive member: {info.filename}")
                    continue
                files[info.filename] = archive.read(info)
            if errors:
                return errors
            if "manifest.json" not in files:
                return [f"{path}: missing manifest.json"]
            try:
                manifest = json.loads(files["manifest.json"].decode("utf-8"))
            except (UnicodeDecodeError, json.JSONDecodeError) as exc:
                return [f"{path}: invalid manifest JSON: {exc}"]
            return validate_manifest_and_files(manifest, files, repo_root, str(path))
    except (OSError, zipfile.BadZipFile) as exc:
        return [f"{path}: cannot read ZIP: {exc}"]


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("path", type=Path, help="visual-pack directory or ZIP archive")
    args = parser.parse_args()
    errors = validate_zip(args.path) if args.path.suffix.lower() == ".zip" else validate_directory(args.path)
    if errors:
        for error in sorted(set(errors)):
            print(f"ERROR: {error}", file=sys.stderr)
        return 1
    print(f"Validated visual pack: {args.path}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
