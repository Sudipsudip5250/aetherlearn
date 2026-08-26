package com.aetherlearn.app.data

import android.content.Context

/** Lightweight metadata used by the Learn list. */
data class ModuleSummary(
    val id: String,
    val title: String,
    val availability: String,
    val estimatedMinutes: Int,
)

class ModuleCatalog(private val context: Context) {
    private val parser = LessonParser(context)

    fun loadLessons(): List<LessonDocument> = MODULE_ASSETS.mapNotNull { assetName ->
        runCatching { parser.parseAsset(assetName) }.getOrNull()
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
            "dev-01-terminal-command-line.md",
        )
    }
}
