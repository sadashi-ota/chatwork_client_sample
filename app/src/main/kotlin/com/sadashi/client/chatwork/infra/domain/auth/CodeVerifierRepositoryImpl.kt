package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.infra.datasource.local.CodeVerifierLocalStore
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

class CodeVerifierRepositoryImpl(
    private val localStore: CodeVerifierLocalStore,
    private val ioScheduler: Scheduler
) : CodeVerifierRepository {
    override fun exists(): Single<Boolean> {
        return localStore.get().subscribeOn(ioScheduler).map { it.isNotEmpty() }
    }

    override fun find(): Single<CodeVerifier> {
        return localStore.get().subscribeOn(ioScheduler).map { CodeVerifier(it) }
    }

    override fun store(codeVerifier: CodeVerifier): Completable {
        return localStore.put(codeVerifier.value).subscribeOn(ioScheduler)
    }

    override fun delete(): Completable {
        return localStore.delete().subscribeOn(ioScheduler)
    }
}