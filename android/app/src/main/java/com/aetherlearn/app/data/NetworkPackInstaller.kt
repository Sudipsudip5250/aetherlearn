package com.aetherlearn.app.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URI
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantLock
import java.util.zip.ZipFile
import java.security.MessageDigest

/** User-controlled download state; no background or automatic network work is started. */
enum class NetworkDownloadStatus { DOWNLOADING, PAUSED, COMPLETE, FAILED, CANCELLED }

data class NetworkDownloadProgress(
    val status: NetworkDownloadStatus,
    val url: String,
    val downloadedBytes: Long,
    val totalBytes: Long,
    val message: String,
)

class NetworkDownloadHandle internal constructor(
    private val control: DownloadControl,
    private val worker: ExecutorService,
) {
    fun pause() = control.pause()
    fun resume() = control.resume()
    fun cancel() = control.cancel()
    fun close() {
        control.cancel()
        worker.shutdownNow()
    }
}

internal class DownloadControl(private val onControl: (NetworkDownloadStatus, String) -> Unit) {
    private val paused = AtomicBoolean(false)
    private val cancelled = AtomicBoolean(false)
    private val lock = ReentrantLock()
    private val resumed = lock.newCondition()
    private var activeConnection: HttpURLConnection? = null

    fun attachConnection(connection: HttpURLConnection) {
        lock.lock()
        try {
            activeConnection = connection
            if (cancelled.get()) connection.disconnect()
        } finally {
            lock.unlock()
        }
    }

    fun detachConnection(connection: HttpURLConnection) {
        lock.lock()
        try {
            if (activeConnection === connection) activeConnection = null
        } finally {
            lock.unlock()
        }
    }

    fun pause() {
        lock.lock()
        try {
            if (!cancelled.get()) paused.set(true)
        } finally {
            lock.unlock()
        }
        onControl(NetworkDownloadStatus.PAUSED, "Download paused. Resume when ready.")
    }

    fun resume() {
        lock.lock()
        try {
            if (!cancelled.get()) {
                paused.set(false)
                resumed.signalAll()
            }
        } finally {
            lock.unlock()
        }
        onControl(NetworkDownloadStatus.DOWNLOADING, "Resuming download…")
    }

    fun cancel() {
        val connection: HttpURLConnection?
        lock.lock()
        try {
            cancelled.set(true)
            paused.set(false)
            resumed.signalAll()
            connection = activeConnection
        } finally {
            lock.unlock()
        }
        connection?.disconnect()
        onControl(NetworkDownloadStatus.CANCELLED, "Download cancelled. The active pack was kept.")
    }

    fun isCancelled(): Boolean = cancelled.get()

    fun awaitIfPaused() {
        lock.lock()
        try {
            while (paused.get() && !cancelled.get()) resumed.await(250L, TimeUnit.MILLISECONDS)
        } finally {
            lock.unlock()
        }
    }
}

