package com.aetherlearn.app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aetherlearn.app.data.*

@Composable
internal fun SearchScreen(
    padding: PaddingValues,
    lessons: List<LessonDocument>,
    progress: Map<String, ModuleProgress>,
    onLessonClick: (String) -> Unit,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val matches = remember(query, lessons) {
        val normalized = query.trim().lowercase()
        if (normalized.isBlank()) emptyList() else lessons.filter { it.searchText.lowercase().contains(normalized) }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Search offline", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search module titles and lesson text") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(if (query.isBlank()) "Search works from the local content assets with no network." else "${matches.size} result(s)")
        }
        items(matches, key = { it.id }) { lesson ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLessonClick(lesson.id) },
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(lesson.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("${lesson.id} · ${progressLabel(progress[lesson.id]?.state ?: LearningState.NOT_STARTED)}")
                    Text(searchExcerpt(lesson.searchText, query), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

private fun searchExcerpt(text: String, query: String): String {
    val clean = text.replace(Regex("\\s+"), " ").trim()
    val index = clean.lowercase().indexOf(query.trim().lowercase())
    if (index < 0) return clean.take(160)
    val start = (index - 50).coerceAtLeast(0)
    return clean.substring(start, (start + 180).coerceAtMost(clean.length))
}
