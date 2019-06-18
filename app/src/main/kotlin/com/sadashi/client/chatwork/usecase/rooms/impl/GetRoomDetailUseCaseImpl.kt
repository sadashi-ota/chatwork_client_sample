package com.sadashi.client.chatwork.usecase.rooms.impl

import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.usecase.rooms.GetRoomDetailUseCase
import io.reactivex.Single

class GetRoomDetailUseCaseImpl(
    private val roomRepository: RoomRepository
) : GetRoomDetailUseCase {
    override fun execute(roomId: RoomId): Single<Room> = roomRepository.getRoom(roomId)
}