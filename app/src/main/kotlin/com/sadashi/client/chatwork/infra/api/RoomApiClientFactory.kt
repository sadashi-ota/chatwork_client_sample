package com.sadashi.client.chatwork.infra.api

import com.sadashi.client.chatwork.BuildConfig
import com.sadashi.client.chatwork.domain.auth.AuthorizedTokenRepository
import com.sadashi.client.chatwork.infra.api.interceptor.OAuthHeaderInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RoomApiClientFactory {

    fun create(tokenRepository: AuthorizedTokenRepository): RoomApiClient {
        return provideRetrofit(tokenRepository).create(RoomApiClient::class.java)
    }

    private fun provideRetrofit(tokenRepository: AuthorizedTokenRepository): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_DOMAIN)
            .client(createClient(tokenRepository))
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun createClient(tokenRepository: AuthorizedTokenRepository): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        okHttpClientBuilder.addInterceptor(OAuthHeaderInterceptor(tokenRepository))
        return okHttpClientBuilder.build()
    }
}