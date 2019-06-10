package com.sadashi.client.chatwork.infra.preference

import android.content.Context

class CodeVerifierPreference(context: Context) : StringPreference(context, PREF_NAME) {
    companion object {
        private const val KEY_CODE_VERIFIER = "codeVerifier"
        private const val PREF_NAME = "CodeVerifier"
    }

    override val key = KEY_CODE_VERIFIER
}