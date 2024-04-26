package com.example.chattraductor.data.repository.local.message

import androidx.room.Dao
import androidx.room.Query
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.data.model.Message
import com.example.chattraductor.data.repository.local.CommonChatRepository
import com.example.chattraductor.data.repository.local.CommonMessageRepository
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource
import java.util.Date

class RoomMessageDataSource : CommonMessageRepository {

    private val messageDao: MessageDao = MyApp.db.messageDao()

    override suspend fun getMessages(chatId: Int): Resource<List<Message>> {
        val response = messageDao.getMessages(chatId).map { it.toMessage() }
        return Resource.success(response)
    }

    override suspend fun createMessage(message: Message): Resource<Message> {
        val dbMessage = message.toDbMessage()
        val dbMessageId = messageDao.createMessage(
            dbMessage.id,
            dbMessage.text,
            dbMessage.senderId,
            dbMessage.receiverId
        )
        message.id = dbMessageId.toInt()
        return Resource.success(message)
    }

    fun DbMessage.toMessage() = Message(id, text, senderId, receiverId)
    fun Message.toDbMessage() = DbMessage(id, text, senderId, receiverId)

    @Dao
    interface MessageDao {
        @Query("SELECT * FROM chats ORDER BY id")
        suspend fun getMessages(chatId: Int): List<DbMessage>

        @Query(
            "INSERT INTO messages (id, text, senderId, receiverId) " +
                    "VALUES (:id, :text, :senderId, :receiverId)"
        )
        suspend fun createMessage(id: Int?, text: String, senderId: Int?, receiverId: Int?): Long
    }
}