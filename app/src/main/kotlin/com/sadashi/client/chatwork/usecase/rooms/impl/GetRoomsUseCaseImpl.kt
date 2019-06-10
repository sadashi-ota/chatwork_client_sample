package com.sadashi.client.chatwork.usecase.rooms.impl

import com.sadashi.client.chatwork.domain.auth.AuthorizedTokenRepository
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.usecase.rooms.GetRoomsUseCase
import io.reactivex.Single

class GetRoomsUseCaseImpl(
    private val roomRepository: RoomRepository,
    private val authorizedTokenRepository: AuthorizedTokenRepository
) : GetRoomsUseCase {
    override fun execute(): Single<List<Room>> {
        return authorizedTokenRepository.find().flatMapSingle { authorizedToken ->
            roomRepository.getRooms(authorizedToken.accessToken)
        }
    }
}