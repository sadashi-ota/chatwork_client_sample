package com.sadashi.client.chatwork.ui.room.detail

import com.sadashi.client.chatwork.domain.rooms.Account
import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.domain.rooms.RoomId

interface RoomDetailContract {
    interface Presentation {
        fun setUp(view: View, roomsTransition: RoomDetailTransition)
        fun terminate()
        fun onStart(roomId: RoomId)
        fun loadFirstMessage()
        fun loadNextMessage()
        fun loadMembers()
    }

    interface View {
        fun showRoomDetail(room: Room)
        fun showMessages(messages: List<Message>)
        fun showMembers(members: List<Account>)
        fun showProgress()
        fun dismissProgress()
        fun showErrorDialog(throwable: Throwable)
    }
}