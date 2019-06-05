package com.sadashi.client.chatwork.utility

import org.apache.commons.lang3.RandomStringUtils

object RandomStringBuilder {

    @Suppress("SpellCheckingInspection")
    private const val BASE_VALUE = "0123456789ABCDEFGHIJKLMNOPQRSTUYWXYZabcdefghijklmnopqrstuvwxyz"

    fun build(length: Int): String {
        return RandomStringUtils.random(length, BASE_VALUE)
    }
}