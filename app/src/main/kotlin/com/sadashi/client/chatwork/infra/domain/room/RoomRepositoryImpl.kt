package com.sadashi.client.chatwork.infra.domain.room

import com.sadashi.client.chatwork.domain.auth.AccessToken
import com.sadashi.client.chatwork.domain.rooms.Account
import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.infra.api.RoomApiClient
import io.reactivex.Scheduler
import io.reactivex.Single

class RoomRepositoryImpl(
    private val apiClient: RoomApiClient,
    private val ioScheduler: Scheduler
) : RoomRepository {
    override fun getRooms(): Single<List<Room>> {
        return apiClient.getRooms()
            .map { RoomConverter.convertToDomainModelFromList(it) }
            .subscribeOn(ioScheduler)
    }

    override fun getRoom(roomId: RoomId): Single<Room> {
        return apiClient.getRoom(roomId = roomId.value)
            .map { RoomConverter.convertToDomainModel(it) }
            .subscribeOn(ioScheduler)
    }

    override fun getMessages(roomId: RoomId, force: Boolean): Single<List<Message>> {
        val forceInt = if (force) { 1 } else { 0 }

        return apiClient.getMessages(roomId = roomId.value, force = forceInt)
            .map { MessageConverter.convertToDomainModelFromList(it) }
            .subscribeOn(ioScheduler)
    }

    override fun getMembers(roomId: RoomId): Single<List<Account>> {
        return apiClient.getMembers(roomId = roomId.value)
            .map { AccountConverter.convertToDomainModelFromList(it) }
            .subscribeOn(ioScheduler)
    }

}