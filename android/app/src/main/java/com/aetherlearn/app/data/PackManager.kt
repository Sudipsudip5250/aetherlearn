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
            require(manifest.optString("version").isNotBlank()) { "Pack version is missing." }
            require(manifest.optString("name").isNotBlank()) { "Pack name is missing." }

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
                    version = manifest.getString("version"),
                    name = manifest.getString("name"),
                    description = manifest.optString("description"),
                    checksum = available.expectedChecksum,
                    sizeBytes = manifestBytes.size.toLong(),
                    installPath = destination.absolutePath,
                    installedAt = System.currentTimeMillis(),
                ),
            )
            PackResult(true, "${available.name} installed and verified locally.")
        }.getOrElse { failure ->
            PackResult(false, failure.message ?: "The optional pack could not be installed.")
        }
    }

    fun deletePack(packId: String): PackResult {
        if (packId == CORE_PACK_ID) return PackResult(false, "The core eleven-module pack is protected.")
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
        private const val SUPPORTED_SCHEMA = 1
        private const val DATABASE_NAME = "aetherlearn_local.db"
    }
}

data class PackResult(
    val success: Boolean,
    val message: String,
    val cancelled: Boolean = false,
)
