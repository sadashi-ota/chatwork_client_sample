package com.sadashi.client.chatwork.infra.api

import com.sadashi.client.chatwork.infra.api.json.AccountDetailJson
import com.sadashi.client.chatwork.infra.api.json.MessageJson
import com.sadashi.client.chatwork.infra.api.json.RoomResponseJson
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RoomApiClient {

    @GET("v2/rooms")
    fun getRooms(): Single<List<RoomResponseJson>>

    @GET("v2/rooms/{roomId}")
    fun getRoom(@Path("roomId") roomId: Int): Single<RoomResponseJson>

    @GET("v2/rooms/{roomId}/messages")
    fun getMessages(@Path("roomId") roomId: Int, @Query("force") force: Int): Single<List<MessageJson>>

    @GET("v2/rooms/{roomId}/members")
    fun getMembers(@Path("roomId") roomId: Int): Single<List<AccountDetailJson>>
}