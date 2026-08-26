package com.aetherlearn.app

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.aetherlearn.app.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    store: LocalStore,
    lessons: List<LessonDocument>,
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onContentChanged: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val packManager = remember { PackManager(context.applicationContext, store) }
    var installedPacks by remember { mutableStateOf(packManager.installedPacks()) }
    var packMessage by remember { mutableStateOf<String?>(null) }
    var exportWarning by rememberSaveable { mutableStateOf<String?>(null) }
    var exportMessage by remember { mutableStateOf<String?>(null) }
    var networkUrl by rememberSaveable { mutableStateOf("") }
    var networkProgress by remember { mutableStateOf<NetworkDownloadProgress?>(null) }
    var networkHandle by remember { mutableStateOf<NetworkDownloadHandle?>(null) }
    val networkInstaller = remember { NetworkPackInstaller(context.applicationContext, store) }
    val latestNetworkHandle by rememberUpdatedState(networkHandle)
    DisposableEffect(Unit) {
        onDispose { latestNetworkHandle?.cancel() }
    }
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*"),
    ) { uri ->
        val format = exportWarning
        if (uri != null && format != null) {
            val content = if (format == "json") ExportManager.json(store, lessons) else ExportManager.markdown(store, lessons)
            exportMessage = if (ExportManager.write(context.contentResolver, uri, content)) {
                "Export saved to the location you selected."
            } else {
                "The export could not be written to the selected location."
            }
        }
        exportWarning = null
    }

    if (exportWarning != null) {
        AlertDialog(
            onDismissRequest = { exportWarning = null },
            title = { Text("Export local learning data?") },
            text = { Text("The export is created only after you choose a destination. It may contain personal learning notes. AetherLearn will not upload it automatically.") },
            confirmButton = {
                TextButton(onClick = {
                    val format = exportWarning ?: "markdown"
                    exportLauncher.launch(if (format == "json") "aetherlearn-learning.json" else "aetherlearn-learning.md")
                }) { Text("Choose destination") }
            },
            dismissButton = { TextButton(onClick = { exportWarning = null }) { Text("Cancel") } },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Appearance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Choose whether AetherLearn follows the device or uses a fixed theme.")
            ThemeMode.entries.forEach { mode ->
                ThemeOptionRow(mode, mode == themeMode) { onThemeModeChanged(mode) }
            }
            HorizontalDivider()
            Text("Export", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Export your progress, quiz attempts, notes, and bookmarks offline. You choose the destination with the Android file picker.")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { exportWarning = "markdown" }, modifier = Modifier.weight(1f)) { Text("Markdown") }
                Button(onClick = { exportWarning = "json" }, modifier = Modifier.weight(1f)) { Text("JSON") }
            }
            exportMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            HorizontalDivider()
            Text("Storage & content packs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Core pack: ${ModuleCatalog(context).loadLessons().size} modules, ${formatBytes(packManager.coreSizeBytes())}, always available offline and protected from deletion.")
            Text("Learning data and installed optional packs: approximately ${formatBytes(packManager.learningDataSizeBytes())}.")
            packManager.availablePacks().forEach { available ->
                OptionalPackCard(
                    available = available,
                    installed = installedPacks.firstOrNull { it.id == available.id },
                    onInstall = {
                        val result = packManager.installPack(available.id)
                        packMessage = result.message
                        installedPacks = packManager.installedPacks()
                    },
                    onDelete = {
                        val result = packManager.deletePack(available.id)
                        packMessage = result.message
                        installedPacks = packManager.installedPacks()
                    },
                )
            }
            Text("Network content pack", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Network required · user initiated. Enter an HTTPS URL to an AetherLearn ZIP pack. The core path stays available offline.")
            OutlinedTextField(
                value = networkUrl,
                onValueChange = { networkUrl = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("HTTPS pack URL") },
                placeholder = { Text("https://example.org/aetherlearn-pack.zip") },
                singleLine = true,
                enabled = networkHandle == null,
            )
            val transfer = networkProgress
            if (transfer != null) {
                Text(transfer.message, style = MaterialTheme.typography.bodySmall)
                if (transfer.totalBytes > 0L) {
                    LinearProgressIndicator(
                        progress = { (transfer.downloadedBytes.toFloat() / transfer.totalBytes.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxWidth().semantics { contentDescription = "Pack download progress" },
                    )
                    Text("${formatBytes(transfer.downloadedBytes)} of ${formatBytes(transfer.totalBytes)}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                when (transfer?.status) {
                    NetworkDownloadStatus.DOWNLOADING -> {
                        Button(onClick = { networkHandle?.pause() }, modifier = Modifier.weight(1f)) { Text("Pause") }
                        TextButton(onClick = { networkHandle?.cancel(); networkHandle = null }, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    }
                    NetworkDownloadStatus.PAUSED -> {
                        Button(onClick = { networkHandle?.resume() }, modifier = Modifier.weight(1f)) { Text("Resume") }
                        TextButton(onClick = { networkHandle?.cancel(); networkHandle = null }, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    }
                    else -> {
                        Button(
                            onClick = {
                                runCatching { NetworkPackInstaller.validateUrl(networkUrl) }
                                    .onSuccess {
                                        networkProgress = NetworkDownloadProgress(NetworkDownloadStatus.DOWNLOADING, it, 0L, 0L, "Preparing download…")
                                        networkHandle = networkInstaller.start(
                                            it,
                                            onProgress = { progress -> networkProgress = progress },
                                            onComplete = { result ->
                                                packMessage = result.message
                                                installedPacks = packManager.installedPacks()
                                                if (result.success) onContentChanged()
                                                networkProgress = networkProgress?.copy(
                                                    status = when {
                                                        result.cancelled -> NetworkDownloadStatus.CANCELLED
                                                        result.success -> NetworkDownloadStatus.COMPLETE
                                                        else -> NetworkDownloadStatus.FAILED
                                                    },
                                                    message = result.message,
                                                )
                                                networkHandle = null
                                            },
                                        )
                                    }
                                    .onFailure { failure -> packMessage = failure.message ?: "Enter a valid HTTPS pack URL." }
                            },
                            modifier = Modifier.weight(1f),
                        ) { Text("Download pack") }
                    }
                }
            }
            packMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            HorizontalDivider()
            Text("Privacy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Progress, quiz attempts, notes, bookmarks, preferences, and pack status are stored in app-private SQLite storage.")
            Text("Network downloads are explicit and limited to the HTTPS URL you provide. The app sends no learning data, credentials, cookies, or analytics.")
        }
    }
}

@Composable
private fun OptionalPackCard(
    available: AvailablePack,
    installed: InstalledPack?,
    onInstall: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(available.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(available.description)
            Text("Version ${available.version} · ${formatBytes(available.sizeBytes)} · ${if (installed == null) "Available locally" else "Installed and verified"}")
            if (installed == null) {
                Button(onClick = onInstall, modifier = Modifier.fillMaxWidth()) { Text("Install local pack") }
            } else {
                Text("Checksum: ${installed.checksum.take(12)}…", style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) { Text("Delete optional pack") }
            }
        }
    }
}

private fun formatBytes(bytes: Long): String = when {
    bytes < 1024L -> "$bytes B"
    bytes < 1024L * 1024L -> "${bytes / 1024L} KB"
    else -> "${bytes / (1024L * 1024L)} MB"
}

@Composable
private fun ThemeOptionRow(mode: ThemeMode, selected: Boolean, onSelected: () -> Unit) {
    val label = when (mode) {
        ThemeMode.SYSTEM -> "Follow system"
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelected)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(label, modifier = Modifier.padding(start = 12.dp))
    }
}
