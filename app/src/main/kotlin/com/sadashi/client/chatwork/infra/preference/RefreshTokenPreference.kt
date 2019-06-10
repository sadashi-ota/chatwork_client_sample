package com.sadashi.client.chatwork.infra.preference

import android.content.Context

class RefreshTokenPreference(context: Context) : StringPreference(context, PREF_NAME) {
    companion object {
        private const val KEY_REFRESH_TOKEN = "refreshToken"
        private const val PREF_NAME = "RefreshToken"
    }

    override val key = KEY_REFRESH_TOKEN
}