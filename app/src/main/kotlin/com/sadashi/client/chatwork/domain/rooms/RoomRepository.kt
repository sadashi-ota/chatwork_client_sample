package com.sadashi.client.chatwork.domain.rooms

import com.sadashi.client.chatwork.domain.auth.AccessToken
import io.reactivex.Single

interface RoomRepository {
    fun getRooms(accessToken: AccessToken): Single<List<Room>>
}