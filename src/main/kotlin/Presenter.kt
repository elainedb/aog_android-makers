import Interactor.getSessions
import Interactor.getSlots
import com.google.actions.api.response.ResponseBuilder
import com.google.actions.api.response.helperintent.SelectionList
import com.google.api.services.actions_fulfillment.v2.model.LinkOutSuggestion
import com.google.api.services.actions_fulfillment.v2.model.ListSelectListItem
import com.google.api.services.actions_fulfillment.v2.model.OpenUrlAction
import com.google.api.services.actions_fulfillment.v2.model.OptionInfo
import extensions.*
import model.Language
import model.Level
import model.Session
import org.slf4j.LoggerFactory
import util.DateUtils
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

    fun getSessionsByFilterSimpleResponse(
        responseBuilder: ResponseBuilder,
        customTime: String, language: String, level: String, time: String, locale: Locale
    ): ResponseBuilder {

        val sessions = getSessionsByFilter(customTime, language, level, time)

        when (sessions.size) {
            0 -> responseBuilder
                .add(getStringResource("no_sessions_found", locale))
                .addSuggestions(suggestions(locale))
            1 -> responseBuilder
                .add(getStringResource("session_found", locale))
                .add(sessions[0].title)
            else -> responseBuilder
                .add(getStringResource("sessions_found", locale))
                .add(sessions.joinToString { it.title })
        }

        return responseBuilder
    }

    fun getSessionsByFilterListResponse(
        responseBuilder: ResponseBuilder,
        customTime: String, language: String, level: String, time: String, locale: Locale
    ): ResponseBuilder {

        val sessions = getSessionsByFilter(customTime, language, level, time)

        when (sessions.size) {
            0 -> responseBuilder
                .add(getStringResource("no_sessions_found", locale))
                .addSuggestions(suggestions(locale))
            1 -> {
                responseBuilder
                    .add("${getStringResource("session_found", locale)} ${sessions[0].title} (${sessions[0].getTimeSlot()}) ${sessions[0].getShortDescription()}")
                    .addSuggestions(suggestions(locale))
            }
            else -> {
                responseBuilder
                    .add(getStringResource("sessions_found", locale))
                    .add(SelectionList().setItems(sessions.map {
                        ListSelectListItem()
                            .setTitle("${it.title} (${it.getTimeSlot()})")
                            .setDescription(it.description)
                            .setOptionInfo(OptionInfo().setKey(it.id + "___" + it.getShortDescription()))
                    }))
                    .addSuggestions(suggestions(locale))
            }
        }

        return responseBuilder
    }

    fun getSessionsByFilterOptionResponse(
        responseBuilder: ResponseBuilder,
        locale: Locale, option: String
    ): ResponseBuilder {

        val sessionId = option.split("___")[0]
        val url = "https://androidmakers.fr/schedule/?sessionId=$sessionId"

        responseBuilder
            .add("${getStringResource("session_found", locale)} ${option.split("___")[1]}}")
            .add(LinkOutSuggestion().setDestinationName("App").setOpenUrlAction(OpenUrlAction().setUrl(url)))
            .addSuggestions(arrayOf(getStringResource("suggestion_1", locale)))

        return responseBuilder
    }

    private fun suggestions(locale: Locale) = arrayOf(
        getStringResource("suggestion_1", locale),
        getStringResource("suggestion_2", locale),
        getStringResource("suggestion_3", locale),
        getStringResource("suggestion_4", locale)
    )

    private fun getStringResource(key: String, locale: Locale): String {
        return ResourceBundle.getBundle("resources", locale).getFormattedString(key)
    }

    private val LOG = LoggerFactory.getLogger(Presenter::class.java)

}