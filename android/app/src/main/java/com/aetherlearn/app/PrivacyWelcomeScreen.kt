package com.aetherlearn.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun PrivacyWelcomeScreen(onContinue: () -> Unit) {
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
        Text("You can change the theme later in Settings. The core learning path continues offline after the initial content is installed.", style = MaterialTheme.typography.bodyMedium)
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Continue to AetherLearn offline shell" },
        ) { Text("Continue") }
    }
}
