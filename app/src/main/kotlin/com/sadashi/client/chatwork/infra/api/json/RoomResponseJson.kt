package com.sadashi.client.chatwork.infra.api.json

import com.squareup.moshi.Json

data class RoomResponseJson(
    @Json(name = "file_num") val fileNum: Int,
    @Json(name = "icon_path") val iconPath: String,
    @Json(name = "last_update_time") val lastUpdateTime: Int,
    @Json(name = "mention_num") val mentionNum: Int,
    @Json(name = "message_num") val messageNum: Int,
    @Json(name = "mytask_num") val mytaskNum: Int,
    @Json(name = "name") val name: String,
    @Json(name = "role") val role: String,
    @Json(name = "room_id") val roomId: Int,
    @Json(name = "sticky") val sticky: Boolean,
    @Json(name = "task_num") val taskNum: Int,
    @Json(name = "type") val type: String,
    @Json(name = "unread_num") val unreadNum: Int
)