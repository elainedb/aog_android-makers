package extensions

import model.Language
import model.Level

enum class QueryType { CURRENT_SESSIONS, NEXT_SESSIONS }

fun String.toLanguage() = when (this) {
    "French", "Français", "french", "français", "fr" -> Language.FR
    "English", "Anglais", "english", "anglais", "en" -> Language.EN
    else -> Language.EN
}

fun String.toLevel() = when (this) {
    "Beginner", "Beginner and novice" -> Level.BEGINNER
    "Intermediate" -> Level.INTERMEDIATE
    "Expert", "Expert and senior" -> Level.EXPERT
    else -> Level.BEGINNER
}

fun String.toQueryType() = when (this) {
    "current" -> QueryType.CURRENT_SESSIONS
    "next" -> QueryType.NEXT_SESSIONS
    else -> QueryType.CURRENT_SESSIONS
}