package extensions

fun String.getShortString(): String {
    var len = length
    if (len > 500) len = 500
    return substring(0 until len)
}