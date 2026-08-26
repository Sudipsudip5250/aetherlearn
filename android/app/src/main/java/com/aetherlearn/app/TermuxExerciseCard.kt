package com.aetherlearn.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aetherlearn.app.data.*

@Composable
internal fun TermuxExerciseCard(
    lessonId: String,
    store: LocalStore,
    onChanged: () -> Unit,
) {
    val context = LocalContext.current
    val wrapper = remember(lessonId) { TermuxWrapperRegistry.wrappers.firstOrNull { it.lessonId == lessonId } }
    if (wrapper == null) return
    var confirmOpen by rememberSaveable(wrapper.wrapperId) { mutableStateOf(false) }
    var guidanceOpen by rememberSaveable(wrapper.wrapperId) { mutableStateOf(false) }
    var started by rememberSaveable(wrapper.wrapperId) { mutableStateOf(false) }
    var completed by remember(wrapper.wrapperId) { mutableStateOf(store.isExerciseCompleted(wrapper.wrapperId)) }
    var statusMessage by rememberSaveable(wrapper.wrapperId) { mutableStateOf<String?>(null) }
    val installed = TermuxBridge.isInstalled(context)

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Optional Termux exercise", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(wrapper.title, style = MaterialTheme.typography.titleMedium)
            Text("Contract v${wrapper.contractVersion} · ${wrapper.wrapperId} · local-only · no network")
            Text(wrapper.expectedEffects)
            Text("Termux status: ${if (installed) "installed" else "not detected"}.")
            Text("Fallback: ${wrapper.fallback}")
            if (guidanceOpen || !installed) {
                Text(
                    "Setup guidance: install Termux from a trusted source, grant AetherLearn the Termux RUN_COMMAND permission in Android App Info, and set allow-external-apps=true in Termux’s termux.properties. Do not grant or change anything automatically. The in-app fallback remains available.",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (installed) confirmOpen = true else guidanceOpen = !guidanceOpen
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(if (installed) "Review and open" else "Setup guidance")
                }
                Button(
                    onClick = {
                        store.setExerciseCompleted(wrapper.wrapperId, wrapper.contractVersion)
                        completed = true
                        statusMessage = "Exercise marked complete by you. Terminal output was not used as proof."
                        onChanged()
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(if (completed) "Completed" else "Mark complete")
                }
            }
            if (started && !completed) {
                TextButton(
                    onClick = {
                        store.setExerciseCompleted(wrapper.wrapperId, wrapper.contractVersion)
                        completed = true
                        statusMessage = "Exercise marked complete after your confirmation."
                        onChanged()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("I finished it in Termux") }
            }
            statusMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
    }

    if (confirmOpen) {
        AlertDialog(
            onDismissRequest = { confirmOpen = false },
            title = { Text("Confirm local Termux exercise") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Wrapper ID: ${wrapper.wrapperId} · contract v${wrapper.contractVersion}")
                    Text("Executable: ${wrapper.executable}")
                    Text("Arguments: ${wrapper.arguments.joinToString(" ") { displayArgument(it) }}")
                    Text("Working directory: ${wrapper.workingDirectory}")
                    Text("Prerequisites: ${wrapper.prerequisites.joinToString("; ")}")
                    Text("Expected effects: ${wrapper.expectedEffects}")
                    Text("No network, shared storage, credentials, package installation, or remote host is used.")
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmOpen = false
                    when (val result = TermuxBridge.launch(context, wrapper)) {
                        TermuxLaunchResult.Started -> {
                            started = true
                            statusMessage = "Termux was asked to start the fixed local wrapper. Return here and confirm completion yourself."
                        }
                        TermuxLaunchResult.NotInstalled -> {
                            guidanceOpen = true
                            statusMessage = "Termux is not installed. Use the in-app fallback."
                        }
                        TermuxLaunchResult.PermissionRequired -> {
                            guidanceOpen = true
                            statusMessage = "Termux rejected the request because RUN_COMMAND permission or external-app setup is missing."
                        }
                        TermuxLaunchResult.NotSupported -> {
                            guidanceOpen = true
                            statusMessage = "This Termux installation does not support the required RUN_COMMAND service. Use the in-app fallback."
                        }
                        is TermuxLaunchResult.Failed -> {
                            guidanceOpen = true
                            statusMessage = result.message
                        }
                    }
                }) { Text("Confirm and open") }
            },
            dismissButton = { TextButton(onClick = { confirmOpen = false }) { Text("Cancel") } },
        )
    }
}

private fun displayArgument(argument: String): String = if (argument.any { it.isWhitespace() }) "\"$argument\"" else argument
