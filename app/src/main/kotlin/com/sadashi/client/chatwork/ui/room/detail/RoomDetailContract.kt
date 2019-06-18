package com.sadashi.client.chatwork.ui.room.detail

import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.Room

interface RoomDetailContract {
    interface Presentation {
        fun setUp(view: View, roomsTransition: RoomDetailTransition)
        fun terminate()
        fun onStart()
    }

    interface View {
        fun showRoomDetail(room: Room)
        fun showMessages(messages: List<Message>)
        fun showProgress()
        fun dismissProgress()
        fun showErrorDialog(throwable: Throwable)
    }
}