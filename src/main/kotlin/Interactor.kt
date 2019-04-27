import extensions.QueryType
import extensions.deleteOneMinute
import extensions.isOKForCurrentSessions
import extensions.isOKForNextSessions
import model.*
import util.DateUtils
import java.util.*

object Interactor {

    fun getSlots(queryType: QueryType, targetTimeArg: Date): List<ScheduleSlot> {
        val slots = API.getSlotsPOJO()

        var okSlots = listOf<ScheduleSlot>()
        val targetTime = targetTimeArg.deleteOneMinute()

        slots?.let {
            okSlots = slots.filter {
                val startDate = DateUtils.dateParseFR(it.startDate)
                val endDate = DateUtils.dateParseFR(it.endDate)
                val okForCurrentSessions =
                    queryType == QueryType.CURRENT_SESSIONS && targetTime.isOKForCurrentSessions(startDate, endDate)
                val okForNextSessions =
                    queryType == QueryType.NEXT_SESSIONS && targetTime.isOKForNextSessions(startDate, endDate)
                okForCurrentSessions || okForNextSessions
            }

            println("OK SLOTS")

            okSlots.forEach { println("${it.startDate} => ${it.sessionId}") }
        }

        return okSlots
    }

    fun getSessions(okSlots: List<ScheduleSlot>, sessionLanguage: Language?, sessionLevel: Level?): List<Session> {
        val sessions = API.getSessionsPOJO() ?: emptyMap()

        var okSessions = sessions
            .filterKeys { okSlots.map { slot -> slot.sessionId }.contains(it) }
            .map {
                val id = it.component1()
                val slot = okSlots.first { slot -> slot.sessionId == id }
                val startDate = slot.startDate
                val endDate = slot.endDate
                it.component2().toSession(id, startDate, endDate)
            }

        okSessions.forEach { println("${it.id} -> ${it.title} ${it.language.code} ${it.level.label} ${it.start} ${it.end}") }

        sessionLanguage?.let { lang -> okSessions = okSessions.filter { it.language == lang } }
        sessionLevel?.let { lev -> okSessions = okSessions.filter { it.level == lev } }

        println("FILTER")

        okSessions.forEach { println("${it.id} -> ${it.title} ${it.language.code} ${it.level.label} ${it.start} ${it.end}") }

        return okSessions
    }

}