package com.aetherlearn.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aetherlearn.app.data.*

@Composable
internal fun PracticeScreen(
    padding: PaddingValues,
    lessons: List<LessonDocument>,
    progress: Map<String, ModuleProgress>,
    onLessonClick: (String) -> Unit,
) {
    val started = progress.values.any { it.state != LearningState.NOT_STARTED }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Practice", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Offline exercises from the current core lessons. Each short-answer check uses explicit local answer variants and explanations; interactive code runners are intentionally deferred.")
        }
        if (lessons.isEmpty()) {
            item {
                LocalEmptyNote(
                    title = "No practice is loaded yet",
                    body = "Cache or keep the core lessons on this device. Practice stays local and never requires an account.",
                )
            }
        } else if (!started) {
            item {
                LocalEmptyNote(
                    title = "No checks attempted yet",
                    body = "Open any lesson to try its knowledge check. Results stay on this device and do not block completion.",
                )
            }
        }
        items(lessons, key = { "${it.id}-practice" }) { lesson ->
            val state = progress[lesson.id]?.state ?: LearningState.NOT_STARTED
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(role = Role.Button) { onLessonClick(lesson.id) }
                    .semantics { contentDescription = "Open ${lesson.title} for practice, ${progressLabel(state)}" },
                colors = CardDefaults.cardColors(
                    containerColor = when (state) {
                        LearningState.COMPLETED -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                        LearningState.IN_PROGRESS -> MaterialTheme.colorScheme.primaryContainer
                        LearningState.NOT_STARTED -> MaterialTheme.colorScheme.surface
                    },
                ),
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(lesson.offlinePractice, style = MaterialTheme.typography.bodyLarge)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusBadge(state)
                        Text(
                            "Short answer · ${lesson.quizQuestions.size} checks · ${lesson.estimatedMinutes} min",
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    Text("Open lesson", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}