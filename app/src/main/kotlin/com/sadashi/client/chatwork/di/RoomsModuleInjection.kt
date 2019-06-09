package com.sadashi.client.chatwork.di

import android.content.Context
import com.sadashi.client.chatwork.domain.auth.AccessTokenRepository
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.infra.api.RoomApiClient
import com.sadashi.client.chatwork.infra.datasource.local.AccessTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.impl.AccessTokenLocalStoreImpl
import com.sadashi.client.chatwork.infra.domain.auth.AccessTokenRepositoryImpl
import com.sadashi.client.chatwork.infra.domain.room.RoomRepositoryImpl
import com.sadashi.client.chatwork.infra.preference.AccessTokenPreference
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
    private val accessTokenLocalStore: AccessTokenLocalStore
        get() {
            return AccessTokenLocalStoreImpl(AccessTokenPreference(context))
        }

    private val accessTokenRepository: AccessTokenRepository
        get() {
            return AccessTokenRepositoryImpl(accessTokenLocalStore)
        }

    private val roomApiClient: RoomApiClient = ApiModule.getRoomApiClient(accessTokenRepository)

    private val roomRepository: RoomRepository = RoomRepositoryImpl(roomApiClient, Schedulers.io())
    private val getRoomsUseCase: GetRoomsUseCase
        get() {
            return GetRoomsUseCaseImpl(roomRepository, accessTokenRepository)
        }

    private val existsAccessTokenUseCase: ExistsAccessTokenUseCase
        get() {
            return ExistsAccessTokenUseCaseImpl(accessTokenRepository)
        }

    fun getPresenter(): RoomsContract.Presentation {
        return RoomsPresenter(existsAccessTokenUseCase, getRoomsUseCase, AndroidSchedulers.mainThread())
    }
}