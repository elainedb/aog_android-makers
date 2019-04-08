import Interactor.getSessions
import Interactor.getSlots
import extensions.QueryType
import extensions.toLanguage
import extensions.toLevel
import extensions.toQueryType
import model.Language
import model.Level
import util.DateUtils
import util.adjust

object Presenter {

    fun getSessionsByFilter(customTime: String?, language: String?, level: String?, time: String?) {
        var queryType = QueryType.CURRENT_SESSIONS // custom time: "next" "current"
        var targetTime = DateUtils.fakeNow() // time: sys.time
        var sessionLanguage: Language? = null // language: sys.language
        var sessionLevel: Level? = null // level: "Beginner" "Intermediate" "Expert"

        time?.let { targetTime = DateUtils.dateParseFR(time).adjust() } // comment this line if developing in an "exotic" timezone (BRT for instance :D)
        customTime?.let { queryType = customTime.toQueryType() }
        language?.let { sessionLanguage = language.toLanguage() }
        level?.let { sessionLevel = level.toLevel() }

        val okSlots = getSlots(queryType, targetTime)
        val okSessions = getSessions(okSlots, sessionLanguage, sessionLevel)
        // TODO build response

    }

}

fun main() {
    val targetTime = DateUtils.fakeNowString
    val customTime = null
    val language: String? = "fr"
    val level: String? = null

    Presenter.getSessionsByFilter(customTime, language, level, targetTime)

}