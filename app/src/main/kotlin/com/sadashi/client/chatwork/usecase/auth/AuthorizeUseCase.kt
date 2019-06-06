package com.sadashi.client.chatwork.usecase.auth

import io.reactivex.Completable

interface AuthorizeUseCase {
    fun execute(code: String, codeVerifier: String): Completable
}