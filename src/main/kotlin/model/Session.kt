package model

import com.squareup.moshi.JsonClass
import util.DateUtils
import java.util.*

@JsonClass(generateAdapter = true)
data class SessionJson(
    val complexity: String = "",
    val speakers: List<String> = listOf(),
    val description: String = "",
    val language: String = "",
    val title: String = "",
    val image: String? = null,
    val icon: String? = null,
    val tags: List<String> = listOf()
)

data class Session(
    val id: String,
    val level: Level,
    val description: String,
    val title: String,
    val language: Language,
    val start: Date,
    val end: Date
)

enum class Level(val label: String) { BEGINNER("Beginner"), INTERMEDIATE("Intermediate"), EXPERT("Expert") }
enum class Language(val code: String) { EN("en"), FR("fr") }

fun SessionJson.toSession(id: String, start: String, end: String): Session {
    return Session(
        id,
        getSessionLevel(complexity),
        description,
        title,
        getSessionLanguage(language),
        DateUtils.dateParseFR(start),
        DateUtils.dateParseFR(end)
    )
}

fun getSessionLevel(label: String) = when (label) {
    "Beginner" -> Level.BEGINNER
    "Intermediate" -> Level.INTERMEDIATE
    "Expert" -> Level.EXPERT
    else -> Level.BEGINNER
}

fun getSessionLanguage(label: String) = when (label) {
    "English" -> Language.EN
    "French" -> Language.FR
    else -> Language.EN
}