package com.sadashi.client.chatwork.domain.auth

import io.reactivex.Completable
import io.reactivex.Single

interface CodeVerifierRepository {
    fun exists(): Single<Boolean>
    fun find(): Single<CodeVerifier>
    fun store(codeVerifier: CodeVerifier): Completable
    fun delete(): Completable
}