package com.sadashi.client.chatwork.usecase.auth

import io.reactivex.Completable

interface DeleteAccessTokenUseCase {
    fun execute(): Completable
}