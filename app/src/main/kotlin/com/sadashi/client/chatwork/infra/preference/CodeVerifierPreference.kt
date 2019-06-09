package com.sadashi.client.chatwork.infra.preference

import android.content.Context

class CodeVerifierPreference(context: Context) {
    companion object {
        private const val KEY_CODE_VERIFIER = "codeVerifier"
        private const val PREF_NAME = "CodeVerifier"
    }

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun put(accessToken: String) {
        sharedPreferences.edit().putString(KEY_CODE_VERIFIER, accessToken).apply()
    }

    fun get(): String? = sharedPreferences.getString(KEY_CODE_VERIFIER, "")

    fun delete() = sharedPreferences.edit().remove(KEY_CODE_VERIFIER).apply()
}