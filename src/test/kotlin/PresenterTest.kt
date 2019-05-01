import org.junit.Test
import util.DateUtils

class PresenterTest {
    @Test
    fun test() {
        val targetTime = DateUtils.fakeNowString
        val customTime = ""
        val language = "fr"
        val level = ""

        Presenter.getSessionsByFilter(customTime, language, level, targetTime)
    }
}