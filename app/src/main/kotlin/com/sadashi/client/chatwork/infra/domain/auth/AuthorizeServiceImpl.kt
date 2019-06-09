package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import io.reactivex.Scheduler
import io.reactivex.Single

class AuthorizeServiceImpl(
    private val apiClient: AuthApiClient,
    private val ioScheduler: Scheduler
) : AuthorizeService {

    override fun execute(code: String, codeVerifier: CodeVerifier): Single<AccessToken> {
        return apiClient.getAccessToken(code = code, codeVerifier = codeVerifier.value)
            .map { AccessTokenConverter.convertToDomainModel(it) }
            .subscribeOn(ioScheduler)
    }
}