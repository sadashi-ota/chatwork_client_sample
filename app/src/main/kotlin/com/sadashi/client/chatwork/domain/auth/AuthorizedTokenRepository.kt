package com.sadashi.client.chatwork.domain.auth

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface AuthorizedTokenRepository {
    fun exists(): Single<Boolean>
    fun find(): Maybe<AuthorizedToken>
    fun store(authorizedToken: AuthorizedToken): Completable
    fun delete(): Completable
}