package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.usecase.auth.DeleteAccessTokenUseCase
import io.reactivex.Completable

class DeleteAccessTokenUseCaseImpl(
    private val authorizeService: AuthorizeService
) : DeleteAccessTokenUseCase {
    override fun execute(): Completable = authorizeService.deleteToken()
}