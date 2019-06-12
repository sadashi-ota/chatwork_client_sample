package com.sadashi.client.chatwork.infra.datasource.local.impl

import com.sadashi.client.chatwork.infra.datasource.local.CodeVerifierLocalStore
import com.sadashi.client.chatwork.infra.preference.CodeVerifierPreference
import io.reactivex.Completable
import io.reactivex.Single

class CodeVerifierLocalStoreImpl(
    private val preference: CodeVerifierPreference
) : CodeVerifierLocalStore {
    override fun get(): Single<String> {
        return Single.create { source ->
            val codeVerifier = preference.get()
            if (codeVerifier.isNullOrEmpty()) {
                source.onError(Throwable("Stored code verifier is invalid."))
                preference.delete()
                return@create
            }
            source.onSuccess(codeVerifier)
        }
    }

    override fun put(codeVerifier: String): Completable {
        return Completable.fromCallable { preference.put(codeVerifier) }
    }

    override fun delete(): Completable = Completable.fromCallable { preference.delete() }
}