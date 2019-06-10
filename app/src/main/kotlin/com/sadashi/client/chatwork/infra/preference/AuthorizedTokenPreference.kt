package com.sadashi.client.chatwork.infra.preference

import android.content.Context

class AuthorizedTokenPreference(context: Context) : StringPreference(context, PREF_NAME) {
    companion object {
        private const val KEY_AUTHORIZED_TOKEN = "AuthorizedToken"
        private const val PREF_NAME = "AuthorizedToken"
    }

    override val key = KEY_AUTHORIZED_TOKEN
}