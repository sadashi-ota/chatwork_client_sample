package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.usecase.auth.AuthorizeUseCase
import io.reactivex.Completable

class AuthorizeUseCaseImpl(
    private val authorizeService: AuthorizeService,
    private val codeVerifierRepository: CodeVerifierRepository
) : AuthorizeUseCase {
    override fun execute(code: String): Completable {
        return codeVerifierRepository.find()
            .flatMapCompletable { codeVerifier ->
                authorizeService.executeAuthorize(code, codeVerifier)
            }
    }
}