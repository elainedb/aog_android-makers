import util.DateUtils

fun main() {
    val targetTime = DateUtils.fakeNowString
    val customTime = ""
    val language = "fr"
    val level = ""

    Presenter.getSessionsByFilter(customTime, language, level, targetTime)

}