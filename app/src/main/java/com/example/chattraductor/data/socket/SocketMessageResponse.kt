package com.example.chattraductor.data.socket

data class SocketMessageResponse(
    val messageType: MessageType,
    val id: Int,
    val message: String,
    val senderName: String,
    val senderId: Int,
    val receiverId: Int,
    val receiverName: String
)
