package com.sadashi.client.chatwork.infra.api

import com.sadashi.client.chatwork.infra.api.json.RoomResponseJson
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header

interface RoomApiClient {

    @GET("v2/rooms")
    fun getRooms(): Single<List<RoomResponseJson>>
}