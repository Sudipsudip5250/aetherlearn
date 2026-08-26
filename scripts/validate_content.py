#!/usr/bin/env python3
"""Validate AetherLearn Markdown lessons and optionally verify a pack manifest."""

from __future__ import annotations

import argparse
import hashlib
import json
import re
import sys
from collections import defaultdict
from dataclasses import dataclass
from datetime import date, datetime
from pathlib import Path
from typing import Any

import yaml


REQUIRED_FIELDS = {
    "id",
    "title",
    "strand",
    "level",
    "version",
    "prerequisites",
    "estimated_minutes",
    "availability",
    "risk_tier",
    "core_asset_bytes",
    "optional_asset_bytes",
    "objectives",
    "review_status",
    "last_reviewed",
}
OPTIONAL_FIELDS = {"tags", "sources", "assets", "termux"}
REQUIRED_SECTIONS = [
    "Objectives",
    "Prerequisites",
    "Availability",
    "Explanation",
    "Worked example",
    "Common mistakes",
    "Offline practice",
    "Knowledge check",
    "Project or application",
    "Accessibility notes",
    "Safety and responsible use",
    "Further reading",
    "Change log",
]
AVAILABILITY = {"offline", "offline-pack", "termux-optional", "network-optional"}
RISK_TIERS = {"S0", "S1", "S2"}
STRANDS = {"digital-literacy", "python-fundamentals", "algorithms", "developer-foundations", "security-ethics"}
LEVELS = {"beginner", "intermediate"}
REVIEW_STATUSES = {"draft", "in-review", "released", "needs-update", "deprecated"}
REGISTRY_SCHEMA_VERSION = 2
REGISTRY_STAGE_STATUSES = {"approved", "draft", "in-review", "released", "deprecated"}
MVP_BASELINE_ID = "mvp-20"
MVP_BASELINE_IDS = (
    "dl-01-digital-information",
    "dl-02-files-folders-storage-backups",
    "dl-03-android-settings-permissions-apps",
    "dl-04-internet-browsers-urls-search",
    "dl-05-privacy-passwords-phishing",
    "py-01-problems-algorithms-instructions",
    "py-02-python-setup-expressions-values",
    "py-03-variables-types-input-output",
    "py-04-conditions-boolean-logic",
    "py-05-loops-repetition-tracing",
    "py-06-functions-scope-reusable-code",
    "py-07-lists-dictionaries-strings-data",
    "al-01-data-structures",
    "al-02-arrays-lists-stacks-queues",
    "al-03-searching-sorting",
    "al-04-complexity-growth",
    "al-05-recursion-trees-graphs",
    "dev-01-terminal-command-line",
    "dev-02-git-local-repositories-history",
    "dev-03-debugging-error-messages",
)
ID_PATTERN = re.compile(r"^(dl|py|al|dev|sec)-\d{2}(?:-[a-z0-9]+)+$")
SEMVER_PATTERN = re.compile(r"^\d+\.\d+\.\d+(?:-[0-9A-Za-z.-]+)?$")
LINK_PATTERN = re.compile(r"!?(?:\[[^\]]*\])\(([^)]+)\)")


@dataclass
class Lesson:
    path: Path
    metadata: dict[str, Any]
    body: str

    @property
    def module_id(self) -> str:
        return str(self.metadata.get("id", ""))


def sha256_bytes(data: bytes) -> str:
    return hashlib.sha256(data).hexdigest()


def parse_frontmatter(path: Path) -> tuple[dict[str, Any], str, list[str]]:
    errors: list[str] = []
    raw = path.read_text(encoding="utf-8")
    if not raw.startswith("---\n"):
        return {}, raw, [f"{path}: missing YAML frontmatter opening delimiter"]
    end = raw.find("\n---\n", 4)
    if end == -1:
        return {}, raw, [f"{path}: missing YAML frontmatter closing delimiter"]
    frontmatter = raw[4:end]
    body = raw[end + len("\n---\n") :]
    try:
        metadata = yaml.safe_load(frontmatter) or {}
    except yaml.YAMLError as exc:
        return {}, body, [f"{path}: invalid YAML frontmatter: {exc}"]
    if not isinstance(metadata, dict):
        errors.append(f"{path}: frontmatter must be a YAML mapping")
        metadata = {}
    return metadata, body, errors


