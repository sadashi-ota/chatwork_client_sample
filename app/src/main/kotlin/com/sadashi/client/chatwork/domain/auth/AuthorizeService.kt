package com.sadashi.client.chatwork.domain.auth

import io.reactivex.Completable
import io.reactivex.Single

interface AuthorizeService {
    fun createLoginUrl(): Single<String>
    fun executeAuthorize(code: String): Completable
}