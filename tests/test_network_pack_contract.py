from __future__ import annotations

import json
import subprocess
import sys
import tempfile
import unittest
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


class NetworkPackContractTests(unittest.TestCase):
    def test_generated_pack_has_safe_manifest_and_module_paths(self) -> None:
        with tempfile.TemporaryDirectory() as temporary:
            output = Path(temporary) / "pack"
            subprocess.run(
                [sys.executable, "scripts/build_pack.py", "--content-dir", "content/core", "--output-dir", str(output)],
                cwd=ROOT,
                check=True,
                capture_output=True,
                text=True,
            )
            manifest = json.loads((output / "manifest.json").read_text(encoding="utf-8"))
            archive_path = output.with_suffix(".zip")
            self.assertEqual(manifest["module_count"], len(manifest["modules"]))
            expected = {"manifest.json", *(f"modules/{entry['path']}" for entry in manifest["modules"])}
            with zipfile.ZipFile(archive_path) as archive:
                files = {entry.filename for entry in archive.infolist() if not entry.is_dir()}
                self.assertEqual(files, expected)
                self.assertTrue(all(".." not in name and name.startswith("modules/") for name in files if name != "manifest.json"))


if __name__ == "__main__":
    unittest.main()
