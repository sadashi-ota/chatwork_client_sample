package com.sadashi.client.chatwork.di

import android.content.Context
import com.sadashi.client.chatwork.domain.auth.AuthorizeRepository
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.api.RoomApiClient
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.CodeVerifierLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.impl.AuthorizedTokenLocalStoreImpl
import com.sadashi.client.chatwork.infra.datasource.local.impl.CodeVerifierLocalStoreImpl
import com.sadashi.client.chatwork.infra.domain.auth.AuthorizeRepositoryImpl
import com.sadashi.client.chatwork.infra.domain.room.RoomRepositoryImpl
import com.sadashi.client.chatwork.infra.preference.AuthorizedTokenPreference
import com.sadashi.client.chatwork.infra.preference.CodeVerifierPreference
import com.sadashi.client.chatwork.ui.room.detail.RoomDetailContract
import com.sadashi.client.chatwork.ui.room.detail.RoomDetailPresenter
import com.sadashi.client.chatwork.usecase.rooms.GetMembersUseCase
import com.sadashi.client.chatwork.usecase.rooms.GetMessagesUseCase
import com.sadashi.client.chatwork.usecase.rooms.GetRoomDetailUseCase
import com.sadashi.client.chatwork.usecase.rooms.impl.GetMembersUseCaseImpl
import com.sadashi.client.chatwork.usecase.rooms.impl.GetMessagesUseCaseImpl
import com.sadashi.client.chatwork.usecase.rooms.impl.GetRoomDetailUseCaseImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RoomDetailModuleInjection(val context: Context) {

    private val authApiClient: AuthApiClient = ApiModule.getAuthApiClient()

    private val accessTokenLocalStore: AuthorizedTokenLocalStore
        get() {
            return AuthorizedTokenLocalStoreImpl(AuthorizedTokenPreference(context))
        }

    private val authorizeRepository: AuthorizeRepository
        get() {
            return AuthorizeRepositoryImpl(authApiClient, accessTokenLocalStore, Schedulers.io())
        }

    private val roomApiClient: RoomApiClient = ApiModule.getRoomApiClient()

    private val roomRepository: RoomRepository = RoomRepositoryImpl(
        authorizeRepository,
        roomApiClient,
        Schedulers.io()
    )

    private val getRoomUseCase: GetRoomDetailUseCase = GetRoomDetailUseCaseImpl(roomRepository)
    private val getMembersUseCase: GetMembersUseCase = GetMembersUseCaseImpl(roomRepository)
    private val getMessagesUseCase: GetMessagesUseCase = GetMessagesUseCaseImpl(roomRepository)

    fun getPresenter(): RoomDetailContract.Presentation {
        return RoomDetailPresenter(
            getRoomUseCase,
            getMembersUseCase,
            getMessagesUseCase,
            AndroidSchedulers.mainThread()
        )
    }
}