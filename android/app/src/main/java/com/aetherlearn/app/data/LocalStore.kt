package com.aetherlearn.app.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * App-private storage boundary for settings and future learning state.
 * Content packs are kept separately in assets/files and are never mixed with user state.
 */
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
        putValue(db, KEY_SCHEMA_VERSION, DATABASE_VERSION.toString())
        putValue(db, KEY_FIRST_RUN_COMPLETE, "false")
        putValue(db, KEY_THEME_MODE, ThemeMode.SYSTEM.name)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Future migrations for progress, notes, bookmarks, and scores belong here.
        putValue(db, KEY_SCHEMA_VERSION, newVersion.toString())
    }

    fun isFirstRunComplete(): Boolean = getValue(KEY_FIRST_RUN_COMPLETE) == "true"

    fun markFirstRunComplete() {
        putValue(KEY_FIRST_RUN_COMPLETE, "true")
    }

    fun getThemeMode(): ThemeMode = runCatching {
        ThemeMode.valueOf(getValue(KEY_THEME_MODE) ?: ThemeMode.SYSTEM.name)
    }.getOrDefault(ThemeMode.SYSTEM)

    fun setThemeMode(mode: ThemeMode) {
        putValue(KEY_THEME_MODE, mode.name)
    }

    private fun getValue(key: String): String? {
        readableDatabase.query(
            TABLE_METADATA,
            arrayOf(COLUMN_VALUE),
            "$COLUMN_KEY = ?",
            arrayOf(key),
            null,
            null,
            null,
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.getString(0) else null
        }
    }

    private fun putValue(key: String, value: String) {
        putValue(writableDatabase, key, value)
    }

    private fun putValue(db: SQLiteDatabase, key: String, value: String) {
        db.execSQL(
            "INSERT OR REPLACE INTO $TABLE_METADATA ($COLUMN_KEY, $COLUMN_VALUE) VALUES (?, ?)",
            arrayOf(key, value),
        )
    }

    companion object {
        private const val DATABASE_NAME = "aetherlearn_local.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_METADATA = "app_metadata"
        private const val COLUMN_KEY = "key"
        private const val COLUMN_VALUE = "value"
        private const val KEY_SCHEMA_VERSION = "schema_version"
        private const val KEY_FIRST_RUN_COMPLETE = "first_run_complete"
        private const val KEY_THEME_MODE = "theme_mode"
    }
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
}