def is_nonempty_string(value: Any) -> bool:
    return isinstance(value, str) and bool(value.strip())


def validate_date(value: Any) -> bool:
    if isinstance(value, (date, datetime)):
        return True
    if isinstance(value, str):
        try:
            date.fromisoformat(value)
            return True
        except ValueError:
            return False
    return False


def section_blocks(body: str) -> dict[str, str]:
    matches = list(re.finditer(r"^##\s+(.+?)\s*$", body, flags=re.MULTILINE))
    blocks: dict[str, str] = {}
    for index, match in enumerate(matches):
        title = match.group(1).strip()
        start = match.end()
        end = matches[index + 1].start() if index + 1 < len(matches) else len(body)
        blocks[title] = body[start:end].strip()
    return blocks


def validate_links(path: Path, body: str, repo_root: Path) -> list[str]:
    errors: list[str] = []
    for target in LINK_PATTERN.findall(body):
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
            errors.append(f"{path}: internal link escapes repository: {target}")
            continue
        if not resolved.exists():
            errors.append(f"{path}: broken internal link: {target}")
    return errors


def validate_assets(lesson: Lesson, content_root: Path) -> list[str]:
    errors: list[str] = []
    assets = lesson.metadata.get("assets", [])
    if assets is None:
        assets = []
    if not isinstance(assets, list):
        return [f"{lesson.path}: assets must be a list"]
    totals = {"core": 0, "optional": 0}
    for asset in assets:
        if not isinstance(asset, dict) or not is_nonempty_string(asset.get("path")):
            errors.append(f"{lesson.path}: every asset must have a non-empty path")
            continue
        kind = asset.get("kind", "core")
        if kind not in totals:
            errors.append(f"{lesson.path}: asset kind must be core or optional")
            continue
        asset_path = (content_root / asset["path"]).resolve()
        try:
            asset_path.relative_to(content_root.resolve())
        except ValueError:
            errors.append(f"{lesson.path}: asset path escapes content root: {asset['path']}")
            continue
        if not asset_path.is_file():
            errors.append(f"{lesson.path}: missing declared asset: {asset['path']}")
            continue
        actual_size = asset_path.stat().st_size
        declared_size = asset.get("bytes")
        if not isinstance(declared_size, int) or declared_size < 0:
            errors.append(f"{lesson.path}: asset bytes must be a non-negative integer: {asset['path']}")
        elif declared_size != actual_size:
            errors.append(
                f"{lesson.path}: asset size mismatch for {asset['path']}: "
                f"declared {declared_size}, actual {actual_size}"
            )
        totals[kind] += actual_size
    for key, field in (("core", "core_asset_bytes"), ("optional", "optional_asset_bytes")):
        if lesson.metadata.get(field) != totals[key]:
            errors.append(
                f"{lesson.path}: {field} mismatch: declared {lesson.metadata.get(field)!r}, "
                f"computed {totals[key]}"
            )
    return errors


