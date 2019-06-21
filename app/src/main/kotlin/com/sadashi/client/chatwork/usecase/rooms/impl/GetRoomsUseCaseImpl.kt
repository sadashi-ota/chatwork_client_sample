package com.sadashi.client.chatwork.usecase.rooms.impl

import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.usecase.rooms.GetRoomsUseCase
import io.reactivex.Single

class GetRoomsUseCaseImpl(
    private val roomRepository: RoomRepository
) : GetRoomsUseCase {
    override fun execute(): Single<List<Room>> = roomRepository.getRooms()
}