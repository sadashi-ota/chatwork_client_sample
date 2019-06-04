package com.sadashi.client.chatwork.infra.datasource.local

import io.reactivex.Completable
import io.reactivex.Single

interface AccessTokenLocalStore {
    fun get(): Single<String>
    fun put(accessToken: String): Completable
    fun delete(): Completable
}