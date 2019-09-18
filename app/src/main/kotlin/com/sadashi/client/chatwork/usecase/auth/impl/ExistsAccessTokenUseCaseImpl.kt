package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizeRepository
import com.sadashi.client.chatwork.usecase.auth.ExistsAccessTokenUseCase
import io.reactivex.Single

class ExistsAccessTokenUseCaseImpl(
    private val authorizeRepository: AuthorizeRepository
) : ExistsAccessTokenUseCase {
    override fun execute(): Single<Boolean> {
        return authorizeRepository.existsToken()
    }
}