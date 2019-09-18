package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizeRepository
import com.sadashi.client.chatwork.usecase.auth.DeleteAccessTokenUseCase
import io.reactivex.Completable

class DeleteAccessTokenUseCaseImpl(
    private val authorizeRepository: AuthorizeRepository
) : DeleteAccessTokenUseCase {
    override fun execute(): Completable = authorizeRepository.deleteToken()
}