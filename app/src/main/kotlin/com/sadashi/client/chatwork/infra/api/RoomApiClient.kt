package com.sadashi.client.chatwork.infra.api

import com.sadashi.client.chatwork.infra.api.json.AccountDetailJson
import com.sadashi.client.chatwork.infra.api.json.MessageResponseJson
import com.sadashi.client.chatwork.infra.api.json.RoomResponseJson
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RoomApiClient {

    @GET("v2/rooms")
    fun getRooms(
        @Header("Authorization") authorization: String
    ): Single<List<RoomResponseJson>>

    @GET("v2/rooms/{roomId}")
    fun getRoom(
        @Header("Authorization") authorization: String,
        @Path("roomId") roomId: Int
    ): Single<RoomResponseJson>

    @GET("v2/rooms/{roomId}/messages")
    fun getMessages(
        @Header("Authorization") authorization: String,
        @Path("roomId") roomId: Int,
        @Query("force") force: Int
    ): Single<List<MessageResponseJson>>

    @GET("v2/rooms/{roomId}/members")
    fun getMembers(
        @Header("Authorization") authorization: String,
        @Path("roomId") roomId: Int
    ): Single<List<AccountDetailJson>>
}