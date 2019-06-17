package com.sadashi.client.chatwork.domain.rooms

import io.reactivex.Single

interface RoomRepository {
    fun getRooms(): Single<List<Room>>
    fun getRoom(roomId: RoomId): Single<Room>
    fun getMessages(roomId: RoomId, force: Boolean): Single<List<Message>>
    fun getMembers(roomId: RoomId): Single<List<Account>>
}