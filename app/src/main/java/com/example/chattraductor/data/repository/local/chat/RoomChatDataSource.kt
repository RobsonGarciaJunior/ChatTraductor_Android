package com.example.chattraductor.data.repository.local.chat

import androidx.room.Dao
import androidx.room.Query
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.data.repository.local.CommonChatRepository
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource

class RoomChatDataSource : CommonChatRepository {

    private val chatDao: ChatDao = MyApp.db.chatDao()

    override suspend fun getChats(userId: Int): Resource<List<Chat>> {
        val response = chatDao.getChats(userId).map { it.toChat() }
        return Resource.success(response)
    }

    override suspend fun createChat(chat: Chat): Resource<Chat> {
        TODO("Not yet implemented")
        return Resource.success(chat)
    }
}

fun DbChat.toChat() = Chat(id, name)
fun Chat.toDbChat() = DbChat(id, name)

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE id IN (SELECT chatId FROM user_chat WHERE userId1 = :userId OR userId2 = :userId) ORDER BY id")
    suspend fun getChats(userId:Int): List<DbChat>
}