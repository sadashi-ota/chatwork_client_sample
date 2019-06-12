package com.sadashi.client.chatwork.infra.datasource.local.impl

import com.sadashi.client.chatwork.infra.preference.CodeVerifierPreference
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class CodeVerifierLocalStoreImplTest : Spek({

    describe("#get") {
        context("When stored valid string in preference") {
            it("calls onSuccess") {
                val preference: CodeVerifierPreference = mockk()
                val localStore = CodeVerifierLocalStoreImpl(preference)

                every { preference.get() } returns storedVerifierCode

                localStore.get().test().await()
                    .assertValue(storedVerifierCode)
                    .assertComplete()

                verify(exactly = 1) {
                    preference.get()
                }
                confirmVerified(preference)
            }
        }
        context("When stored invalid string in preference") {
            context("returns empty string from preference") {
                it("calls onError and deletes string in preference") {
                    val preference: CodeVerifierPreference = mockk()
                    val localStore = CodeVerifierLocalStoreImpl(preference)

                    every { preference.get() } returns ""
                    every { preference.delete() } returns Unit

                    localStore.get().test().await()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()

                    verify(exactly = 1) {
                        preference.get()
                        preference.delete()
                    }
                    confirmVerified(preference)
                }
            }
            context("returns null from preference") {
                it("calls onError and deletes string in preference") {
                    val preference: CodeVerifierPreference = mockk()
                    val localStore = CodeVerifierLocalStoreImpl(preference)

                    every { preference.get() } returns null
                    every { preference.delete() } returns Unit

                    localStore.get().test().await()
                        .assertError(Throwable::class.java)
                        .assertNotComplete()

                    verify(exactly = 1) {
                        preference.get()
                        preference.delete()
                    }
                    confirmVerified(preference)
                }
            }
        }
    }

    describe("#put") {
        context("When arguments is valid token") {
            it("Succeed to store data") {

                val preference: CodeVerifierPreference = mockk()
                val localStore = CodeVerifierLocalStoreImpl(preference)

                every { preference.put(eq(storedVerifierCode)) } returns Unit

                localStore.put(storedVerifierCode).test().await()
                    .assertComplete()

                verify(exactly = 1) {
                    preference.put(eq(storedVerifierCode))
                }
                confirmVerified(preference)
            }
        }
    }

    describe("#delete") {
        context("When call delete") {
            it("calls onComplete") {
                val preference: CodeVerifierPreference = mockk()
                val localStore = CodeVerifierLocalStoreImpl(preference)

                every { preference.delete() } returns Unit

                localStore.delete().test().await()
                    .assertComplete()

                verify(exactly = 1) {
                    preference.delete()
                }
                confirmVerified(preference)
            }
        }
    }

}) {
    companion object {
        const val storedVerifierCode = "dummy_code"
    }
}