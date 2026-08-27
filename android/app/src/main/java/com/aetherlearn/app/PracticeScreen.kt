package com.aetherlearn.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aetherlearn.app.data.*

@Composable
internal fun PracticeScreen(
    padding: PaddingValues,
    lessons: List<LessonDocument>,
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
            Text("Offline exercises from all ${lessons.size} current lessons. Interactive code runners are intentionally deferred.")
        }
        lessons.forEach { lesson ->
            item(key = "${lesson.id}-practice") {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onLessonClick(lesson.id) },
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(lesson.offlinePractice, style = MaterialTheme.typography.bodyLarge)
                        Text("Open lesson for context", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
