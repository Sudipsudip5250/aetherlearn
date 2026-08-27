package com.aetherlearn.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Practice", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Offline exercises from all ${lessons.size} current lessons. Each short-answer check uses explicit local answer variants and explanations; interactive code runners are intentionally deferred.")
        }
        lessons.forEach { lesson ->
            item(key = "${lesson.id}-practice") {
                val lessonProgress = progress[lesson.id]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(role = Role.Button) { onLessonClick(lesson.id) }
                        .semantics { contentDescription = "Open practice for ${lesson.title}" },
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(lesson.offlinePractice, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "Short answer · ${lesson.quizQuestions.size} checks · ${lesson.estimatedMinutes} min · ${availabilityLabel(lesson.availability)}",
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Text("Lesson status: ${progressLabel(lessonProgress?.state ?: LearningState.NOT_STARTED)}", style = MaterialTheme.typography.bodySmall)
                        Text("Open lesson for context and retry", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
