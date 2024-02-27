package com.example.chattraductor.data.repository.local

import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.utils.Resource

interface CommonChatRepository {
    suspend fun getChats(idUser:Int) : Resource<List<Chat>>

}