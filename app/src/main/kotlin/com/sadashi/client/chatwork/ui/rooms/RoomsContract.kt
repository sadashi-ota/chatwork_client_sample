package com.sadashi.client.chatwork.ui.rooms

import com.sadashi.client.chatwork.domain.rooms.Room

interface RoomsContract {
    interface Presentation {
        fun setUp(view: View, roomsTransition: RoomsTransition)
        fun terminate()
        fun onStart()
    }

    interface View {
        fun showRoomsList(rooms: List<Room>)
        fun clearRoomsList()
        fun showProgress()
        fun dismissProgress()
        fun showErrorDialog(throwable: Throwable)
    }
}