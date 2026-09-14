package com.aetherlearn.app.data

/**
 * A deterministic, local-only recommendation for the Learn surface.
 * It never changes ordering or blocks a learner from browsing any lesson.
 */
internal fun recommendedLesson(
    lessons: List<ModuleSummary>,
    progress: Map<String, ModuleProgress>,
    startingLevel: StartingLevel? = null,
): ModuleSummary? {
    val inProgress = lessons.firstOrNull { progress[it.id]?.state == LearningState.IN_PROGRESS }
    if (inProgress != null) return inProgress

    val preferred = when (startingLevel) {
        StartingLevel.TRIED_PROGRAMMING -> lessons.filter { it.strand.equals("python-fundamentals", ignoreCase = true) }
        StartingLevel.DIGITAL_BASICS, StartingLevel.NEW_TO_COMPUTING -> lessons.filter { it.strand.equals("digital-literacy", ignoreCase = true) }
        null -> lessons
    }.ifEmpty { lessons }
    val completed = progress.filterValues { it.state == LearningState.COMPLETED }.keys
    val ready = preferred.firstOrNull { lesson ->
        progress[lesson.id]?.state != LearningState.COMPLETED &&
            lesson.prerequisites.all { prerequisite -> prerequisite in completed }
    }
    return ready ?: lessons.firstOrNull { progress[it.id]?.state != LearningState.COMPLETED }
}

internal fun recommendationReason(
    lesson: ModuleSummary,
    progress: Map<String, ModuleProgress>,
): String {
    return if (progress[lesson.id]?.state == LearningState.IN_PROGRESS) {
        "You started this lesson already, so it is the easiest place to continue."
    } else if (lesson.prerequisites.isEmpty()) {
        "It has no prerequisites, so it is a calm place to start."
    } else {
        "Its prerequisites are complete, so it is a sensible next step."
    }
}

enum class StartingLevel(val label: String, val description: String) {
    NEW_TO_COMPUTING("New to computing", "Start with everyday digital concepts and build confidence."),
    DIGITAL_BASICS("Familiar with basic digital concepts", "Begin with the digital foundations, then move into programming."),
    TRIED_PROGRAMMING("I have tried programming before", "Start with the Python path while keeping every lesson available."),
}
