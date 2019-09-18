package com.sadashi.client.chatwork.ui.login

import android.net.Uri
import android.util.Base64
import com.sadashi.client.chatwork.BuildConfig
import com.sadashi.client.chatwork.usecase.auth.AuthorizeUseCase
import com.sadashi.client.chatwork.usecase.auth.LoginUseCase
import com.sadashi.client.chatwork.utility.RandomStringBuilder
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.security.MessageDigest

class LoginPresenter(
    private val loginUseCase: LoginUseCase,
    private val authorizeUseCase: AuthorizeUseCase,
    private val uiScheduler: Scheduler
) : LoginContract.Presentation {

    private lateinit var view: LoginContract.View
    private lateinit var loginTransition: LoginTransition

    private val disposables = CompositeDisposable()

    override fun setUp(
        view: LoginContract.View,
        loginTransition: LoginTransition
    ) {
        this.view = view
        this.loginTransition = loginTransition
    }

    override fun login() {
        loginUseCase.execute()
            .observeOn(uiScheduler)
            .subscribe({ url ->
                loginTransition.moveLoginHtmlPage(url)
            }, { throwable ->
                view.showErrorDialog(throwable)
            })
            .addTo(disposables)
    }

    override fun onLoaded(uri: Uri): Boolean {
        val code = uri.getQueryParameter("code") ?: return false

        authorizeUseCase.execute(code)
            .doOnSubscribe {
                view.showProgress()
            }
            .observeOn(uiScheduler)
            .subscribe({
                view.dismissProgress()
                loginTransition.moveRooms()
            }, { throwable ->
                view.dismissProgress()
                view.showErrorDialog(throwable)
            })
            .addTo(disposables)
        return true
    }

}