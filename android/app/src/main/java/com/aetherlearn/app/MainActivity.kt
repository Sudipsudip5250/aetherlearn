package com.aetherlearn.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.aetherlearn.app.data.AvailablePack
import com.aetherlearn.app.data.ExportManager
import com.aetherlearn.app.data.InstalledPack
import com.aetherlearn.app.data.LessonDocument
import com.aetherlearn.app.data.LearningState
import com.aetherlearn.app.data.LocalStore
import com.aetherlearn.app.data.ModuleCatalog
import com.aetherlearn.app.data.ModuleProgress
import com.aetherlearn.app.data.ModuleSummary
import com.aetherlearn.app.data.NoteSummary
import com.aetherlearn.app.data.PackManager
import com.aetherlearn.app.data.TermuxBridge
import com.aetherlearn.app.data.TermuxExerciseWrapper
import com.aetherlearn.app.data.TermuxLaunchResult
import com.aetherlearn.app.data.TermuxWrapperRegistry
import com.aetherlearn.app.data.ThemeMode
import com.aetherlearn.app.data.QuizQuestion
import com.aetherlearn.app.ui.theme.AetherLearnTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AetherLearnApp() }
    }
}

private enum class Destination(val label: String) {
    LEARN("Learn"),
    PRACTICE("Practice"),
    SEARCH("Search"),
    PROGRESS("Progress"),
}