def validate_lesson(lesson: Lesson, content_root: Path, repo_root: Path) -> list[str]:
    metadata = lesson.metadata
    errors: list[str] = []
    missing = REQUIRED_FIELDS - metadata.keys()
    unknown = set(metadata.keys()) - REQUIRED_FIELDS - OPTIONAL_FIELDS
    errors.extend(f"{lesson.path}: missing required field: {field}" for field in sorted(missing))
    errors.extend(f"{lesson.path}: unknown frontmatter field: {field}" for field in sorted(unknown))

    if not ID_PATTERN.fullmatch(str(metadata.get("id", ""))):
        errors.append(f"{lesson.path}: id must use stable lowercase prefix and kebab-case: {metadata.get('id')!r}")
    for field in ("title", "strand", "level", "version", "availability", "risk_tier", "review_status"):
        if not is_nonempty_string(metadata.get(field)):
            errors.append(f"{lesson.path}: {field} must be a non-empty string")
    if metadata.get("strand") not in STRANDS:
        errors.append(f"{lesson.path}: invalid strand: {metadata.get('strand')!r}")
    if metadata.get("level") not in LEVELS:
        errors.append(f"{lesson.path}: invalid level: {metadata.get('level')!r}")
    if not SEMVER_PATTERN.fullmatch(str(metadata.get("version", ""))):
        errors.append(f"{lesson.path}: version must be semantic-version shaped")
    if metadata.get("availability") not in AVAILABILITY:
        errors.append(f"{lesson.path}: invalid availability: {metadata.get('availability')!r}")
    if metadata.get("risk_tier") not in RISK_TIERS:
        errors.append(f"{lesson.path}: invalid or out-of-scope risk tier: {metadata.get('risk_tier')!r}")
    if metadata.get("review_status") not in REVIEW_STATUSES:
        errors.append(f"{lesson.path}: invalid review_status: {metadata.get('review_status')!r}")
    if not isinstance(metadata.get("estimated_minutes"), int) or metadata.get("estimated_minutes", 0) <= 0:
        errors.append(f"{lesson.path}: estimated_minutes must be a positive integer")
    for field in ("core_asset_bytes", "optional_asset_bytes"):
        if not isinstance(metadata.get(field), int) or metadata.get(field, -1) < 0:
            errors.append(f"{lesson.path}: {field} must be a non-negative integer")
    if not validate_date(metadata.get("last_reviewed")):
        errors.append(f"{lesson.path}: last_reviewed must be an ISO date")

    prerequisites = metadata.get("prerequisites")
    if not isinstance(prerequisites, list) or not all(is_nonempty_string(item) for item in prerequisites):
        errors.append(f"{lesson.path}: prerequisites must be a list of non-empty stable IDs")
    objectives = metadata.get("objectives")
    if not isinstance(objectives, list) or len(objectives) < 2 or not all(is_nonempty_string(item) for item in objectives):
        errors.append(f"{lesson.path}: objectives must contain at least two non-empty strings")
    if "sources" in metadata and (
        not isinstance(metadata["sources"], list)
        or not all(is_nonempty_string(item) and item.startswith(("http://", "https://")) for item in metadata["sources"])
    ):
        errors.append(f"{lesson.path}: sources must be an optional list of HTTP(S) URLs")
    if "tags" in metadata and (
        not isinstance(metadata["tags"], list) or not all(is_nonempty_string(item) for item in metadata["tags"])
    ):
        errors.append(f"{lesson.path}: tags must be an optional list of non-empty strings")

    if metadata.get("availability") == "termux-optional":
        termux = metadata.get("termux")
        if not isinstance(termux, dict) or not is_nonempty_string(termux.get("exercise_id")):
            errors.append(f"{lesson.path}: termux-optional modules require termux.exercise_id")
        elif not is_nonempty_string(termux.get("fallback")):
            errors.append(f"{lesson.path}: termux-optional modules require termux.fallback")
    elif "termux" in metadata and metadata["termux"] is not None:
        errors.append(f"{lesson.path}: termux metadata is only allowed for termux-optional modules")

    blocks = section_blocks(lesson.body)
    for section in REQUIRED_SECTIONS:
        if section not in blocks:
            errors.append(f"{lesson.path}: missing required body section: ## {section}")
        elif not blocks[section].strip():
            errors.append(f"{lesson.path}: required body section is empty: ## {section}")
    if "Further reading" in blocks and not LINK_PATTERN.search(blocks["Further reading"]):
        errors.append(f"{lesson.path}: Further reading must contain at least one Markdown link")
    if "Change log" in blocks and str(metadata.get("version", "")) not in blocks["Change log"]:
        errors.append(f"{lesson.path}: Change log must mention the current version")
    errors.extend(validate_links(lesson.path, lesson.body, repo_root))
    errors.extend(validate_assets(lesson, content_root))
    return errors


