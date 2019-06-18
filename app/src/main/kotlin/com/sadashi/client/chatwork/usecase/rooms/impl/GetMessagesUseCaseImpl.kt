package com.sadashi.client.chatwork.usecase.rooms.impl

import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.RoomId
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.usecase.rooms.GetMessagesUseCase
import io.reactivex.Single

class GetMessagesUseCaseImpl(
    private val roomRepository: RoomRepository
) : GetMessagesUseCase {
    override fun execute(roomId: RoomId, force: Boolean): Single<List<Message>> {
        return roomRepository.getMessages(roomId, force)
    }
}