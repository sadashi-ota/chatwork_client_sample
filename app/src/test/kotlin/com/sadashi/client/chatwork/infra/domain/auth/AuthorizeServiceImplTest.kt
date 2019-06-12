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

internal class AuthorizeServiceImplTest : Spek({

    lateinit var apiClient: AuthApiClient
    lateinit var localStore: AuthorizedTokenLocalStore
    val scheduler: Scheduler = Schedulers.trampoline()

    lateinit var authorizeService: AuthorizeServiceImpl

    beforeEachTest {
        mockkObject(AuthorizedTokenConverter)
        apiClient = mockk()
        localStore = mockk()

        authorizeService = AuthorizeServiceImpl(apiClient, localStore, scheduler)
    }

    afterEachTest {
        unmockkObject(AuthorizedTokenConverter)
    }

    describe("#executeAuthorize") {
        context("When arguments is valid") {
            it("calls onComplete") {
                every {
                    apiClient.getToken(code = VALID_CODE, codeVerifier = VALID_CODE_VERIFIER)
                } returns Single.just(VALID_RESPONSE)
                every {
                    AuthorizedTokenConverter.convertToDomainModel(VALID_RESPONSE)
                } returns VALID_AUTHORIZED_TOKEN
                every { localStore.put(VALID_AUTHORIZED_TOKEN) } returns Completable.fromCallable {}

                authorizeService
                    .executeAuthorize(VALID_CODE, CodeVerifier(VALID_CODE_VERIFIER))
                    .test().await()
                    .assertNoErrors()
                    .assertNotComplete()

                verify(exactly = 1) {
                    apiClient.getToken(code = VALID_CODE, codeVerifier = VALID_CODE_VERIFIER)
                    AuthorizedTokenConverter.convertToDomainModel(VALID_RESPONSE)
                    localStore.put(VALID_AUTHORIZED_TOKEN)
                }
                confirmVerified(apiClient, localStore, AuthorizedTokenConverter)
            }
        }

        context("When api is failed") {
            it("calls onError") {
                every {
                    apiClient.getToken(code = any(), codeVerifier = any())
                } returns Single.error(Throwable("Dummy error."))

                authorizeService
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

        context("When authorized token is failed to store") {
            it("calls onError") {
                every {
                    apiClient.getToken(code = VALID_CODE, codeVerifier = VALID_CODE_VERIFIER)
                } returns Single.just(VALID_RESPONSE)
                every {
                    AuthorizedTokenConverter.convertToDomainModel(VALID_RESPONSE)
                } returns VALID_AUTHORIZED_TOKEN
                every { localStore.put(VALID_AUTHORIZED_TOKEN) } returns Completable.error(Throwable("Dummy error"))

                authorizeService.executeAuthorize(VALID_CODE, CodeVerifier(VALID_CODE_VERIFIER))
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

    describe("#existsToken") {
        context("When get valid token from local store") {
            it("calls onSuccess and value is true") {
                every { localStore.get() } returns Maybe.just(VALID_AUTHORIZED_TOKEN)

                authorizeService.existsToken().test().await()
                    .assertValue(true)
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.get()
                }
                confirmVerified(apiClient, localStore)
            }
        }
        context("When does not exists token at local store") {
            it("calls onSuccess and value is false") {
                every { localStore.get() } returns Maybe.empty()

                authorizeService.existsToken().test().await()
                    .assertValue(false)
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.get()
                }
                confirmVerified(apiClient, localStore)
            }
        }
        context("When fail to get token from local store") {
            it("calls onError") {
                every { localStore.get() } returns Maybe.error(Throwable("Dummy error"))

                authorizeService.existsToken().test().await()
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
            context("when the token has not expired") {
                it("calls onSuccess") {
                    every { localStore.get() } returns Maybe.just(spyToken)
                    every { spyToken.isExpired() } returns false

                    authorizeService.getToken().test().await()
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
                context("when succeeds to refresh token") {
                    context("when succeeds to store refresh token") {
                        it("calls onSuccess") {
                            every { localStore.get() } returns Maybe.just(spyToken)
                            every { spyToken.isExpired() } returns true
                            every { spyToken.refreshToken.value } returns VALID_RESPONSE.refreshToken
                            every {
                                apiClient.refreshToken(refreshToken = eq(VALID_RESPONSE.refreshToken))
                            } returns Single.just(VALID_RESPONSE)
                            every {
                                AuthorizedTokenConverter.convertToDomainModel(eq(VALID_RESPONSE))
                            } returns VALID_AUTHORIZED_TOKEN
                            every {
                                localStore.put(eq(VALID_AUTHORIZED_TOKEN))
                            } returns Completable.fromCallable { }

                            authorizeService.getToken().test().await()
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
                        it("calls onError") {
                            every { localStore.get() } returns Maybe.just(spyToken)
                            every { spyToken.isExpired() } returns true
                            every { spyToken.refreshToken.value } returns VALID_RESPONSE.refreshToken
                            every {
                                apiClient.refreshToken(refreshToken = eq(VALID_RESPONSE.refreshToken))
                            } returns Single.just(VALID_RESPONSE)
                            every {
                                AuthorizedTokenConverter.convertToDomainModel(eq(VALID_RESPONSE))
                            } returns VALID_AUTHORIZED_TOKEN
                            every {
                                localStore.put(any())
                            } returns Completable.error(Throwable("Dummy error"))

                            authorizeService.getToken().test().await()
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
                    it("calls onError") {
                        every { localStore.get() } returns Maybe.just(spyToken)
                        every { spyToken.isExpired() } returns true
                        every { spyToken.refreshToken.value } returns VALID_RESPONSE.refreshToken
                        every {
                            apiClient.refreshToken(refreshToken = eq(VALID_RESPONSE.refreshToken))
                        } returns Single.error(Throwable("Dummy error"))

                        authorizeService.getToken().test().await()
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
            it("calls onError") {
                every { localStore.get() } returns Maybe.error(Throwable("Dummy error"))

                authorizeService.getToken().test().await()
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
            it("calls onComplete") {
                every { localStore.delete() } returns Completable.fromCallable { }

                authorizeService.deleteToken().test().await()
                    .assertNoErrors()
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.delete()
                }
                confirmVerified(apiClient, localStore, AuthorizedTokenConverter)
            }
        }
        context("When fails to delete from local store") {
            it("calls onError") {
                every { localStore.delete() } returns Completable.error(Throwable("Dummy error"))

                authorizeService.deleteToken().test().await()
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

