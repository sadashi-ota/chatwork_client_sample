package com.sadashi.client.chatwork.ui.room.detail

import com.sadashi.client.chatwork.usecase.rooms.GetMembersUseCase
import com.sadashi.client.chatwork.usecase.rooms.GetMessagesUseCase
import com.sadashi.client.chatwork.usecase.rooms.GetRoomDetailUseCase

class RoomDetailPresenter(
    private val getRoomUseCase: GetRoomDetailUseCase,
    private val getMembersUseCase: GetMembersUseCase,
    private val getMessagesUseCase: GetMessagesUseCase
) : RoomDetailContract.Presentation {

    lateinit var view: RoomDetailContract.View
    lateinit var roomsTransition: RoomDetailTransition

    override fun setUp(view: RoomDetailContract.View, roomsTransition: RoomDetailTransition) {
        this.view = view
        this.roomsTransition = roomsTransition
    }

    override fun terminate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}