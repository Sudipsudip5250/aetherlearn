package com.aetherlearn.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onLessonClick: (String) -> Unit,
    onDeleteNote: (String) -> Unit,
) {
    val completed = progress.values.count { it.state == LearningState.COMPLETED }
    val inProgress = progress.values.count { it.state == LearningState.IN_PROGRESS }
    val remaining = (lessons.size - completed).coerceAtLeast(0)
    val titles = lessons.associateBy { it.id }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Progress", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$completed completed · $inProgress in progress · $remaining remaining")
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { if (lessons.isEmpty()) 0f else completed.toFloat() / lessons.size },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item { Text("Modules", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
        items(lessons, key = { "progress-${it.id}" }) { lesson ->
            val state = progress[lesson.id]?.state ?: LearningState.NOT_STARTED
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLessonClick(lesson.id) },
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
            item { Text("No bookmarked lessons yet.") }
        } else {
            items(bookmarks.toList().sorted(), key = { "bookmark-$it" }) { moduleId ->
                TextButton(onClick = { onLessonClick(moduleId) }) { Text(titles[moduleId]?.title ?: moduleId) }
            }
        }
        item { Text("Recent notes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
        if (notes.isEmpty()) {
            item { Text("No notes yet. Add one from a lesson reader.") }
        } else {
            items(notes.take(5), key = { "note-${it.moduleId}" }) { note ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(titles[note.moduleId]?.title ?: note.moduleId, fontWeight = FontWeight.SemiBold)
                        Text(note.body)
                        TextButton(onClick = { onDeleteNote(note.moduleId) }, modifier = Modifier.fillMaxWidth()) { Text("Delete note") }
                    }
                }
            }
        }
    }
}