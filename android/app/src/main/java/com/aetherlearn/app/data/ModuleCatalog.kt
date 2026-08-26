package com.aetherlearn.app.data

import android.content.Context
import java.io.File

/** Lightweight metadata used by the Learn list. */
data class ModuleSummary(
    val id: String,
    val title: String,
    val availability: String,
    val estimatedMinutes: Int,
)

class ModuleCatalog(
    private val context: Context,
    private val store: LocalStore? = null,
) {
    private val parser = LessonParser(context)

    fun loadLessons(): List<LessonDocument> {
        val bundled = MODULE_ASSETS.mapNotNull { assetName -> runCatching { parser.parseAsset(assetName) }.getOrNull() }
        val installed = store?.getInstalledPacks().orEmpty().flatMap { pack ->
            val modulesDirectory = File(pack.installPath, "modules")
            modulesDirectory.listFiles { file -> file.isFile && file.extension == "md" }
                ?.sortedBy { it.name }
                ?.mapNotNull { file -> runCatching { parser.parse(file.readText(Charsets.UTF_8)) }.getOrNull() }
                .orEmpty()
        }
        return (bundled + installed).distinctBy { it.id }
    }

    fun loadSummaries(): List<ModuleSummary> = loadLessons().map { lesson ->
        ModuleSummary(
            id = lesson.id,
            title = lesson.title,
            availability = lesson.availability,
            estimatedMinutes = lesson.estimatedMinutes,
        )
    }

    companion object {
        private val MODULE_ASSETS = listOf(
            "dl-01-digital-information.md",
            "dl-02-files-folders-storage-backups.md",
            "dl-03-android-settings-permissions-apps.md",
            "dl-04-internet-browsers-urls-search.md",
            "dl-05-privacy-passwords-phishing.md",
            "py-01-problems-algorithms-instructions.md",
            "py-02-python-setup-expressions-values.md",
            "py-03-variables-types-input-output.md",
            "py-04-conditions-boolean-logic.md",
            "py-05-loops-repetition-tracing.md",
            "py-06-functions-scope-reusable-code.md",
            "py-07-lists-dictionaries-strings-data.md",
            "al-01-data-structures.md",
            "al-02-arrays-lists-stacks-queues.md",
            "al-03-searching-sorting.md",
            "al-04-complexity-growth.md",
            "al-05-recursion-trees-graphs.md",
            "dev-01-terminal-command-line.md",
            "dev-02-git-local-repositories-history.md",
            "dev-03-debugging-error-messages.md",
        )
    }
}
