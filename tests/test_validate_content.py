import json
import sys
import tempfile
import unittest
from pathlib import Path

import yaml

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
    def test_repository_core_pack_is_valid(self):
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
        self.assertEqual(len(lessons), 37)
        self.assertEqual(len(known_ids), 37)
        self.assertTrue({lesson.module_id for lesson in lessons}.issuperset({
            "dl-01-digital-information",
            "dl-05-privacy-passwords-phishing",
            "dl-06-computing-language-history",
            "dl-07-how-programs-run",
            "dl-08-networks-web-concepts",
            "dev-04-testing-pure-functions",
            "dev-07-open-source-accessibility",
            "sec-01-ethics-scope-and-harm",
            "sec-04-security-organizations-and-roles",
            "web-01-semantic-html-accessibility",
            "web-04-data-modeling-and-json",
            "sec-05-morris-worm-history-and-response",
            "sec-06-cybersecurity-career-role-families",
        }))

    def test_registry_rejects_changed_mvp_baseline(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            (root / "content").mkdir()
            registry = {
                "schema_version": 2,
                "curriculum_id": "aetherlearn",
                "curriculum_version": "1.3.0",
                "mvp_baseline": {"id": "mvp-20", "module_count": 19, "modules": []},
                "stages": [],
                "modules": [],
            }
            (root / "content" / "curriculum.yml").write_text(yaml.safe_dump(registry), encoding="utf-8")
            _, errors = load_curriculum_registry(root)
            self.assertTrue(any("mvp_baseline.module_count must remain 20" in error for error in errors))
            self.assertTrue(any("mvp_baseline must preserve" in error for error in errors))

    def test_registry_rejects_unapproved_stage_module(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            (root / "content").mkdir()
            baseline = [{"id": f"dl-{index:02d}-placeholder", "title": "Placeholder"} for index in range(1, 21)]
            # Keep the fixture focused on stage validation; the baseline itself is intentionally invalid and must be reported too.
            registry = {
                "schema_version": 2,
                "curriculum_id": "aetherlearn",
                "curriculum_version": "1.3.0",
                "mvp_baseline": {"id": "mvp-20", "module_count": 20, "modules": baseline},
                "stages": [{"id": "stage-1", "title": "Stage 1", "status": "draft", "module_count": 1, "modules": [{"id": "dl-06-example", "title": "Example"}]}],
                "modules": [{"id": "dl-06-example", "title": "Example", "stage": "stage-1"}],
            }
            (root / "content" / "curriculum.yml").write_text(yaml.safe_dump(registry), encoding="utf-8")
            _, errors = load_curriculum_registry(root)
            self.assertTrue(any("non-approved stage" in error for error in errors))

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
