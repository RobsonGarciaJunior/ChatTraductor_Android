package com.example.chattraductor.data.socket

data class SocketMessageRequest(
    val message: String,
    val receiverId: Int?
)