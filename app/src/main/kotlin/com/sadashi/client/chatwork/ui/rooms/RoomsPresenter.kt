package com.sadashi.client.chatwork.ui.rooms

import com.sadashi.client.chatwork.usecase.auth.ExistsAccessTokenUseCase
import com.sadashi.client.chatwork.usecase.rooms.GetRoomsUseCase
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class RoomsPresenter(
    private val existsAccessTokenUseCase: ExistsAccessTokenUseCase,
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
            .doOnDispose { view.dismissProgress() }
            .subscribe({ isLogin ->
                when (isLogin) {
                    true -> loadRooms()
                    false -> roomsTransition.moveLoginPage()
                }
            }, { throwable ->
                view.showErrorDialog(throwable)
            })
            .addTo(disposables)
    }

    private fun loadRooms() {
        getRoomUseCase.execute()
            .doOnSubscribe { view.showProgress() }
            .observeOn(uiScheduler)
            .doOnDispose { view.dismissProgress() }
            .subscribe({ rooms ->
                view.showRoomsList(rooms)
            }, { throwable ->
                view.showErrorDialog(throwable)
            })
            .addTo(disposables)
    }
}