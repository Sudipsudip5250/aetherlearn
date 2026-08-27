package com.aetherlearn.app.data

import org.junit.Test
import kotlin.test.assertEquals

class LearningGuideTest {
    private val lessons = listOf(
        ModuleSummary("intro", "Intro", "offline", 20, "digital-literacy"),
        ModuleSummary("python", "Python", "offline", 30, "python-fundamentals", listOf("intro")),
        ModuleSummary("algorithms", "Algorithms", "offline", 40, "algorithms", listOf("python")),
    )

    @Test
    fun prefersExistingInProgressLesson() {
        val result = recommendedLesson(
            lessons,
            mapOf(
                "intro" to ModuleProgress("intro", LearningState.COMPLETED, 1L),
                "python" to ModuleProgress("python", LearningState.IN_PROGRESS, 2L),
            ),
        )

        assertEquals("python", result?.id)
    }

    @Test
    fun choosesFirstLessonWhosePrerequisitesAreComplete() {
        val result = recommendedLesson(
            lessons,
            mapOf("intro" to ModuleProgress("intro", LearningState.COMPLETED, 1L)),
        )

        assertEquals("python", result?.id)
    }

    @Test
    fun startingLevelPrefersDigitalFoundationsWithoutBlockingOtherLessons() {
        val result = recommendedLesson(
            lessons,
            emptyMap(),
            StartingLevel.NEW_TO_COMPUTING,
        )

        assertEquals("intro", result?.id)
    }

    @Test
    fun startingLevelCanPreferPythonWhenThatPathIsReady() {
        val result = recommendedLesson(
            lessons,
            mapOf("intro" to ModuleProgress("intro", LearningState.COMPLETED, 1L)),
            StartingLevel.TRIED_PROGRAMMING,
        )

        assertEquals("python", result?.id)
    }

    @Test
    fun fallsBackToFirstUnfinishedLessonWhenReadinessIsUnknown() {
        val blockedLessons = listOf(
            ModuleSummary("blocked", "Blocked", "offline", 20, "digital-literacy", listOf("missing")),
            ModuleSummary("later", "Later", "offline", 20, "python-fundamentals", listOf("blocked")),
        )
        val result = recommendedLesson(blockedLessons, emptyMap())

        assertEquals("blocked", result?.id)
    }
}
