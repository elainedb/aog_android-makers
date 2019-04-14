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
    const val PRINT_PATTERN_WITH_DAY = "dd MMM HH:mm"
    const val PRINT_PATTERN_HOUR = "HH:mm"

    @JvmField
    val FIREBASE_PATTERN_FORMAT_FR = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            val formatter = SimpleDateFormat(FIREBASE_PATTERN, Locale.FRANCE)
            formatter.timeZone = TimeZone.getTimeZone("Europe/Paris")
            return formatter
        }
    }

    private fun nowWithLocale() = formatWithParisTimeZone(Date(), FIREBASE_PATTERN, Locale.FRANCE)

    fun formatWithParisTimeZone(date: Date, format: String, locale: Locale): String {
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