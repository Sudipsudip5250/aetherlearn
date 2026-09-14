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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import android.Manifest
import android.os.Build
import com.aetherlearn.app.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    store: LocalStore,
    lessons: List<LessonDocument>,
    themeMode: ThemeMode,
    readingTheme: ReadingTheme,
    startingLevel: StartingLevel?,
    remindersOptIn: Boolean,
    onStartingLevelChanged: (StartingLevel?) -> Unit,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onReadingThemeChanged: (ReadingTheme) -> Unit,
    onRemindersOptInChanged: (Boolean) -> Unit,
    onContentChanged: () -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val packManager = remember { PackManager(context.applicationContext, store) }
    var installedPacks by remember { mutableStateOf(packManager.installedPacks()) }
    var packMessage by remember { mutableStateOf<String?>(null) }
    var packStatusNotes by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var exportWarning by rememberSaveable { mutableStateOf<String?>(null) }
    var exportMessage by remember { mutableStateOf<String?>(null) }
    var clearDataWarning by rememberSaveable { mutableStateOf(false) }
    var clearNotesWarning by rememberSaveable { mutableStateOf(false) }
    var dataMessage by remember { mutableStateOf<String?>(null) }
    var networkUrl by rememberSaveable { mutableStateOf("") }
    var networkProgress by remember { mutableStateOf<NetworkDownloadProgress?>(null) }
    var networkHandle by remember { mutableStateOf<NetworkDownloadHandle?>(null) }
    val networkInstaller = remember { NetworkPackInstaller(context.applicationContext, store) }
    val latestNetworkHandle by rememberUpdatedState(networkHandle)
    val reminderPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        onRemindersOptInChanged(granted)
        if (granted) GoalReminders.ensureChannel(context)
    }
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

    if (clearNotesWarning) {
        AlertDialog(
            onDismissRequest = { clearNotesWarning = false },
            title = { Text("Delete all notes?") },
            text = { Text("This removes every private note stored on this device. Progress, quiz attempts, bookmarks, preferences, and content packs are kept. This cannot be undone unless you have an export.") },
            confirmButton = {
                TextButton(onClick = {
                    store.clearAllNotes()
                    dataMessage = "All local notes deleted. Other learning data was kept."
                    clearNotesWarning = false
                    onContentChanged()
                }) { Text("Delete all notes") }
            },
            dismissButton = { TextButton(onClick = { clearNotesWarning = false }) { Text("Cancel") } },
        )
    }

    if (clearDataWarning) {
        AlertDialog(
            onDismissRequest = { clearDataWarning = false },
            title = { Text("Delete local learning data?") },
            text = { Text("This removes progress, quiz attempts, notes, bookmarks, Termux exercise completion, and local learning goals from this device. It does not remove the bundled or installed content packs, your theme, or the first-run privacy setting. This cannot be undone unless you have an export.") },
            confirmButton = {
                TextButton(onClick = {
                    store.clearLearningData()
                    dataMessage = "Local learning data deleted. Content packs and settings were kept."
                    clearDataWarning = false
                    onContentChanged()
                }) { Text("Delete learning data") }
            },
            dismissButton = { TextButton(onClick = { clearDataWarning = false }) { Text("Cancel") } },
        )
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
            Text("Light and dark stay local. Reading palettes only change colors and a subtle on-device pattern; nothing is downloaded.")
            ThemeMode.entries.forEach { mode ->
                ThemeOptionRow(mode, mode == themeMode) { onThemeModeChanged(mode) }
            }
            Text("Reading palette", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            ReadingTheme.entries.forEach { theme ->
                ReadingThemeRow(theme, theme == readingTheme) { onReadingThemeChanged(theme) }
            }
            Text("Learning path", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("This optional choice changes the recommended next lesson only. It never locks content or records identity.")
            StartingLevel.entries.forEach { level ->
                StartingLevelOptionRow(level, level == startingLevel) { onStartingLevelChanged(level) }
            }
            TextButton(onClick = { onStartingLevelChanged(null) }, modifier = Modifier.fillMaxWidth()) { Text("Clear starting-point choice") }
            HorizontalDivider()
            Text("Learning data", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Export your progress, quiz attempts, notes, bookmarks, and local goals. You choose the destination with the Android file picker. Nothing is uploaded.")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { exportWarning = "markdown" }, modifier = Modifier.weight(1f)) { Text("Markdown") }
                Button(onClick = { exportWarning = "json" }, modifier = Modifier.weight(1f)) { Text("JSON") }
            }
            exportMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            Text("Learning records stay in app-private storage. Export first if you may want to restore them later; plain exports are not encrypted backups.")
            OutlinedButton(onClick = { clearNotesWarning = true }, modifier = Modifier.fillMaxWidth()) { Text("Delete all notes") }
            OutlinedButton(onClick = { clearDataWarning = true }, modifier = Modifier.fillMaxWidth()) { Text("Delete all local learning data") }
            dataMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            HorizontalDivider()
            Text("Content packs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Core pack: ${ModuleCatalog(context).loadLessons().size} lessons, ${formatBytes(packManager.coreSizeBytes())}, always available offline and protected from deletion.")
            Text("Learning data and installed optional packs: approximately ${formatBytes(packManager.learningDataSizeBytes())}.")
            packManager.availablePacks().forEach { available ->
                OptionalPackCard(
                    available = available,
                    installed = installedPacks.firstOrNull { it.id == available.id },
                    statusNote = packStatusNotes[available.id],
                    onInstall = {
                        val result = packManager.installPack(available.id)
                        packMessage = result.message
                        installedPacks = packManager.installedPacks()
                        packStatusNotes = packStatusNotes + (available.id to packOutcomeLabel(result))
                    },
                    onDelete = {
                        val result = packManager.deletePack(available.id)
                        packMessage = result.message
                        installedPacks = packManager.installedPacks()
                        packStatusNotes = packStatusNotes + (available.id to if (result.success) "Available locally" else "Failed")
                    },
                )
            }
            Text("Network content pack", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Network required · user initiated. Paste an HTTPS URL you trust, such as a GitHub Release asset for an AetherLearn ZIP. The app does not crawl releases. Core lessons stay available if a pack fails.")
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
                                                packStatusNotes = packStatusNotes + ("network" to packOutcomeLabel(result))
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
            packStatusNotes["network"]?.let { Text("Network pack status: $it", style = MaterialTheme.typography.bodySmall) }
            HorizontalDivider()
            Text("About / open-source", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Progress, quiz attempts, notes, bookmarks, goals, preferences, and pack status are stored in app-private SQLite storage.")
            Text("Network downloads are explicit and limited to the HTTPS URL you provide. The app sends no learning data, credentials, cookies, or analytics.")
            Text("Optional local reminders use on-device notifications only. They require your opt-in and never use a cloud push service.")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Local goal reminders", fontWeight = FontWeight.SemiBold)
                    Text("Opt in to on-device notifications for goals you create.", style = MaterialTheme.typography.bodySmall)
                }
                Switch(
                    checked = remindersOptIn,
                    onCheckedChange = { enabled ->
                        if (enabled && Build.VERSION.SDK_INT >= 33 && !GoalReminders.canNotify(context)) {
                            reminderPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            onRemindersOptInChanged(enabled)
                            if (enabled) GoalReminders.ensureChannel(context)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun OptionalPackCard(
    available: AvailablePack,
    installed: InstalledPack?,
    statusNote: String?,
    onInstall: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(available.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(available.description)
            Text("Version ${available.version} · ${formatBytes(available.sizeBytes)} · ${statusNote ?: packStatusLabel(installed)}")
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

private fun packStatusLabel(installed: InstalledPack?): String =
    if (installed == null) "Available locally" else "Installed and verified"

private fun packOutcomeLabel(result: PackResult): String = when {
    result.success -> "Installed and verified"
    result.cancelled -> "Cancelled; previous pack kept"
    result.message.contains("rollback", ignoreCase = true) -> "Failed; rolled back to last good pack"
    else -> "Failed; core lessons kept"
}

@Composable
private fun StartingLevelOptionRow(level: StartingLevel, selected: Boolean, onSelected: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, role = Role.RadioButton, onClick = onSelected)
            .padding(vertical = 4.dp)
            .semantics { contentDescription = "Starting level: ${level.label}. ${level.description}" },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(level.label, fontWeight = FontWeight.SemiBold)
            Text(level.description, style = MaterialTheme.typography.bodySmall)
        }
    }
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

@Composable
private fun ReadingThemeRow(theme: ReadingTheme, selected: Boolean, onSelected: () -> Unit) {
    val (label, detail) = when (theme) {
        ReadingTheme.DEFAULT -> "Default" to "Current AetherLearn colors"
        ReadingTheme.WARM_PAPER -> "Soft paper" to "Warm cream page for longer reading"
        ReadingTheme.COOL -> "Cool contrast" to "Blue-gray page with cooler ink"
        ReadingTheme.HIGH_CONTRAST -> "High contrast" to "Stronger borders and black/white type"
        ReadingTheme.SOFT_PATTERN -> "Soft pattern" to "Subtle local dots; no download"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, role = Role.RadioButton, onClick = onSelected)
            .padding(vertical = 4.dp)
            .semantics { contentDescription = "Reading palette: $label. $detail" },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(label, fontWeight = FontWeight.SemiBold)
            Text(detail, style = MaterialTheme.typography.bodySmall)
        }
    }
}
