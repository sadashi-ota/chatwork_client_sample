package com.sadashi.client.chatwork.infra.datasource.local

import io.reactivex.Completable
import io.reactivex.Single

interface RefreshTokenLocalStore {
    fun get(): Single<String>
    fun put(refreshToken: String): Completable
    fun delete(): Completable
}