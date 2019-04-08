package util

import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val DF_SIMPLE_STRING = "yyyy/MM/dd HH:mm:ss"
    private const val HOUR_PATTERN = "HH:mm:ss"
    private const val WS_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'" //       2019-04-23T11:30:00Z
    private const val FIREBASE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX" // 2019-04-23T09:00:00+02:00

    @JvmField
    val FIREBASE_PATTERN_FORMAT_FR = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            val formatter = SimpleDateFormat(FIREBASE_PATTERN, Locale.FRANCE)
            formatter.timeZone = TimeZone.getTimeZone("Europe/Paris")
            return formatter
        }
    }

    fun nowWithLocale() =
        formatWithParisTimeZone(Date(), FIREBASE_PATTERN, Locale.FRANCE)

    fun formatWithParisTimeZone(date: Date, format: String, locale: Locale) : String {
        val formatter = SimpleDateFormat(format, locale)
        formatter.timeZone = TimeZone.getTimeZone("Europe/Paris")
        return formatter.format(date)
    }

    fun dateParseFR(s: String): Date = FIREBASE_PATTERN_FORMAT_FR.get().parse(s, ParsePosition(0))

    fun now() = FIREBASE_PATTERN_FORMAT_FR.get().parse(nowWithLocale(), ParsePosition(0))

    fun fakeNow() = dateParseFR(fakeNowString)

    const val fakeNowString = "2019-04-23T11:30:00+02:00"

// TODO https://stackoverflow.com/questions/2891361/how-to-set-time-zone-of-a-java-util-date
    //https://stackoverflow.com/questions/4542679/java-time-zone-when-parsing-dateformat
}

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

fun Date.isOKForCurrentSessions(start: Date, end: Date) = start.before(this) && end.after(this)

fun Date.isOKForNextSessions(start: Date, end: Date) = start.after(this) && end.before(this.addTwoHours())