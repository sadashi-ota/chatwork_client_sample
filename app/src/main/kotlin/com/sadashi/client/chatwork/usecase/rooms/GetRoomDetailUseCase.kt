package com.sadashi.client.chatwork.usecase.rooms

import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId
import io.reactivex.Single

interface GetRoomDetailUseCase {
    fun execute(roomId: RoomId): Single<Room>
}