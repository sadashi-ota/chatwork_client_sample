package com.sadashi.client.chatwork.di

import android.content.Context
import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.CodeVerifierLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.impl.AuthorizedTokenLocalStoreImpl
import com.sadashi.client.chatwork.infra.datasource.local.impl.CodeVerifierLocalStoreImpl
import com.sadashi.client.chatwork.infra.domain.auth.AuthorizeServiceImpl
import com.sadashi.client.chatwork.infra.domain.auth.CodeVerifierRepositoryImpl
import com.sadashi.client.chatwork.infra.preference.AuthorizedTokenPreference
import com.sadashi.client.chatwork.infra.preference.CodeVerifierPreference
import com.sadashi.client.chatwork.ui.login.LoginContract
import com.sadashi.client.chatwork.ui.login.LoginPresenter
import com.sadashi.client.chatwork.usecase.auth.AuthorizeUseCase
import com.sadashi.client.chatwork.usecase.auth.DeleteCodeVerifierUseCase
import com.sadashi.client.chatwork.usecase.auth.StoreCodeVerifierUseCase
import com.sadashi.client.chatwork.usecase.auth.impl.AuthorizeUseCaseImpl
import com.sadashi.client.chatwork.usecase.auth.impl.DeleteCodeVerifierUseCaseImpl
import com.sadashi.client.chatwork.usecase.auth.impl.StoreCodeVerifierUseCaseImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginModuleInjection(
    private val context: Context
) {
    private val authApiClient: AuthApiClient = ApiModule.getAuthApiClient()

    private val accessTokenLocalStore: AuthorizedTokenLocalStore
        get() {
            return AuthorizedTokenLocalStoreImpl(AuthorizedTokenPreference(context))
        }

    private val codeVerifierLocalStore: CodeVerifierLocalStore
        get() {
            return CodeVerifierLocalStoreImpl(CodeVerifierPreference(context))
        }

    private val authorizeService: AuthorizeService
        get() {
            return AuthorizeServiceImpl(authApiClient, accessTokenLocalStore, Schedulers.io())
        }

    private val codeVerifierRepository: CodeVerifierRepository
        get() {
            return CodeVerifierRepositoryImpl(codeVerifierLocalStore)
        }

    private val authorizeUseCase: AuthorizeUseCase
        get() {
            return AuthorizeUseCaseImpl(authorizeService, codeVerifierRepository)
        }

    private val storeCodeVerifierUseCase: StoreCodeVerifierUseCase
        get() {
            return StoreCodeVerifierUseCaseImpl(codeVerifierRepository)
        }

    private val deleteCodeVerifierUseCase: DeleteCodeVerifierUseCase
        get() {
            return DeleteCodeVerifierUseCaseImpl(codeVerifierRepository)
        }

    fun getPresenter(): LoginContract.Presentation {
        return LoginPresenter(
            authorizeUseCase,
            storeCodeVerifierUseCase,
            deleteCodeVerifierUseCase,
            AndroidSchedulers.mainThread()
        )
    }
}