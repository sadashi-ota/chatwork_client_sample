package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.usecase.auth.DeleteCodeVerifierUseCase
import io.reactivex.Completable

class DeleteCodeVerifierUseCaseImpl(
    private val codeVerifierRepository: CodeVerifierRepository
) : DeleteCodeVerifierUseCase {
    override fun execute(): Completable = codeVerifierRepository.delete()
}