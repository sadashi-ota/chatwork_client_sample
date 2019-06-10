package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import io.reactivex.Scheduler
import io.reactivex.Single

class AuthorizeServiceImpl(
    private val apiClient: AuthApiClient,
    private val ioScheduler: Scheduler
) : AuthorizeService {

    override fun execute(
        code: String,
        codeVerifier: CodeVerifier
    ): Single<AuthorizedToken> {
        return apiClient.getToken(code = code, codeVerifier = codeVerifier.value)
            .map { AuthorizedTokenConverter.convertToDomainModel(it) }
            .subscribeOn(ioScheduler)
    }
}