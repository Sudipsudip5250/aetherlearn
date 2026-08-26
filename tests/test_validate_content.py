import json
import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))

from validate_content import (  # noqa: E402
    load_curriculum_registry,
    load_lessons,
    validate_graph,
    validate_lesson,
    verify_manifest,
)


SECTIONS = [
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


def lesson_text(module_id="dl-01-example", prerequisites=None, body_override=None):
    prerequisites = prerequisites or []
    body = body_override or "\n\n".join(
        f"## {section}\n\nExample content. [Reference](https://example.com/reference)."
        if section == "Further reading"
        else f"## {section}\n\nExample content."
        for section in SECTIONS
    )
    frontmatter = "\n".join(
        [
            "---",
            f"id: {module_id}",
            "title: Example lesson",
            "strand: digital-literacy",
            "level: beginner",
            "version: 1.0.0",
            f"prerequisites: {json.dumps(prerequisites)}",
            "estimated_minutes: 20",
            "availability: offline",
            "risk_tier: S0",
            "core_asset_bytes: 0",
            "optional_asset_bytes: 0",
            "objectives:",
            "  - Explain the example concept.",
            "  - Apply the example concept.",
            "review_status: draft",
            "last_reviewed: 2026-08-24",
            "---",
            "",
        ]
    )
    return frontmatter + body + "\n"


class ValidateContentTests(unittest.TestCase):
    def test_repository_sample_pack_is_valid(self):
        repo_root = Path(__file__).resolve().parents[1]
        content_root = repo_root / "content" / "core"
        known_ids, registry_errors = load_curriculum_registry(repo_root)
        lessons, errors = load_lessons(content_root)
        self.assertEqual(registry_errors, [])
        self.assertEqual(errors, [])
        all_errors = []
        for lesson in lessons:
            all_errors.extend(validate_lesson(lesson, content_root, repo_root))
        all_errors.extend(validate_graph(lessons, known_ids))
        self.assertEqual(all_errors, [])
        self.assertEqual(len(lessons), 5)

    def test_unknown_prerequisite_is_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            path = root / "dl-01-example.md"
            path.write_text(lesson_text(prerequisites=["dl-99-missing"]), encoding="utf-8")
            lessons, parse_errors = load_lessons(root)
            self.assertEqual(parse_errors, [])
            errors = validate_graph(lessons)
            self.assertTrue(any("unknown prerequisite" in error for error in errors))

    def test_prerequisite_cycle_is_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            (root / "dl-01-first.md").write_text(
                lesson_text("dl-01-first", ["dl-02-second"]), encoding="utf-8"
            )
            (root / "dl-02-second.md").write_text(
                lesson_text("dl-02-second", ["dl-01-first"]), encoding="utf-8"
            )
            lessons, parse_errors = load_lessons(root)
            self.assertEqual(parse_errors, [])
            errors = validate_graph(lessons)
            self.assertTrue(any("prerequisite cycle" in error for error in errors))

    def test_broken_internal_link_is_rejected(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            body = "\n\n".join(
                f"## {section}\n\n"
                + ("[Missing](missing.md)" if section == "Explanation" else "Example content.")
                for section in SECTIONS
            )
            path = root / "dl-01-example.md"
            path.write_text(lesson_text(body_override=body), encoding="utf-8")
            lessons, parse_errors = load_lessons(root)
            self.assertEqual(parse_errors, [])
            errors = validate_lesson(lessons[0], root, root)
            self.assertTrue(any("broken internal link" in error for error in errors))

    def test_manifest_verification_detects_tampering(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            path = root / "dl-01-example.md"
            path.write_text(lesson_text(), encoding="utf-8")
            lessons, parse_errors = load_lessons(root)
            self.assertEqual(parse_errors, [])
            manifest_path = root / "manifest.json"
            manifest_path.write_text(
                json.dumps(
                    {
                        "schema_version": 1,
                        "pack_id": "core",
                        "pack_version": "1.0.0",
                        "module_count": 0,
                        "total_module_bytes": 0,
                        "modules": [],
                    }
                ),
                encoding="utf-8",
            )
            errors = verify_manifest(manifest_path, lessons, root)
            self.assertTrue(any("manifest does not match" in error for error in errors))


if __name__ == "__main__":
    unittest.main()
