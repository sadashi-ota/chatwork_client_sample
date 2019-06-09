package com.sadashi.client.chatwork.infra.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class NoAuthHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .header("Accept", "application/json")
            .method(original.method(), original.body())
            .build()
        return chain.proceed(request)
    }
}