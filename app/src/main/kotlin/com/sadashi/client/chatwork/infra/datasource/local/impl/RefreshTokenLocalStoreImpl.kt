package com.sadashi.client.chatwork.infra.datasource.local.impl

import com.sadashi.client.chatwork.infra.datasource.local.RefreshTokenLocalStore
import com.sadashi.client.chatwork.infra.preference.RefreshTokenPreference
import io.reactivex.Completable
import io.reactivex.Single

class RefreshTokenLocalStoreImpl(
    private val preference: RefreshTokenPreference
): RefreshTokenLocalStore {
    override fun get(): Single<String> {
        return Single.create { source ->
            val refreshToken = preference.get()
            if (refreshToken is String) {
                source.onSuccess(refreshToken)
            } else {
                source.onError(Throwable("Stored refresh token is invalid."))
                delete()
            }
        }
    }

    override fun put(refreshToken: String): Completable {
        return Completable.fromCallable { preference.put(refreshToken) }
    }

    override fun delete(): Completable = Completable.fromCallable { preference.delete() }
}