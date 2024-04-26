package com.example.chattraductor.data.repository.remote

import com.example.chattraductor.data.model.Message

class RemoteMessageDataSource: BaseDataSource(), RemoteMessageRepository  {
    override suspend fun getMessages(messageId: Int?) = getResult{
        RetrofitClient.apiInterface.getMessages(messageId)
    }

    override suspend fun getMessagesFromGroup(idGroup: Int) = getResult {
        RetrofitClient.apiInterface.getMessageByChatId(idGroup)
    }

    override suspend fun createMessage(message: Message) = getResult {
        RetrofitClient.apiInterface.createMessage(message)
    }

}