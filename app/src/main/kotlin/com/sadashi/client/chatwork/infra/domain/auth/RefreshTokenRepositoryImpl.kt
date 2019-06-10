package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.RefreshToken
import com.sadashi.client.chatwork.domain.auth.RefreshTokenRepository
import com.sadashi.client.chatwork.infra.datasource.local.RefreshTokenLocalStore
import io.reactivex.Completable
import io.reactivex.Single

class RefreshTokenRepositoryImpl(
    private val localStore: RefreshTokenLocalStore
) : RefreshTokenRepository {
    override fun exists(): Single<Boolean> = localStore.get().map { it.isNotEmpty() }

    override fun find(): Single<RefreshToken> = localStore.get().map { RefreshToken(it) }

    override fun store(refreshToken: RefreshToken): Completable = localStore.put(refreshToken.value)

    override fun delete(): Completable = localStore.delete()
}