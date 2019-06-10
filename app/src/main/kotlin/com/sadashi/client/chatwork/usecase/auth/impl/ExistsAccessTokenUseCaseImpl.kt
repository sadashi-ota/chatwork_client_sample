package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.usecase.auth.ExistsAccessTokenUseCase
import io.reactivex.Single

class ExistsAccessTokenUseCaseImpl(
    private val authorizeService: AuthorizeService
) : ExistsAccessTokenUseCase {
    override fun execute(): Single<Boolean> {
        return authorizeService.existsToken()
    }
}