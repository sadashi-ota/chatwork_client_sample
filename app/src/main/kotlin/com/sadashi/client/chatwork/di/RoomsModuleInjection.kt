package com.sadashi.client.chatwork.di

import android.content.Context
import com.sadashi.client.chatwork.domain.auth.impl.AuthorizeServiceImpl
import com.sadashi.client.chatwork.domain.rooms.RoomRepository
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.api.RoomApiClient
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.impl.AuthorizedTokenLocalStoreImpl
import com.sadashi.client.chatwork.infra.domain.auth.AuthorizeRepositoryImpl
import com.sadashi.client.chatwork.infra.domain.room.RoomRepositoryImpl
import com.sadashi.client.chatwork.infra.preference.AuthorizedTokenPreference
import com.sadashi.client.chatwork.ui.room.list.RoomsContract
import com.sadashi.client.chatwork.ui.room.list.RoomsPresenter
import com.sadashi.client.chatwork.usecase.auth.DeleteAccessTokenUseCase
import com.sadashi.client.chatwork.usecase.auth.ExistsAccessTokenUseCase
import com.sadashi.client.chatwork.usecase.auth.impl.DeleteAccessTokenUseCaseImpl
import com.sadashi.client.chatwork.usecase.auth.impl.ExistsAccessTokenUseCaseImpl
import com.sadashi.client.chatwork.usecase.rooms.GetRoomsUseCase
import com.sadashi.client.chatwork.usecase.rooms.impl.GetRoomsUseCaseImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RoomsModuleInjection(
    private val context: Context
) {
    private val authApiClient: AuthApiClient = ApiModule.getAuthApiClient()

    private val authorizedTokenLocalStore: AuthorizedTokenLocalStore
        get() {
            return AuthorizedTokenLocalStoreImpl(AuthorizedTokenPreference(context))
        }

    private val authorizeRepository: AuthorizeRepositoryImpl
        get() {
            return AuthorizeRepositoryImpl(authApiClient, authorizedTokenLocalStore, Schedulers.io())
        }

    private val roomApiClient: RoomApiClient = ApiModule.getRoomApiClient(authorizedTokenLocalStore)

    private val roomRepository: RoomRepository = RoomRepositoryImpl(roomApiClient, Schedulers.io())
    private val getRoomsUseCase: GetRoomsUseCase = GetRoomsUseCaseImpl(roomRepository)

    private val existsAccessTokenUseCase: ExistsAccessTokenUseCase
        get() {
            return ExistsAccessTokenUseCaseImpl(authorizeRepository)
        }

    private val deleteAccessTokenUseCase: DeleteAccessTokenUseCase
        get() {
            return DeleteAccessTokenUseCaseImpl(authorizeRepository)
        }

    fun getPresenter(): RoomsContract.Presentation {
        return RoomsPresenter(
            existsAccessTokenUseCase,
            deleteAccessTokenUseCase,
            getRoomsUseCase,
            AndroidSchedulers.mainThread()
        )
    }
}