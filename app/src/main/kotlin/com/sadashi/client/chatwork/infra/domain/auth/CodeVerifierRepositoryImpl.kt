package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.infra.datasource.local.CodeVerifierLocalStore
import io.reactivex.Completable
import io.reactivex.Single

class CodeVerifierRepositoryImpl(
    private val localStore: CodeVerifierLocalStore
) : CodeVerifierRepository {
    override fun exists(): Single<Boolean> = localStore.get().map { it.isNotEmpty() }

    override fun find(): Single<CodeVerifier> = localStore.get().map { CodeVerifier(it) }

    override fun store(codeVerifier: CodeVerifier): Completable = localStore.put(codeVerifier.value)

    override fun delete(): Completable = localStore.delete()
}