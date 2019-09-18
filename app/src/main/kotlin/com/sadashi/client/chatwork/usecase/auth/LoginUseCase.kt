package com.sadashi.client.chatwork.usecase.auth

import io.reactivex.Single

interface LoginUseCase {
    fun execute(): Single<String>
}