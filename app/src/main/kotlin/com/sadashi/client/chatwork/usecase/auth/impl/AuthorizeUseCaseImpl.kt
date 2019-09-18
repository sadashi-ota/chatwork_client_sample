package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.usecase.auth.AuthorizeUseCase
import io.reactivex.Completable
import io.reactivex.Single

class AuthorizeUseCaseImpl(
    private val authorizeService: AuthorizeService
) : AuthorizeUseCase {
    override fun execute(code: String): Completable = authorizeService.executeAuthorize(code)
}