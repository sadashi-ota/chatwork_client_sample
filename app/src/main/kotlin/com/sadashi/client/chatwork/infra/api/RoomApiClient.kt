package com.sadashi.client.chatwork.infra.api

import com.sadashi.client.chatwork.infra.api.json.RoomResponseJson
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface RoomApiClient {

    @GET("/rooms")
    fun getRooms(
        @Header("X-ChatWorkToken") token: String
    ): Single<List<RoomResponseJson>>
}