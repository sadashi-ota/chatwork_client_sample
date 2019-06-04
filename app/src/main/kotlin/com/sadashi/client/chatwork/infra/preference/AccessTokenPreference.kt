package com.sadashi.client.chatwork.infra.preference

import android.content.Context

class AccessTokenPreference(context: Context) {
    companion object {
        private const val KEY_ACCESS_TOKEN = "accessToken"
        private const val PREF_NAME = "AccessToken"
    }

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun put(accessToken: String) {
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply()
    }

    fun get(): String? = sharedPreferences.getString(KEY_ACCESS_TOKEN, "")

    fun delete() = sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).apply()
}