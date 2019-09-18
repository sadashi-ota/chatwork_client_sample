package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.domain.auth.RefreshToken
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.api.json.AuthorizedTokenResponseJson
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkObject
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.Date

internal class AuthorizeRepositoryImplTest : Spek({

    lateinit var apiClient: AuthApiClient
    lateinit var localStore: AuthorizedTokenLocalStore
    val scheduler: Scheduler = Schedulers.trampoline()

    lateinit var authorizeRepository: AuthorizeRepositoryImpl

    beforeEachTest {
        mockkObject(AuthorizedTokenConverter)
        apiClient = mockk()
        localStore = mockk()

        authorizeRepository = AuthorizeRepositoryImpl(apiClient, localStore, scheduler)
    }

    afterEachTest {
        unmockkObject(AuthorizedTokenConverter)
    }

    describe("#executeAuthorize") {
        context("When api is succeed") {
            beforeEach {
                every {
                    apiClient.getToken(code = VALID_CODE, codeVerifier = VALID_CODE_VERIFIER)
                } returns Single.just(VALID_RESPONSE)
                every { AuthorizedTokenConverter.convertToDomainModel(VALID_RESPONSE) } returns VALID_AUTHORIZED_TOKEN
            }
            context("When authorized token is succeed to store") {
                beforeEach {
                    every { localStore.put(VALID_AUTHORIZED_TOKEN) } returns Completable.complete()
                }
                it("calls onComplete") {
                    authorizeRepository
                        .executeAuthorize(VALID_CODE, CodeVerifier(VALID_CODE_VERIFIER))
                        .test().await()
                        .assertNoErrors()
                        .assertComplete()

                    verify(exactly = 1) {
                        apiClient.getToken(code = VALID_CODE, codeVerifier = VALID_CODE_VERIFIER)
                        AuthorizedTokenConverter.convertToDomainModel(VALID_RESPONSE)
                        localStore.put(VALID_AUTHORIZED_TOKEN)
                    }
                    confirmVerified(apiClient, localStore, AuthorizedTokenConverter)
                }
            }
            context("When authorized token is failed to store") {
                beforeEach {
                    every {
                        localStore.put(VALID_AUTHORIZED_TOKEN)
                    } returns Completable.error(Throwable("Dummy error"))
                }
                it("calls onError") {
                    authorizeRepository.executeAuthorize(VALID_CODE, CodeVerifier(VALID_CODE_VERIFIER))
                        .test().await()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()

                    verify(exactly = 1) {
                        apiClient.getToken(code = VALID_CODE, codeVerifier = VALID_CODE_VERIFIER)
                        AuthorizedTokenConverter.convertToDomainModel(VALID_RESPONSE)
                        localStore.put(VALID_AUTHORIZED_TOKEN)
                    }
                    confirmVerified(apiClient, localStore, AuthorizedTokenConverter)
                }
            }
        }
        context("When api is failed") {
            beforeEach {
                every {
                    apiClient.getToken(code = any(), codeVerifier = any())
                } returns Single.error(Throwable("Dummy error."))
            }

            it("calls onError") {
                authorizeRepository
                    .executeAuthorize("dummy", CodeVerifier("dummy"))
                    .test().await()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()

                verify(exactly = 1) {
                    apiClient.getToken(code = any(), codeVerifier = any())
                }
                confirmVerified(apiClient, localStore, AuthorizedTokenConverter)
            }
        }
    }

    describe("#existsToken") {
        context("When get valid token from local store") {
            beforeEach { every { localStore.get() } returns Maybe.just(VALID_AUTHORIZED_TOKEN) }

            it("calls onSuccess and value is true") {
                authorizeRepository.existsToken().test().await()
                    .assertValue(true)
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.get()
                }
                confirmVerified(apiClient, localStore)
            }
        }
        context("When does not exists token at local store") {
            beforeEach { every { localStore.get() } returns Maybe.empty() }

            it("calls onSuccess and value is false") {
                authorizeRepository.existsToken().test().await()
                    .assertValue(false)
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.get()
                }
                confirmVerified(apiClient, localStore)
            }
        }
        context("When fail to get token from local store") {
            beforeEach { every { localStore.get() } returns Maybe.error(Throwable("Dummy error")) }

            it("calls onError") {
                authorizeRepository.existsToken().test().await()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()

                verify(exactly = 1) {
                    localStore.get()
                }
                confirmVerified(apiClient, localStore)
            }
        }
    }

    describe("#getToken") {
        lateinit var spyToken: AuthorizedToken

        beforeEach {
            spyToken = spyk(VALID_AUTHORIZED_TOKEN)
        }

        context("When get valid token from local store") {
            beforeEach { every { localStore.get() } returns Maybe.just(spyToken) }

            context("when the token has not expired") {
                beforeEach { every { spyToken.isExpired() } returns false }

                it("calls onSuccess") {

                    authorizeRepository.getToken().test().await()
                        .assertValue(spyToken)
                        .assertComplete()

                    verify(exactly = 1) {
                        localStore.get()
                        spyToken.isExpired()
                    }
                    confirmVerified(apiClient, localStore, spyToken, AuthorizedTokenConverter)
                }
            }

            context("when the token has expired") {
                beforeEach {
                    every { spyToken.isExpired() } returns true
                    every { spyToken.refreshToken.value } returns VALID_RESPONSE.refreshToken
                }

                context("when succeeds to refresh token") {
                    beforeEach {
                        every {
                            apiClient.refreshToken(refreshToken = eq(VALID_RESPONSE.refreshToken))
                        } returns Single.just(VALID_RESPONSE)
                        every {
                            AuthorizedTokenConverter.convertToDomainModel(eq(VALID_RESPONSE))
                        } returns VALID_AUTHORIZED_TOKEN
                    }
                    context("when succeeds to store refresh token") {
                        beforeEach {
                            every { localStore.put(eq(VALID_AUTHORIZED_TOKEN)) } returns Completable.complete()
                        }
                        it("calls onSuccess") {
                            authorizeRepository.getToken().test().await()
                                .assertValue(VALID_AUTHORIZED_TOKEN)
                                .assertComplete()

                            verify(exactly = 1) {
                                localStore.get()
                                spyToken.isExpired()
                                spyToken.refreshToken.value
                                apiClient.refreshToken(refreshToken = eq(VALID_RESPONSE.refreshToken))
                                AuthorizedTokenConverter.convertToDomainModel(eq(VALID_RESPONSE))
                                localStore.put(eq(VALID_AUTHORIZED_TOKEN))
                            }
                            confirmVerified(apiClient, localStore, spyToken, AuthorizedTokenConverter)
                        }
                    }
                    context("when fails to store refresh token") {
                        beforeEach {
                            every { localStore.put(any()) } returns Completable.error(Throwable("Dummy error"))
                        }

                        it("calls onError") {
                            authorizeRepository.getToken().test().await()
                                .assertError(Throwable::class.java)
                                .assertNotComplete()

                            verify(exactly = 1) {
                                localStore.get()
                                spyToken.isExpired()
                                spyToken.refreshToken.value
                                apiClient.refreshToken(refreshToken = eq(VALID_RESPONSE.refreshToken))
                                AuthorizedTokenConverter.convertToDomainModel(eq(VALID_RESPONSE))
                                localStore.put(any())
                            }
                            confirmVerified(apiClient, localStore, spyToken, AuthorizedTokenConverter)
                        }
                    }
                }
                context("when fails to refresh token") {
                    beforeEach {
                        every {
                            apiClient.refreshToken(refreshToken = eq(VALID_RESPONSE.refreshToken))
                        } returns Single.error(Throwable("Dummy error"))
                    }
                    it("calls onError") {
                        authorizeRepository.getToken().test().await()
                            .assertError(Throwable::class.java)
                            .assertNotComplete()

                        verify(exactly = 1) {
                            localStore.get()
                            spyToken.isExpired()
                            spyToken.refreshToken.value
                            apiClient.refreshToken(refreshToken = eq(VALID_RESPONSE.refreshToken))
                        }
                        confirmVerified(apiClient, localStore, spyToken, AuthorizedTokenConverter)
                    }
                }
            }
        }
        context("When fails to get token from local store") {
            beforeEach { every { localStore.get() } returns Maybe.error(Throwable("Dummy error")) }

            it("calls onError") {
                authorizeRepository.getToken().test().await()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()

                verify(exactly = 1) {
                    localStore.get()
                }
                confirmVerified(apiClient, localStore, AuthorizedTokenConverter)
            }
        }
    }

    describe("#deleteToken") {
        context("When succeeds to delete from local store") {
            beforeEach { every { localStore.delete() } returns Completable.complete() }

            it("calls onComplete") {
                authorizeRepository.deleteToken().test().await()
                    .assertNoErrors()
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.delete()
                }
                confirmVerified(apiClient, localStore, AuthorizedTokenConverter)
            }
        }
        context("When fails to delete from local store") {
            beforeEach { every { localStore.delete() } returns Completable.error(Throwable("Dummy error")) }

            it("calls onError") {
                authorizeRepository.deleteToken().test().await()
                    .assertError(Throwable::class.java)
                    .assertNotComplete()

                verify(exactly = 1) {
                    localStore.delete()
                }
                confirmVerified(apiClient, localStore, AuthorizedTokenConverter)
            }
        }
    }
}) {
    companion object {
        const val VALID_CODE = "valid_code"
        const val VALID_CODE_VERIFIER = "valid_code_verifier"

        val VALID_RESPONSE = AuthorizedTokenResponseJson(
            accessToken = "dummy_access_token",
            refreshToken = "dummy_refresh_token",
            expiresInSec = 1800,
            tokenType = "dummy_token_type",
            scope = "dummy_scope"
        )

        val VALID_AUTHORIZED_TOKEN = AuthorizedToken(
            AccessToken(VALID_RESPONSE.accessToken),
            RefreshToken(VALID_RESPONSE.refreshToken),
            Date(System.currentTimeMillis() + (VALID_RESPONSE.expiresInSec * 1000)),
            VALID_RESPONSE.tokenType,
            VALID_RESPONSE.scope
        )
    }
}

