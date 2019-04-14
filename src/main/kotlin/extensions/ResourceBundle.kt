package extensions

import java.nio.charset.Charset
import java.text.MessageFormat
import java.util.*

fun ResourceBundle.getFormattedString(key: String, vararg args: Any): String {
    val decodedString = getString(key)
    val encodedString = String(decodedString.toByteArray(Charset.forName("ISO-8859-1")), Charset.forName("UTF-8"))
    return MessageFormat.format(encodedString, *args)
}