package com.sadashi.client.chatwork.infra.api.json

import com.squareup.moshi.Json

data class AuthorizedTokenResponseJson(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "expires_in") val expiresInSec: Int,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "scope") val scope: String
)