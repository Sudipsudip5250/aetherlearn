#!/usr/bin/env python3
"""Validate Android network, backup, and storage permission boundaries."""

from __future__ import annotations

import argparse
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ANDROID_NS = "{http://schemas.android.com/apk/res/android}"
REQUIRED_PERMISSIONS = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"}
FORBIDDEN_PERMISSIONS = {
    "android.permission.READ_EXTERNAL_STORAGE",
    "android.permission.WRITE_EXTERNAL_STORAGE",
    "android.permission.MANAGE_EXTERNAL_STORAGE",
}


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("manifest", type=Path, nargs="?", default=Path("android/app/src/main/AndroidManifest.xml"))
    args = parser.parse_args()
    root = ET.parse(args.manifest).getroot()
    permissions = {node.get(f"{ANDROID_NS}name", "") for node in root.findall("uses-permission")}
    errors: list[str] = []
    missing = REQUIRED_PERMISSIONS - permissions
    if missing:
        errors.append(f"missing required network permission(s): {', '.join(sorted(missing))}")
    forbidden = FORBIDDEN_PERMISSIONS & permissions
    if forbidden:
        errors.append(f"forbidden broad-storage permission(s): {', '.join(sorted(forbidden))}")
    application = root.find("application")
    if application is None:
        errors.append("application element is missing")
    else:
        if application.get(f"{ANDROID_NS}allowBackup") != "false":
            errors.append("android:allowBackup must be false")
        if application.get(f"{ANDROID_NS}usesCleartextTraffic") != "false":
            errors.append("android:usesCleartextTraffic must be false")
    if errors:
        for error in errors:
            print(f"ERROR: {error}", file=sys.stderr)
        return 1
    print(f"Android manifest boundary checks passed: {args.manifest}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
