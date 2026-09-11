package com.aetherlearn.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aetherlearn.app.data.*

@Composable
internal fun ProgressScreen(
    padding: PaddingValues,
    lessons: List<ModuleSummary>,
    progress: Map<String, ModuleProgress>,
    bookmarks: Set<String>,
    notes: List<NoteSummary>,
    goals: List<LearningGoal>,
    remindersOptIn: Boolean,
    onLessonClick: (String) -> Unit,
    onDeleteNote: (String) -> Unit,
    onAddGoal: (String, String?, Boolean) -> Unit,
    onToggleGoal: (LearningGoal) -> Unit,
    onDeleteGoal: (LearningGoal) -> Unit,
) {
    val completed = progress.values.count { it.state == LearningState.COMPLETED }
    val inProgress = progress.values.count { it.state == LearningState.IN_PROGRESS }
    val remaining = (lessons.size - completed).coerceAtLeast(0)
    val titles = lessons.associateBy { it.id }
    val weekAgo = System.currentTimeMillis() - 7L * 24L * 60L * 60L * 1000L
    val weekly = progress.values.count { it.updatedAt >= weekAgo && it.state != LearningState.NOT_STARTED }
    val recent = progress.values.filter { it.updatedAt > 0L }.sortedByDescending { it.updatedAt }.take(5)
    var goalTitle by remember { mutableStateOf("") }
    var remindTomorrow by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Progress", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("This dashboard uses timestamps already stored on this device. Nothing is sent anywhere.")
            Spacer(modifier = Modifier.height(8.dp))
            Text("$completed completed · $inProgress in progress · $remaining remaining")
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { if (lessons.isEmpty()) 0f else completed.toFloat() / lessons.size },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("$weekly lesson${if (weekly == 1) "" else "s"} updated in the last 7 days", style = MaterialTheme.typography.bodyMedium)
            if (completed == 0 && inProgress == 0) {
                Spacer(modifier = Modifier.height(12.dp))
                LocalEmptyNote(
                    title = "No progress yet",
                    body = "Open a lesson when you are ready. Completion, bookmarks, notes, and goals stay on this device and are never synced.",
                )
            }
        }
        if (recent.isNotEmpty()) {
            item { Text("Recent", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
            items(recent, key = { "recent-${it.moduleId}" }) { item ->
                val lesson = titles[item.moduleId]
                if (lesson != null) {
                    TextButton(onClick = { onLessonClick(lesson.id) }, modifier = Modifier.fillMaxWidth()) {
                        Text("${lesson.title} · ${progressLabel(item.state)}")
                    }
                }
            }
        }
        item { Text("Local goals", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
        item {
            Text("Create a private task on this device. You can optionally link it to a lesson. Goals are included in an export you choose.")
            OutlinedTextField(
                value = goalTitle,
                onValueChange = { goalTitle = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Goal") },
                placeholder = { Text("Finish DL-01 knowledge check") },
                singleLine = true,
            )
            if (remindersOptIn) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = remindTomorrow, onCheckedChange = { remindTomorrow = it })
                    Text("Remind me tomorrow on this device")
                }
            }
            Button(
                onClick = {
                    val title = goalTitle.trim()
                    if (title.isNotEmpty()) {
                        onAddGoal(title, null, remindTomorrow && remindersOptIn)
                        goalTitle = ""
                        remindTomorrow = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Save goal locally") }
        }
        if (goals.isEmpty()) {
            item { LocalEmptyNote(title = "No goals yet", body = "Add a task above. It stays on this device and is never sent to a server.") }
        } else {
            items(goals, key = { "goal-${it.id}" }) { goal ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = goal.done, onCheckedChange = { onToggleGoal(goal) })
                            Text(goal.title, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        }
                        goal.lessonId?.let { id ->
                            val title = titles[id]?.title ?: id
                            TextButton(onClick = { onLessonClick(id) }) { Text("Open $title") }
                        }
                        if (goal.remindAt != null) Text("Local reminder set", style = MaterialTheme.typography.bodySmall)
                        TextButton(onClick = { onDeleteGoal(goal) }) { Text("Delete goal") }
                    }
                }
            }
        }
        item { Text("Modules", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
        items(lessons, key = { "progress-${it.id}" }) { lesson ->
            val state = progress[lesson.id]?.state ?: LearningState.NOT_STARTED
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLessonClick(lesson.id) }
                    .semantics { contentDescription = "Open ${lesson.title}, ${progressLabel(state)}" },
                colors = CardDefaults.cardColors(
                    containerColor = when (state) {
                        LearningState.COMPLETED -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                        LearningState.IN_PROGRESS -> MaterialTheme.colorScheme.primaryContainer
                        LearningState.NOT_STARTED -> MaterialTheme.colorScheme.surface
                    },
                ),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(lesson.title, fontWeight = FontWeight.SemiBold)
                        Text(lesson.id, style = MaterialTheme.typography.bodySmall)
                    }
                    StatusBadge(state)
                }
            }
        }
        item { Text("Bookmarks", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
        if (bookmarks.isEmpty()) {
            item { LocalEmptyNote(title = "No bookmarks yet", body = "Bookmark a lesson from its reader. The list stays on this device.") }
        } else {
            items(bookmarks.toList().sorted(), key = { "bookmark-$it" }) { moduleId ->
                TextButton(onClick = { onLessonClick(moduleId) }) { Text(titles[moduleId]?.title ?: moduleId) }
            }
        }
        item { Text("Recent notes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
        if (notes.isEmpty()) {
            item { LocalEmptyNote(title = "No notes yet", body = "Write a private note from a lesson reader. Notes are stored only on this device.") }
        } else {
            items(notes.take(5), key = { "note-${it.moduleId}" }) { note ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(titles[note.moduleId]?.title ?: note.moduleId, fontWeight = FontWeight.SemiBold)
                        Text(note.body)
                        TextButton(onClick = { onDeleteNote(note.moduleId) }) { Text("Delete note") }
                    }
                }
            }
        }
    }
}