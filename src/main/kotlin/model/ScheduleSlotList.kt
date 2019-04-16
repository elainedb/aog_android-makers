package model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleSlotList(
    var all: List<ScheduleSlot> = arrayListOf()
)