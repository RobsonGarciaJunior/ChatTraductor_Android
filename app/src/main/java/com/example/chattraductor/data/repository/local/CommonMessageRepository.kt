package com.example.chattraductor.data.repository.local

import com.example.chattraductor.data.model.Message
import com.example.chattraductor.utils.Resource

interface CommonMessageRepository {
    suspend fun getMessages(senderId:Int, rece) : Resource<List<Message>>
    suspend fun createMessage(message: Message): Resource<Message>

}