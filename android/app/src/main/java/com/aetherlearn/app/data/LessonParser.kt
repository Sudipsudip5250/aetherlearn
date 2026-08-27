package com.aetherlearn.app.data

import android.content.Context

class LessonParser(private val context: Context) {
    fun parseAsset(assetName: String): LessonDocument {
        val source = context.assets.open("content/core/$assetName").bufferedReader().use { it.readText() }
        return parse(source)
    }

    fun parse(source: String): LessonDocument {
        val normalized = source.replace("\r\n", "\n")
        require(normalized.trimStart().startsWith("---")) { "Lesson is missing YAML frontmatter" }
        val start = normalized.indexOf("---")
        val closing = normalized.indexOf("\n---", start + 3)
        require(closing >= 0) { "Lesson is missing YAML frontmatter closing delimiter" }

        val frontmatter = normalized.substring(start + 3, closing)
        val body = normalized.substring(closing + 4).trim()
        val fields = parseFrontmatter(frontmatter)
        val sections = parseSections(body)
        val objectives = fields["objectives"].orEmpty()
        val prerequisites = fields["prerequisites"].orEmpty()
        val questions = parseQuestions(sections["Knowledge check"].orEmpty())

        return LessonDocument(
            id = scalar(fields["id"].orEmpty().firstOrNull().orEmpty()),
            title = scalar(fields["title"].orEmpty().firstOrNull().orEmpty()),
            strand = scalar(fields["strand"].orEmpty().firstOrNull().orEmpty()),
            level = scalar(fields["level"].orEmpty().firstOrNull().orEmpty()),
            estimatedMinutes = scalar(fields["estimated_minutes"].orEmpty().firstOrNull().orEmpty()).toIntOrNull() ?: 0,
            availability = scalar(fields["availability"].orEmpty().firstOrNull().orEmpty()),
            riskTier = scalar(fields["risk_tier"].orEmpty().firstOrNull().orEmpty()),
            prerequisites = prerequisites.map(::scalar),
            objectives = objectives.map(::scalar),
            sections = sections,
            quizQuestions = questions,
        )
    }

    private fun parseFrontmatter(frontmatter: String): Map<String, List<String>> {
        val result = linkedMapOf<String, MutableList<String>>()
        var activeKey: String? = null
        frontmatter.lines().forEach { rawLine ->
            val line = rawLine.trimEnd()
            val field = FIELD_PATTERN.matchEntire(line)
            if (field != null) {
                val key = field.groupValues[1]
                val value = field.groupValues[2].trim()
                activeKey = key
                result[key] = mutableListOf()
                if (value.isNotEmpty() && value != "[]") {
                    result.getValue(key).add(value)
                }
            } else if (line.trimStart().startsWith("-") && activeKey != null) {
                result.getValue(activeKey.orEmpty()).add(line.trimStart().removePrefix("-").trim())
            }
        }
        return result
    }

    private fun parseSections(body: String): LinkedHashMap<String, String> {
        val sections = linkedMapOf<String, String>()
        var current: String? = null
        val buffer = StringBuilder()
        fun flush() {
            val name = current ?: return
            sections[name] = buffer.toString().trim()
            buffer.clear()
        }

        body.lines().forEach { line ->
            val heading = SECTION_PATTERN.matchEntire(line.trim())
            if (heading != null) {
                flush()
                current = heading.groupValues[1].trim()
            } else if (current != null && !line.trimStart().startsWith("# ")) {
                buffer.append(line).append('\n')
            }
        }
        flush()
        return sections
    }

    private fun parseQuestions(section: String): List<QuizQuestion> = section.lines()
        .mapNotNull { line ->
            val numbered = QUESTION_PATTERN.matchEntire(line.trim()) ?: return@mapNotNull null
            val number = numbered.groupValues[1].toIntOrNull() ?: return@mapNotNull null
            val content = numbered.groupValues[2].trim()
            val answer = ANSWER_PATTERN.matchEntire(content)
            if (answer == null) {
                QuizQuestion(number, content, "", "", acceptedAnswers = emptyList())
            } else {
                val expected = answer.groupValues[2].trim()
                QuizQuestion(
                    number = number,
                    prompt = answer.groupValues[1].trim(),
                    expectedAnswer = expected,
                    explanation = answer.groupValues[3].trim(),
                    acceptedAnswers = acceptedAnswerVariants(expected),
                )
            }
        }

    private fun acceptedAnswerVariants(expected: String): List<String> = buildList {
        add(expected)
        expected.split(Regex(",\\s*|\\s+or\\s+"))
            .map(String::trim)
            .filter { it.length >= 4 }
            .forEach(::add)
    }.distinct()

    private fun scalar(value: String): String = value.trim().removeSurrounding("\"", "'")

    companion object {
        private val FIELD_PATTERN = Regex("^([A-Za-z_][A-Za-z0-9_]*):\\s*(.*)$")
        private val SECTION_PATTERN = Regex("^##\\s+(.+)$")
        private val QUESTION_PATTERN = Regex("^(\\d+)\\.\\s+(.+)$")
        private val ANSWER_PATTERN = Regex("^(.*?)\\*\\*(.*?)\\*\\*\\.?\\s*(.*)$")
    }
}
