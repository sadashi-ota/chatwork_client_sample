package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.RefreshToken
import com.sadashi.client.chatwork.infra.api.json.AuthorizedTokenResponseJson
import java.util.Date

object AuthorizedTokenConverter {
    fun convertToDomainModel(json: AuthorizedTokenResponseJson): AuthorizedToken {
        return AuthorizedToken(
            AccessToken(json.accessToken),
            RefreshToken(json.refreshToken),
            Date(System.currentTimeMillis() + (json.expiresInSec * 1000)),
            json.tokenType,
            json.scope
        )
    }
}