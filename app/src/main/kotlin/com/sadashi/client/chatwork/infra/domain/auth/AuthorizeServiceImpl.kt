package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.api.json.AccessTokenRequestJson
import com.sadashi.client.chatwork.infra.api.json.AccessTokenRequestJsonConverter
import io.reactivex.Scheduler
import io.reactivex.Single

class AuthorizeServiceImpl(
    private val apiClient: AuthApiClient,
    private val ioScheduler: Scheduler
) : AuthorizeService {

    override fun execute(code: String, codeVerifier: String): Single<AccessToken> {
        val queryMap = AccessTokenRequestJsonConverter.convert(
            AccessTokenRequestJson(code = code, codeVerifier = codeVerifier)
        )
        return apiClient.getAccessToken(queryMap)
            .map { AccessTokenConverter.convertToDomainModel(it) }
            .subscribeOn(ioScheduler)
    }
}