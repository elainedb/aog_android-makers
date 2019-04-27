package extensions

import util.DateUtils
import util.DateUtils.PRINT_PATTERN_HOUR
import util.DateUtils.PRINT_PATTERN_WITH_DAY
import java.util.*

fun Date.adjust(): Date {
    val cal = Calendar.getInstance()
    cal.time = this

    if (cal.get(Calendar.HOUR_OF_DAY) < 7) {
        cal.add(Calendar.HOUR_OF_DAY, 12)
    }

    return cal.time
}

fun Date.addTwoHours(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.HOUR_OF_DAY, 2)

    return cal.time
}

fun Date.deleteOneMinute(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.add(Calendar.MINUTE, -1)

    return cal.time
}

fun Date.isOKForCurrentSessions(start: Date, end: Date) = start.deleteOneMinute().before(this) && end.after(this)

fun Date.isOKForNextSessions(start: Date, end: Date) = start.after(this.deleteOneMinute()) && end.before(this.addTwoHours())

fun Date.printDayHour() = DateUtils.formatWithParisTimeZone(this, PRINT_PATTERN_WITH_DAY, Locale.FRANCE)

fun Date.printHour() = DateUtils.formatWithParisTimeZone(this, PRINT_PATTERN_HOUR, Locale.FRANCE)