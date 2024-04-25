package com.example.chattraductor.data.repository.local.chat

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.data.repository.local.CommonChatRepository
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource
import java.util.Date

class RoomChatDataSource : CommonChatRepository {

    private val chatDao: ChatDao = MyApp.db.chatDao()

    override suspend fun getChats(userId: Int): Resource<List<Chat>> {
        val response = chatDao.getChats(userId).map { it.toChat() }
        return Resource.success(response)
    }

    override suspend fun createChat(chat: Chat): Resource<Chat> {
        return try {
            chatDao.createGroup(chat.toDbChat())
            Log.d("p1", "Entra")
            Resource.success()
        } catch (exception: SQLiteConstraintException) {
            Resource.error("El nombre del grupo ya esta en uso")
        }
    }
}

fun DbChat.toChat() = Chat(id, name, chatter1, chatter2)
fun Chat.toDbChat() = DbChat(id, name, chatter1, chatter2)

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE id IN (SELECT chatId FROM user_chat WHERE userId1 = :userId OR userId2 = :userId) ORDER BY id")
    suspend fun getChats(userId:Int): List<DbChat>
    @Insert
    abstract fun createGroup(chat: DbChat)
}