package com.sadashi.client.chatwork.usecase.auth

import io.reactivex.Single

interface ExistsAccessTokenUseCase {
    fun execute(): Single<Boolean>
}