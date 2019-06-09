package com.sadashi.client.chatwork.infra.datasource.local

import io.reactivex.Completable
import io.reactivex.Single

interface CodeVerifierLocalStore {
    fun get(): Single<String>
    fun put(codeVerifier: String): Completable
    fun delete(): Completable
}