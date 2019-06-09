package com.sadashi.client.chatwork.ui.login

import android.net.Uri
import android.util.Base64
import android.util.Log
import com.sadashi.client.chatwork.BuildConfig
import com.sadashi.client.chatwork.usecase.auth.AuthorizeUseCase
import com.sadashi.client.chatwork.usecase.auth.DeleteCodeVerifierUseCase
import com.sadashi.client.chatwork.usecase.auth.StoreCodeVerifierUseCase
import com.sadashi.client.chatwork.utility.RandomStringBuilder
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import java.security.MessageDigest

class LoginPresenter(
    private val authorizeUseCase: AuthorizeUseCase,
    private val storeCodeVerifierUseCase: StoreCodeVerifierUseCase,
    private val deleteCodeVerifierUseCase: DeleteCodeVerifierUseCase,
    private val uiScheduler: Scheduler
) : LoginContract.Presentation {

    companion object {
        private const val CODE_LENGTH = 64
        private const val LOGIN_URL = "${BuildConfig.LOGIN_URL}?response_type=code&client_id=${BuildConfig.CLIENT_ID}" +
                "&redirect_uri=${BuildConfig.AUTH_CALLBACK}&scope=users.all:read rooms.all:read_write" +
                "&code_challenge_method=S256&code_challenge="
    }

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
        val codeVerifier = RandomStringBuilder.build(CODE_LENGTH)
        val messageDigest = MessageDigest.getInstance("SHA-256").apply {
            update(codeVerifier.toByteArray())
        }
        val url = LOGIN_URL +
                Base64.encodeToString(messageDigest.digest(), Base64.NO_PADDING + Base64.URL_SAFE)

        storeCodeVerifierUseCase.execute(codeVerifier)
            .observeOn(uiScheduler)
            .subscribe {
                loginTransition.moveLoginHtmlPage(url)
            }
            .addTo(disposables)
    }

    override fun logout() {
        TODO("not implemented")
    }

    override fun onLoaded(uri: Uri): Boolean {
        val code = uri.getQueryParameter("code") ?: return false

        authorizeUseCase.execute(code)
            .doOnSubscribe {
                view.showProgress()
            }
            .observeOn(uiScheduler)
            .doOnComplete { deleteCodeVerifierUseCase.execute() }
            .subscribe({
                view.dismissProgress()
                loginTransition.navigationBack()
            }, { throwable ->
                view.dismissProgress()
                view.showErrorDialog(throwable)
            })
            .addTo(disposables)
        return true
    }

}