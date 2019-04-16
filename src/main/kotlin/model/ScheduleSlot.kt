package model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleSlot(
    val endDate: String = "",
    val sessionId: String = "",
    val roomId: String = "",
    val startDate: String = ""
)