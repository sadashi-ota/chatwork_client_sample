package com.sadashi.client.chatwork.usecase.auth.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class AuthorizeUseCaseImplTest : Spek({

    lateinit var authorizeService: AuthorizeService
    lateinit var codeVerifierRepository: CodeVerifierRepository

    lateinit var authorizeUseCase: AuthorizeUseCaseImpl

    beforeEachTest {
        authorizeService = mockk()
        codeVerifierRepository = mockk()
        authorizeUseCase = AuthorizeUseCaseImpl(authorizeService, codeVerifierRepository)
    }

    describe("#execute") {
        context("When succeeds to get code verifier") {
            beforeEach {
                every { codeVerifierRepository.find() } returns Single.just(VALID_CODE_VERIFIER)
            }
            context("When succeeds to authorize") {
                beforeEach {
                    every {
                        authorizeService.executeAuthorize(eq(VALID_CODE), eq(VALID_CODE_VERIFIER))
                    } returns Completable.complete()
                }

                it("calls onComplete") {
                    authorizeUseCase.execute(VALID_CODE).test().await()
                        .assertNoErrors()
                        .assertComplete()

                    verify(exactly = 1) {
                        codeVerifierRepository.find()
                        authorizeService.executeAuthorize(eq(VALID_CODE), eq(VALID_CODE_VERIFIER))
                    }
                    confirmVerified(authorizeService, codeVerifierRepository)
                }
            }
            context("When fails to authorize") {
                beforeEach {
                    every {
                        authorizeService.executeAuthorize(eq(INVALID_CODE), eq(VALID_CODE_VERIFIER))
                    } returns Completable.error(Throwable("Dummy error."))
                }

                it("calls onError") {
                    authorizeUseCase.execute(INVALID_CODE).test().await()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()

                    verify(exactly = 1) {
                        codeVerifierRepository.find()
                        authorizeService.executeAuthorize(eq(INVALID_CODE), eq(VALID_CODE_VERIFIER))
                    }
                    confirmVerified(authorizeService, codeVerifierRepository)
                }
            }
        }
        context("When fails to get code verifier") {
            beforeEach {
                every { codeVerifierRepository.find() } returns Single.error(Throwable("Dummy error."))
            }
            it("calls onError") {
                authorizeUseCase.execute(INVALID_CODE).test().await()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()

                verify(exactly = 1) {
                    codeVerifierRepository.find()
                }
                confirmVerified(authorizeService, codeVerifierRepository)
            }
        }
    }
}) {
    companion object {
        const val VALID_CODE = "valid_code"
        const val INVALID_CODE = "invalid_code"
        val VALID_CODE_VERIFIER = CodeVerifier("valid_code_verifier")
    }
}