package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AccessTokenRepository
import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.infra.datasource.local.AccessTokenLocalStore
import io.reactivex.Completable
import io.reactivex.Single

internal class AccessTokenRepositoryImpl(
    private val localStore: AccessTokenLocalStore
) : AccessTokenRepository {
    override fun exists(): Single<Boolean> = localStore.get().map { it.isNotEmpty() }

    override fun find(): Single<AccessToken> = localStore.get().map { AccessToken(it) }

    override fun store(accessToken: AccessToken): Completable = localStore.put(accessToken.value)

    override fun delete(): Completable = localStore.delete()
}