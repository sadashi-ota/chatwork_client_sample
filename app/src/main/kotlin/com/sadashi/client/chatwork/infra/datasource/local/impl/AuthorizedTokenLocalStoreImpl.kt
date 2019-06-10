package com.sadashi.client.chatwork.infra.datasource.local.impl

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.auth.AuthorizedToken
import com.sadashi.client.chatwork.domain.auth.RefreshToken
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.json.AuthorizedTokenJson
import com.sadashi.client.chatwork.infra.preference.AuthorizedTokenPreference
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Maybe
import java.util.Date

class AuthorizedTokenLocalStoreImpl(
    private val preference: AuthorizedTokenPreference
) : AuthorizedTokenLocalStore {
    override fun get(): Maybe<AuthorizedToken> {
        return Maybe.create { source ->
            val authorizedTokenJson = preference.get() ?: run {
                source.onComplete()
                return@create
            }

            val token = convertFromJson(authorizedTokenJson) ?: run {
                source.onError(Throwable("Stored access token is invalid."))
                delete()
                return@create
            }

            source.onSuccess(token)
        }
    }

    override fun put(authorizedToken: AuthorizedToken): Completable {
        return Completable.fromCallable { preference.put(convertToJson(authorizedToken)) }
    }

    override fun delete(): Completable = Completable.fromCallable { preference.delete() }

    private fun convertToJson(authorizedToken: AuthorizedToken): String {
        return Moshi.Builder().build().adapter(AuthorizedTokenJson::class.java)
            .toJson(
                AuthorizedTokenJson(
                    accessToken = authorizedToken.accessToken.value,
                    refreshToken = authorizedToken.refreshToken.value,
                    expiredTime = authorizedToken.expiredTime.time,
                    tokenType = authorizedToken.tokenType,
                    scope = authorizedToken.scope
                )
            )
    }

    private fun convertFromJson(json: String): AuthorizedToken? {
        val jsonObject = Moshi.Builder().build().adapter(AuthorizedTokenJson::class.java)
            .fromJson(json) ?: return null

        return AuthorizedToken(
            accessToken = AccessToken(jsonObject.accessToken),
            refreshToken = RefreshToken(jsonObject.refreshToken),
            expiredTime = Date(jsonObject.expiredTime),
            tokenType = jsonObject.tokenType,
            scope = jsonObject.scope
        )
    }
}