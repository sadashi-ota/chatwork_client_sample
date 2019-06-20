package com.sadashi.client.chatwork.ui.room.detail

import com.sadashi.client.chatwork.domain.rooms.Message
import com.sadashi.client.chatwork.domain.rooms.RoomId
import com.sadashi.client.chatwork.usecase.rooms.GetMembersUseCase
import com.sadashi.client.chatwork.usecase.rooms.GetMessagesUseCase
import com.sadashi.client.chatwork.usecase.rooms.GetRoomDetailUseCase
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class RoomDetailPresenter(
    private val getRoomUseCase: GetRoomDetailUseCase,
    private val getMembersUseCase: GetMembersUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val uiScheduler: Scheduler
) : RoomDetailContract.Presentation {

    lateinit var view: RoomDetailContract.View
    lateinit var roomsTransition: RoomDetailTransition
    lateinit var roomId: RoomId

    private val disposables = CompositeDisposable()

    override fun setUp(view: RoomDetailContract.View, roomsTransition: RoomDetailTransition) {
        this.view = view
        this.roomsTransition = roomsTransition
    }

    override fun terminate() {
        disposables.clear()
    }

    override fun onStart(roomId: RoomId) {
        this.roomId = roomId
        loadMessages(true)
            .subscribe({ messages ->
                view.showMessages(messages)
                getRoomUseCase.execute(roomId)
                    .observeOn(uiScheduler)
                    .subscribe({ room ->
                        view.dismissProgress()
                        view.showRoomDetail(room)
                    }, { throwable ->
                        view.dismissProgress()
                        view.showErrorDialog(throwable)
                    })
            }, {
                view.dismissProgress()
                view.showErrorDialog(it)
            })
            .addTo(disposables)
    }

    override fun loadFirstMessage() {
        loadMessages(true)
            .subscribe({ messages ->
                view.dismissProgress()
                view.showMessages(messages)
            }, {
                view.dismissProgress()
                view.showErrorDialog(it)
            })
            .addTo(disposables)
    }

    override fun loadNextMessage() {
        loadMessages(false)
            .subscribe({ messages ->
                view.dismissProgress()
                view.showMessages(messages)
            }, {
                view.dismissProgress()
                view.showErrorDialog(it)
            })
            .addTo(disposables)
    }

    override fun loadMembers() {
        getMembersUseCase.execute(roomId)
            .doOnSubscribe { view.showProgress() }
            .observeOn(uiScheduler)
            .subscribe({
                view.dismissProgress()
                view.showMembers(it)
            }, {
                view.dismissProgress()
                view.showErrorDialog(it)
            }).addTo(disposables)
    }

    private fun loadMessages(force: Boolean): Single<List<Message>> {
        return getMessagesUseCase.execute(roomId, force)
            .doOnSubscribe { view.showProgress() }
            .observeOn(uiScheduler)
    }
}