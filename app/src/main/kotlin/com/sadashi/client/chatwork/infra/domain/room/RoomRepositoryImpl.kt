package com.sadashi.client.chatwork.infra.domain.room

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.infra.api.RoomApiClient
import io.reactivex.Scheduler
import io.reactivex.Single

class RoomRepositoryImpl(
    private val apiClient: RoomApiClient,
    private val ioScheduler: Scheduler
) : RoomRepository {
    override fun getRooms(accessToken: AccessToken): Single<List<Room>> {
        return apiClient.getRooms(token = accessToken.value)
            .map { RoomConverter.convertToDomainModelFromList(it) }
            .subscribeOn(ioScheduler)
    }
}