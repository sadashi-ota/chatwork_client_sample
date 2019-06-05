package com.sadashi.client.chatwork.infra.api.json

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object AccessTokenRequestJsonConverter {

    @Suppress("UNCHECKED_CAST")
    fun convert(request: AccessTokenRequestJson): Map<String, String> {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            .adapter(AccessTokenRequestJson::class.java)
        return moshi.toJsonValue(request) as Map<String, String>
    }
}