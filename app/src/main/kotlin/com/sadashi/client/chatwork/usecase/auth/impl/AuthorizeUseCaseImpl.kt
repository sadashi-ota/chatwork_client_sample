package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AccessTokenRepository
import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.usecase.auth.AuthorizeUseCase
import io.reactivex.Completable

class AuthorizeUseCaseImpl(
    private val authorizeService: AuthorizeService,
    private val accessTokenRepository: AccessTokenRepository
): AuthorizeUseCase {
    override fun execute(code: String, codeVerifier: String): Completable {
        return authorizeService.execute(code, codeVerifier)
            .flatMapCompletable { accessTokenRepository.store(it) }
    }
}