def load_curriculum_registry(repo_root: Path) -> tuple[set[str], list[str]]:
    registry_path = repo_root / "content" / "curriculum.yml"
    try:
        data = yaml.safe_load(registry_path.read_text(encoding="utf-8")) or {}
    except (OSError, yaml.YAMLError) as exc:
        return set(), [f"{registry_path}: cannot read curriculum registry: {exc}"]
    if not isinstance(data, dict):
        return set(), [f"{registry_path}: registry must be a YAML mapping"]

    errors: list[str] = []
    if data.get("schema_version") != REGISTRY_SCHEMA_VERSION:
        errors.append(
            f"{registry_path}: schema_version must be {REGISTRY_SCHEMA_VERSION} for the versioned post-MVP registry"
        )
    if not is_nonempty_string(data.get("curriculum_id")):
        errors.append(f"{registry_path}: curriculum_id must be a non-empty string")
    if not SEMVER_PATTERN.fullmatch(str(data.get("curriculum_version", ""))):
        errors.append(f"{registry_path}: curriculum_version must be semantic-version shaped")

    baseline = data.get("mvp_baseline")
    baseline_modules = baseline.get("modules") if isinstance(baseline, dict) else None
    if not isinstance(baseline, dict) or baseline.get("id") != MVP_BASELINE_ID:
        errors.append(f"{registry_path}: mvp_baseline.id must remain {MVP_BASELINE_ID}")
    if not isinstance(baseline, dict) or baseline.get("module_count") != len(MVP_BASELINE_IDS):
        errors.append(f"{registry_path}: mvp_baseline.module_count must remain {len(MVP_BASELINE_IDS)}")
    baseline_ids: list[str] = []
    if not isinstance(baseline_modules, list):
        errors.append(f"{registry_path}: mvp_baseline.modules must be a list")
    else:
        for item in baseline_modules:
            if isinstance(item, dict) and is_nonempty_string(item.get("id")):
                baseline_ids.append(str(item["id"]))
            else:
                errors.append(f"{registry_path}: every MVP baseline module needs a non-empty id")
        if tuple(baseline_ids) != MVP_BASELINE_IDS:
            errors.append(
                f"{registry_path}: mvp_baseline must preserve the exact stable {MVP_BASELINE_ID} module order and IDs"
            )

    stages = data.get("stages")
    stage_by_id: dict[str, dict[str, Any]] = {}
    if not isinstance(stages, list):
        errors.append(f"{registry_path}: stages must be a list")
    else:
        for stage in stages:
            if not isinstance(stage, dict) or not is_nonempty_string(stage.get("id")):
                errors.append(f"{registry_path}: each stage needs a non-empty id")
                continue
            stage_id = str(stage["id"])
            if stage_id in stage_by_id:
                errors.append(f"{registry_path}: duplicate curriculum stage ID: {stage_id}")
                continue
            stage_by_id[stage_id] = stage
            if not re.fullmatch(r"stage-\d+", stage_id):
                errors.append(f"{registry_path}: invalid curriculum stage ID: {stage_id}")
            if not is_nonempty_string(stage.get("title")):
                errors.append(f"{registry_path}: stage {stage_id} needs a non-empty title")
            if stage.get("status") not in REGISTRY_STAGE_STATUSES:
                errors.append(f"{registry_path}: stage {stage_id} has an invalid status")
            stage_modules = stage.get("modules")
            if not isinstance(stage_modules, list):
                errors.append(f"{registry_path}: stage {stage_id}.modules must be a list")
            elif stage.get("module_count") != len(stage_modules):
                errors.append(f"{registry_path}: stage {stage_id}.module_count does not match its modules")

    modules = data.get("modules")
    if not isinstance(modules, list):
        errors.append(f"{registry_path}: modules must be a list")
        return set(), errors

    ids: set[str] = set()
    module_stage: dict[str, str] = {}
    for item in modules:
        if not isinstance(item, dict) or not is_nonempty_string(item.get("id")):
            errors.append(f"{registry_path}: each module needs a non-empty id")
            continue
        module_id = str(item["id"])
        if module_id in ids:
            errors.append(f"{registry_path}: duplicate curriculum module ID: {module_id}")
        ids.add(module_id)
        if not ID_PATTERN.fullmatch(module_id):
            errors.append(f"{registry_path}: invalid curriculum module ID: {module_id}")
        if not is_nonempty_string(item.get("title")):
            errors.append(f"{registry_path}: module {module_id} needs a non-empty title")
        stage_id = item.get("stage")
        if stage_id == "mvp":
            if module_id not in MVP_BASELINE_IDS:
                errors.append(f"{registry_path}: non-baseline module cannot use stage mvp: {module_id}")
        elif is_nonempty_string(stage_id):
            module_stage[module_id] = str(stage_id)
            stage = stage_by_id.get(str(stage_id))
            if stage is None:
                errors.append(f"{registry_path}: module {module_id} references unknown stage: {stage_id}")
            elif stage.get("status") != "approved":
                errors.append(f"{registry_path}: module {module_id} is listed under a non-approved stage: {stage_id}")
        else:
            errors.append(f"{registry_path}: module {module_id} needs stage mvp or an approved stage ID")

    baseline_in_registry = tuple(
        item.get("id") for item in modules
        if isinstance(item, dict) and item.get("id") in MVP_BASELINE_IDS
    )
    if baseline_in_registry != MVP_BASELINE_IDS:
        errors.append(f"{registry_path}: the stable {MVP_BASELINE_ID} modules must retain their exact relative order")
    if set(ids) & set(MVP_BASELINE_IDS) != set(MVP_BASELINE_IDS):
        errors.append(f"{registry_path}: all stable {MVP_BASELINE_ID} modules must remain present")

    for stage_id, stage in stage_by_id.items():
        stage_modules = stage.get("modules", [])
        if not isinstance(stage_modules, list):
            continue
        for item in stage_modules:
            if not isinstance(item, dict) or not is_nonempty_string(item.get("id")):
                continue
            module_id = str(item["id"])
            if module_id not in ids:
                errors.append(f"{registry_path}: approved stage module is missing from modules: {module_id}")
            if module_stage.get(module_id) != stage_id:
                errors.append(f"{registry_path}: module {module_id} has inconsistent stage metadata")

    return ids, errors


