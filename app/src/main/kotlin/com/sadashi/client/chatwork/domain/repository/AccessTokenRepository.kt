package com.sadashi.client.chatwork.domain.repository

import com.sadashi.client.chatwork.domain.vo.AccessToken
import io.reactivex.Completable
import io.reactivex.Single

interface AccessTokenRepository {
    fun exists(): Single<Boolean>
    fun find(): Single<AccessToken>
    fun store(accessToken: AccessToken): Completable
    fun delete(): Completable
}