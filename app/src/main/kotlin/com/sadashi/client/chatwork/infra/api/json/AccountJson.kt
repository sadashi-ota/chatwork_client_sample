package com.sadashi.client.chatwork.infra.api.json

import com.squareup.moshi.Json

data class AccountJson(
    @Json(name = "account_id") val accountId: Int,
    @Json(name = "avatar_image_url") val avatarImageUrl: String,
    @Json(name = "name") val name: String
)