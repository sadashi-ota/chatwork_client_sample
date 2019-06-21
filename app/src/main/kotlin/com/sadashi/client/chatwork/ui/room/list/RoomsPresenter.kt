package com.sadashi.client.chatwork.ui.room.list

import com.sadashi.client.chatwork.domain.rooms.Room
import com.sadashi.client.chatwork.usecase.auth.DeleteAccessTokenUseCase
import com.sadashi.client.chatwork.usecase.auth.ExistsAccessTokenUseCase
import com.sadashi.client.chatwork.usecase.rooms.GetRoomsUseCase
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class RoomsPresenter(
    private val existsAccessTokenUseCase: ExistsAccessTokenUseCase,
    private val deleteAccessTokenUseCase: DeleteAccessTokenUseCase,
    private val getRoomUseCase: GetRoomsUseCase,
    private val uiScheduler: Scheduler
) : RoomsContract.Presentation {

    private lateinit var view: RoomsContract.View
    private lateinit var roomsTransition: RoomsTransition

    private val disposables = CompositeDisposable()

    override fun setUp(view: RoomsContract.View, roomsTransition: RoomsTransition) {
        this.view = view
        this.roomsTransition = roomsTransition
    }

    override fun terminate() {
        disposables.clear()
    }

    override fun onStart() {
        existsAccessTokenUseCase.execute()
            .doOnSubscribe { view.showProgress() }
            .observeOn(uiScheduler)
            .subscribe({ isLogin ->
                view.dismissProgress()
                when (isLogin) {
                    true -> loadRooms()
                    false -> roomsTransition.moveLoginPage()
                }
            }, { throwable ->
                view.showErrorDialog(throwable)
            })
            .addTo(disposables)
    }

    override fun logout() {
        deleteAccessTokenUseCase.execute()
            .subscribe { roomsTransition.moveLoginPage() }
            .addTo(disposables)
    }

    override fun onClickRoom(room: Room) {
        roomsTransition.moveRoomDetail(room)
    }

    private fun loadRooms() {
        getRoomUseCase.execute()
            .doOnSubscribe { view.showProgress() }
            .observeOn(uiScheduler)
            .subscribe({ rooms ->
                view.showRoomsList(rooms)
                view.dismissProgress()
            }, { throwable ->
                view.showErrorDialog(throwable)
                view.dismissProgress()
            })
            .addTo(disposables)
    }
}