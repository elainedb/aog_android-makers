import Interactor.getSessions
import Interactor.getSlots
import com.google.actions.api.response.ResponseBuilder
import extensions.*
import model.Language
import model.Level
import model.Session
import org.slf4j.LoggerFactory
import util.DateUtils
import util.adjust
import java.util.*

object Presenter {

    fun getSessionsByFilter(customTime: String, language: String, level: String, time: String): List<Session> {
        var queryType = QueryType.CURRENT_SESSIONS // custom time: "next" "current"
        var targetTime = DateUtils.fakeNow() // time: sys.time
        var sessionLanguage: Language? = null // language: sys.language
        var sessionLevel: Level? = null // level: "Beginner" "Intermediate" "Expert"

        LOG.info("getSessionsByFilter 1 $customTime, $language, $level, $time")

        // comment this line if developing in an "exotic" timezone (BRT for instance :D)
        if (time.isNotBlank()) targetTime = DateUtils.dateParseFR(time).adjust()
        if (customTime.isNotBlank()) queryType = customTime.toQueryType()
        if (language.isNotBlank()) sessionLanguage = language.toLanguage()
        if (level.isNotBlank()) sessionLevel = level.toLevel()

        LOG.info("getSessionsByFilter 2 $targetTime, $queryType, $sessionLanguage, $sessionLevel")

        val okSlots = getSlots(queryType, targetTime)
        return getSessions(okSlots, sessionLanguage, sessionLevel)
    }

    fun getSessionsByFilterResponse(
        responseBuilder: ResponseBuilder,
        customTime: String, language: String, level: String, time: String
    ): ResponseBuilder {

        val sessions = getSessionsByFilter(customTime, language, level, time)

        when (sessions.size) {
            0 -> responseBuilder
                .add(getStringResource("no_sessions_found"))
                .addSuggestions(
                    arrayOf(
                        getStringResource("suggestion_1"),
                        getStringResource("suggestion_2"),
                        getStringResource("suggestion_3"),
                        getStringResource("suggestion_4")
                    )
                )
            1 -> responseBuilder
                .add(getStringResource("session_found"))
                .add(sessions[0].title)
            else -> responseBuilder
                .add(getStringResource("sessions_found"))
                .add(sessions.joinToString { it.title })
        }
//        responseBuilder.add("LAla")

        return responseBuilder
    }

    private fun getStringResource(key: String): String {
        return ResourceBundle.getBundle("resources").getFormattedString(key)
    }

    private val LOG = LoggerFactory.getLogger(Presenter::class.java)

}