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
internal fun LearnScreen(
    padding: PaddingValues,
    lessons: List<ModuleSummary>,
    progress: Map<String, ModuleProgress>,
    bookmarks: Set<String>,
    recommended: ModuleSummary?,
    onLessonClick: (String) -> Unit,
) {
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
                text = "Read the current core lessons, practice at your pace, and keep learning data on this device.",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            val completed = progress.values.count { it.state == LearningState.COMPLETED }
            Text(
                text = "$completed of ${lessons.size} lessons completed",
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { if (lessons.isEmpty()) 0f else completed.toFloat() / lessons.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Overall progress: $completed of ${lessons.size} lessons completed" },
            )
        }
        recommended?.let { lesson ->
            item {
                RecommendedCard(
                    lesson = lesson,
                    progress = progress[lesson.id] ?: ModuleProgress(lesson.id, LearningState.NOT_STARTED, 0L),
                    reason = recommendationReason(lesson, progress),
                    onClick = { onLessonClick(lesson.id) },
                )
            }
        }
        lessons.groupBy { it.strand.ifBlank { "Other lessons" } }.forEach { (strand, strandLessons) ->
            item(key = "strand-$strand") {
                Text(
                    text = strand.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            items(strandLessons, key = { it.id }) { module ->
                ModuleCard(
                    module = module,
                    progress = progress[module.id] ?: ModuleProgress(module.id, LearningState.NOT_STARTED, 0L),
                    bookmarked = module.id in bookmarks,
                    onClick = { onLessonClick(module.id) },
                )
            }
        }
    }
}

@Composable
private fun RecommendedCard(
    lesson: ModuleSummary,
    progress: ModuleProgress,
    reason: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = "Open recommended lesson ${lesson.title}" },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("Recommended next", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text(lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(reason, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${lesson.strand} · ${progressLabel(progress.state)} · ${lesson.estimatedMinutes} min",
                style = MaterialTheme.typography.labelLarge,
            )
            Text("Open lesson", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ModuleCard(
    module: ModuleSummary,
    progress: ModuleProgress,
    bookmarked: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = "Open lesson ${module.title}" },
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(module.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(module.id, style = MaterialTheme.typography.bodySmall)
                }
                if (bookmarked) Text("Saved", style = MaterialTheme.typography.labelMedium)
            }
            Text(
                text = "${progressLabel(progress.state)} · ${module.estimatedMinutes} min · ${availabilityLabel(module.availability)}",
                style = MaterialTheme.typography.bodyMedium,
            )
            LinearProgressIndicator(
                progress = { if (progress.state == LearningState.COMPLETED) 1f else if (progress.state == LearningState.IN_PROGRESS) 0.5f else 0f },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

internal fun progressLabel(state: LearningState): String = when (state) {
    LearningState.NOT_STARTED -> "Not started"
    LearningState.IN_PROGRESS -> "In progress"
    LearningState.COMPLETED -> "Completed"
}

internal fun availabilityLabel(value: String): String = when (value) {
    "termux-optional" -> "Termux optional"
    "offline-pack" -> "Optional download"
    "network-optional" -> "Network optional"
    else -> "Offline"
}
