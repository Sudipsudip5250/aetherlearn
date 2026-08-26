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
            "dl-05-privacy-passwords-phishing.md",
            "py-01-problems-algorithms-instructions.md",
            "py-02-python-setup-expressions-values.md",
            "dev-01-terminal-command-line.md",
        )
    }
}
