package com.aetherlearn.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.platform.LocalContext
import com.aetherlearn.app.data.*
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
    var contentRevision by remember { mutableIntStateOf(0) }
    val lessons = remember(contentRevision) { ModuleCatalog(context, localStore).loadLessons() }
    val summaries = remember(lessons) {
        lessons.map { lesson ->
            ModuleSummary(
                id = lesson.id,
                title = lesson.title,
                availability = lesson.availability,
                estimatedMinutes = lesson.estimatedMinutes,
                strand = lesson.strand,
                prerequisites = lesson.prerequisites,
            )
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
    val recommended = remember(refreshToken, summaries) { recommendedLesson(summaries, progress) }
    val selectedLesson = lessons.firstOrNull { it.id == selectedLessonId }

    if (settingsOpen) {
        SettingsScreen(
            store = localStore,
            lessons = lessons,
            themeMode = themeMode,
            onThemeModeChanged = onThemeModeChanged,
            onContentChanged = { contentRevision++; refreshToken++ },
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
                    recommended = recommended,
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
                    onDeleteNote = { lessonId -> localStore.deleteNote(lessonId); refreshToken++ },
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
