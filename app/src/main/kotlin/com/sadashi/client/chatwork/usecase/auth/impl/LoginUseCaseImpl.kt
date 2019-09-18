package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.usecase.auth.LoginUseCase
import io.reactivex.Single

class LoginUseCaseImpl(
    private val authorizeService: AuthorizeService
) : LoginUseCase {
    override fun execute(): Single<String> = authorizeService.createLoginUrl()
}