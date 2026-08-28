package com.aetherlearn.app.data

import android.content.Context
import org.json.JSONObject
import java.io.File

/** Reads only the validated visual-foundations pack; visual assets never become lessons. */
data class InstalledVisualAsset(
    val assetId: String,
    val moduleId: String,
    val file: File,
    val altText: String,
    val caption: String,
    val textEquivalent: String,
    val license: String,
    val attribution: String,
    val practicePrompt: String,
    val practiceSteps: List<String>,
    val practiceSuccessCriteria: String,
)

object VisualPackCatalog {
    private const val PACK_ID = "visual-foundations"
    private const val MAX_ASSET_BYTES = 256 * 1024L

    fun findForLesson(store: LocalStore, lessonId: String): InstalledVisualAsset? {
        val pack = store.getInstalledPacks().firstOrNull { it.id == PACK_ID } ?: return null
        val root = File(pack.installPath).canonicalFile
        val manifestFile = File(root, "manifest.json").canonicalFile
        if (!manifestFile.isFile || !manifestFile.path.startsWith(root.path + File.separator)) return null
        return runCatching {
            val manifest = JSONObject(manifestFile.readText(Charsets.UTF_8))
            if (manifest.optString("pack_kind") != "visuals") return@runCatching null
            val assets = manifest.optJSONArray("assets") ?: return@runCatching null
            (0 until assets.length()).asSequence()
                .mapNotNull { index -> assets.optJSONObject(index) }
                .mapNotNull { asset -> parseAsset(root, asset) }
                .firstOrNull { it.moduleId == lessonId }
        }.getOrNull()
    }

    private fun parseAsset(root: File, asset: JSONObject): InstalledVisualAsset? {
        val path = asset.optString("path")
        if (!path.startsWith("assets/") || path.contains("..") || path.startsWith("/")) return null
        val file = File(root, path).canonicalFile
        if (!file.path.startsWith(root.path + File.separator) || !file.isFile || file.length() > MAX_ASSET_BYTES) return null
        if (asset.optString("mime") != "image/svg+xml") return null
        val practical = asset.optJSONObject("practical") ?: return null
        if (practical.optString("mode") != "observe-and-trace") return null
        val stepsArray = practical.optJSONArray("steps") ?: return null
        val steps = (0 until stepsArray.length()).map { stepsArray.optString(it) }
        val practicePrompt = practical.optString("prompt")
        val practiceSuccessCriteria = practical.optString("success_criteria")
        return InstalledVisualAsset(
            assetId = asset.optString("asset_id"),
            moduleId = asset.optString("module_id"),
            file = file,
            altText = asset.optString("alt_text"),
            caption = asset.optString("caption"),
            textEquivalent = asset.optString("text_equivalent"),
            license = asset.optString("license"),
            attribution = asset.optString("attribution"),
            practicePrompt = practicePrompt,
            practiceSteps = steps,
            practiceSuccessCriteria = practiceSuccessCriteria,
        ).takeIf {
            it.assetId.isNotBlank() && it.moduleId.isNotBlank() && it.altText.isNotBlank() &&
                it.caption.isNotBlank() && it.textEquivalent.isNotBlank() &&
                it.practicePrompt.isNotBlank() && it.practiceSteps.isNotEmpty() &&
                it.practiceSteps.size <= 5 && it.practiceSteps.all(String::isNotBlank) &&
                it.practiceSuccessCriteria.isNotBlank()
        }
    }
}
