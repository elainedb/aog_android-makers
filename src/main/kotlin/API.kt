import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import model.ScheduleSlot
import model.SessionJson
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

object API {
    private val moshi = Moshi.Builder().build()

    private fun getContents(url: String):String? {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.doInput = true

        connection.connect()

        val status = connection.responseCode
        val responseBody = try {
            connection.inputStream.bufferedReader().readText()
        } catch(e: Exception) {
            System.err.println("$url: cannot read body")
            return null
        }

        if (status != HttpURLConnection.HTTP_OK) {
            System.err.println("$url: cannot get contents: $responseBody")
            return null
        }

        return responseBody
    }

    fun getSlotsPOJO(): List<ScheduleSlot>? {
        val adapter = moshi.adapter<List<ScheduleSlot>>(Types.newParameterizedType(List::class.java, ScheduleSlot::class.java))

        return getContents("https://android-makers-2019.appspot.com/")?.let {
            adapter.fromJson(it)
        }
    }

    fun getSessionsPOJO(): Map<String, SessionJson>? {
        val adapter = moshi.adapter<Map<String, SessionJson>>(Types.newParameterizedType(Map::class.java, String::class.java, SessionJson::class.java))

        return getContents("https://raw.githubusercontent.com/paug/android-makers-2019/master/data/database/sessions.json")?.let {
            adapter.fromJson(it)
        }
    }
}