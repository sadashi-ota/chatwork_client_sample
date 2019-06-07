package com.sadashi.client.chatwork.di

import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.api.AuthApiClientFactory
import com.sadashi.client.chatwork.infra.api.RoomsApiClient
import com.sadashi.client.chatwork.infra.api.RoomsApiClientFactory

object ApiModule {

    private var AUTH_API_INSTANCE: AuthApiClient? = null
    private var ROOMS_API_INSTANCE: RoomsApiClient? = null

    fun getAuthApiClient(): AuthApiClient {
        return AUTH_API_INSTANCE ?: run {
            AuthApiClientFactory.create().also { AUTH_API_INSTANCE = it }
        }
    }

    fun getRoomsApiClient(): RoomsApiClient {
        return ROOMS_API_INSTANCE ?: run {
            RoomsApiClientFactory.create().also { ROOMS_API_INSTANCE = it }
        }
    }
}