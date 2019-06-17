package com.sadashi.client.chatwork.infra.api.json

import com.squareup.moshi.Json

data class MessageJson(
    @Json(name = "account") val account: AccountJson,
    @Json(name = "body") val body: String,
    @Json(name = "message_id") val messageId: String,
    @Json(name = "send_time") val sendTime: Int,
    @Json(name = "update_time") val updateTime: Int
)