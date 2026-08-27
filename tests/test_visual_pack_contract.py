from __future__ import annotations

import json
import shutil
import sys
import zipfile
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "scripts"))

from build_visual_pack import build  # noqa: E402
from check_visual_pack_mirrors import check_mirrors  # noqa: E402
from validate_visual_pack import validate_directory, validate_zip  # noqa: E402


ROOT = Path(__file__).resolve().parents[1]
FIXTURE = ROOT / "media" / "visuals" / "visual-foundations"


class VisualPackContractTests(unittest.TestCase):
    def copy_fixture(self) -> Path:
        temp_root = Path(tempfile.mkdtemp(prefix="aetherlearn-visual-pack-"))
        self.addCleanup(shutil.rmtree, temp_root, ignore_errors=True)
        destination = temp_root / "visual-foundations"
        shutil.copytree(FIXTURE, destination)
        return destination

    def read_manifest(self, pack_dir: Path) -> dict:
        return json.loads((pack_dir / "manifest.json").read_text(encoding="utf-8"))

    def write_manifest(self, pack_dir: Path, manifest: dict) -> None:
        (pack_dir / "manifest.json").write_text(json.dumps(manifest, indent=2) + "\n", encoding="utf-8")

    def test_repository_visual_pack_is_valid(self) -> None:
        self.assertEqual(validate_directory(FIXTURE), [])

    def test_missing_asset_is_rejected(self) -> None:
        pack_dir = self.copy_fixture()
        (pack_dir / "assets" / "dl-01-bits-bytes.svg").unlink()
        errors = validate_directory(pack_dir)
        self.assertTrue(any("missing declared asset file" in error for error in errors))

    def test_wrong_hash_is_rejected(self) -> None:
        pack_dir = self.copy_fixture()
        manifest = self.read_manifest(pack_dir)
        manifest["assets"][0]["sha256"] = "0" * 64
        self.write_manifest(pack_dir, manifest)
        errors = validate_directory(pack_dir)
        self.assertTrue(any("SHA-256 mismatch" in error for error in errors))

    def test_unsafe_path_is_rejected(self) -> None:
        pack_dir = self.copy_fixture()
        manifest = self.read_manifest(pack_dir)
        manifest["assets"][0]["path"] = "assets/../escape.svg"
        self.write_manifest(pack_dir, manifest)
        errors = validate_directory(pack_dir)
        self.assertTrue(any("safe relative path" in error for error in errors))

    def test_unsupported_mime_is_rejected(self) -> None:
        pack_dir = self.copy_fixture()
        manifest = self.read_manifest(pack_dir)
        manifest["assets"][0]["mime"] = "application/javascript"
        self.write_manifest(pack_dir, manifest)
        errors = validate_directory(pack_dir)
        self.assertTrue(any("unsupported MIME" in error for error in errors))

    def test_missing_accessibility_metadata_is_rejected(self) -> None:
        pack_dir = self.copy_fixture()
        manifest = self.read_manifest(pack_dir)
        manifest["assets"][0]["alt_text"] = ""
        self.write_manifest(pack_dir, manifest)
        errors = validate_directory(pack_dir)
        self.assertTrue(any("alt_text must be non-empty" in error for error in errors))

    def test_active_svg_content_is_rejected(self) -> None:
        pack_dir = self.copy_fixture()
        asset = pack_dir / "assets" / "dl-01-bits-bytes.svg"
        asset.write_text(asset.read_text(encoding="utf-8").replace("</svg>", "<script>alert(1)</script></svg>"), encoding="utf-8")
        errors = validate_directory(pack_dir)
        self.assertTrue(any("forbidden active-content marker" in error for error in errors))

    def test_duplicate_asset_id_is_rejected(self) -> None:
        pack_dir = self.copy_fixture()
        manifest = self.read_manifest(pack_dir)
        manifest["assets"][1]["asset_id"] = manifest["assets"][0]["asset_id"]
        self.write_manifest(pack_dir, manifest)
        errors = validate_directory(pack_dir)
        self.assertTrue(any("duplicate asset_id" in error for error in errors))

    def test_size_mismatch_is_rejected(self) -> None:
        pack_dir = self.copy_fixture()
        manifest = self.read_manifest(pack_dir)
        manifest["assets"][0]["installed_bytes"] += 1
        self.write_manifest(pack_dir, manifest)
        errors = validate_directory(pack_dir)
        self.assertTrue(any("installed_bytes mismatch" in error for error in errors))

    def test_unsafe_zip_member_is_rejected(self) -> None:
        temp_root = Path(tempfile.mkdtemp(prefix="aetherlearn-visual-zip-"))
        self.addCleanup(shutil.rmtree, temp_root, ignore_errors=True)
        archive = temp_root / "unsafe.zip"
        with zipfile.ZipFile(archive, "w") as handle:
            handle.writestr("../escape.svg", b"not safe")
        errors = validate_zip(archive)
        self.assertTrue(any("unsafe archive member" in error for error in errors))

    def test_client_mirrors_are_byte_identical(self) -> None:
        self.assertEqual(check_mirrors(ROOT), [])

    def test_builder_is_deterministic(self) -> None:
        temp_root = Path(tempfile.mkdtemp(prefix="aetherlearn-visual-build-"))
        self.addCleanup(shutil.rmtree, temp_root, ignore_errors=True)
        first = temp_root / "first.zip"
        second = temp_root / "second.zip"
        build(FIXTURE, first)
        build(FIXTURE, second)
        self.assertEqual(first.read_bytes(), second.read_bytes())


if __name__ == "__main__":
    unittest.main()
