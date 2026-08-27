package com.aetherlearn.app.data

import android.content.ContentResolver
import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject

object ExportManager {
    fun markdown(store: LocalStore, lessons: List<LessonDocument>): String = buildString {
        appendLine("# AetherLearn learning data")
        appendLine()
        appendLine("> This export was created by the user on this device. It contains local learning progress and may contain personal notes.")
        appendLine()
        appendLine("## Module progress")
        appendLine()
        appendLine("| Module | State | Attempts | Best score | Bookmarked |")
        appendLine("|---|---|---:|---:|---|")
        val bookmarks = store.getBookmarkedIds()
        lessons.forEach { lesson ->
            val progress = store.getProgress(lesson.id)
            val best = progress.bestScore?.let { "$it/${lesson.quizQuestions.size}" } ?: "—"
            appendLine("| ${lesson.title.escapeTable()} | ${progress.state.displayName} | ${progress.attemptCount} | $best | ${if (lesson.id in bookmarks) "Yes" else "No"} |")
        }
        appendLine()
        appendLine("## Notes")
        appendLine()
        val notes = store.getNotes()
        if (notes.isEmpty()) {
            appendLine("No notes were saved.")
        } else {
            notes.forEach { note ->
                val title = lessons.firstOrNull { it.id == note.moduleId }?.title ?: note.moduleId
                appendLine("### ${title.escapeHeading()}")
                appendLine()
                appendLine(note.body)
                appendLine()
            }
        }
        appendLine("## Quiz attempts")
        appendLine()
        val attempts = store.getQuizAttempts()
        if (attempts.isEmpty()) {
            appendLine("No quiz attempts were saved.")
        } else {
            attempts.forEach { attempt ->
                val title = lessons.firstOrNull { it.id == attempt.moduleId }?.title ?: attempt.moduleId
                appendLine("- ${title.escapeHeading()}: ${attempt.score}/${attempt.total}")
            }
        }
    }

    fun json(store: LocalStore, lessons: List<LessonDocument>): String {
        val root = JSONObject()
            .put("format", "aetherlearn-learning-export")
            .put("format_version", 1)
            .put("contains_personal_notes", store.getNotes().isNotEmpty())
            .put("starting_level", store.getStartingLevel()?.name ?: JSONObject.NULL)
        val modules = JSONArray()
        val bookmarks = store.getBookmarkedIds()
        lessons.forEach { lesson ->
            val progress = store.getProgress(lesson.id)
            modules.put(
                JSONObject()
                    .put("module_id", lesson.id)
                    .put("title", lesson.title)
                    .put("state", progress.state.displayName)
                    .put("attempt_count", progress.attemptCount)
                    .put("best_score", progress.bestScore ?: JSONObject.NULL)
                    .put("bookmarked", lesson.id in bookmarks),
            )
        }
        root.put("modules", modules)
        root.put("notes", JSONArray().also { notes ->
            store.getNotes().forEach { note ->
                notes.put(JSONObject().put("module_id", note.moduleId).put("body", note.body).put("updated_at", note.updatedAt))
            }
        })
        root.put("quiz_attempts", JSONArray().also { attempts ->
            store.getQuizAttempts().forEach { attempt ->
                attempts.put(
                    JSONObject()
                        .put("module_id", attempt.moduleId)
                        .put("score", attempt.score)
                        .put("total", attempt.total)
                        .put("attempted_at", attempt.attemptedAt),
                )
            }
        })
        return root.toString(2)
    }

    fun write(resolver: ContentResolver, uri: Uri, content: String): Boolean = runCatching {
        resolver.openOutputStream(uri)?.bufferedWriter().use { writer ->
            requireNotNull(writer) { "The selected destination could not be opened." }
            writer.write(content)
        }
        true
    }.getOrDefault(false)

    private fun String.escapeTable(): String = replace("|", "\\|").replace("\n", " ")
    private fun String.escapeHeading(): String = replace("#", "")
    private val LearningState.displayName: String
        get() = when (this) {
            LearningState.NOT_STARTED -> "not started"
            LearningState.IN_PROGRESS -> "in progress"
            LearningState.COMPLETED -> "completed"
        }
}
