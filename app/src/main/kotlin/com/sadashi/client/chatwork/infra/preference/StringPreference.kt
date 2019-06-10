package com.sadashi.client.chatwork.infra.preference

import android.content.Context

abstract class StringPreference(context: Context, prefName: String) {
    abstract val key: String

    private val sharedPreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)

    fun put(value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun get(): String? = sharedPreferences.getString(key, "")

    fun delete() = sharedPreferences.edit().remove(key).apply()
}