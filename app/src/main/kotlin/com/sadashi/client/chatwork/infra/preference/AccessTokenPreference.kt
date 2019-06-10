package com.sadashi.client.chatwork.infra.preference

import android.content.Context

class AccessTokenPreference(context: Context) : StringPreference(context, PREF_NAME) {
    companion object {
        private const val KEY_ACCESS_TOKEN = "accessToken"
        private const val PREF_NAME = "AccessToken"
    }

    override val key = KEY_ACCESS_TOKEN
}