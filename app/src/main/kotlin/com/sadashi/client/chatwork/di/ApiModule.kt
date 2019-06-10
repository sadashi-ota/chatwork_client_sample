package com.sadashi.client.chatwork.di

import com.sadashi.client.chatwork.domain.auth.AuthorizedTokenRepository
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

    fun getRoomApiClient(authorizedTokenRepository: AuthorizedTokenRepository): RoomApiClient {
        return roomApiInstance ?: run {
            RoomApiClientFactory.create(authorizedTokenRepository).also { roomApiInstance = it }
        }
    }
}