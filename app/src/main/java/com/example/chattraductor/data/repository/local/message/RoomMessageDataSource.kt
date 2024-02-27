package com.example.chattraductor.data.repository.local.message

import androidx.room.Dao
import androidx.room.Query
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.data.model.Message
import com.example.chattraductor.data.repository.local.CommonChatRepository
import com.example.chattraductor.data.repository.local.CommonMessageRepository
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource

class RoomChatDataSource : CommonMessageRepository {

    private val messageDao: MessageDao = MyApp.db.messageDao()

    override suspend fun getMessages(chatId: Int): Resource<List<Message>> {
        val response = messageDao.getMessages(chatId).map { it.toMessage() }
        return Resource.success(response)
    }
}

fun DbMessage.toMessage() = Message(id, text, chatId, userId)
fun Message.toDbMessage() = DbMessage(id, text, chatId, userId)

@Dao
interface MessageDao {
    @Query("SELECT * FROM chats ORDER BY id")
    suspend fun getMessages(chatId:Int): List<DbMessage>
}