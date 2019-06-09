package com.sadashi.client.chatwork.usecase.auth

import io.reactivex.Completable

interface DeleteCodeVerifierUseCase {
    fun execute(): Completable
}