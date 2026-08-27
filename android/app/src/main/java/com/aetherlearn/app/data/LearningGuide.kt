package com.aetherlearn.app.data

/**
 * A deterministic, local-only recommendation for the Learn surface.
 * It never changes ordering or blocks a learner from browsing any lesson.
 */
internal fun recommendedLesson(
    lessons: List<ModuleSummary>,
    progress: Map<String, ModuleProgress>,
): ModuleSummary? {
    val inProgress = lessons.firstOrNull { progress[it.id]?.state == LearningState.IN_PROGRESS }
    if (inProgress != null) return inProgress

    val completed = progress.filterValues { it.state == LearningState.COMPLETED }.keys
    return lessons.firstOrNull { lesson ->
        progress[lesson.id]?.state != LearningState.COMPLETED &&
            lesson.prerequisites.all { prerequisite -> prerequisite in completed }
    } ?: lessons.firstOrNull { progress[it.id]?.state != LearningState.COMPLETED }
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
