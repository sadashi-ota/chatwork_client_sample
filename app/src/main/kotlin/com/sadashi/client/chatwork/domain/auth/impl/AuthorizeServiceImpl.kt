package com.sadashi.client.chatwork.domain.auth.impl

import android.util.Base64
import com.sadashi.client.chatwork.BuildConfig
import com.sadashi.client.chatwork.domain.auth.AuthorizeRepository
import com.sadashi.client.chatwork.domain.auth.AuthorizeService
import com.sadashi.client.chatwork.domain.auth.CodeVerifier
import com.sadashi.client.chatwork.domain.auth.CodeVerifierRepository
import com.sadashi.client.chatwork.utility.RandomStringBuilder
import io.reactivex.Completable
import io.reactivex.Single
import java.security.MessageDigest

class AuthorizeServiceImpl(
    private val codeVerifierRepository: CodeVerifierRepository,
    private val authorizeRepository: AuthorizeRepository
) : AuthorizeService {
    companion object {
        private const val HASH_ALGORITHM = "SHA-256"
        private const val CODE_LENGTH = 64
        private const val LOGIN_URL = "${BuildConfig.LOGIN_URL}?" +
                "response_type=code" +
                "&client_id=${BuildConfig.CLIENT_ID}" +
                "&redirect_uri=${BuildConfig.AUTH_CALLBACK}" +
                "&scope=users.all:read rooms.all:read_write" +
                "&code_challenge_method=S256&code_challenge="
    }

    override fun createLoginUrl(): Single<String> {
        val codeVerifier = RandomStringBuilder.build(CODE_LENGTH)
        val messageDigest = MessageDigest.getInstance(HASH_ALGORITHM).apply {
            update(codeVerifier.toByteArray())
        }
        val url = LOGIN_URL +
                Base64.encodeToString(messageDigest.digest(), Base64.NO_PADDING + Base64.URL_SAFE)
        return codeVerifierRepository
            .store(CodeVerifier(codeVerifier))
            .toSingleDefault(url)
    }

    override fun executeAuthorize(code: String): Completable {
        return codeVerifierRepository.find()
            .flatMapCompletable { codeVerifier ->
                authorizeRepository.executeAuthorize(code, codeVerifier)
            }
            .doOnComplete { codeVerifierRepository.delete() }
    }
}