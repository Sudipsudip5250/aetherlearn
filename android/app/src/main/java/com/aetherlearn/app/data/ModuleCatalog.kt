package com.aetherlearn.app.data

import android.content.Context

/** Lightweight metadata used by the M2 shell before the M3 lesson reader exists. */
data class ModuleSummary(
    val id: String,
    val title: String,
    val availability: String,
)

class ModuleCatalog(private val context: Context) {
    fun loadSummaries(): List<ModuleSummary> = MODULE_ASSETS.mapNotNull { assetName ->
        val text = runCatching {
            context.assets.open("content/core/$assetName").bufferedReader().use { it.readText() }
        }.getOrNull() ?: return@mapNotNull null
        val metadata = text.substringAfter("---\n", "").substringBefore("\n---\n", "")
        val values = metadata.lineSequence()
            .mapNotNull { line ->
                val separator = line.indexOf(":")
                if (separator < 1) null else line.substring(0, separator).trim() to line.substring(separator + 1).trim()
            }
            .toMap()
        ModuleSummary(
            id = values["id"] ?: return@mapNotNull null,
            title = values["title"] ?: return@mapNotNull null,
            availability = values["availability"] ?: "offline",
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
