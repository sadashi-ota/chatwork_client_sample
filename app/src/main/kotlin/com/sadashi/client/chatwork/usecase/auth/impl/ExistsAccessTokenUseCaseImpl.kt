package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AccessTokenRepository
import com.sadashi.client.chatwork.usecase.auth.ExistsAccessTokenUseCase
import io.reactivex.Single

class ExistsAccessTokenUseCaseImpl(
    private val repository: AccessTokenRepository
) : ExistsAccessTokenUseCase {
    override fun execute(): Single<Boolean> {
        return repository.exists()
    }
}