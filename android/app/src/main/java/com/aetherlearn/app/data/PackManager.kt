package com.aetherlearn.app.data

import android.content.Context
import org.json.JSONObject
import java.io.File
import java.security.MessageDigest

 data class AvailablePack(
    val id: String,
    val version: String,
    val name: String,
    val description: String,
    val assetPath: String,
    val expectedChecksum: String,
    val sizeBytes: Long,
    val assetDirectory: String = "",
)

class PackManager(
    private val context: Context,
    private val store: LocalStore,
) {
    fun availablePacks(): List<AvailablePack> = listOf(
        AvailablePack(
            id = OPTIONAL_PACK_ID,
            version = "1.0.0",
            name = "Optional Foundations Lab",
            description = "A local lifecycle example with no additional curriculum modules yet.",
            assetPath = OPTIONAL_MANIFEST_ASSET,
            expectedChecksum = OPTIONAL_MANIFEST_SHA256,
            sizeBytes = OPTIONAL_MANIFEST_SIZE,
        ),
        AvailablePack(
            id = VISUAL_PACK_ID,
            version = "1.0.0",
            name = "Visual Foundations",
            description = "Three optional, accessible SVG diagrams for digital information, file storage, and semantic Web structure.",
            assetPath = VISUAL_MANIFEST_ASSET,
            expectedChecksum = VISUAL_MANIFEST_SHA256,
            sizeBytes = VISUAL_PACK_SIZE,
            assetDirectory = VISUAL_ASSET_DIRECTORY,
        ),
    )

    fun installedPacks(): List<InstalledPack> = store.getInstalledPacks()

    fun coreSizeBytes(): Long = assetFiles("content/core").sumOf { path ->
        context.assets.open("content/core/$path").use { it.readBytes().size.toLong() }
    }

    fun learningDataSizeBytes(): Long {
        val databaseBytes = context.getDatabasePath(DATABASE_NAME).length()
        val optionalBytes = optionalRoot().walkTopDown().filter { it.isFile }.sumOf { it.length() }
        return databaseBytes + optionalBytes
    }

    fun installPack(packId: String): PackResult {
        val available = availablePacks().firstOrNull { it.id == packId }
            ?: return PackResult(false, "This optional pack is not available locally.")
        return runCatching {
            val manifestBytes = context.assets.open(available.assetPath).use { it.readBytes() }
            val checksum = sha256(manifestBytes)
            require(checksum == available.expectedChecksum) { "The bundled pack checksum does not match." }
            val manifest = JSONObject(String(manifestBytes, Charsets.UTF_8))
            require(manifest.optInt("schema_version", -1) == SUPPORTED_SCHEMA) { "Unsupported pack schema." }
            require(manifest.optString("pack_id") == available.id) { "Pack ID does not match its manifest." }
            val packVersion = manifest.optString("version", manifest.optString("pack_version"))
            val packName = manifest.optString("name", manifest.optString("pack_id"))
            val packKind = manifest.optString("pack_kind", "content")
            require(packVersion.isNotBlank()) { "Pack version is missing." }
            require(packName.isNotBlank()) { "Pack name is missing." }
            require(packKind == "content" || packKind == "visuals") { "Unsupported pack kind." }
            if (packKind == "visuals") validateVisualManifest(manifest)

            val root = optionalRoot()
            root.mkdirs()
            val destination = File(root, available.id)
            val staging = File(root, ".${available.id}.staging")
            val backup = File(root, ".${available.id}.backup")
            staging.deleteRecursively()
            backup.deleteRecursively()
            if (destination.exists() && !destination.renameTo(backup)) {
                error("Could not stage the previous pack for rollback.")
            }
            try {
                require(staging.mkdirs()) { "Could not create the staging directory." }
                File(staging, "manifest.json").writeBytes(manifestBytes)
                require(sha256(File(staging, "manifest.json").readBytes()) == available.expectedChecksum) {
                    "Staged pack checksum verification failed."
                }
                if (packKind == "visuals") copyVisualAssets(manifest, available, staging)
                require(staging.renameTo(destination)) { "Could not atomically activate the pack." }
                backup.deleteRecursively()
            } catch (failure: Throwable) {
                staging.deleteRecursively()
                if (destination.exists()) destination.deleteRecursively()
                if (backup.exists()) backup.renameTo(destination)
                throw failure
            }
            store.saveInstalledPack(
                InstalledPack(
                    id = available.id,
                    version = packVersion,
                    name = packName,
                    description = manifest.optString("description"),
                    checksum = available.expectedChecksum,
                    sizeBytes = destination.walkTopDown().filter { it.isFile }.sumOf { it.length() },
                    installPath = destination.absolutePath,
                    installedAt = System.currentTimeMillis(),
                ),
            )
            PackResult(true, "${available.name} installed and verified locally.")
        }.getOrElse { failure ->
            PackResult(false, failure.message ?: "The optional pack could not be installed.")
        }
    }

    private fun validateVisualManifest(manifest: JSONObject) {
        require(manifest.optString("name").isNotBlank()) { "Visual pack name is missing." }
        require(manifest.optString("description").isNotBlank()) { "Visual pack description is missing." }
        require(manifest.optString("pack_version").matches(SEMVER)) { "Visual pack version is invalid." }
        require(manifest.optString("minimum_app_version").matches(SEMVER)) { "Visual pack minimum app version is invalid." }
        require(manifest.optString("distribution_status") == "unsigned-development") { "Only the unsigned development visual fixture is accepted locally." }
        require(manifest.isNull("signature")) { "Unsigned visual packs must not contain a signature." }
        require(manifest.optString("revocation_status") == "not-revoked") { "Visual pack revocation status is invalid." }

        val knownModuleIds = ModuleCatalog(context, store).loadSummaries().map { it.id }.toSet()
        val moduleIds = manifest.optJSONArray("module_ids") ?: error("Visual pack module associations are missing.")
        require(moduleIds.length() in 1..MAX_VISUAL_ASSETS) { "Visual pack module association count is outside the safe limit." }
        val declaredModules = mutableSetOf<String>()
        for (index in 0 until moduleIds.length()) {
            val moduleId = moduleIds.optString(index)
            require(moduleId.isNotBlank() && knownModuleIds.contains(moduleId)) { "Visual pack references an unknown lesson module." }
            require(declaredModules.add(moduleId)) { "Visual pack module associations are duplicated." }
        }

        val assets = manifest.optJSONArray("assets") ?: error("Visual pack assets are missing.")
        require(assets.length() in 1..MAX_VISUAL_ASSETS) { "Visual pack asset count is outside the safe limit." }
        val assetIds = mutableSetOf<String>()
        val paths = mutableSetOf<String>()
        var installedTotal = 0L
        var compressedTotal = 0L
        for (index in 0 until assets.length()) {
            val asset = assets.optJSONObject(index) ?: error("Visual asset metadata is invalid.")
            val assetId = asset.optString("asset_id")
            val path = asset.optString("path")
            val moduleId = asset.optString("module_id")
            require(assetId.matches(ASSET_ID)) { "Visual asset ID is invalid." }
            require(assetIds.add(assetId)) { "Visual asset ID is duplicated." }
            require(moduleId in declaredModules) { "Visual asset references an undeclared lesson module." }
            require(path.matches(ASSET_PATH)) { "Visual asset path is unsafe or unsupported." }
            require(paths.add(path)) { "Visual asset path is duplicated." }
            require(asset.optString("kind").isNotBlank()) { "Visual asset kind is missing." }
            require(asset.optString("mime") == "image/svg+xml") { "Only static SVG visual assets are accepted locally." }
            require(asset.has("required") && asset.opt("required") is Boolean) { "Visual asset required flag is missing or invalid." }
            require(asset.optString("alt_text").isNotBlank() && asset.optString("caption").isNotBlank()) { "Visual asset accessibility metadata is incomplete." }
            require(asset.optString("text_equivalent").isNotBlank() && asset.optString("reduced_motion_alternative").isNotBlank()) { "Visual asset text alternative is incomplete." }
            require(asset.optString("license").isNotBlank() && asset.optString("attribution").isNotBlank() && asset.optString("author").isNotBlank() && asset.optString("locale").isNotBlank()) { "Visual asset licensing metadata is incomplete." }
            val sourceUrl = asset.opt("source_url")
            require(sourceUrl == null || sourceUrl == JSONObject.NULL || sourceUrl.toString().startsWith("https://")) { "Visual asset source URL must be HTTPS or null." }
            val installedBytes = asset.optLong("installed_bytes", -1L)
            val compressedBytes = asset.optLong("compressed_bytes", -1L)
            require(installedBytes >= 0L && compressedBytes >= 0L && installedBytes <= MAX_ASSET_BYTES) { "Visual asset size metadata is invalid." }
            require(asset.optString("sha256").matches(SHA256)) { "Visual asset checksum metadata is invalid." }
            installedTotal += installedBytes
            compressedTotal += compressedBytes
        }
        require(manifest.optLong("installed_bytes", -1L) == installedTotal) { "Visual pack installed size metadata is invalid." }
        require(manifest.optLong("compressed_bytes", -1L) == compressedTotal) { "Visual pack compressed size metadata is invalid." }
        require(installedTotal <= MAX_PACK_BYTES) { "Visual pack exceeds the safe installed-size limit." }
    }

    private fun copyVisualAssets(manifest: JSONObject, available: AvailablePack, staging: File) {
        val assets = manifest.optJSONArray("assets") ?: error("Visual pack assets are missing.")
        require(assets.length() in 1..MAX_VISUAL_ASSETS) { "Visual pack asset count is outside the safe limit." }
        val paths = mutableSetOf<String>()
        for (index in 0 until assets.length()) {
            val asset = assets.optJSONObject(index) ?: error("Visual asset metadata is invalid.")
            val path = asset.optString("path")
            require(path.startsWith("assets/") && !path.contains("..") && !path.startsWith("/")) { "Visual asset path is unsafe." }
            require(paths.add(path)) { "Visual asset path is duplicated." }
            val destination = File(staging, path)
            require(destination.canonicalPath.startsWith(staging.canonicalPath + File.separator)) { "Visual asset path escapes staging." }
            destination.parentFile?.mkdirs()
            context.assets.open("${available.assetDirectory}/$path").use { input ->
                destination.outputStream().use { output -> input.copyTo(output) }
            }
            require(destination.length() == asset.optLong("installed_bytes", -1L)) { "Visual asset size mismatch: $path" }
            require(destination.length() <= MAX_ASSET_BYTES) { "Visual asset exceeds the safe size limit: $path" }
            val svgText = destination.readBytes().toString(Charsets.UTF_8).lowercase()
            require(svgText.contains("<svg") && listOf("<script", "javascript:", "onload=", "onclick=", "<foreignobject").none { marker -> svgText.contains(marker) }) { "Visual asset contains unsupported active content: $path" }
            require(sha256(destination.readBytes()) == asset.optString("sha256")) { "Visual asset checksum mismatch: $path" }
        }
    }

    fun deletePack(packId: String): PackResult {
        if (packId == CORE_PACK_ID) return PackResult(false, "The bundled core pack is protected.")
        val installed = installedPacks().firstOrNull { it.id == packId }
            ?: return PackResult(false, "That optional pack is not installed.")
        val deleted = File(installed.installPath).deleteRecursively()
        if (!deleted && File(installed.installPath).exists()) return PackResult(false, "The optional pack could not be deleted.")
        store.removeInstalledPack(packId)
        return PackResult(true, "${installed.name} deleted. Core lessons and learning data were kept.")
    }

    private fun optionalRoot(): File = File(context.filesDir, "optional_packs")

    private fun assetFiles(path: String): List<String> = context.assets.list(path)?.toList().orEmpty()

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes)
        .joinToString("") { byte -> "%02x".format(byte) }

    companion object {
        const val CORE_PACK_ID = "core"
        private const val OPTIONAL_PACK_ID = "optional-foundations"
        private const val OPTIONAL_MANIFEST_ASSET = "packs/optional-foundations/manifest.json"
        private const val OPTIONAL_MANIFEST_SHA256 = "e92be73ef7bddb4983a9a48c1ed1f14d42e312a04410d3b90cfeb7ec87d46cf3"
        private const val OPTIONAL_MANIFEST_SIZE = 276L
        private const val VISUAL_PACK_ID = "visual-foundations"
        private const val VISUAL_MANIFEST_ASSET = "packs/visual-foundations/manifest.json"
        private const val VISUAL_ASSET_DIRECTORY = "packs/visual-foundations"
        private const val VISUAL_MANIFEST_SHA256 = "d4fee669353a159d0aca99feafa4f8e98fc831c991f56f63891d11d41b5c501f"
        private const val VISUAL_PACK_SIZE = 8967L
        private const val MAX_VISUAL_ASSETS = 24
        private const val MAX_ASSET_BYTES = 256 * 1024L
        private const val MAX_PACK_BYTES = 2 * 1024 * 1024L
        private val ASSET_ID = Regex("^[a-z0-9]+(?:-[a-z0-9]+)+$")
        private val ASSET_PATH = Regex("^assets/[a-z0-9-]+\\.svg$")
        private val SHA256 = Regex("^[0-9a-f]{64}$")
        private val SEMVER = Regex("^\\d+\\.\\d+\\.\\d+(?:-[0-9A-Za-z.-]+)?$")
        private const val SUPPORTED_SCHEMA = 1
        private const val DATABASE_NAME = "aetherlearn_local.db"
    }
}

data class PackResult(
    val success: Boolean,
    val message: String,
    val cancelled: Boolean = false,
)
