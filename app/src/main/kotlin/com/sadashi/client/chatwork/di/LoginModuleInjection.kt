package com.sadashi.client.chatwork.di

import android.content.Context
import com.sadashi.client.chatwork.domain.auth.AccessTokenRepository
import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.domain.auth.RefreshTokenRepository
import com.sadashi.client.chatwork.infra.api.AuthApiClient
import com.sadashi.client.chatwork.infra.domain.auth.AccessTokenRepositoryImpl
import com.sadashi.client.chatwork.infra.domain.auth.AuthorizeServiceImpl
import com.sadashi.client.chatwork.infra.datasource.local.AccessTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.CodeVerifierLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.RefreshTokenLocalStore
import com.sadashi.client.chatwork.infra.datasource.local.impl.AccessTokenLocalStoreImpl
import com.sadashi.client.chatwork.infra.datasource.local.impl.CodeVerifierLocalStoreImpl
import com.sadashi.client.chatwork.infra.datasource.local.impl.RefreshTokenLocalStoreImpl
import com.sadashi.client.chatwork.infra.domain.auth.CodeVerifierRepositoryImpl
import com.sadashi.client.chatwork.infra.domain.auth.RefreshTokenRepositoryImpl
import com.sadashi.client.chatwork.infra.preference.AccessTokenPreference
import com.sadashi.client.chatwork.infra.preference.CodeVerifierPreference
import com.sadashi.client.chatwork.infra.preference.RefreshTokenPreference
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

    private fun getAccessTokenLocalStore(): AccessTokenLocalStore {
        return AccessTokenLocalStoreImpl(AccessTokenPreference(context))
    }

    private fun getRefreshTokenLocalStore(): RefreshTokenLocalStore {
        return RefreshTokenLocalStoreImpl(RefreshTokenPreference(context))
    }

    private fun getCodeVerifierLocalStore(): CodeVerifierLocalStore {
        return CodeVerifierLocalStoreImpl(CodeVerifierPreference(context))
    }

    private fun getAuthorizeService(): AuthorizeService {
        return AuthorizeServiceImpl(getAuthApiClient(), Schedulers.io())
    }

    private fun getAccessTokenRepository(): AccessTokenRepository {
        return AccessTokenRepositoryImpl(getAccessTokenLocalStore())
    }

    private fun getRefreshTokenRepository(): RefreshTokenRepository {
        return RefreshTokenRepositoryImpl(getRefreshTokenLocalStore())
    }

    private fun getCodeVerifierRepository(): CodeVerifierRepository {
        return CodeVerifierRepositoryImpl(getCodeVerifierLocalStore())
    }

    private fun getAuthorizeUseCase(): AuthorizeUseCase {
        return AuthorizeUseCaseImpl(
            getAuthorizeService(),
            getAccessTokenRepository(),
            getRefreshTokenRepository(),
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