package com.sadashi.client.chatwork.ui.room.list

import com.sadashi.client.chatwork.domain.rooms.Room

interface RoomsTransition {
    fun moveLoginPage()
    fun moveRoomDetail(room: Room)
}