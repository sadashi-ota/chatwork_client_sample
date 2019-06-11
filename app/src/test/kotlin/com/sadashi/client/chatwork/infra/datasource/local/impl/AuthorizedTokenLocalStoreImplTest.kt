package com.sadashi.client.chatwork.infra.datasource.local.impl

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.RefreshToken
import com.sadashi.client.chatwork.infra.preference.AuthorizedTokenPreference
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.Date

internal class AuthorizedTokenLocalStoreImplTest : Spek({

    describe("#get") {
        context("When stored valid json in preference") {
            it("calls onSuccess") {
                val preference: AuthorizedTokenPreference = mockk()
                val localStore = spyk(
                    AuthorizedTokenLocalStoreImpl(preference),
                    recordPrivateCalls = true
                )

                every { preference.get() } returns validJson

                localStore.get().test().await()
                    .assertValue(authorizedToken)
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.get()
                    preference.get()
                    localStore["convertFromJson"](eq(validJson))
                }
                confirmVerified(preference, localStore)
            }
        }

        context("When not stored json in preference") {
            context("returns empty string from preference") {
                it("calls onComplete") {
                    val preference: AuthorizedTokenPreference = mockk()
                    val localStore = spyk(AuthorizedTokenLocalStoreImpl(preference))

                    every { preference.get() } returns ""

                    localStore.get().test().await()
                        .assertComplete()

                    verify(exactly = 1) {
                        localStore.get()
                        preference.get()
                    }
                    confirmVerified(preference, localStore)
                }
            }
            context("returns null from preference") {
                it("calls onComplete") {
                    val preference: AuthorizedTokenPreference = mockk()
                    val localStore = spyk(AuthorizedTokenLocalStoreImpl(preference))

                    every { preference.get() } returns null

                    localStore.get().test().await()
                        .assertComplete()

                    verify(exactly = 1) {
                        localStore.get()
                        preference.get()
                    }
                    confirmVerified(preference, localStore)
                }
            }
        }

        context("When stored invalid json in preference") {
            context("Not json format") {
                val invalidJson = "dummy string"
                it("calls onError") {
                    val preference: AuthorizedTokenPreference = mockk()
                    val localStore = spyk(
                        AuthorizedTokenLocalStoreImpl(preference),
                        recordPrivateCalls = true
                    )

                    every { preference.get() } returns invalidJson
                    every { preference.delete() } returns Unit

                    localStore.get().test().await()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()

                    verify(exactly = 1) {
                        localStore.get()
                        preference.get()
                        localStore["convertFromJson"](eq(invalidJson))
                        preference.delete()
                    }
                    confirmVerified(preference, localStore)
                }
            }
            context("Not contains to need data") {
                val invalidJson = "{}"
                it("calls onError") {
                    val preference: AuthorizedTokenPreference = mockk()
                    val localStore = spyk(
                        AuthorizedTokenLocalStoreImpl(preference),
                        recordPrivateCalls = true
                    )

                    every { preference.get() } returns invalidJson
                    every { preference.delete() } returns Unit

                    localStore.get().test().await()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()

                    verify(exactly = 1) {
                        localStore.get()
                        preference.get()
                        localStore["convertFromJson"](eq(invalidJson))
                        preference.delete()
                    }
                    confirmVerified(preference, localStore)
                }
            }
        }
    }

    describe("#put") {
        context("When arguments is valid token") {
            it("Succeed to store data") {
                val preference: AuthorizedTokenPreference = mockk()
                val localStore = spyk(
                    AuthorizedTokenLocalStoreImpl(preference),
                    recordPrivateCalls = true
                )

                every { preference.put(eq(validJson)) } returns Unit

                localStore.put(authorizedToken).test().await()
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.put(eq(authorizedToken))
                    localStore["convertToJson"](eq(authorizedToken))
                    preference.put(eq(validJson))
                }
                confirmVerified(preference, localStore)
            }
        }
    }

    describe("#delete") {
        context("When call delete") {
            it("calls onComplete") {
                val preference: AuthorizedTokenPreference = mockk()
                val localStore = spyk(AuthorizedTokenLocalStoreImpl(preference))

                every { preference.delete() } returns Unit

                localStore.delete().test().await()
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.delete()
                    preference.delete()
                }
                confirmVerified(preference, localStore)
            }
        }
    }
}) {
    companion object {
        val authorizedToken = AuthorizedToken(
            accessToken = AccessToken("dummy_access_token"),
            refreshToken = RefreshToken("dummy_refresh_token"),
            expiredTime = Date(),
            tokenType = "dummy_token_type",
            scope = "dummy_scope"
        )
        val validJson = """
                |{
                |    "accessToken" : "${authorizedToken.accessToken.value}",
                |    "expiredTime" : ${authorizedToken.expiredTime.time},
                |    "refreshToken" : "${authorizedToken.refreshToken.value}",
                |    "scope" : "${authorizedToken.scope}",
                |    "tokenType" : "${authorizedToken.tokenType}"
                |}
                |"""
            .trimMargin()
            .replace("\n", "")
            .replace(" ", "")
    }
}
