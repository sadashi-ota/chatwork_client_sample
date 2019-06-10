package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.AuthorizedTokenRepository
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

internal class AuthorizedTokenRepositoryImpl(
    private val localStore: AuthorizedTokenLocalStore
) : AuthorizedTokenRepository {
    override fun exists(): Single<Boolean> = localStore.get().isEmpty.map { !it }

    override fun find(): Maybe<AuthorizedToken> = localStore.get()

    override fun store(authorizedToken: AuthorizedToken): Completable {
        return localStore.put(authorizedToken)
    }

    override fun delete(): Completable = localStore.delete()
}