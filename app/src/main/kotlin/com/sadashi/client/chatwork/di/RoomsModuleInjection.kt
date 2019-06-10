package com.sadashi.client.chatwork.di

import android.content.Context
import com.sadashi.client.chatwork.domain.auth.AuthorizedTokenRepository
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.infra.api.RoomApiClient
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.impl.AuthorizedTokenLocalStoreImpl
import com.sadashi.client.chatwork.infra.domain.auth.AuthorizedTokenRepositoryImpl
import com.sadashi.client.chatwork.infra.domain.room.RoomRepositoryImpl
import com.sadashi.client.chatwork.infra.preference.AuthorizedTokenPreference
import com.sadashi.client.chatwork.ui.rooms.RoomsContract
import com.sadashi.client.chatwork.ui.rooms.RoomsPresenter
import com.sadashi.client.chatwork.usecase.auth.ExistsAccessTokenUseCase
import com.sadashi.client.chatwork.usecase.auth.impl.ExistsAccessTokenUseCaseImpl
import com.sadashi.client.chatwork.usecase.rooms.GetRoomsUseCase
import com.sadashi.client.chatwork.usecase.rooms.impl.GetRoomsUseCaseImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RoomsModuleInjection(
    private val context: Context
) {
    private val authorizedTokenLocalStore: AuthorizedTokenLocalStore
        get() {
            return AuthorizedTokenLocalStoreImpl(AuthorizedTokenPreference(context))
        }

    private val authorizedTokenRepository: AuthorizedTokenRepository
        get() {
            return AuthorizedTokenRepositoryImpl(authorizedTokenLocalStore)
        }

    private val roomApiClient: RoomApiClient = ApiModule.getRoomApiClient(authorizedTokenRepository)

    private val roomRepository: RoomRepository = RoomRepositoryImpl(roomApiClient, Schedulers.io())
    private val getRoomsUseCase: GetRoomsUseCase
        get() {
            return GetRoomsUseCaseImpl(roomRepository, authorizedTokenRepository)
        }

    private val existsAccessTokenUseCase: ExistsAccessTokenUseCase
        get() {
            return ExistsAccessTokenUseCaseImpl(authorizedTokenRepository)
        }

    fun getPresenter(): RoomsContract.Presentation {
        return RoomsPresenter(existsAccessTokenUseCase, getRoomsUseCase, AndroidSchedulers.mainThread())
    }
}