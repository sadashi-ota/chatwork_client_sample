package com.sadashi.client.chatwork.usecase.rooms

import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.RoomId
import io.reactivex.Single

interface GetMessagesUseCase {
    fun execute(roomId: RoomId, force: Boolean): Single<List<Message>>
}