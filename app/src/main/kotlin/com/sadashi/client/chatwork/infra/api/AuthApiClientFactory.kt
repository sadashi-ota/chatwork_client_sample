package com.sadashi.client.chatwork.infra.api

import com.sadashi.client.chatwork.BuildConfig

object AuthApiClientFactory {
    fun create(): AuthApiClient {
        return RetrofitFactory.provide(BuildConfig.AUTH_DOMAIN)
            .create(AuthApiClient::class.java)
    }
}