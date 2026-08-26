package com.aetherlearn.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aetherlearn.app.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LessonReaderScreen(
    lesson: LessonDocument,
    store: LocalStore,
    progress: ModuleProgress,
    bookmarked: Boolean,
    onBack: () -> Unit,
    onChanged: () -> Unit,
) {
    var noteText by remember(lesson.id) { mutableStateOf(store.getNote(lesson.id)?.body.orEmpty()) }
    var quizAnswers by remember(lesson.id) { mutableStateOf(List(lesson.quizQuestions.size) { "" }) }
    var quizResult by remember(lesson.id) { mutableStateOf<Pair<Int, Int>?>(null) }
    var isBookmarked by remember(lesson.id, bookmarked) { mutableStateOf(bookmarked) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(lesson.title, maxLines = 1) },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(lesson.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                text = "${lesson.strand} · ${lesson.level} · ${lesson.estimatedMinutes} min · ${availabilityLabel(lesson.availability)}",
                style = MaterialTheme.typography.labelLarge,
            )
            Text("Risk tier: ${lesson.riskTier}", style = MaterialTheme.typography.bodySmall)
            if (lesson.objectives.isNotEmpty()) {
                LessonSection("Objectives") {
                    BulletList(lesson.objectives)
                }
            }
            if (lesson.prerequisites.isNotEmpty()) {
                LessonSection("Prerequisites") {
                    Text(lesson.prerequisites.joinToString())
                }
            }
            lesson.sections.forEach { (name, body) ->
                if (name != "Objectives" && name != "Prerequisites" && name != "Knowledge check") {
                    LessonSection(name) { MarkdownBody(body) }
                }
            }
            if (lesson.availability == "termux-optional") {
                TermuxExerciseCard(lesson.id, store, onChanged)
            }
            if (lesson.quizQuestions.isNotEmpty()) {
                QuizSection(
                    questions = lesson.quizQuestions,
                    answers = quizAnswers,
                    result = quizResult,
                    onAnswerChanged = { index, value ->
                        quizAnswers = quizAnswers.toMutableList().also { it[index] = value }
                    },
                    onCheck = {
                        val score = lesson.quizQuestions.indices.count { index ->
                            matchesExpected(quizAnswers[index], lesson.quizQuestions[index].expectedAnswer)
                        }
                        quizResult = score to lesson.quizQuestions.size
                        store.saveQuizAttempt(lesson.id, score, lesson.quizQuestions.size)
                        onChanged()
                    },
                    onRetry = {
                        quizAnswers = List(lesson.quizQuestions.size) { "" }
                        quizResult = null
                    },
                )
            }
            HorizontalDivider()
            Text("Your learning data", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Status: ${progressLabel(progress.state)} · Attempts: ${progress.attemptCount}${progress.bestScore?.let { " · Best score: $it/${lesson.quizQuestions.size}" } ?: ""}")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        store.setCompleted(lesson.id)
                        onChanged()
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(if (progress.state == LearningState.COMPLETED) "Completed" else "Mark complete")
                }
                Button(
                    onClick = {
                        isBookmarked = !isBookmarked
                        store.setBookmarked(lesson.id, isBookmarked)
                        onChanged()
                    },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(if (isBookmarked) "Remove bookmark" else "Bookmark")
                }
            }
            Text("Private note", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Write a note about this lesson") },
                minLines = 4,
            )
            Button(
                onClick = {
                    store.saveNote(lesson.id, noteText)
                    onChanged()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save note on this device")
            }
        }
    }
}

@Composable
private fun LessonSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        content()
    }
}

@Composable
private fun BulletList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item -> Text("• $item", style = MaterialTheme.typography.bodyLarge) }
    }
}

@Composable
private fun MarkdownBody(body: String) {
    val cleaned = body
        .replace(Regex("\\[([^]]+)]\\([^)]*\\)"), "$1")
        .replace("**", "")
        .replace("`", "")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        cleaned.split(Regex("\\n\\s*\\n"))
            .map(String::trim)
            .filter(String::isNotEmpty)
            .forEach { paragraph ->
                val display = paragraph.lines().joinToString("\n") { line ->
                    if (line.trimStart().startsWith("- ")) "• ${line.trimStart().removePrefix("- ").trim()}" else line
                }
                Text(display, style = MaterialTheme.typography.bodyLarge)
            }
    }
}

@Composable
private fun QuizSection(
    questions: List<QuizQuestion>,
    answers: List<String>,
    result: Pair<Int, Int>?,
    onAnswerChanged: (Int, String) -> Unit,
    onCheck: () -> Unit,
    onRetry: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Knowledge check", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Text("Answer in your own words. The check is for feedback; it does not block completion.")
        questions.forEachIndexed { index, question ->
            Text("${question.number}. ${question.prompt}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = answers.getOrElse(index) { "" },
                onValueChange = { onAnswerChanged(index, it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Your answer") },
                minLines = 2,
            )
            if (result != null) {
                val correct = matchesExpected(answers.getOrElse(index) { "" }, question.expectedAnswer)
                Text(if (correct) "Correct" else "Review: ${question.expectedAnswer}", fontWeight = FontWeight.SemiBold)
                if (question.explanation.isNotBlank()) Text(question.explanation)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onCheck, modifier = Modifier.weight(1f)) { Text("Check answers") }
            if (result != null) Button(onClick = onRetry, modifier = Modifier.weight(1f)) { Text("Retry") }
        }
        result?.let { (score, total) ->
            Text("Result: $score/$total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

private fun matchesExpected(answer: String, expected: String): Boolean {
    val normalizedAnswer = answer.lowercase().replace(Regex("[^a-z0-9 ]"), " ").replace(Regex("\\s+"), " ").trim()
    val normalizedExpected = expected.lowercase().replace(Regex("[^a-z0-9 ]"), " ").replace(Regex("\\s+"), " ").trim()
    if (normalizedAnswer.isBlank() || normalizedExpected.isBlank()) return false
    if (normalizedAnswer == normalizedExpected || normalizedAnswer.contains(normalizedExpected) || normalizedExpected.contains(normalizedAnswer)) return true
    val alternatives = expected.split(",", " or ")
        .map { it.lowercase().replace(Regex("[^a-z0-9 ]"), " ").trim() }
        .filter { it.length >= 4 }
    return alternatives.any { normalizedAnswer.contains(it) }
}