def load_lessons(content_root: Path) -> tuple[list[Lesson], list[str]]:
    lessons: list[Lesson] = []
    errors: list[str] = []
    for path in sorted(content_root.glob("*.md")):
        if path.name.lower() == "readme.md":
            continue
        metadata, body, parse_errors = parse_frontmatter(path)
        errors.extend(parse_errors)
        lessons.append(Lesson(path=path, metadata=metadata, body=body))
    if not lessons:
        errors.append(f"{content_root}: no lesson Markdown files found")
    return lessons, errors


def validate_graph(lessons: list[Lesson], known_ids: set[str] | None = None) -> list[str]:
    errors: list[str] = []
    known_ids = known_ids or set()
    by_id: dict[str, Lesson] = {}
    for lesson in lessons:
        module_id = lesson.module_id
        if module_id in by_id:
            errors.append(f"duplicate stable module ID: {module_id} in {by_id[module_id].path} and {lesson.path}")
        elif module_id:
            by_id[module_id] = lesson
    graph: dict[str, list[str]] = defaultdict(list)
    for lesson in lessons:
        module_id = lesson.module_id
        prerequisites = lesson.metadata.get("prerequisites", [])
        if not isinstance(prerequisites, list):
            continue
        for prerequisite in prerequisites:
            if prerequisite not in by_id and prerequisite not in known_ids:
                errors.append(f"{lesson.path}: unknown prerequisite: {prerequisite}")
            elif prerequisite == module_id:
                errors.append(f"{lesson.path}: module cannot list itself as a prerequisite")
            else:
                graph[module_id].append(prerequisite)

    visiting: set[str] = set()
    visited: set[str] = set()

    def visit(node: str, trail: list[str]) -> None:
        if node in visiting:
            cycle = " -> ".join(trail + [node])
            errors.append(f"prerequisite cycle detected: {cycle}")
            return
        if node in visited:
            return
        visiting.add(node)
        for dependency in graph.get(node, []):
            visit(dependency, trail + [node])
        visiting.remove(node)
        visited.add(node)

    for module_id in by_id:
        visit(module_id, [])
    return errors


