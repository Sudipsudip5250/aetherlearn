package com.aetherlearn.app

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import android.graphics.Color
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.text.withStyle
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
    var noteStatus by remember(lesson.id) { mutableStateOf("") }

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
            Text("Completion: mark the lesson complete yourself; checks provide feedback and do not block completion.", style = MaterialTheme.typography.bodySmall)
            if (lesson.objectives.isNotEmpty()) {
                LessonSection("Objectives") {
                    BulletList(lesson.objectives)
                }
            }
            OptionalVisualSection(store = store, lessonId = lesson.id)
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
                            matchesExpected(quizAnswers[index], lesson.quizQuestions[index])
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
                    noteStatus = "Saved privately on this device."
                    onChanged()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save note on this device")
            }
            if (noteStatus.isNotBlank()) {
                Text(noteStatus, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun OptionalVisualSection(store: LocalStore, lessonId: String) {
    val packVersion = remember { store.getInstalledPacks().firstOrNull { it.id == "visual-foundations" }?.version }
    val asset = remember(lessonId, packVersion) { VisualPackCatalog.findForLesson(store, lessonId) } ?: return
    val svg = remember(asset.file.absolutePath, asset.file.lastModified()) { runCatching { asset.file.readText(Charsets.UTF_8) }.getOrNull() } ?: return
    LessonSection("Optional visual aid") {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = false
                    settings.domStorageEnabled = false
                    settings.allowFileAccess = false
                    settings.allowContentAccess = false
                    webViewClient = WebViewClient()
                    setNetworkAvailable(false)
                    setBackgroundColor(Color.TRANSPARENT)
                }
            },
            update = { webView -> webView.loadDataWithBaseURL(null, svg, "image/svg+xml", "UTF-8", null) },
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .semantics { contentDescription = asset.altText },
        )
        Text(asset.caption, fontWeight = FontWeight.SemiBold)
        Text("Text equivalent: ${asset.textEquivalent}")
        Text("Try it", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(asset.practicePrompt)
        asset.practiceSteps.forEachIndexed { index, step ->
            Text("${index + 1}. $step")
        }
        Text("Self-check: ${asset.practiceSuccessCriteria}")
        Text("License: ${asset.license} · ${asset.attribution}", style = MaterialTheme.typography.bodySmall)
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
    val clipboard = LocalClipboardManager.current
    var copiedCode by remember(body) { mutableStateOf<String?>(null) }
    val blocks = mutableListOf<MarkdownBlock>()
    val paragraph = StringBuilder()
    var inCode = false
    val tableRows = mutableListOf<List<String>>()
    val flushTable = {
        if (tableRows.isNotEmpty()) {
            val header = tableRows.first()
            val rows = tableRows.drop(1).filterNot { row -> row.all { cell -> cell.matches(Regex(":?-{3,}:?")) } }
            blocks += MarkdownBlock.Table(header, rows)
            tableRows.clear()
        }
    }
    val flushParagraph = {
        val value = paragraph.toString().trim()
        if (value.isNotEmpty()) blocks += MarkdownBlock.Text(value)
        paragraph.clear()
    }
    body.lines().forEach { line ->
        if (line.trimStart().startsWith("```")) {
            if (inCode) {
                blocks += MarkdownBlock.Code(paragraph.toString().trimEnd())
                paragraph.clear()
            } else {
                flushTable()
                flushParagraph()
            }
            inCode = !inCode
        } else if (inCode) {
            paragraph.append(line).append('\n')
        } else if (line.trimStart().startsWith("|") && line.trimEnd().endsWith("|")) {
            flushParagraph()
            tableRows += line.trim().removePrefix("|").removeSuffix("|").split("|").map(String::trim)
        } else if (line.isBlank()) {
            flushTable()
            flushParagraph()
        } else {
            flushTable()
            if (paragraph.isNotEmpty()) paragraph.append('\n')
            paragraph.append(line)
        }
    }
    if (inCode) blocks += MarkdownBlock.Code(paragraph.toString().trimEnd()) else { flushTable(); flushParagraph() }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        blocks.forEach { block ->
            when (block) {
                is MarkdownBlock.Text -> Text(inlineMarkdown(block.value), style = MaterialTheme.typography.bodyLarge)
                is MarkdownBlock.Table -> Surface(color = MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                        TableRow(block.header, header = true)
                        block.rows.forEach { row -> TableRow(row, header = false) }
                    }
                }
                is MarkdownBlock.Code -> Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Code example", style = MaterialTheme.typography.labelMedium)
                            TextButton(onClick = { clipboard.setText(AnnotatedString(block.value)); copiedCode = block.value }) { Text(if (copiedCode == block.value) "Copied" else "Copy code") }
                        }
                        Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 12.dp, vertical = 4.dp)) {
                            Text(block.value, fontFamily = FontFamily.Monospace, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

private sealed interface MarkdownBlock {
    data class Text(val value: String) : MarkdownBlock
    data class Table(val header: List<String>, val rows: List<List<String>>) : MarkdownBlock
    data class Code(val value: String) : MarkdownBlock
}

@Composable
private fun TableRow(cells: List<String>, header: Boolean) {
    Row {
        cells.forEach { cell ->
            Text(
                text = inlineMarkdown(cell),
                modifier = Modifier
                    .widthIn(min = 120.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
                    .padding(8.dp),
                fontWeight = if (header) FontWeight.Bold else FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

private fun inlineMarkdown(value: String): AnnotatedString {
    val cleaned = value.replace(Regex("\\[([^]]+)]\\([^)]*\\)"), "$1").lines().joinToString("\n") { line ->
        if (line.trimStart().startsWith("- ")) "• ${line.trimStart().removePrefix("- ").trim()}" else line
    }
    val pattern = Regex("(\\*\\*[^*]+\\*\\*|`[^`]+`)")
    return buildAnnotatedString {
        var cursor = 0
        pattern.findAll(cleaned).forEach { match ->
            append(cleaned.substring(cursor, match.range.first))
            val token = match.value
            when {
                token.startsWith("**") -> withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(token.removeSurrounding("**")) }
                token.startsWith("`") -> withStyle(SpanStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Medium)) { append(token.removeSurrounding("`")) }
            }
            cursor = match.range.last + 1
        }
        append(cleaned.substring(cursor))
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
                val correct = matchesExpected(answers.getOrElse(index) { "" }, question)
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

private fun matchesExpected(answer: String, question: QuizQuestion): Boolean {
    val normalizedAnswer = normalizeAnswer(answer)
    if (normalizedAnswer.isBlank()) return false
    return question.acceptedAnswers.any { normalizeAnswer(it) == normalizedAnswer }
}

private fun normalizeAnswer(value: String): String = value
    .lowercase()
    .replace(Regex("[^a-z0-9 ]"), " ")
    .replace(Regex("\\s+"), " ")
    .trim()
