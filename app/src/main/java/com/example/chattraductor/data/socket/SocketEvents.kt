package com.example.chattraductor.data.socket

enum class SocketEvents(val value: String) {
    ON_MESSAGE_RECEIVED("receive message"),
    ON_SEND_MESSAGE("send message"),
    ON_CONNECT("connect"),
    ON_DISCONNECT("disconnect"),

}