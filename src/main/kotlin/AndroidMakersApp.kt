import com.google.actions.api.*
import extensions.getFormattedString
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Implements all intent handlers for this Action. Note that your App must extend from DialogflowApp
 * if using Dialogflow or ActionsSdkApp for ActionsSDK based Actions.
 */
class AndroidMakersApp : DialogflowApp() {

    private val LOGGER = LoggerFactory.getLogger(AndroidMakersApp::class.java)

    @ForIntent("Default Welcome Intent") // not yet called
    fun welcome(request: ActionRequest): ActionResponse {
        LOGGER.info("Welcome intent start.")
        val responseBuilder = getResponseBuilder(request)
        if (request.user?.lastSeen != null) {
            responseBuilder.add(getStringResource("welcome_back"))
        } else {
            responseBuilder.add(getStringResource("welcome"))
        }

        LOGGER.info("Welcome intent end.")
        return responseBuilder.build()
    }

    @ForIntent("sessions.byfilter")
    fun sessionsByFilter(request: ActionRequest): ActionResponse {
        LOGGER.info("sessionsByFilter intent start.")

        val customTime = request.getParameter("custom-time") as String
        val language = request.getParameter("language") as String
        val level = request.getParameter("level") as String
        val time = request.getParameter("time") as String

        val responseBuilder = when {
            request.hasCapability(Capability.WEB_BROWSER.value) -> {
                // Phone
                Presenter.getSessionsByFilterResponse(getResponseBuilder(request), customTime, language, level, time)
            }
            request.hasCapability(Capability.SCREEN_OUTPUT.value) -> {
                // Home Hub
                Presenter.getSessionsByFilterResponse(getResponseBuilder(request), customTime, language, level, time)
            }
            else -> {
                // Home
                Presenter.getSessionsByFilterResponse(getResponseBuilder(request), customTime, language, level, time)
            }
        }

        LOGGER.info("sessionsByFilter intent end.")
        return responseBuilder.build()
    }

    private fun getStringResource(key: String): String {
        return ResourceBundle.getBundle("resources").getFormattedString(key)
    }
}