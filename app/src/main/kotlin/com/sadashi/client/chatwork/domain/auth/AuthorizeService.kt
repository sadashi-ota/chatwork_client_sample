package com.sadashi.client.chatwork.domain.auth

import io.reactivex.Single

interface AuthorizeService {
    fun execute(code: String, codeVerifier: CodeVerifier): Single<AuthorizedToken>
}