def manifest_entries(lessons: list[Lesson], content_root: Path) -> list[dict[str, Any]]:
    entries: list[dict[str, Any]] = []
    for lesson in lessons:
        payload = lesson.path.read_bytes()
        entries.append(
            {
                "id": lesson.module_id,
                "path": lesson.path.relative_to(content_root).as_posix(),
                "size": len(payload),
                "sha256": sha256_bytes(payload),
                "version": lesson.metadata.get("version"),
                "availability": lesson.metadata.get("availability"),
                "risk_tier": lesson.metadata.get("risk_tier"),
            }
        )
    return entries


def write_manifest(path: Path, lessons: list[Lesson], content_root: Path) -> None:
    entries = manifest_entries(lessons, content_root)
    manifest = {
        "schema_version": 1,
        "pack_id": "core",
        "pack_version": "1.0.0",
        "module_count": len(entries),
        "total_module_bytes": sum(item["size"] for item in entries),
        "modules": entries,
    }
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(manifest, indent=2, sort_keys=True) + "\n", encoding="utf-8")


def verify_manifest(path: Path, lessons: list[Lesson], content_root: Path) -> list[str]:
    errors: list[str] = []
    try:
        manifest = json.loads(path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as exc:
        return [f"{path}: cannot read manifest: {exc}"]
    actual = manifest_entries(lessons, content_root)
    expected = {
        "schema_version": 1,
        "pack_id": "core",
        "pack_version": "1.0.0",
        "module_count": len(actual),
        "total_module_bytes": sum(item["size"] for item in actual),
        "modules": actual,
    }
    if manifest != expected:
        errors.append(f"{path}: manifest does not match current content; rebuild it")
    return errors


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--content-dir", type=Path, default=Path("content/core"))
    parser.add_argument("--manifest", type=Path, help="Verify an existing deterministic manifest")
    parser.add_argument("--write-manifest", type=Path, help="Write a deterministic manifest after validation")
    args = parser.parse_args()

    content_root = args.content_dir.resolve()
    repo_root = Path(__file__).resolve().parents[1]
    known_ids, registry_errors = load_curriculum_registry(repo_root)
    lessons, errors = load_lessons(content_root)
    errors.extend(registry_errors)
    for lesson in lessons:
        errors.extend(validate_lesson(lesson, content_root, repo_root))
    errors.extend(validate_graph(lessons, known_ids))

    if not errors and args.manifest:
        errors.extend(verify_manifest(args.manifest, lessons, content_root))
    if not errors and args.write_manifest:
        write_manifest(args.write_manifest, lessons, content_root)

    if errors:
        for error in sorted(set(errors)):
            print(f"ERROR: {error}", file=sys.stderr)
        print(f"Validation failed with {len(set(errors))} error(s).", file=sys.stderr)
        return 1

    print(f"Validated {len(lessons)} lesson(s) in {content_root}.")
    if args.manifest:
        print(f"Verified manifest: {args.manifest}")
    if args.write_manifest:
        print(f"Wrote manifest: {args.write_manifest}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
