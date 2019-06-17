package com.sadashi.client.chatwork.ui.room.list

import com.sadashi.client.chatwork.domain.rooms.Room

interface RoomsContract {
    interface Presentation {
        fun setUp(view: View, roomsTransition: RoomsTransition)
        fun terminate()
        fun onStart()
        fun logout()
    }

    interface View {
        fun showRoomsList(rooms: List<Room>)
        fun clearRoomsList()
        fun showProgress()
        fun dismissProgress()
        fun showErrorDialog(throwable: Throwable)
    }
}