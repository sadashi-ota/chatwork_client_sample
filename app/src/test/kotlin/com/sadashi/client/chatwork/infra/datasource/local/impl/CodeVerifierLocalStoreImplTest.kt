package com.sadashi.client.chatwork.infra.datasource.local.impl

import com.sadashi.client.chatwork.infra.preference.CodeVerifierPreference
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

internal class CodeVerifierLocalStoreImplTest : Spek({

    lateinit var preference: CodeVerifierPreference
    lateinit var localStore: CodeVerifierLocalStoreImpl

    beforeEachTest {
        preference = mockk()
        localStore = CodeVerifierLocalStoreImpl(preference)
    }

    describe("#get") {
        context("When stored valid string in preference") {
            beforeEach { every { preference.get() } returns STORED_VERIFIER_CODE }

            it("calls onSuccess") {
                localStore.get().test().await()
                    .assertValue(STORED_VERIFIER_CODE)
                    .assertComplete()

                verify(exactly = 1) {
                    preference.get()
                }
                confirmVerified(preference)
            }
        }
        context("When stored invalid string in preference") {
            context("returns empty string from preference") {
                beforeEach { every { preference.get() } returns "" }

                it("calls onError and deletes string in preference") {
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
                beforeEach { every { preference.get() } returns null }

                it("calls onError and deletes string in preference") {
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
            beforeEach { every { preference.put(eq(STORED_VERIFIER_CODE)) } returns Unit }

            it("Succeed to store data") {
                localStore.put(STORED_VERIFIER_CODE).test().await()
                    .assertComplete()

                verify(exactly = 1) {
                    preference.put(eq(STORED_VERIFIER_CODE))
                }
                confirmVerified(preference)
            }
        }
    }

    describe("#delete") {
        context("When call delete") {
            beforeEach { every { preference.delete() } returns Unit }

            it("calls onComplete") {

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
        const val STORED_VERIFIER_CODE = "dummy_code"
    }
}