package com.sadashi.client.chatwork.ui.login

interface LoginTransition {
    fun moveLoginHtmlPage(url: String)
    fun moveRooms()
}