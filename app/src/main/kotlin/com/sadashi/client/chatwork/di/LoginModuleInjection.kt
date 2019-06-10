package com.sadashi.client.chatwork.di

import android.content.Context
import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.AuthorizedTokenRepository
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.datasource.local.AuthorizedTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.CodeVerifierLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.impl.AuthorizedTokenLocalStoreImpl
import com.sadashi.client.chatwork.infra.datasource.local.impl.CodeVerifierLocalStoreImpl
import com.sadashi.client.chatwork.infra.domain.auth.AuthorizeServiceImpl
import com.sadashi.client.chatwork.infra.domain.auth.AuthorizedTokenRepositoryImpl
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
    private fun getAuthApiClient(): AuthApiClient = ApiModule.getAuthApiClient()

    private fun getAccessTokenLocalStore(): AuthorizedTokenLocalStore {
        return AuthorizedTokenLocalStoreImpl(AuthorizedTokenPreference(context))
    }

    private fun getCodeVerifierLocalStore(): CodeVerifierLocalStore {
        return CodeVerifierLocalStoreImpl(CodeVerifierPreference(context))
    }

    private fun getAuthorizeService(): AuthorizeService {
        return AuthorizeServiceImpl(getAuthApiClient(), Schedulers.io())
    }

    private fun getAccessTokenRepository(): AuthorizedTokenRepository {
        return AuthorizedTokenRepositoryImpl(getAccessTokenLocalStore())
    }

    private fun getCodeVerifierRepository(): CodeVerifierRepository {
        return CodeVerifierRepositoryImpl(getCodeVerifierLocalStore())
    }

    private fun getAuthorizeUseCase(): AuthorizeUseCase {
        return AuthorizeUseCaseImpl(
            getAuthorizeService(),
            getAccessTokenRepository(),
            getCodeVerifierRepository()
        )
    }

    private fun getStoreCodeVerifierUseCase(): StoreCodeVerifierUseCase {
        return StoreCodeVerifierUseCaseImpl(getCodeVerifierRepository())
    }

    private fun getDeleteCodeVerifierUseCase(): DeleteCodeVerifierUseCase {
        return DeleteCodeVerifierUseCaseImpl(getCodeVerifierRepository())
    }

    fun getPresenter(): LoginContract.Presentation {
        return LoginPresenter(
            getAuthorizeUseCase(),
            getStoreCodeVerifierUseCase(),
            getDeleteCodeVerifierUseCase(),
            AndroidSchedulers.mainThread()
        )
    }
}