class NetworkPackInstaller(
    private val context: Context,
    private val store: LocalStore,
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private val parser = LessonParser(context)

    fun start(
        rawUrl: String,
        onProgress: (NetworkDownloadProgress) -> Unit,
        onComplete: (PackResult) -> Unit,
    ): NetworkDownloadHandle {
        val url = validateUrl(rawUrl)
        val worker = Executors.newSingleThreadExecutor()
        lateinit var control: DownloadControl
        control = DownloadControl { status, message ->
            mainHandler.post { onProgress(NetworkDownloadProgress(status, url, 0L, 0L, message)) }
        }
        worker.execute {
            try {
                val result = downloadAndInstall(url, control) { progress ->
                    mainHandler.post { onProgress(progress) }
                }
                mainHandler.post { onComplete(result) }
            } catch (failure: Throwable) {
                if (!control.isCancelled()) {
                    mainHandler.post {
                        onProgress(NetworkDownloadProgress(NetworkDownloadStatus.FAILED, url, 0L, 0L, failure.message ?: "Network pack download failed."))
                        onComplete(PackResult(false, failure.message ?: "The network pack could not be installed."))
                    }
                }
            } finally {
                worker.shutdown()
            }
        }
        return NetworkDownloadHandle(control, worker)
    }

    private fun downloadAndInstall(
        url: String,
        control: DownloadControl,
        onProgress: (NetworkDownloadProgress) -> Unit,
    ): PackResult {
        val partial = File(downloadRoot(), "${sha256(url.toByteArray())}.zip.partial")
        partial.parentFile?.mkdirs()
        var offset = partial.length()
        var total = -1L
        val connection = openConnection(url, offset)
        control.attachConnection(connection)
        try {
            val code = connection.responseCode
            if (code != HttpURLConnection.HTTP_PARTIAL && code != HttpURLConnection.HTTP_OK) {
                if (code == 416 && offset > 0L) {
                    partial.delete()
                    offset = 0L
                    connection.disconnect()
                    return downloadAndInstall(url, control, onProgress)
                }
                throw IOException("Pack server returned HTTP $code.")
            }
            val appending = offset > 0L && code == HttpURLConnection.HTTP_PARTIAL
            if (!appending) {
                offset = 0L
                partial.delete()
            }
            val contentLength = connection.contentLengthLong
            total = if (contentLength > 0L) offset + contentLength else -1L
            require(total <= MAX_DOWNLOAD_BYTES || total < 0L) { "The pack is larger than the $MAX_DOWNLOAD_BYTES-byte safety limit." }
            onProgress(NetworkDownloadProgress(NetworkDownloadStatus.DOWNLOADING, url, offset, total, if (appending) "Resuming cached partial download…" else "Downloading pack…"))
            connection.inputStream.use { input ->
                FileOutputStream(partial, appending).use { output ->
                    val buffer = ByteArray(BUFFER_SIZE)
                    var downloaded = offset
                    while (true) {
                        if (control.isCancelled()) {
                            partial.delete()
                            return PackResult(false, "Download cancelled. The active pack was kept.", cancelled = true)
                        }
                        control.awaitIfPaused()
                        if (control.isCancelled()) {
                            partial.delete()
                            return PackResult(false, "Download cancelled. The active pack was kept.", cancelled = true)
                        }
                        val count = input.read(buffer)
                        if (count < 0) break
                        output.write(buffer, 0, count)
                        downloaded += count
                        require(downloaded <= MAX_DOWNLOAD_BYTES) { "The pack exceeded the safety size limit." }
                        onProgress(NetworkDownloadProgress(NetworkDownloadStatus.DOWNLOADING, url, downloaded, total, "Downloading pack…"))
                    }
                }
            }
        } catch (failure: Throwable) {
            if (control.isCancelled()) {
                partial.delete()
                return PackResult(false, "Download cancelled. The active pack was kept.", cancelled = true)
            }
            throw failure
        } finally {
            control.detachConnection(connection)
            connection.disconnect()
        }
        return try {
            installZip(partial)
        } finally {
            // Once a complete archive has been handed to the validator, it is no longer resumable.
            partial.delete()
        }
    }

    private fun installZip(zipPath: File): PackResult {
        require(zipPath.isFile && zipPath.length() > 0L) { "The downloaded pack is empty." }
        val zipChecksum = sha256(zipPath)
        val root = optionalRoot()
        root.mkdirs()
        val staging = File(root, ".network-${zipChecksum.take(16)}.staging")
        val backupByPack = mutableMapOf<String, File>()
        staging.deleteRecursively()
        require(staging.mkdirs()) { "Could not create network-pack staging storage." }
        try {
            ZipFile(zipPath).use { archive ->
                val manifestEntry = archive.getEntry("manifest.json") ?: error("The pack manifest is missing.")
                require(!manifestEntry.isDirectory) { "The pack manifest must be a file." }
                require(archive.size() <= MAX_ARCHIVE_ENTRIES) { "The pack contains too many ZIP entries." }
                val archiveFiles = mutableSetOf<String>()
                archive.entries().asSequence().filter { !it.isDirectory }.forEach { entry ->
                    require(archiveFiles.add(entry.name)) { "The pack contains a duplicate ZIP filename." }
                }
                val manifestText = archive.getInputStream(manifestEntry).use { input -> readBoundedText(input, MAX_MANIFEST_BYTES) }
                val manifest = JSONObject(manifestText)
                val packId = manifest.optString("pack_id")
                val packVersion = manifest.optString("pack_version")
                val packName = manifest.optString("name", packId)
                require(PACK_ID_PATTERN.matches(packId)) { "The pack ID is invalid." }
                require(packId != PackManager.CORE_PACK_ID) { "The protected core pack cannot be replaced by a network pack." }
                require(packVersion.isNotBlank()) { "The pack version is missing." }
                require(manifest.optInt("schema_version", -1) == SUPPORTED_SCHEMA) { "Unsupported pack schema." }
                require(packName.isNotBlank()) { "The pack name is missing." }
                val modules = manifest.optJSONArray("modules") ?: error("The pack module list is missing.")
                require(modules.length() in 1..MAX_MODULES) { "The pack module count is outside the supported range." }
                require(manifest.optInt("module_count", -1) == modules.length()) { "The pack module count is incorrect." }
                val coreIds = ModuleCatalog(context).loadLessons().map { it.id }.toSet()
                val packIds = mutableSetOf<String>()
                val packPaths = mutableSetOf<String>()
                var declaredBytes = 0L
                val modulesDir = File(staging, "modules")
                require(modulesDir.mkdirs()) { "Could not create module staging storage." }
                for (index in 0 until modules.length()) {
                    val entry = modules.getJSONObject(index)
                    val id = entry.optString("id")
                    val path = entry.optString("path")
                    val archivePath = "modules/$path"
                    val expectedSize = entry.optLong("size", -1L)
                    val expectedHash = entry.optString("sha256")
                    require(LESSON_ID_PATTERN.matches(id)) { "The pack contains an invalid lesson ID." }
                    require(packIds.add(id)) { "The pack contains a duplicate lesson ID." }
                    require(id in APPROVED_CURRICULUM_IDS) { "Lesson $id is outside the frozen MVP curriculum." }
                    require(!coreIds.contains(id)) { "Optional pack lesson $id would replace core content." }
                    require(packPaths.add(archivePath)) { "The pack contains a duplicate module path." }
                    require(SAFE_MODULE_FILENAME_PATTERN.matches(path)) { "The pack contains an unsafe module path." }
                    require(expectedSize in 1..MAX_LESSON_BYTES) { "Lesson $id has an invalid declared size." }
                    require(HEX_SHA256_PATTERN.matches(expectedHash)) { "Lesson $id has an invalid checksum." }
                    declaredBytes += expectedSize
                    require(declaredBytes <= MAX_DOWNLOAD_BYTES) { "The pack’s declared lesson bytes exceed the safety limit." }
                    val zipEntry = archive.getEntry(archivePath) ?: error("The pack is missing $archivePath.")
                    require(!zipEntry.isDirectory) { "The pack module path is a directory." }
                    val destination = File(modulesDir, path)
                    FileOutputStream(destination).use { output ->
                        archive.getInputStream(zipEntry).use { input ->
                            val buffer = ByteArray(BUFFER_SIZE)
                            var copied = 0L
                            while (true) {
                                val count = input.read(buffer)
                                if (count < 0) break
                                copied += count
                                require(copied <= MAX_LESSON_BYTES) { "Lesson $id exceeds the safety size limit." }
                                output.write(buffer, 0, count)
                            }
                        }
                    }
                    val bytes = destination.readBytes()
                    require(bytes.size.toLong() == expectedSize) { "Lesson $id size does not match its manifest." }
                    require(sha256(bytes) == expectedHash) { "Lesson $id checksum does not match its manifest." }
                    val parsed = parser.parse(String(bytes, Charsets.UTF_8))
                    require(parsed.id == id) { "Lesson $id frontmatter ID does not match its manifest." }
                    require(parsed.availability in ALLOWED_AVAILABILITY) { "Lesson $id has an unsupported availability label." }
                    require(parsed.riskTier in ALLOWED_RISK_TIERS) { "Lesson $id has an unsupported risk tier." }
                    require(parsed.sections.keys.containsAll(REQUIRED_SECTIONS)) { "Lesson $id is missing a required section." }
                }
                require(declaredBytes == manifest.optLong("total_module_bytes", -1L)) { "The pack total size does not match its module declarations." }
                val expectedArchivePaths = packPaths + "manifest.json"
                val unexpected = archive.entries().asSequence().filter { !it.isDirectory && it.name !in expectedArchivePaths }.toList()
                require(unexpected.isEmpty()) { "The pack contains an unexpected archive path." }
                File(staging, "manifest.json").writeText(manifestText, Charsets.UTF_8)
                val active = File(root, packId)
                val backup = File(root, ".${packId}.backup")
                backup.deleteRecursively()
                if (active.exists()) require(active.renameTo(backup)) { "Could not stage the previous pack for rollback." }
                backupByPack[packId] = backup
                try {
                    require(staging.renameTo(active)) { "Could not atomically activate the network pack." }
                    store.saveInstalledPack(
                        InstalledPack(packId, packVersion, packName, manifest.optString("description"), zipChecksum, zipPath.length(), active.absolutePath, System.currentTimeMillis()),
                    )
                    backup.deleteRecursively()
                    return PackResult(true, "$packName downloaded, verified, and installed locally.")
                } catch (failure: Throwable) {
                    staging.deleteRecursively()
                    if (active.exists() && !active.renameTo(staging)) active.deleteRecursively()
                    backup.renameTo(active)
                    throw failure
                }
            }
        } catch (failure: Throwable) {
            staging.deleteRecursively()
            backupByPack.values.forEach { it.renameTo(File(root, it.name.removePrefix(".").removeSuffix(".backup"))) }
            return PackResult(false, failure.message ?: "The network pack failed validation and was not activated.")
        }
    }

    private fun openConnection(url: String, offset: Long): HttpURLConnection {
        val connection = URI(url).toURL().openConnection() as? HttpURLConnection ?: error("The pack source is not an HTTP(S) URL.")
        connection.instanceFollowRedirects = false
        connection.connectTimeout = CONNECT_TIMEOUT_MS
        connection.readTimeout = READ_TIMEOUT_MS
        connection.setRequestProperty("Accept", "application/zip, application/octet-stream")
        if (offset > 0L) connection.setRequestProperty("Range", "bytes=$offset-")
        return connection
    }

    private fun downloadRoot(): File = File(context.filesDir, "network_downloads")
    private fun optionalRoot(): File = File(context.filesDir, "optional_packs")

    companion object {
        private const val SUPPORTED_SCHEMA = 1
        private const val BUFFER_SIZE = 32 * 1024
        private const val CONNECT_TIMEOUT_MS = 15_000
        private const val READ_TIMEOUT_MS = 30_000
        private const val MAX_DOWNLOAD_BYTES = 25L * 1024L * 1024L
        private const val MAX_MANIFEST_BYTES = 512 * 1024
        private const val MAX_LESSON_BYTES = 512L * 1024L
        private const val MAX_MODULES = 100
        private const val MAX_ARCHIVE_ENTRIES = 202L
        private val PACK_ID_PATTERN = Regex("[a-z0-9][a-z0-9-]{2,63}")
        private val LESSON_ID_PATTERN = Regex("[a-z0-9][a-z0-9-]{2,100}")
        private val SAFE_MODULE_FILENAME_PATTERN = Regex("[A-Za-z0-9._-]+\\.md")
        private val HEX_SHA256_PATTERN = Regex("[0-9a-f]{64}")
        private val ALLOWED_AVAILABILITY = setOf("offline", "offline-pack", "termux-optional", "network-optional")
        private val ALLOWED_RISK_TIERS = setOf("S0", "S1")
        private val REQUIRED_SECTIONS = setOf("Objectives", "Prerequisites", "Availability", "Explanation", "Worked example", "Common mistakes", "Offline practice", "Knowledge check", "Project or application", "Accessibility notes", "Safety and responsible use", "Further reading", "Change log")
        private val APPROVED_CURRICULUM_IDS = setOf(
            "dl-01-digital-information", "dl-02-files-folders-storage-backups", "dl-03-android-settings-permissions-apps", "dl-04-internet-browsers-urls-search", "dl-05-privacy-passwords-phishing",
            "py-01-problems-algorithms-instructions", "py-02-python-setup-expressions-values", "py-03-variables-types-input-output", "py-04-conditions-boolean-logic", "py-05-loops-repetition-tracing", "py-06-functions-scope-reusable-code", "py-07-lists-dictionaries-strings-data",
            "al-01-data-structures", "al-02-arrays-lists-stacks-queues", "al-03-searching-sorting", "al-04-complexity-growth", "al-05-recursion-trees-graphs",
            "dev-01-terminal-command-line", "dev-02-git-local-repositories-history", "dev-03-debugging-error-messages",
        )

        fun validateUrl(rawUrl: String): String {
            val url = rawUrl.trim()
            val uri = URI(url)
            require(uri.scheme.equals("https", ignoreCase = true)) { "Pack sources must use HTTPS." }
            require(!uri.host.isNullOrBlank()) { "Pack source host is missing." }
            require(uri.userInfo == null) { "Pack source credentials are not allowed in the URL." }
            require(uri.fragment == null) { "Pack source fragments are not allowed." }
            return uri.toASCIIString()
        }

        fun isSafeModulePath(path: String): Boolean = SAFE_MODULE_FILENAME_PATTERN.matches(path)

        fun isApprovedCurriculumId(id: String): Boolean = id in APPROVED_CURRICULUM_IDS
    }
}

private fun readBoundedText(input: java.io.InputStream, maxBytes: Int): String {
    val output = java.io.ByteArrayOutputStream()
    val buffer = ByteArray(8 * 1024)
    var total = 0
    while (true) {
        val count = input.read(buffer)
        if (count < 0) break
        total += count
        require(total <= maxBytes) { "The pack manifest exceeds the safety size limit." }
        output.write(buffer, 0, count)
    }
    return String(output.toByteArray(), Charsets.UTF_8)
}

private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
    .digest(bytes)
    .joinToString("") { byte -> "%02x".format(byte) }

private fun sha256(file: File): String = MessageDigest.getInstance("SHA-256").let { digest ->
    FileInputStream(file).use { input ->
        val buffer = ByteArray(32 * 1024)
        while (true) {
            val count = input.read(buffer)
            if (count < 0) break
            digest.update(buffer, 0, count)
        }
    }
    digest.digest().joinToString("") { byte -> "%02x".format(byte) }
}
