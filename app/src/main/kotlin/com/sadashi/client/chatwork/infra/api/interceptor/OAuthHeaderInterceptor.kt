package com.sadashi.client.chatwork.infra.api.interceptor

import com.sadashi.client.chatwork.domain.auth.AuthorizedTokenRepository
import okhttp3.Interceptor
import okhttp3.Response

class OAuthHeaderInterceptor(
    private val tokenRepository: AuthorizedTokenRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenRepository.find().blockingGet()
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", "${token.tokenType} ${token.accessToken.value}")
            .header("Accept", "application/json")
            .method(original.method(), original.body())
            .build()
        return chain.proceed(request)
    }
}