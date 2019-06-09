package com.sadashi.client.chatwork.usecase.auth

import io.reactivex.Completable

interface StoreCodeVerifierUseCase {
    fun execute(codeVerifier: String): Completable
}