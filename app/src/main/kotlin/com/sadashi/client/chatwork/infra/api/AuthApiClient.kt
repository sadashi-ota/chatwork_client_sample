package com.sadashi.client.chatwork.infra.api

import com.sadashi.client.chatwork.infra.api.json.AccessTokenResponseJson
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface AuthApiClient {
    @POST("token")
    fun getAccessToken(@QueryMap query: Map<String, String>): Single<AccessTokenResponseJson>
}