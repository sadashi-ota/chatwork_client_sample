package com.sadashi.client.chatwork.infra.api

import com.sadashi.client.chatwork.BuildConfig

object RoomApiClientFactory {
    fun create(): RoomApiClient {
        return RetrofitFactory.provide(BuildConfig.API_DOMAIN)
            .create(RoomApiClient::class.java)
    }
}