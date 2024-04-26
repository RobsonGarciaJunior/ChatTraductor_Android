package com.example.chattraductor.data.repository.remote
import com.example.chattraductor.data.model.Message
import com.example.chattraductor.utils.Resource

interface RemoteMessageRepository {
    suspend fun getMessages(messageId: Int?): Resource<List<Message>>
    suspend fun getMessagesFromGroup(idGroup: Int) : Resource<List<Message>>
    suspend fun createMessage(message: Message) : Resource<Message>
}