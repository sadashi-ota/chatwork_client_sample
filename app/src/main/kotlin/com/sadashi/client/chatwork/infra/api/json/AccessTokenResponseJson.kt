package com.sadashi.client.chatwork.infra.api.json

import com.squareup.moshi.Json

data class AccessTokenResponseJson(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "expires_in") val expiresInSec: Int,
    @Json(name = "scope") val scope: String
)