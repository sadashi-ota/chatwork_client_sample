package com.sadashi.client.chatwork.infra.datasource.local

import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import io.reactivex.Completable
import io.reactivex.Maybe

interface AuthorizedTokenLocalStore {
    fun get(): Maybe<AuthorizedToken>
    fun put(authorizedToken: AuthorizedToken): Completable
    fun delete(): Completable
}