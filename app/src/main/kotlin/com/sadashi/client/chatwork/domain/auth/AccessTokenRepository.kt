package com.sadashi.client.chatwork.domain.auth

import io.reactivex.Completable
import io.reactivex.Single

interface AccessTokenRepository {
    fun exists(): Single<Boolean>
    fun find(): Single<AccessToken>
    fun store(accessToken: AccessToken): Completable
    fun delete(): Completable
}