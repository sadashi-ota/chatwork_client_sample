package com.sadashi.client.chatwork.domain.auth

import java.util.Date

data class AuthorizedToken(
    val accessToken: AccessToken,
    val refreshToken: RefreshToken,
    val expiredTime: Date,
    val tokenType: String,
    val scope: String
) {
    val accessTokenString: String by lazy {
        "$tokenType ${accessToken.value}"
    }

    fun isExpired(): Boolean = (Date() > expiredTime)
}