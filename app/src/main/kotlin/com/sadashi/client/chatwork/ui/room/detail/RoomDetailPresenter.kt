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
    private var isLoadingMessages = false

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
                    .doOnSubscribe{ view.showProgress() }
                    .observeOn(uiScheduler)
                    .doAfterTerminate { view.dismissProgress() }
                    .subscribe({ room ->
                        view.showRoomDetail(room)
                    }, { throwable ->
                        view.showErrorDialog(throwable)
                    })
            }, {
                view.showErrorDialog(it)
            })
            .addTo(disposables)
    }

    override fun loadFirstMessage() {
        disposables.isDisposed || let {
            disposables.dispose()
            true
        }

        loadMessages(true)
            .subscribe({ messages ->
                view.showMessages(messages)
            }, {
                view.showErrorDialog(it)
            })
            .addTo(disposables)
    }

    override fun loadNextMessage() {
        isLoadingMessages && return

        loadMessages(false)
            .subscribe({ messages ->
                view.showMessages(messages)
            }, {
                view.showErrorDialog(it)
            })
            .addTo(disposables)
    }

    override fun loadMembers() {
        getMembersUseCase.execute(roomId)
            .doOnSubscribe { view.showProgress() }
            .observeOn(uiScheduler)
            .doAfterTerminate { view.dismissProgress() }
            .subscribe({
                view.showMembers(it)
            }, {
                view.showErrorDialog(it)
            }).addTo(disposables)
    }

    private fun loadMessages(force: Boolean): Single<List<Message>> {
        return getMessagesUseCase.execute(roomId, force)
            .doOnSubscribe {
                view.showProgress()
                isLoadingMessages = true
            }
            .observeOn(uiScheduler)
            .doAfterTerminate {
                view.dismissProgress()
                isLoadingMessages = false
            }
    }
}