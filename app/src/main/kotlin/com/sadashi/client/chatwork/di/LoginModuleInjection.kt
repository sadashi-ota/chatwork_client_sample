package com.sadashi.client.chatwork.di

import android.content.Context
import com.sadashi.client.chatwork.domain.auth.AccessTokenRepository
import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.api.AuthApiClientFactory
import com.sadashi.client.chatwork.infra.auth.AccessTokenRepositoryImpl
import com.sadashi.client.chatwork.infra.auth.AuthorizeServiceImpl
import com.sadashi.client.chatwork.infra.datasource.local.AccessTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.impl.AccessTokenLocalStoreImpl
import com.sadashi.client.chatwork.infra.preference.AccessTokenPreference
import com.sadashi.client.chatwork.ui.login.LoginContract
import com.sadashi.client.chatwork.ui.login.LoginPresenter
import com.sadashi.client.chatwork.usecase.auth.AuthorizeUseCase
import com.sadashi.client.chatwork.usecase.auth.impl.AuthorizeUseCaseImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginModuleInjection(
    private val context: Context
) {
    private fun getAuthApiClient(): AuthApiClient = ApiModule.getAuthApiClient()

    private fun getAccessTokenLocalStore(): AccessTokenLocalStore {
        return AccessTokenLocalStoreImpl(AccessTokenPreference(context))
    }

    private fun getAuthorizeService(): AuthorizeService {
        return AuthorizeServiceImpl(getAuthApiClient(), Schedulers.io())
    }

    private fun getAccessTokenRepository(): AccessTokenRepository {
        return AccessTokenRepositoryImpl(getAccessTokenLocalStore())
    }

    private fun getAuthorizeUseCase(): AuthorizeUseCase {
        return AuthorizeUseCaseImpl(getAuthorizeService(), getAccessTokenRepository())
    }

    fun getPresenter(): LoginContract.Presentation {
        return LoginPresenter(getAuthorizeUseCase(), AndroidSchedulers.mainThread())
    }
}