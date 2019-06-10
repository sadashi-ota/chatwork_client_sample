package com.sadashi.client.chatwork.domain.auth

import io.reactivex.Completable
import io.reactivex.Single

interface RefreshTokenRepository {
    fun exists(): Single<Boolean>
    fun find(): Single<RefreshToken>
    fun store(refreshToken: RefreshToken): Completable
    fun delete(): Completable
}