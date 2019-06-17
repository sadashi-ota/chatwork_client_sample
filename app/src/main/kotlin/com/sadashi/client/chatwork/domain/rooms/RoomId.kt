package com.sadashi.client.chatwork.domain.rooms

import com.sadashi.client.chatwork.core.domain.Identifier

class RoomId(roomId: Int) : Identifier<Int>(roomId) {
    companion object {
        const val INVALID_ID = -1
    }

    fun isValid() = (value == INVALID_ID)
}

