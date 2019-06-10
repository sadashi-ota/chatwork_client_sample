package com.sadashi.client.chatwork.infra.datasource.local.json

import com.squareup.moshi.Json

data class AuthorizedTokenJson(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "expired_time") val expiredTime: Long,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "scope") val scope: String
)