package com.sadashi.client.chatwork.domain.auth.impl

import android.util.Base64
import com.sadashi.client.chatwork.domain.auth.AuthorizeRepository
import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.utility.RandomStringBuilder
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class AuthorizeServiceImplTest : Spek({

    lateinit var authorizeServiceImpl: AuthorizeServiceImpl
    lateinit var codeVerifierRepository: CodeVerifierRepository
    lateinit var authorizeRepository: AuthorizeRepository

    val dummyCodeVerifierString = "dummy_code_verifier"

    beforeEachTest {
        codeVerifierRepository = mockk()
        authorizeRepository = mockk()
        authorizeServiceImpl = AuthorizeServiceImpl(codeVerifierRepository, authorizeRepository)
    }

    describe("#createLoginUrl") {
        beforeEach {
            mockkObject(RandomStringBuilder)
            mockkStatic(Base64::class)
            every { RandomStringBuilder.build(any()) } returns dummyCodeVerifierString
            every { Base64.encodeToString(any(), any()) } returns "dummy encode string"
        }
        afterEach {
            unmockkObject(RandomStringBuilder)
            unmockkStatic(Base64::class)
        }
        context("When succeed to store CodeVerifier") {
            beforeEach {
                every {
                    codeVerifierRepository.store(CodeVerifier(dummyCodeVerifierString))
                } returns Completable.complete()
            }

            it("Succeed to createLoginUrl method and return url string.") {
                val single = authorizeServiceImpl.createLoginUrl()
                single.test().await()
                    .assertValue { it.isNotEmpty() }
                    .assertNoErrors()
                    .assertComplete()

                verify(exactly = 1) {
                    RandomStringBuilder.build(any())
                    Base64.encodeToString(any(), any())
                    codeVerifierRepository.store(CodeVerifier(dummyCodeVerifierString))
                }
                confirmVerified(RandomStringBuilder, codeVerifierRepository)
            }
        }
        context("When failed to store CodeVerifier") {
            beforeEach {
                every {
                    codeVerifierRepository.store(CodeVerifier(dummyCodeVerifierString))
                } returns Completable.error(Throwable("dummy error"))
            }

            it("Failed to createLoginUrl method.") {
                val single = authorizeServiceImpl.createLoginUrl()
                single.test().await()
                    .assertNoValues()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()

                verify(exactly = 1) {
                    RandomStringBuilder.build(any())
                    Base64.encodeToString(any(), any())
                    codeVerifierRepository.store(CodeVerifier(dummyCodeVerifierString))
                }
                confirmVerified(RandomStringBuilder, codeVerifierRepository)
            }
        }
    }

    describe("#executeAuthorize") {
        val code = "dummy code"
        beforeEach {
            every { codeVerifierRepository.delete() } returns Completable.complete()
        }
        context("When succeed to find CodeVerifier") {
            beforeEach {
                every {
                    codeVerifierRepository.find()
                } returns Single.just(CodeVerifier(dummyCodeVerifierString))
            }

            context("When succeed to executeAuthorize at authorizeRepository") {
                beforeEach {
                    every {
                        authorizeRepository.executeAuthorize(
                            code, CodeVerifier(dummyCodeVerifierString)
                        )
                    } returns Completable.complete()
                }

                it("Succeed to authorize") {
                    val single = authorizeServiceImpl.executeAuthorize(code)
                    single.test().await()
                        .assertNoErrors()
                        .assertComplete()

                    verify(exactly = 1) {
                        codeVerifierRepository.find()
                        authorizeRepository.executeAuthorize(
                            code, CodeVerifier(dummyCodeVerifierString)
                        )
                        codeVerifierRepository.delete()
                    }
                    confirmVerified(codeVerifierRepository, authorizeRepository)
                }
            }
            context("When failed to executeAuthorize at authorizeRepository") {
                beforeEach {
                    every {
                        authorizeRepository.executeAuthorize(
                            code, CodeVerifier(dummyCodeVerifierString)
                        )
                    } returns Completable.error(Throwable("dummy error"))
                }

                it("Failed to authorize") {
                    val single = authorizeServiceImpl.executeAuthorize(code)
                    single.test().await()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()

                    verify(exactly = 1) {
                        codeVerifierRepository.find()
                        authorizeRepository.executeAuthorize(
                            code, CodeVerifier(dummyCodeVerifierString)
                        )
                    }
                    confirmVerified(codeVerifierRepository, authorizeRepository)
                }
            }
        }

        context("When does not find CodeVerifier") {
            beforeEach {
                every {
                    codeVerifierRepository.find()
                } returns Single.error(Throwable("dummy code"))
            }

            it("Failed to executeAuthorize method") {
                val single = authorizeServiceImpl.executeAuthorize(code)
                single.test().await()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()

                verify(exactly = 1) {
                    codeVerifierRepository.find()
                }
                confirmVerified(codeVerifierRepository, authorizeRepository)
            }
        }
    }
})