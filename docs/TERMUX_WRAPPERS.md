# M5–M6 Termux Wrapper Contract

## Scope

This document defines the version 1 Termux pilot contract used by the native Android client. It selects **four** Termux-optional exercises from the eleven-module core pack: PY-02, PY-03, PY-05, and DEV-01. All are S1, local-only, read-only or deterministic, and completable without Termux.

The contract and allowlist are implemented in the Android client. Package detection, setup guidance, confirmation UI, and the guarded native handoff are also implemented. The pilot intentionally does not request a result callback: completion remains learner-confirmed, and runtime device testing remains the final evidence-limited M5 gate.

## Contract version 1

A wrapper is an immutable application-owned record. The user may review it but cannot edit its executable, arguments, working directory, lesson text, or completion policy. The app constructs a `TermuxLaunchRequest` only from a known wrapper ID.

```text
TermuxWrapperContract {
  contract_version: positive integer
  wrapper_id: stable identifier
  lesson_id: approved lesson identifier
  title: human-readable exercise title
  executable: absolute Termux executable path
  args: ordered fixed argument list
  working_directory: dedicated local practice directory
  prerequisites: user-visible setup requirements
  expected_effects: user-visible read-only or deterministic effects
  fallback: offline alternative that completes the learning objective
  completion_mode: learner-confirmed | validated-local-result
  network_required: false for every wrapper
}
```

The contract intentionally has no field for free-form command text, user-entered arguments, remote hosts, credentials, environment-variable injection, shared-storage paths, package installation, or network destinations. The bridge must pass executable and argument arrays separately to Termux; it must not reconstruct a shell command string.

## Selected pilot exercises

| Wrapper ID | Lesson | Fixed executable | Fixed arguments | Working directory | Effects | Offline fallback |
|---|---|---|---|---|---|---|
| `py-02-local-expressions` | `py-02-python-setup-expressions-values` | `/data/data/com.termux/files/usr/bin/python` | `-c`, `print(2 * (3 + 4))`, `print("learn" + " " + "locally")` | `/data/data/com.termux/files/home/aetherlearn-practice/py-02` | Prints `14` and `learn locally`; changes no files and uses no network | Complete the in-app tracing exercise and record predicted outputs in notes |
| `py-03-local-variables-output` | `py-03-variables-types-input-output` | `/data/data/com.termux/files/usr/bin/python` | `-c`, `name = "Learner"`, `minutes = 25`, `print(name)`, `print(minutes)` | `/data/data/com.termux/files/home/aetherlearn-practice/py-03` | Prints `Learner` and `25`; changes no files and uses no network | Complete the variable-tracing exercise and record each predicted output in notes |
| `py-05-local-loop-trace` | `py-05-loops-repetition-tracing` | `/data/data/com.termux/files/usr/bin/python` | `-c`, `for step in range(1, 4):`, `print(step * 10)` | `/data/data/com.termux/files/home/aetherlearn-practice/py-05` | Prints `10`, `20`, and `30`; changes no files and uses no network | Complete the loop-tracing table and record the predicted output in notes |
| `dev-01-safe-navigation` | `dev-01-terminal-command-line` | `/data/data/com.termux/files/usr/bin/ls` | `-la` | `/data/data/com.termux/files/home/aetherlearn-practice/dev-01` | Reads a dedicated local practice directory; changes no files and uses no network | Use the in-app fictional directory-tree simulator and predict `pwd`, `ls`, and `cd notes` results |

The Python wrappers use fixed `-c` argument arrays and do not accept Python code from the learner. The developer wrapper is deliberately limited to a read-only directory listing. None of the wrappers installs packages, accesses shared storage, runs shell built-ins through `sh -c`, uses `find`, reads arbitrary paths, or invokes a network tool.

## User-visible confirmation requirements

The implemented confirmation screen shows the wrapper ID and contract version, the exact executable and arguments, the working directory, prerequisites, expected effects, and the offline fallback. The user must explicitly confirm. If Termux is absent, the integration permission is denied, or the external-command setting is unavailable, the app shows setup guidance and keeps the fallback available.

The learner must explicitly mark the exercise complete after returning to AetherLearn. A process exit code, terminal text, callback, or deep-link opening is not completion proof. This pilot uses `learner-confirmed` completion and does not import result files. The bridge sends only the documented action, explicit Termux service component, command path, fixed argument array, fixed working directory, foreground flag, and session-action field; it does not request terminal output or a pending-intent callback.

## Validation rules

The registry validator must reject an unknown wrapper ID, contract-version mismatch, executable mismatch, altered argument order or values, working-directory mismatch, missing lesson mapping, network-required wrapper, relative executable, shared-storage path, shell metacharacter in a fixed argument, package-install command, SSH/remote-host argument, or any request containing user-authored command text. Every registry entry must have a non-empty fallback and must be mapped to an existing `termux-optional` S1 lesson.

## Threat and residual-risk summary

The main trust boundary is the app-to-Termux intent. The app treats Termux as an external, untrusted runtime. The allowlist narrows the command surface; fixed paths and arguments prevent command injection; dedicated private working directories reduce accidental data exposure; explicit confirmation prevents silent execution; and learner-confirmed completion prevents false progress updates. Residual risks include a compromised device, a malicious Termux installation, or a learner independently running unsafe commands outside AetherLearn. The app must not claim to eliminate those risks.

## References

The design follows the project’s existing safety and architecture rules in [`SAFETY.md`](SAFETY.md) and [`ARCHITECTURE.md`](ARCHITECTURE.md), especially the Termux-specific controls and the explicit-confirmation flow. The eventual handoff must be implemented against the Termux project’s documented [RUN_COMMAND Intent](https://github.com/termux/termux-app/wiki/RUN_COMMAND-Intent), not against guessed extras or shell behavior.
