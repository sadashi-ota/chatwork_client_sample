package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizedTokenRepository
import com.sadashi.client.chatwork.usecase.auth.ExistsAccessTokenUseCase
import io.reactivex.Single

class ExistsAccessTokenUseCaseImpl(
    private val repository: AuthorizedTokenRepository
) : ExistsAccessTokenUseCase {
    override fun execute(): Single<Boolean> {
        return repository.exists()
    }
}