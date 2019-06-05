package com.sadashi.client.chatwork.di

import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.api.AuthApiClientFactory
import com.sadashi.client.chatwork.infra.auth.AuthorizeServiceImpl
import com.sadashi.client.chatwork.ui.login.LoginContract
import com.sadashi.client.chatwork.ui.login.LoginPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object LoginModuleInjection {
    private fun getAuthApiClient(): AuthApiClient = AuthApiClientFactory.create()

    private fun getAuthorizeService(): AuthorizeService {
        return AuthorizeServiceImpl(getAuthApiClient(), Schedulers.io())
    }

    fun getPresenter(): LoginContract.Presentation {
        return LoginPresenter(getAuthorizeService(), AndroidSchedulers.mainThread())
    }
}