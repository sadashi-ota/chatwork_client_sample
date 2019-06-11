package com.sadashi.client.chatwork.domain.auth

import io.reactivex.Completable
import io.reactivex.Single

interface AuthorizeService {
    fun execute(code: String, codeVerifier: CodeVerifier): Completable
    fun existsToken(): Single<Boolean>
    fun getToken(): Single<AuthorizedToken>
    fun deleteToken(): Completable
}