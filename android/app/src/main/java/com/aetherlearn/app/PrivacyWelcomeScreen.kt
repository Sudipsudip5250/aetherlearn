package com.aetherlearn.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aetherlearn.app.data.StartingLevel

@Composable
internal fun PrivacyWelcomeScreen(onContinue: (StartingLevel?) -> Unit) {
    var selectedLevel by remember { mutableStateOf<StartingLevel?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        Text("Welcome to AetherLearn", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("A private, offline-first computer-science learning space for your phone.", style = MaterialTheme.typography.titleMedium)
        Text("No account is required. Your progress, notes, bookmarks, and scores stay on this device for the core experience.", style = MaterialTheme.typography.bodyLarge)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("What AetherLearn does not collect", fontWeight = FontWeight.SemiBold)
                Text("The core app does not require your name, email address, location, contacts, advertising identifier, or personal learning data.")
                Text("Optional downloads and exports are user-controlled. Nothing is uploaded automatically from this screen.")
            }
        }
        Text("Choose a starting point if you want a gentler recommendation. This changes suggestions only; every lesson stays available.", style = MaterialTheme.typography.bodyMedium)
        StartingLevel.entries.forEach { level ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedLevel == level,
                        role = Role.RadioButton,
                        onClick = { selectedLevel = level },
                    )
                    .padding(vertical = 4.dp)
                    .semantics { contentDescription = "Starting level: ${level.label}. ${level.description}" },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(selected = selectedLevel == level, onClick = null)
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(level.label, fontWeight = FontWeight.SemiBold)
                    Text(level.description, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        Text("You can change this choice later in Settings. The core learning path continues offline after the initial content is installed.", style = MaterialTheme.typography.bodyMedium)
        Button(
            onClick = { onContinue(selectedLevel) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Continue to AetherLearn offline shell" },
        ) { Text("Continue") }
        TextButton(onClick = { onContinue(null) }, modifier = Modifier.fillMaxWidth()) { Text("Skip for now") }
    }
}
