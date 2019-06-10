package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

class AuthorizeServiceImpl(
    private val apiClient: AuthApiClient,
    private val localStore: AuthorizedTokenLocalStore,
    private val ioScheduler: Scheduler
) : AuthorizeService {

    override fun execute(
        code: String,
        codeVerifier: CodeVerifier
    ): Completable {
        return Completable.create { emitter ->
            apiClient.getToken(code = code, codeVerifier = codeVerifier.value)
                .map { AuthorizedTokenConverter.convertToDomainModel(it) }
                .subscribe({
                    localStore.put(it)
                    emitter.onComplete()
                }, {
                    emitter.onError(it)
                })
        }.subscribeOn(ioScheduler)
    }

    override fun existsToken(): Single<Boolean> {
        return localStore.get().isEmpty.map { !it }
    }

    override fun getToken(): Single<AuthorizedToken> {
        return localStore.get().flatMapSingle { token ->
            if (token.isExpired()) {
                apiClient.refreshToken(refreshToken = token.refreshToken.value)
                    .map { AuthorizedTokenConverter.convertToDomainModel(it) }
                    .doOnSuccess { localStore.put(it) }
            } else {
                Single.create<AuthorizedToken> {
                    it.onSuccess(token)
                }
            }
        }.subscribeOn(ioScheduler)
    }
}