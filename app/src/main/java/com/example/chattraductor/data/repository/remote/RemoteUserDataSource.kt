package com.example.chattraductor.data.repository.remote

import com.example.chattraductor.data.model.Message
import com.example.chattraductor.utils.Resource

class RemoteUserDataSource: BaseDataSource(), RemoteMessageRepository{
    override suspend fun getMessages(messageId: Int?): Resource<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMessagesFromGroup(idGroup: Int): Resource<List<Message>> {
        TODO("Not yet implemented")
    }

    override suspend fun createMessage(message: Message): Resource<Message> {
        TODO("Not yet implemented")
    }
}