@Composable
fun AetherLearnApp() {
    val context = LocalContext.current
    val localStore = remember { LocalStore(context.applicationContext) }
    var firstRunComplete by remember { mutableStateOf(localStore.isFirstRunComplete()) }
    var themeMode by remember { mutableStateOf(localStore.getThemeMode()) }

    DisposableEffect(localStore) {
        onDispose { localStore.close() }
    }

    AetherLearnTheme(themeMode = themeMode) {
        if (!firstRunComplete) {
            PrivacyWelcomeScreen(
                onContinue = {
                    localStore.markFirstRunComplete()
                    firstRunComplete = true
                },
            )
        } else {
            AppShell(
                localStore = localStore,
                themeMode = themeMode,
                onThemeModeChanged = { mode ->
                    localStore.setThemeMode(mode)
                    themeMode = mode
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppShell(
    localStore: LocalStore,
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
) {
    val context = LocalContext.current
    val lessons = remember { ModuleCatalog(context).loadLessons() }
    val summaries = remember(lessons) {
        lessons.map { lesson ->
            ModuleSummary(lesson.id, lesson.title, lesson.availability, lesson.estimatedMinutes)
        }
    }
    var destination by rememberSaveable { mutableStateOf(Destination.LEARN.name) }
    var settingsOpen by rememberSaveable { mutableStateOf(false) }
    var selectedLessonId by rememberSaveable { mutableStateOf<String?>(null) }
    var refreshToken by remember { mutableIntStateOf(0) }
    val progress = remember(refreshToken, lessons) {
        localStore.getAllProgress(lessons.map { it.id })
    }
    val bookmarks = remember(refreshToken) { localStore.getBookmarkedIds() }
    val selectedLesson = lessons.firstOrNull { it.id == selectedLessonId }

    if (settingsOpen) {
        SettingsScreen(
            store = localStore,
            lessons = lessons,
            themeMode = themeMode,
            onThemeModeChanged = onThemeModeChanged,
            onBack = { settingsOpen = false },
        )
        return
    }

    if (selectedLesson != null) {
        LessonReaderScreen(
            lesson = selectedLesson,
            store = localStore,
            progress = progress[selectedLesson.id] ?: ModuleProgress(selectedLesson.id, LearningState.NOT_STARTED, 0L),
            bookmarked = selectedLesson.id in bookmarks,
            onBack = { selectedLessonId = null },
            onChanged = { refreshToken++ },
        )
        return
    }

    val currentDestination = Destination.entries.firstOrNull { it.name == destination } ?: Destination.LEARN
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AetherLearn") },
                actions = {
                    TextButton(
                        onClick = { settingsOpen = true },
                        modifier = Modifier.semantics { contentDescription = "Open Settings" },
                    ) {
                        Text("Settings")
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                Destination.entries.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination == item,
                        onClick = { destination = item.name },
                        icon = {
                            Text(
                                text = item.label.first().toString(),
                                modifier = Modifier.semantics {
                                    contentDescription = item.label
                                    selected = currentDestination == item
                                },
                            )
                        },
                        label = { Text(item.label) },
                    )
                }
            }
        },
    ) { padding ->
        Surface(modifier = Modifier.fillMaxSize()) {
            when (currentDestination) {
                Destination.LEARN -> LearnScreen(
                    padding = padding,
                    lessons = summaries,
                    progress = progress,
                    bookmarks = bookmarks,
                    onLessonClick = { lessonId ->
                        localStore.markInProgress(lessonId)
                        refreshToken++
                        selectedLessonId = lessonId
                    },
                )
                Destination.PRACTICE -> PracticeScreen(
                    padding = padding,
                    lessons = lessons,
                    onLessonClick = { lessonId ->
                        localStore.markInProgress(lessonId)
                        refreshToken++
                        selectedLessonId = lessonId
                    },
                )
                Destination.SEARCH -> SearchScreen(
                    padding = padding,
                    lessons = lessons,
                    progress = progress,
                    onLessonClick = { lessonId ->
                        localStore.markInProgress(lessonId)
                        refreshToken++
                        selectedLessonId = lessonId
                    },
                )
                Destination.PROGRESS -> ProgressScreen(
                    padding = padding,
                    lessons = summaries,
                    progress = progress,
                    bookmarks = bookmarks,
                    notes = localStore.getNotes(),
                    onLessonClick = { lessonId ->
                        localStore.markInProgress(lessonId)
                        refreshToken++
                        selectedLessonId = lessonId
                    },
                )
            }
        }
    }
}

@Composable
private fun LearnScreen(
    padding: PaddingValues,
    lessons: List<ModuleSummary>,
    progress: Map<String, ModuleProgress>,
    bookmarks: Set<String>,
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
                text = "Read the five-module core pack, practice at your pace, and keep learning data on this device.",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            val completed = progress.values.count { it.state == LearningState.COMPLETED }
            Text(
                text = "$completed of ${lessons.size} modules completed",
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { if (lessons.isEmpty()) 0f else completed.toFloat() / lessons.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Overall progress: $completed of ${lessons.size} modules completed" },
            )
        }
        items(lessons, key = { it.id }) { module ->
            ModuleCard(
                module = module,
                progress = progress[module.id] ?: ModuleProgress(module.id, LearningState.NOT_STARTED, 0L),
                bookmarked = module.id in bookmarks,
                onClick = { onLessonClick(module.id) },
            )
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
            .clickable(onClick = onClick)
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

private fun progressLabel(state: LearningState): String = when (state) {
    LearningState.NOT_STARTED -> "Not started"
    LearningState.IN_PROGRESS -> "In progress"
    LearningState.COMPLETED -> "Completed"
}

private fun availabilityLabel(value: String): String = when (value) {
    "termux-optional" -> "Termux optional"
    "offline-pack" -> "Optional download"
    "network-optional" -> "Network optional"
    else -> "Offline"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LessonReaderScreen(
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

@Composable
private fun PracticeScreen(
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
            Text("Offline exercises from the five core lessons. Interactive code runners are intentionally deferred.")
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

@Composable
private fun SearchScreen(
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
                label = { Text("Search five module titles and lesson text") },
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

@Composable
private fun ProgressScreen(
    padding: PaddingValues,
    lessons: List<ModuleSummary>,
    progress: Map<String, ModuleProgress>,
    bookmarks: Set<String>,
    notes: List<NoteSummary>,
    onLessonClick: (String) -> Unit,
) {
    val completed = progress.values.count { it.state == LearningState.COMPLETED }
    val started = progress.values.count { it.state != LearningState.NOT_STARTED }
    val titles = lessons.associateBy { it.id }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text("Progress", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$completed completed · $started started · ${lessons.size} total")
            LinearProgressIndicator(
                progress = { if (lessons.isEmpty()) 0f else completed.toFloat() / lessons.size },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item { Text("Modules", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) }
        items(lessons, key = { "progress-${it.id}" }) { lesson ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLessonClick(lesson.id) },
            ) {
                Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(lesson.title, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                    Text(progressLabel(progress[lesson.id]?.state ?: LearningState.NOT_STARTED))
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
                    }
                }
            }
        }
    }
}

@Composable
private fun TermuxExerciseCard(
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

@Composable
private fun PrivacyWelcomeScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        Text("Welcome to AetherLearn", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("A private, offline-first computer-science learning space for your phone.", style = MaterialTheme.typography.titleMedium)
        Text("No account is required. Your progress, notes, bookmarks, and scores stay on this device for the core experience.", style = MaterialTheme.typography.bodyLarge)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("What AetherLearn does not collect", fontWeight = FontWeight.SemiBold)
                Text("The core app does not require your name, email address, location, contacts, advertising identifier, or personal learning data.")
                Text("Optional downloads and exports are user-controlled. Nothing is uploaded automatically from this screen.")
            }
        }
        Text("You can change the theme later in Settings. The core learning path continues offline after the initial content is installed.", style = MaterialTheme.typography.bodyMedium)
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Continue to AetherLearn offline shell" },
        ) { Text("Continue") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    store: LocalStore,
    lessons: List<LessonDocument>,
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val packManager = remember { PackManager(context.applicationContext, store) }
    var installedPacks by remember { mutableStateOf(packManager.installedPacks()) }
    var packMessage by remember { mutableStateOf<String?>(null) }
    var exportWarning by rememberSaveable { mutableStateOf<String?>(null) }
    var exportMessage by remember { mutableStateOf<String?>(null) }
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*"),
    ) { uri ->
        val format = exportWarning
        if (uri != null && format != null) {
            val content = if (format == "json") ExportManager.json(store, lessons) else ExportManager.markdown(store, lessons)
            exportMessage = if (ExportManager.write(context.contentResolver, uri, content)) {
                "Export saved to the location you selected."
            } else {
                "The export could not be written to the selected location."
            }
        }
        exportWarning = null
    }

    if (exportWarning != null) {
        AlertDialog(
            onDismissRequest = { exportWarning = null },
            title = { Text("Export local learning data?") },
            text = { Text("The export is created only after you choose a destination. It may contain personal learning notes. AetherLearn will not upload it automatically.") },
            confirmButton = {
                TextButton(onClick = {
                    val format = exportWarning ?: "markdown"
                    exportLauncher.launch(if (format == "json") "aetherlearn-learning.json" else "aetherlearn-learning.md")
                }) { Text("Choose destination") }
            },
            dismissButton = { TextButton(onClick = { exportWarning = null }) { Text("Cancel") } },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Appearance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Choose whether AetherLearn follows the device or uses a fixed theme.")
            ThemeMode.entries.forEach { mode ->
                ThemeOptionRow(mode, mode == themeMode) { onThemeModeChanged(mode) }
            }
            HorizontalDivider()
            Text("Export", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Export your progress, quiz attempts, notes, and bookmarks offline. You choose the destination with the Android file picker.")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { exportWarning = "markdown" }, modifier = Modifier.weight(1f)) { Text("Markdown") }
                Button(onClick = { exportWarning = "json" }, modifier = Modifier.weight(1f)) { Text("JSON") }
            }
            exportMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            HorizontalDivider()
            Text("Storage & content packs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Core pack: 5 modules, ${formatBytes(packManager.coreSizeBytes())}, always available offline and protected from deletion.")
            Text("Learning data and installed optional packs: approximately ${formatBytes(packManager.learningDataSizeBytes())}.")
            packManager.availablePacks().forEach { available ->
                OptionalPackCard(
                    available = available,
                    installed = installedPacks.firstOrNull { it.id == available.id },
                    onInstall = {
                        val result = packManager.installPack(available.id)
                        packMessage = result.message
                        installedPacks = packManager.installedPacks()
                    },
                    onDelete = {
                        val result = packManager.deletePack(available.id)
                        packMessage = result.message
                        installedPacks = packManager.installedPacks()
                    },
                )
            }
            packMessage?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            HorizontalDivider()
            Text("Privacy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Progress, quiz attempts, notes, bookmarks, preferences, and pack status are stored in app-private SQLite storage.")
            Text("No network permission is requested by the core Android app. Optional packs in this M4 foundation are bundled local assets; no network download is implemented.")
        }
    }
}

@Composable
private fun OptionalPackCard(
    available: AvailablePack,
    installed: InstalledPack?,
    onInstall: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(available.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(available.description)
            Text("Version ${available.version} · ${formatBytes(available.sizeBytes)} · ${if (installed == null) "Available locally" else "Installed and verified"}")
            if (installed == null) {
                Button(onClick = onInstall, modifier = Modifier.fillMaxWidth()) { Text("Install local pack") }
            } else {
                Text("Checksum: ${installed.checksum.take(12)}…", style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) { Text("Delete optional pack") }
            }
        }
    }
}

private fun formatBytes(bytes: Long): String = when {
    bytes < 1024L -> "$bytes B"
    bytes < 1024L * 1024L -> "${bytes / 1024L} KB"
    else -> "${bytes / (1024L * 1024L)} MB"
}

@Composable
private fun ThemeOptionRow(mode: ThemeMode, selected: Boolean, onSelected: () -> Unit) {
    val label = when (mode) {
        ThemeMode.SYSTEM -> "Follow system"
        ThemeMode.LIGHT -> "Light"
        ThemeMode.DARK -> "Dark"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelected)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(label, modifier = Modifier.padding(start = 12.dp))
    }
}
