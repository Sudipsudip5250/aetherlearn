package com.aetherlearn.app.data

/** Immutable app-owned request that a future Termux bridge may translate to RUN_COMMAND extras. */
data class TermuxLaunchRequest(
    val contractVersion: Int,
    val wrapperId: String,
    val executable: String,
    val arguments: List<String>,
    val workingDirectory: String,
)

data class TermuxExerciseWrapper(
    val contractVersion: Int,
    val wrapperId: String,
    val lessonId: String,
    val title: String,
    val executable: String,
    val arguments: List<String>,
    val workingDirectory: String,
    val prerequisites: List<String>,
    val expectedEffects: String,
    val fallback: String,
    val completionMode: CompletionMode,
    val networkRequired: Boolean,
)

enum class CompletionMode { LEARNER_CONFIRMED, VALIDATED_LOCAL_RESULT }

object TermuxWrapperRegistry {
    const val CONTRACT_VERSION = 1

    val wrappers: List<TermuxExerciseWrapper> = listOf(
        TermuxExerciseWrapper(
            contractVersion = CONTRACT_VERSION,
            wrapperId = "py-02-local-expressions",
            lessonId = "py-02-python-setup-expressions-values",
            title = "Evaluate two local Python expressions",
            executable = "/data/data/com.termux/files/usr/bin/python",
            arguments = listOf("-c", "print(2 * (3 + 4))", "print(\"learn\" + \" \" + \"locally\")"),
            workingDirectory = "/data/data/com.termux/files/home/aetherlearn-practice/py-02",
            prerequisites = listOf("Termux is installed", "Python is available at the fixed local path", "No network or shared storage is needed"),
            expectedEffects = "Prints 14 and learn locally; changes no files and uses no network.",
            fallback = "Complete the in-app tracing exercise and record the predicted outputs in your notes.",
            completionMode = CompletionMode.LEARNER_CONFIRMED,
            networkRequired = false,
        ),
        TermuxExerciseWrapper(
            contractVersion = CONTRACT_VERSION,
            wrapperId = "py-03-local-variables-output",
            lessonId = "py-03-variables-types-input-output",
            title = "Print fixed local variables",
            executable = "/data/data/com.termux/files/usr/bin/python",
            arguments = listOf("-c", "name = \"Learner\"\nminutes = 25\nprint(name)\nprint(minutes)"),
            workingDirectory = "/data/data/com.termux/files/home/aetherlearn-practice/py-03",
            prerequisites = listOf("Termux is installed", "Python is available at the fixed local path", "No network or shared storage is needed"),
            expectedEffects = "Prints Learner and 25; changes no files and uses no network.",
            fallback = "Complete the variable-tracing exercise in the lesson and record each predicted output in your notes.",
            completionMode = CompletionMode.LEARNER_CONFIRMED,
            networkRequired = false,
        ),
        TermuxExerciseWrapper(
            contractVersion = CONTRACT_VERSION,
            wrapperId = "py-05-local-loop-trace",
            lessonId = "py-05-loops-repetition-tracing",
            title = "Print a fixed loop trace",
            executable = "/data/data/com.termux/files/usr/bin/python",
            arguments = listOf("-c", "for step in range(1, 4):\\n    print(step * 10)"),
            workingDirectory = "/data/data/com.termux/files/home/aetherlearn-practice/py-05",
            prerequisites = listOf("Termux is installed", "Python is available at the fixed local path", "No network or shared storage is needed"),
            expectedEffects = "Prints 10, 20, and 30; changes no files and uses no network.",
            fallback = "Complete the loop-tracing table in the lesson and record the predicted output in your notes.",
            completionMode = CompletionMode.LEARNER_CONFIRMED,
            networkRequired = false,
        ),
        TermuxExerciseWrapper(
            contractVersion = CONTRACT_VERSION,
            wrapperId = "py-06-local-functions",
            lessonId = "py-06-functions-scope-reusable-code",
            title = "Evaluate a fixed local function",
            executable = "/data/data/com.termux/files/usr/bin/python",
            arguments = listOf("-c", "def add_bonus(value):\n    return value + 2\nprint(add_bonus(5))"),
            workingDirectory = "/data/data/com.termux/files/home/aetherlearn-practice/py-06",
            prerequisites = listOf("Termux is installed", "Python is available at the fixed local path", "No network or shared storage is needed"),
            expectedEffects = "Prints 7; changes no files and uses no network.",
            fallback = "Trace the add_bonus function by hand and record the returned value in your notes.",
            completionMode = CompletionMode.LEARNER_CONFIRMED,
            networkRequired = false,
        ),
        TermuxExerciseWrapper(
            contractVersion = CONTRACT_VERSION,
            wrapperId = "py-07-local-data-summary",
            lessonId = "py-07-lists-dictionaries-strings-data",
            title = "Summarize fixed local data",
            executable = "/data/data/com.termux/files/usr/bin/python",
            arguments = listOf("-c", "labels = [\"PY\", \"AL\", \"PY\"]\ncounts = {}\nfor label in labels:\n    counts[label] = counts.get(label, 0) + 1\nprint(counts)"),
            workingDirectory = "/data/data/com.termux/files/home/aetherlearn-practice/py-07",
            prerequisites = listOf("Termux is installed", "Python is available at the fixed local path", "No network or shared storage is needed"),
            expectedEffects = "Prints a count for fixed fictional labels; changes no files and uses no network.",
            fallback = "Complete the data-summary table in the lesson and calculate the final counts by hand.",
            completionMode = CompletionMode.LEARNER_CONFIRMED,
            networkRequired = false,
        ),
        TermuxExerciseWrapper(
            contractVersion = CONTRACT_VERSION,
            wrapperId = "dev-02-local-git-version",
            lessonId = "dev-02-git-local-repositories-history",
            title = "Read the local Git version",
            executable = "/data/data/com.termux/files/usr/bin/git",
            arguments = listOf("--version"),
            workingDirectory = "/data/data/com.termux/files/home/aetherlearn-practice/dev-02",
            prerequisites = listOf("Termux is installed", "Git is available at the fixed local path", "No network or shared storage is needed"),
            expectedEffects = "Prints the installed Git version; changes no files and uses no network.",
            fallback = "Complete the paper repository-state exercise and label the working tree, staging area, commit, and history.",
            completionMode = CompletionMode.LEARNER_CONFIRMED,
            networkRequired = false,
        ),
        TermuxExerciseWrapper(
            contractVersion = CONTRACT_VERSION,
            wrapperId = "dev-01-safe-navigation",
            lessonId = "dev-01-terminal-command-line",
            title = "List the local practice directory",
            executable = "/data/data/com.termux/files/usr/bin/ls",
            arguments = listOf("-la"),
            workingDirectory = "/data/data/com.termux/files/home/aetherlearn-practice/dev-01",
            prerequisites = listOf("Termux is installed", "The dedicated local practice directory exists", "No network or shared storage is needed"),
            expectedEffects = "Reads one dedicated local directory and changes no files.",
            fallback = "Use the in-app fictional directory-tree simulator and predict pwd, ls, and cd notes results.",
            completionMode = CompletionMode.LEARNER_CONFIRMED,
            networkRequired = false,
        ),
    )

