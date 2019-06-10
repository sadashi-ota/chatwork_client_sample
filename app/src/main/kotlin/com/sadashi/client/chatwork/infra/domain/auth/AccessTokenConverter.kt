package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.auth.RefreshToken
import com.sadashi.client.chatwork.infra.api.json.AccessTokenResponseJson

object AccessTokenConverter {
    fun convertToDomainModel(json: AccessTokenResponseJson): Pair<AccessToken, RefreshToken> {
        return Pair(AccessToken(json.accessToken), RefreshToken(json.refreshToken))
    }
}