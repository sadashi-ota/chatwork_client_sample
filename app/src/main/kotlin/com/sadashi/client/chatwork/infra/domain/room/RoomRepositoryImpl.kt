package com.sadashi.client.chatwork.infra.domain.room

import com.sadashi.client.chatwork.domain.auth.AuthorizeRepository
import com.sadashi.client.chatwork.domain.rooms.Account
import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.infra.api.RoomApiClient
import io.reactivex.Scheduler
import io.reactivex.Single

class RoomRepositoryImpl(
    private val authorizeRepository: AuthorizeRepository,
    private val apiClient: RoomApiClient,
    private val ioScheduler: Scheduler
) : RoomRepository {
    override fun getRooms(): Single<List<Room>> {
        return authorizeRepository.getToken().flatMap { token ->
            apiClient.getRooms(token.accessTokenString)
                .map { response ->
                    RoomConverter.convertToDomainModelFromList(response)
                }
        }.subscribeOn(ioScheduler)
    }

    override fun getRoom(roomId: RoomId): Single<Room> {
        return authorizeRepository.getToken().flatMap { token ->
            apiClient.getRoom(
                authorization = token.accessTokenString,
                roomId = roomId.value
            ).map { response ->
                RoomConverter.convertToDomainModel(response)
            }
        }.subscribeOn(ioScheduler)
    }

    override fun getMessages(roomId: RoomId, force: Boolean): Single<List<Message>> {
        val forceInt = if (force) {
            1
        } else {
            0
        }

        return authorizeRepository.getToken().flatMap { token ->
            apiClient.getMessages(
                authorization = token.accessTokenString,
                roomId = roomId.value,
                force = forceInt
            ).map { response ->
                MessageConverter.convertToDomainModelFromList(response)
            }
        }.subscribeOn(ioScheduler)
    }

    override fun getMembers(roomId: RoomId): Single<List<Account>> {
        return authorizeRepository.getToken().flatMap { token ->
            apiClient.getMembers(
                authorization = token.accessTokenString,
                roomId = roomId.value
            ).map { response ->
                AccountConverter.convertToDomainModelFromList(response)
            }
        }.subscribeOn(ioScheduler)
    }

}