    fun find(wrapperId: String): TermuxExerciseWrapper? = wrappers.firstOrNull { it.wrapperId == wrapperId }

    fun buildLaunchRequest(wrapperId: String): TermuxLaunchRequest {
        val wrapper = find(wrapperId) ?: throw IllegalArgumentException("Unknown Termux wrapper ID")
        return TermuxLaunchRequest(
            contractVersion = wrapper.contractVersion,
            wrapperId = wrapper.wrapperId,
            executable = wrapper.executable,
            arguments = wrapper.arguments.toList(),
            workingDirectory = wrapper.workingDirectory,
        )
    }

    /** Validates a request against the immutable registry before any future intent handoff. */
    fun validate(request: TermuxLaunchRequest): Result<Unit> = runCatching {
        val wrapper = find(request.wrapperId) ?: error("Unknown Termux wrapper ID")
        require(request.contractVersion == CONTRACT_VERSION) { "Unsupported wrapper contract version" }
        require(wrapper.contractVersion == request.contractVersion) { "Wrapper contract version mismatch" }
        require(request.executable == wrapper.executable) { "Executable does not match the allowlist" }
        require(request.arguments == wrapper.arguments) { "Arguments do not match the allowlist" }
        require(request.workingDirectory == wrapper.workingDirectory) { "Working directory does not match the allowlist" }
        require(wrapper.lessonId.isNotBlank()) { "Wrapper lesson mapping is missing" }
        require(wrapper.completionMode == CompletionMode.LEARNER_CONFIRMED) { "Unsupported completion policy" }
        require(!wrapper.networkRequired) { "Network-required wrapper is not allowed in this pilot" }
        require(wrapper.executable.startsWith("/data/data/com.termux/files/usr/bin/")) { "Executable must be inside Termux usr/bin" }
        require(wrapper.workingDirectory.startsWith("/data/data/com.termux/files/home/aetherlearn-practice/")) { "Working directory must be dedicated local practice" }
        require(wrapper.arguments.none { it.contains(Regex("[;&|`$]")) }) { "Shell metacharacter is not allowed" }
        require(wrapper.arguments.none { it.lowercase() in setOf("ssh", "scp", "curl", "wget", "pkg", "apt", "apt-get", "su", "rm", "chmod") }) { "Unsafe command argument is not allowed" }
        require(wrapper.fallback.isNotBlank()) { "Offline fallback is required" }
    }

    fun validateRegistry(): List<String> = buildList {
        val ids = wrappers.map { it.wrapperId }
        if (ids.size != ids.toSet().size) add("Duplicate wrapper ID")
        wrappers.forEach { wrapper ->
            validate(
                TermuxLaunchRequest(
                    wrapper.contractVersion,
                    wrapper.wrapperId,
                    wrapper.executable,
                    wrapper.arguments,
                    wrapper.workingDirectory,
                ),
            ).onFailure { add("${wrapper.wrapperId}: ${it.message}") }
        }
    }
}
