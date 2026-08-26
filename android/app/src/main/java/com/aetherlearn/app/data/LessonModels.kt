package com.aetherlearn.app.data

data class QuizQuestion(
    val number: Int,
    val prompt: String,
    val expectedAnswer: String,
    val explanation: String,
)

data class LessonDocument(
    val id: String,
    val title: String,
    val strand: String,
    val level: String,
    val estimatedMinutes: Int,
    val availability: String,
    val riskTier: String,
    val prerequisites: List<String>,
    val objectives: List<String>,
    val sections: LinkedHashMap<String, String>,
    val quizQuestions: List<QuizQuestion>,
) {
    val offlinePractice: String
        get() = sections["Offline practice"].orEmpty()

    val searchText: String
        get() = buildString {
            append(title)
            append('\n')
            sections.values.forEach {
                append(it)
                append('\n')
            }
        }
}

enum class LearningState {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
}

data class ModuleProgress(
    val moduleId: String,
    val state: LearningState,
    val updatedAt: Long,
    val bestScore: Int? = null,
    val attemptCount: Int = 0,
)

data class NoteSummary(
    val moduleId: String,
    val body: String,
    val updatedAt: Long,
)
