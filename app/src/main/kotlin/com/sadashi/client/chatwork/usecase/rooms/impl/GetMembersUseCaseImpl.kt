package com.sadashi.client.chatwork.usecase.rooms.impl

import com.sadashi.client.chatwork.domain.rooms.Account
import com.sadashi.client.chatwork.domain.rooms.RoomId
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.usecase.rooms.GetMembersUseCase
import io.reactivex.Single

class GetMembersUseCaseImpl(
    private val roomRepository: RoomRepository
) : GetMembersUseCase {
    override fun execute(roomId: RoomId): Single<List<Account>> = roomRepository.getMembers(roomId)
}