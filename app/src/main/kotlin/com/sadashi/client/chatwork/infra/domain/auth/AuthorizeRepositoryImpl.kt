package com.sadashi.client.chatwork.infra.domain.auth

import com.sadashi.client.chatwork.domain.auth.AuthorizeRepository
import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

class AuthorizeRepositoryImpl(
    private val apiClient: AuthApiClient,
    private val localStore: AuthorizedTokenLocalStore,
    private val ioScheduler: Scheduler
) : AuthorizeRepository {

    override fun executeAuthorize(
        code: String,
        codeVerifier: CodeVerifier
    ): Completable {
        return Completable.create { emitter ->
            apiClient.getToken(code = code, codeVerifier = codeVerifier.value)
                .map { AuthorizedTokenConverter.convertToDomainModel(it) }
                .subscribe({ token ->
                    localStore.put(token).subscribe({
                        emitter.onComplete()
                    }, { throwable ->
                        emitter.onError(throwable)
                    })
                }, { throwable ->
                    emitter.onError(throwable)
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
                    .flatMap {
                        localStore.put(it).toSingle { it }
                    }
            } else {
                Single.create<AuthorizedToken> {
                    it.onSuccess(token)
                }
            }
        }.subscribeOn(ioScheduler)
    }

    override fun deleteToken(): Completable = localStore.delete()
}