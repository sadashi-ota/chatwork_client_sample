package com.sadashi.client.chatwork.infra.api

import com.sadashi.client.chatwork.BuildConfig
import com.sadashi.client.chatwork.infra.api.json.AccessTokenResponseJson
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApiClient {
    @FormUrlEncoded
    @POST("token")
    fun getAccessToken(
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("client_id") clientId: String = BuildConfig.CLIENT_ID,
        @Field("redirect_uri") redirectUri: String = BuildConfig.AUTH_CALLBACK,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String
    ): Single<AccessTokenResponseJson>
}