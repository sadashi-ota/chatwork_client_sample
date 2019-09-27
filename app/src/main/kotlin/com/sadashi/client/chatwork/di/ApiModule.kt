package com.sadashi.client.chatwork.di

import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.api.AuthApiClientFactory
import com.sadashi.client.chatwork.infra.api.RoomApiClient
import com.sadashi.client.chatwork.infra.api.RoomApiClientFactory

object ApiModule {

    private var authApiInstance: AuthApiClient? = null
    private var roomApiInstance: RoomApiClient? = null

    fun getAuthApiClient(): AuthApiClient {
        return authApiInstance ?: run {
            AuthApiClientFactory.create().also { authApiInstance = it }
        }
    }

    fun getRoomApiClient(): RoomApiClient {
        return roomApiInstance ?: run {
            RoomApiClientFactory.create().also { roomApiInstance = it }
        }
    }
}