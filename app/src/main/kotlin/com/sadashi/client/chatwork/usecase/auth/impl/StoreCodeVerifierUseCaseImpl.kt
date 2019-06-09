package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.usecase.auth.StoreCodeVerifierUseCase
import io.reactivex.Completable

class StoreCodeVerifierUseCaseImpl(
    private val codeVerifierRepository: CodeVerifierRepository
) : StoreCodeVerifierUseCase {
    override fun execute(codeVerifier: String): Completable {
        return codeVerifierRepository.store(CodeVerifier(codeVerifier))
    }
}