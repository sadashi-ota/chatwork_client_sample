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

    lateinit var preference: AuthorizedTokenPreference

    beforeEachTest {
        preference = mockk()
    }

    describe("#get") {
        context("When stored valid json in preference") {
            beforeEach { every { preference.get() } returns VALID_JSON }

            it("calls onSuccess") {
                val localStore = spyk(
                    AuthorizedTokenLocalStoreImpl(preference),
                    recordPrivateCalls = true
                )

                localStore.get().test().await()
                    .assertValue(VALID_AUTHORIZED_TOKEN)
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.get()
                    preference.get()
                    localStore["convertFromJson"](eq(VALID_JSON))
                }
                confirmVerified(preference, localStore)
            }
        }

        context("When not stored json in preference") {
            context("returns empty string from preference") {
                beforeEach { every { preference.get() } returns "" }

                it("calls onComplete") {
                    val localStore = spyk(AuthorizedTokenLocalStoreImpl(preference))

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
                beforeEach { every { preference.get() } returns null }

                it("calls onComplete") {
                    val localStore = spyk(AuthorizedTokenLocalStoreImpl(preference))

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

                beforeEach { every { preference.get() } returns invalidJson }

                it("calls onError and deletes data in preference") {
                    val localStore = spyk(
                        AuthorizedTokenLocalStoreImpl(preference),
                        recordPrivateCalls = true
                    )

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

                beforeEach { every { preference.get() } returns invalidJson }

                it("calls onError and deletes data in preference") {
                    val localStore = spyk(
                        AuthorizedTokenLocalStoreImpl(preference),
                        recordPrivateCalls = true
                    )

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
            beforeEach { every { preference.put(eq(VALID_JSON)) } returns Unit }

            it("Succeed to store data") {
                val localStore = spyk(
                    AuthorizedTokenLocalStoreImpl(preference),
                    recordPrivateCalls = true
                )

                localStore.put(VALID_AUTHORIZED_TOKEN).test().await()
                    .assertComplete()

                verify(exactly = 1) {
                    localStore.put(eq(VALID_AUTHORIZED_TOKEN))
                    localStore["convertToJson"](eq(VALID_AUTHORIZED_TOKEN))
                    preference.put(eq(VALID_JSON))
                }
                confirmVerified(preference, localStore)
            }
        }
    }

    describe("#delete") {
        context("When call delete") {
            beforeEach { every { preference.delete() } returns Unit }

            it("calls onComplete") {
                val localStore = spyk(AuthorizedTokenLocalStoreImpl(preference))

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
        val VALID_AUTHORIZED_TOKEN = AuthorizedToken(
            accessToken = AccessToken("dummy_access_token"),
            refreshToken = RefreshToken("dummy_refresh_token"),
            expiredTime = Date(),
            tokenType = "dummy_token_type",
            scope = "dummy_scope"
        )
        val VALID_JSON = """
                |{
                |    "accessToken" : "${VALID_AUTHORIZED_TOKEN.accessToken.value}",
                |    "expiredTime" : ${VALID_AUTHORIZED_TOKEN.expiredTime.time},
                |    "refreshToken" : "${VALID_AUTHORIZED_TOKEN.refreshToken.value}",
                |    "scope" : "${VALID_AUTHORIZED_TOKEN.scope}",
                |    "tokenType" : "${VALID_AUTHORIZED_TOKEN.tokenType}"
                |}
                |"""
            .trimMargin()
            .replace("\n", "")
            .replace(" ", "")
    }
}
