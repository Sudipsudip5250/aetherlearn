package com.aetherlearn.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.aetherlearn.app.data.LocalStore
import com.aetherlearn.app.data.ModuleCatalog
import com.aetherlearn.app.data.ModuleSummary
import com.aetherlearn.app.data.ThemeMode
import com.aetherlearn.app.ui.theme.AetherLearnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AetherLearnApp() }
    }
}

private enum class Destination(val label: String) {
    LEARN("Learn"),
    PRACTICE("Practice"),
    SEARCH("Search"),
    PROGRESS("Progress"),
}

@Composable
fun AetherLearnApp() {
    val context = LocalContext.current
    val localStore = remember { LocalStore(context.applicationContext) }
    var firstRunComplete by remember { mutableStateOf(localStore.isFirstRunComplete()) }
    var themeMode by remember { mutableStateOf(localStore.getThemeMode()) }

    DisposableEffect(localStore) {
        onDispose { localStore.close() }
    }

    AetherLearnTheme(themeMode = themeMode) {
        if (!firstRunComplete) {
            PrivacyWelcomeScreen(
                onContinue = {
                    localStore.markFirstRunComplete()
                    firstRunComplete = true
                },
            )
        } else {
            AppShell(
                themeMode = themeMode,
                onThemeModeChanged = { mode ->
                    localStore.setThemeMode(mode)
                    themeMode = mode
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppShell(
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
) {
    var destination by remember { mutableStateOf(Destination.LEARN) }
    var settingsOpen by remember { mutableStateOf(false) }

    if (settingsOpen) {
        SettingsScreen(
            themeMode = themeMode,
            onThemeModeChanged = onThemeModeChanged,
            onBack = { settingsOpen = false },
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AetherLearn") },
                actions = {
                    TextButton(
                        onClick = { settingsOpen = true },
                        modifier = Modifier.semantics {
                            contentDescription = "Open Settings"
                        },
                    ) {
                        Text("Settings")
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                Destination.entries.forEach { item ->
                    NavigationBarItem(
                        selected = destination == item,
                        onClick = { destination = item },
                        icon = {
                            Text(
                                text = item.label.first().toString(),
                                modifier = Modifier.semantics {
                                    contentDescription = item.label
                                    selected = destination == item
                                },
                            )
                        },
                        label = { Text(item.label) },
                    )
                }
            }
        },
    ) { padding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            when (destination) {
                Destination.LEARN -> LearnScreen(padding)
                Destination.PRACTICE -> PlaceholderScreen(
                    padding = padding,
                    title = "Practice",
                    message = "M3 will add offline exercises and feedback here."
                )
                Destination.SEARCH -> PlaceholderScreen(
                    padding = padding,
                    title = "Search",
                    message = "M3 will add full-text offline search here."
                )
                Destination.PROGRESS -> PlaceholderScreen(
                    padding = padding,
                    title = "Progress",
                    message = "M3 will add local progress, scores, and learning history here."
                )
            }
        }
    }
}

@Composable
private fun LearnScreen(padding: PaddingValues) {
    val context = LocalContext.current
    val modules = remember { ModuleCatalog(context).loadSummaries() }

    LazyColumn(
        contentPadding = PaddingValues(
            start = 20.dp,
            top = padding.calculateTopPadding() + 16.dp,
            end = 20.dp,
            bottom = padding.calculateBottomPadding() + 20.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Learn privately, offline",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "The M2 shell is ready for the five-module core pack. Lesson reading and progress arrive in M3.",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Core content: ${modules.size} modules detected locally",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        items(modules, key = { it.id }) { module ->
            ModuleCard(module)
        }
    }
}

@Composable
private fun ModuleCard(module: ModuleSummary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = module.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = module.id,
                style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.height(10.dp))
            AssistChip(
                onClick = {},
                label = { Text(availabilityLabel(module.availability)) },
                modifier = Modifier.semantics {
                    contentDescription = "Availability: ${availabilityLabel(module.availability)}"
                },
            )
        }
    }
}

private fun availabilityLabel(value: String): String = when (value) {
    "termux-optional" -> "Termux optional"
    "offline-pack" -> "Optional download"
    "network-optional" -> "Network optional"
    else -> "Offline"
}

@Composable
private fun PlaceholderScreen(
    padding: PaddingValues,
    title: String,
    message: String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(message, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider()
        Text(
            "This placeholder keeps the navigation contract visible while the M3 offline learning loop is implemented.",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun PrivacyWelcomeScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = "Welcome to AetherLearn",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "A private, offline-first computer-science learning space for your phone.",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "No account is required. Your progress, notes, bookmarks, and scores stay on this device for the core experience.",
            style = MaterialTheme.typography.bodyLarge,
        )
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("What AetherLearn does not collect", fontWeight = FontWeight.SemiBold)
                Text("The core app does not require your name, email address, location, contacts, advertising identifier, or personal learning data.")
                Text("Optional downloads and exports are user-controlled. Nothing is uploaded automatically from this screen.")
            }
        }
        Text(
            text = "You can change the theme later in Settings. The core learning path is designed to continue after the initial content is available offline.",
            style = MaterialTheme.typography.bodyMedium,
        )
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Continue to AetherLearn offline shell" },
        ) {
            Text("Continue")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                },
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
                ThemeOptionRow(
                    mode = mode,
                    selected = mode == themeMode,
                    onSelected = { onThemeModeChanged(mode) },
                )
            }
            HorizontalDivider()
            Text("Privacy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("The M2 shell stores first-run state and theme preference in app-private SQLite storage. Learning data tables will be added in M3.")
            Text("No network permission is requested by the core Android app.")
        }
    }
}

@Composable
private fun ThemeOptionRow(
    mode: ThemeMode,
    selected: Boolean,
    onSelected: () -> Unit,
) {
    val label = when (mode) {
        ThemeMode.SYSTEM -> "Follow system"
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onSelected,
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(label, modifier = Modifier.padding(start = 12.dp))
    }
}
