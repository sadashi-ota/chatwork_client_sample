package com.sadashi.client.chatwork.infra.api.json

import com.sadashi.client.chatwork.BuildConfig
import com.squareup.moshi.Json

data class AccessTokenRequestJson(
    @Json(name = "client_id") val clientId: String = BuildConfig.CLIENT_ID,
    @Json(name = "grant_type") val grantType: String = "authorization_code",
    @Json(name = "redirect_uri") val redirectUri: String = BuildConfig.AUTH_CALLBACK,
    @Json(name = "code") val code: String,
    @Json(name = "code_verifier") val codeVerifier: String
)