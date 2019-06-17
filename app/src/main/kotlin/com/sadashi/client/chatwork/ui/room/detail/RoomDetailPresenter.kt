package com.sadashi.client.chatwork.ui.room.detail

class RoomDetailPresenter(
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

    override fun logout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}