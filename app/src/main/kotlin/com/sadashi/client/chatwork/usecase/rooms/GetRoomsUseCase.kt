package com.sadashi.client.chatwork.usecase.rooms

import com.sadashi.client.chatwork.domain.rooms.Room
import io.reactivex.Single

interface GetRoomsUseCase {
    fun execute(): Single<List<Room>>
}