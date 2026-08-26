package com.aetherlearn.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/** App-private storage boundary. Content-pack assets remain separate from user learning data. */
class LocalStore(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION,
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE app_metadata (
                key TEXT PRIMARY KEY NOT NULL,
                value TEXT NOT NULL
            )
            """.trimIndent(),
        )
        createLearningTables(db)
        createPackTable(db)
        createExerciseTable(db)
        putValue(db, KEY_SCHEMA_VERSION, DATABASE_VERSION.toString())
        putValue(db, KEY_FIRST_RUN_COMPLETE, "false")
        putValue(db, KEY_THEME_MODE, ThemeMode.SYSTEM.name)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) createLearningTables(db)
        if (oldVersion < 3) createPackTable(db)
        if (oldVersion < 4) createExerciseTable(db)
        putValue(db, KEY_SCHEMA_VERSION, newVersion.toString())
    }

    fun isFirstRunComplete(): Boolean = getValue(KEY_FIRST_RUN_COMPLETE) == "true"

    fun markFirstRunComplete() = putValue(KEY_FIRST_RUN_COMPLETE, "true")

    fun getThemeMode(): ThemeMode = runCatching {
        ThemeMode.valueOf(getValue(KEY_THEME_MODE) ?: ThemeMode.SYSTEM.name)
    }.getOrDefault(ThemeMode.SYSTEM)

    fun setThemeMode(mode: ThemeMode) = putValue(KEY_THEME_MODE, mode.name)

    fun getProgress(moduleId: String): ModuleProgress = readableDatabase.query(
        TABLE_PROGRESS,
        PROGRESS_COLUMNS,
        "$COLUMN_MODULE_ID = ?",
        arrayOf(moduleId),
        null,
        null,
        null,
    ).use { cursor ->
        if (cursor.moveToFirst()) cursor.toProgress() else ModuleProgress(moduleId, LearningState.NOT_STARTED, 0L)
    }

    fun getAllProgress(moduleIds: List<String>): Map<String, ModuleProgress> = moduleIds.associateWith(::getProgress)

    fun markInProgress(moduleId: String) {
        val existing = getProgress(moduleId)
        upsertProgress(
            existing.copy(
                state = if (existing.state == LearningState.NOT_STARTED) LearningState.IN_PROGRESS else existing.state,
                updatedAt = now(),
            ),
        )
    }

    fun setCompleted(moduleId: String) {
        val existing = getProgress(moduleId)
        upsertProgress(existing.copy(state = LearningState.COMPLETED, updatedAt = now()))
    }

    fun saveQuizAttempt(moduleId: String, score: Int, total: Int) {
        val timestamp = now()
        writableDatabase.insert(
            TABLE_QUIZ_ATTEMPTS,
            null,
            ContentValues().apply {
                put(COLUMN_MODULE_ID, moduleId)
                put(COLUMN_SCORE, score)
                put(COLUMN_TOTAL, total)
                put(COLUMN_ATTEMPTED_AT, timestamp)
            },
        )
        val existing = getProgress(moduleId)
        upsertProgress(
            existing.copy(
                state = if (existing.state == LearningState.COMPLETED) existing.state else LearningState.IN_PROGRESS,
                updatedAt = timestamp,
                bestScore = maxOf(existing.bestScore ?: 0, score),
                attemptCount = existing.attemptCount + 1,
            ),
        )
    }

    fun getQuizAttempts(): List<QuizAttemptSummary> = readableDatabase.query(
        TABLE_QUIZ_ATTEMPTS,
        arrayOf(COLUMN_MODULE_ID, COLUMN_SCORE, COLUMN_TOTAL, COLUMN_ATTEMPTED_AT),
        null,
        null,
        null,
        null,
        "$COLUMN_ATTEMPTED_AT DESC",
    ).use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(
                    QuizAttemptSummary(
                        moduleId = cursor.getString(0),
                        score = cursor.getInt(1),
                        total = cursor.getInt(2),
                        attemptedAt = cursor.getLong(3),
                    ),
                )
            }
        }
    }

    fun saveNote(moduleId: String, body: String) {
        val trimmed = body.trim()
        if (trimmed.isEmpty()) {
            writableDatabase.delete(TABLE_NOTES, "$COLUMN_MODULE_ID = ?", arrayOf(moduleId))
            return
        }
        writableDatabase.insertWithOnConflict(
            TABLE_NOTES,
            null,
            ContentValues().apply {
                put(COLUMN_MODULE_ID, moduleId)
                put(COLUMN_BODY, trimmed)
                put(COLUMN_UPDATED_AT, now())
            },
            SQLiteDatabase.CONFLICT_REPLACE,
        )
    }

    fun getNote(moduleId: String): NoteSummary? = readableDatabase.query(
        TABLE_NOTES,
        arrayOf(COLUMN_MODULE_ID, COLUMN_BODY, COLUMN_UPDATED_AT),
        "$COLUMN_MODULE_ID = ?",
        arrayOf(moduleId),
        null,
        null,
        null,
    ).use { cursor -> if (cursor.moveToFirst()) cursor.toNote() else null }

    fun getNotes(): List<NoteSummary> = readableDatabase.query(
        TABLE_NOTES,
        arrayOf(COLUMN_MODULE_ID, COLUMN_BODY, COLUMN_UPDATED_AT),
        null,
        null,
        null,
        null,
        "$COLUMN_UPDATED_AT DESC",
    ).use { cursor -> buildList { while (cursor.moveToNext()) add(cursor.toNote()) } }

    fun setBookmarked(moduleId: String, bookmarked: Boolean) {
        if (bookmarked) {
            writableDatabase.insertWithOnConflict(
                TABLE_BOOKMARKS,
                null,
                ContentValues().apply {
                    put(COLUMN_MODULE_ID, moduleId)
                    put(COLUMN_CREATED_AT, now())
                },
                SQLiteDatabase.CONFLICT_IGNORE,
            )
        } else {
            writableDatabase.delete(TABLE_BOOKMARKS, "$COLUMN_MODULE_ID = ?", arrayOf(moduleId))
        }
    }

    fun isBookmarked(moduleId: String): Boolean = readableDatabase.query(
        TABLE_BOOKMARKS,
        arrayOf(COLUMN_MODULE_ID),
        "$COLUMN_MODULE_ID = ?",
        arrayOf(moduleId),
        null,
        null,
        null,
        "1",
    ).use { it.moveToFirst() }

    fun getBookmarkedIds(): Set<String> = readableDatabase.query(
        TABLE_BOOKMARKS,
        arrayOf(COLUMN_MODULE_ID),
        null,
        null,
        null,
        null,
        null,
    ).use { cursor -> buildSet { while (cursor.moveToNext()) add(cursor.getString(0)) } }

    fun setExerciseCompleted(wrapperId: String, contractVersion: Int) {
        writableDatabase.insertWithOnConflict(
            TABLE_EXERCISE_PROGRESS,
            null,
            ContentValues().apply {
                put(COLUMN_WRAPPER_ID, wrapperId)
                put(COLUMN_CONTRACT_VERSION, contractVersion)
                put(COLUMN_COMPLETED_AT, now())
            },
            SQLiteDatabase.CONFLICT_REPLACE,
        )
    }

    fun isExerciseCompleted(wrapperId: String): Boolean = readableDatabase.query(
        TABLE_EXERCISE_PROGRESS,
        arrayOf(COLUMN_WRAPPER_ID),
        "$COLUMN_WRAPPER_ID = ?",
        arrayOf(wrapperId),
        null,
        null,
        null,
        "1",
    ).use { it.moveToFirst() }

    fun getCompletedExerciseIds(): Set<String> = readableDatabase.query(
        TABLE_EXERCISE_PROGRESS,
        arrayOf(COLUMN_WRAPPER_ID),
        null,
        null,
        null,
        null,
        null,
    ).use { cursor -> buildSet { while (cursor.moveToNext()) add(cursor.getString(0)) } }

    fun getInstalledPacks(): List<InstalledPack> = readableDatabase.query(
        TABLE_PACKS,
        PACK_COLUMNS,
        null,
        null,
        null,
        null,
        "$COLUMN_PACK_ID ASC",
    ).use { cursor -> buildList { while (cursor.moveToNext()) add(cursor.toPack()) } }

    fun saveInstalledPack(pack: InstalledPack) {
        writableDatabase.insertWithOnConflict(
            TABLE_PACKS,
            null,
            ContentValues().apply {
                put(COLUMN_PACK_ID, pack.id)
                put(COLUMN_VERSION, pack.version)
                put(COLUMN_NAME, pack.name)
                put(COLUMN_DESCRIPTION, pack.description)
                put(COLUMN_CHECKSUM, pack.checksum)
                put(COLUMN_SIZE_BYTES, pack.sizeBytes)
                put(COLUMN_INSTALL_PATH, pack.installPath)
                put(COLUMN_INSTALLED_AT, pack.installedAt)
            },
            SQLiteDatabase.CONFLICT_REPLACE,
        )
    }

    fun removeInstalledPack(packId: String) {
        writableDatabase.delete(TABLE_PACKS, "$COLUMN_PACK_ID = ?", arrayOf(packId))
    }

    private fun upsertProgress(progress: ModuleProgress) {
        writableDatabase.insertWithOnConflict(
            TABLE_PROGRESS,
            null,
            ContentValues().apply {
                put(COLUMN_MODULE_ID, progress.moduleId)
                put(COLUMN_STATE, progress.state.name)
                put(COLUMN_UPDATED_AT, progress.updatedAt)
                progress.bestScore?.let { put(COLUMN_BEST_SCORE, it) } ?: putNull(COLUMN_BEST_SCORE)
                put(COLUMN_ATTEMPT_COUNT, progress.attemptCount)
            },
            SQLiteDatabase.CONFLICT_REPLACE,
        )
    }

    private fun getValue(key: String): String? = readableDatabase.query(
        TABLE_METADATA,
        arrayOf(COLUMN_VALUE),
        "$COLUMN_KEY = ?",
        arrayOf(key),
        null,
        null,
        null,
    ).use { cursor -> if (cursor.moveToFirst()) cursor.getString(0) else null }

    private fun putValue(key: String, value: String) = putValue(writableDatabase, key, value)

    private fun putValue(db: SQLiteDatabase, key: String, value: String) {
        db.execSQL(
            "INSERT OR REPLACE INTO $TABLE_METADATA ($COLUMN_KEY, $COLUMN_VALUE) VALUES (?, ?)",
            arrayOf(key, value),
        )
    }

    private fun createLearningTables(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS module_progress (
                module_id TEXT PRIMARY KEY NOT NULL,
                state TEXT NOT NULL,
                updated_at INTEGER NOT NULL,
                best_score INTEGER,
                attempt_count INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS quiz_attempts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                module_id TEXT NOT NULL,
                score INTEGER NOT NULL,
                total INTEGER NOT NULL,
                attempted_at INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_quiz_attempts_module ON quiz_attempts(module_id)")
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS notes (
                module_id TEXT PRIMARY KEY NOT NULL,
                body TEXT NOT NULL,
                updated_at INTEGER NOT NULL
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS bookmarks (
                module_id TEXT PRIMARY KEY NOT NULL,
                created_at INTEGER NOT NULL
            )
            """.trimIndent(),
        )
    }

    private fun createExerciseTable(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS exercise_progress (
                wrapper_id TEXT PRIMARY KEY NOT NULL,
                contract_version INTEGER NOT NULL,
                completed_at INTEGER NOT NULL
            )
            """.trimIndent(),
        )
    }

    private fun createPackTable(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS content_packs (
                pack_id TEXT PRIMARY KEY NOT NULL,
                version TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                checksum TEXT NOT NULL,
                size_bytes INTEGER NOT NULL,
                install_path TEXT NOT NULL,
                installed_at INTEGER NOT NULL
            )
            """.trimIndent(),
        )
    }

    private fun android.database.Cursor.toProgress(): ModuleProgress = ModuleProgress(
        moduleId = getString(getColumnIndexOrThrow(COLUMN_MODULE_ID)),
        state = runCatching { LearningState.valueOf(getString(getColumnIndexOrThrow(COLUMN_STATE))) }
            .getOrDefault(LearningState.NOT_STARTED),
        updatedAt = getLong(getColumnIndexOrThrow(COLUMN_UPDATED_AT)),
        bestScore = if (isNull(getColumnIndexOrThrow(COLUMN_BEST_SCORE))) null else getInt(getColumnIndexOrThrow(COLUMN_BEST_SCORE)),
        attemptCount = getInt(getColumnIndexOrThrow(COLUMN_ATTEMPT_COUNT)),
    )

    private fun android.database.Cursor.toNote(): NoteSummary = NoteSummary(
        moduleId = getString(getColumnIndexOrThrow(COLUMN_MODULE_ID)),
        body = getString(getColumnIndexOrThrow(COLUMN_BODY)),
        updatedAt = getLong(getColumnIndexOrThrow(COLUMN_UPDATED_AT)),
    )

    private fun android.database.Cursor.toPack(): InstalledPack = InstalledPack(
        id = getString(0),
        version = getString(1),
        name = getString(2),
        description = getString(3),
        checksum = getString(4),
        sizeBytes = getLong(5),
        installPath = getString(6),
        installedAt = getLong(7),
    )

    private fun now(): Long = System.currentTimeMillis()

    companion object {
        private const val DATABASE_NAME = "aetherlearn_local.db"
        private const val DATABASE_VERSION = 4
        private const val TABLE_METADATA = "app_metadata"
        private const val TABLE_PROGRESS = "module_progress"
        private const val TABLE_QUIZ_ATTEMPTS = "quiz_attempts"
        private const val TABLE_NOTES = "notes"
        private const val TABLE_BOOKMARKS = "bookmarks"
        private const val TABLE_PACKS = "content_packs"
        private const val TABLE_EXERCISE_PROGRESS = "exercise_progress"
        private const val COLUMN_KEY = "key"
        private const val COLUMN_VALUE = "value"
        private const val COLUMN_MODULE_ID = "module_id"
        private const val COLUMN_STATE = "state"
        private const val COLUMN_UPDATED_AT = "updated_at"
        private const val COLUMN_BEST_SCORE = "best_score"
        private const val COLUMN_ATTEMPT_COUNT = "attempt_count"
        private const val COLUMN_SCORE = "score"
        private const val COLUMN_TOTAL = "total"
        private const val COLUMN_ATTEMPTED_AT = "attempted_at"
        private const val COLUMN_BODY = "body"
        private const val COLUMN_CREATED_AT = "created_at"
        private const val COLUMN_PACK_ID = "pack_id"
        private const val COLUMN_VERSION = "version"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CHECKSUM = "checksum"
        private const val COLUMN_SIZE_BYTES = "size_bytes"
        private const val COLUMN_INSTALL_PATH = "install_path"
        private const val COLUMN_INSTALLED_AT = "installed_at"
        private const val COLUMN_WRAPPER_ID = "wrapper_id"
        private const val COLUMN_CONTRACT_VERSION = "contract_version"
        private const val COLUMN_COMPLETED_AT = "completed_at"
        private const val KEY_SCHEMA_VERSION = "schema_version"
        private const val KEY_FIRST_RUN_COMPLETE = "first_run_complete"
        private const val KEY_THEME_MODE = "theme_mode"
        private val PROGRESS_COLUMNS = arrayOf(COLUMN_MODULE_ID, COLUMN_STATE, COLUMN_UPDATED_AT, COLUMN_BEST_SCORE, COLUMN_ATTEMPT_COUNT)
        private val PACK_COLUMNS = arrayOf(COLUMN_PACK_ID, COLUMN_VERSION, COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_CHECKSUM, COLUMN_SIZE_BYTES, COLUMN_INSTALL_PATH, COLUMN_INSTALLED_AT)
    }
}

enum class ThemeMode { SYSTEM, LIGHT, DARK }

data class QuizAttemptSummary(
    val moduleId: String,
    val score: Int,
    val total: Int,
    val attemptedAt: Long,
)

data class InstalledPack(
    val id: String,
    val version: String,
    val name: String,
    val description: String,
    val checksum: String,
    val sizeBytes: Long,
    val installPath: String,
    val installedAt: Long,
)
