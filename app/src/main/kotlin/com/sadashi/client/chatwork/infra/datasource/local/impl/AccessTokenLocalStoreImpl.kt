package com.sadashi.client.chatwork.infra.datasource.local.impl

import com.sadashi.client.chatwork.infra.datasource.local.AccessTokenLocalStore
import com.sadashi.client.chatwork.infra.preference.AccessTokenPreference
import io.reactivex.Completable
import io.reactivex.Single

class AccessTokenLocalStoreImpl(
    private val preference: AccessTokenPreference
) : AccessTokenLocalStore {
    override fun get(): Single<String> {
        return Single.create { source ->
            val accessToken = preference.get()
            if (accessToken is String) {
                source.onSuccess(accessToken)
            } else {
                source.onError(Throwable("Stored access token is invalid."))
                delete()
            }
        }
    }

    override fun put(accessToken: String): Completable {
        return Completable.fromCallable { preference.put(accessToken) }
    }

    override fun delete(): Completable = Completable.fromCallable { preference.delete() }
}