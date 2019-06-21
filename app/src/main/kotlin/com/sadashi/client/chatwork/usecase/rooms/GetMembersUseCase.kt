package com.sadashi.client.chatwork.usecase.rooms

import com.sadashi.client.chatwork.domain.rooms.Account
import com.sadashi.client.chatwork.domain.rooms.RoomId
import io.reactivex.Single

interface GetMembersUseCase {
    fun execute(roomId: RoomId): Single<List<Account>>
}