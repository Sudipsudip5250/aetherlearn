# AetherLearn Android signing guide

**Audience:** an authorized release operator who owns the project’s release identity. This guide is human-operated. No keystore, signing password, private key, certificate, or secret configuration is included in the repository.

## Signing boundary

The repository’s `release` variant currently builds `app-release-unsigned.apk`. A successful build and checksum prove only that the artifact was produced and matches its checksum; they do not prove publisher identity or authenticity. A production release requires an authorized operator to create or use a protected release key, sign the APK, verify the signed artifact, and publish it through an approved distribution process.

The private key is the authority that lets a future APK update the installed application. Anyone who obtains it may be able to publish a malicious update Android treats as the same application. Treat the keystore and its passwords as high-impact secrets. Do not send them through chat, email, issue trackers, screenshots, source control, or unprotected artifact storage.

## 1. Decide ownership and storage first

Before creating a key, record the release owner, approved custodians, encrypted offline backup location, rotation/revocation procedure, and package identity `com.aetherlearn.app`. Use a dedicated encrypted password manager and encrypted offline backup controlled by the release owner. Do not store the only copy on a laptop, CI workspace, or repository.

Choose a keystore path outside the repository, for example:

```text
/home/release-operator/secure/aetherlearn-release.jks
```

This example path is illustrative only. Replace it with a location protected by the release owner’s operating system and backup policy. Never create a real keystore as part of an automated repository task.

## 2. Create the keystore interactively

Run the following on the release operator’s secured machine. `keytool` prompts for passwords and certificate fields; do not put password values into the command line or shell history.

```bash
mkdir -p /home/release-operator/secure
umask 077
keytool -genkeypair \
  -v \
  -keystore /home/release-operator/secure/aetherlearn-release.jks \
  -alias aetherlearn-release \
  -keyalg RSA \
  -keysize 4096 \
  -validity 10000
```

Use a project-owned, non-personal certificate identity according to the project’s legal and operational policy. Record the alias, creation date, expiry date, certificate SHA-256 fingerprint, and custodians in the private release inventory, not in a public repository.

Immediately restrict the file and inspect its certificate metadata:

```bash
chmod 600 /home/release-operator/secure/aetherlearn-release.jks
keytool -list -v \
  -keystore /home/release-operator/secure/aetherlearn-release.jks \
  -alias aetherlearn-release
```

The command prompts for the password. Compare the certificate fingerprint with the private release inventory. Do not paste the password or full keystore contents into a report.

## 3. Back up and test recovery

Create at least one encrypted backup in a separate controlled location. Test restoring a copy on a separate secured machine before depending on it for a release. Keep original and backup access paths separate. Record who can retrieve the backup and how access is revoked when a custodian leaves.

A checksum of a keystore is not a replacement for encrypted storage or access control. Do not upload the keystore to GitHub, an issue, a public file host, or an artifact store intended for APKs.

## 4. Connect signing to a local release build

The repository intentionally does not contain a signing configuration or secret values. A release operator may configure Android signing locally or through an approved protected CI environment. Keep the keystore path and passwords outside the repository, preferably in environment variables or the operating-system/CI secret store. Never print them.

After authorized local configuration is in place, build the release variant:

```bash
cd android
./gradlew --no-daemon assembleRelease
```

The unsigned baseline output is:

```text
android/app/build/outputs/apk/release/app-release-unsigned.apk
```

The signed output path depends on the release operator’s approved Gradle signing configuration. Record the exact signed artifact path privately and keep unsigned and signed artifacts clearly separated.

Verify the signed artifact with Android’s signing tools:

```bash
apksigner verify --verbose --print-certs /path/to/aetherlearn-release.apk
```

Confirm the package name, signer certificate fingerprint, and verification result. Then compute a checksum for the exact file that will be transferred:

```bash
sha256sum /path/to/aetherlearn-release.apk
```

A checksum is useful for transfer integrity; the signer certificate is the publisher-identity check. Retain both in the private release record and publish only information approved by the release owner.

## 5. CI signing policy

The public Quality workflow builds and uploads debug and unsigned release artifacts only. Do not add a private keystore or signing password to that workflow. If signing is later added to CI, it must use a protected release environment, least-privilege permissions, restricted artifact access, secret masking, manual approval, pinned actions, auditable logs without secret values, and a documented rollback/revocation procedure.

Pull requests from untrusted contributors must not gain access to signing secrets. A public workflow should continue to build unsigned artifacts unless the project deliberately establishes a protected release workflow.

## 6. Rotation, compromise, and loss

Plan key rotation before the first public release. Android application updates generally depend on continuity of the recognized signing identity, so do not rotate casually or delete the old key. Confirm the chosen distribution path’s key-upgrade mechanism before changing keys.

If the keystore or password may have been exposed, stop distribution, preserve relevant logs, revoke or rotate according to the distribution provider’s process, assess affected artifacts, and document the incident. If the only copy is lost, do not invent a replacement identity and call it an update; consult the approved distribution and application-identity recovery process first.

## 7. Pre-release checklist

| Check | Owner | Result |
|---|---|---|
| Package identity is confirmed as `com.aetherlearn.app` | Release owner |  |
| Keystore is outside the repository and encrypted at rest | Release owner |  |
| Passwords are stored in an approved password manager or protected secret store | Release owner |  |
| Encrypted backup was restored and tested | Release owner |  |
| Certificate fingerprint is recorded privately | Release owner |  |
| Release artifact is signed with the authorized identity | Release owner |  |
| `apksigner verify` passes and signer fingerprint is correct | Release owner |  |
| APK checksum is recorded for the exact distributable file | Release owner |  |
| Device checklist passed on representative environments | Tester / release owner |  |
| Public pack host and distribution channel are approved | Release owner |  |
| No private key, password, or personal test data is committed | Everyone |  |

The project is not production-signed merely because this guide exists. Signing, distribution, and key custody remain human-owned release gates.

## References

[1]: https://developer.android.com/studio/publish/app-signing "Android Developers: Sign your app"
[2]: https://developer.android.com/studio/command-line/apksigner "Android Developers: apksigner"
[3]: https://docs.github.com/en/actions/security-for-github-actions/security-guides/security-hardening-for-github-actions "GitHub Docs: Security hardening for GitHub Actions"
