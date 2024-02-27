package com.example.chattraductor.data.repository.local.user

import androidx.room.Dao
import androidx.room.Query
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.repository.local.CommonChatRepository
import com.example.chattraductor.data.repository.local.CommonUserRepository
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource

class RoomUserDataSource : CommonUserRepository {

    private val userDao: UserDao = MyApp.db.userDao()

    override suspend fun getUsers(): Resource<List<User>> {
        val response = userDao.getUsers().map { it.toUser() }
        return Resource.success(response)
    }
}

fun DbUser.toUser() = User(id, name, surname, phoneNumber)
fun User.toDbUser() = DbUser(id, name, surname, phone_number1)

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY name")
    suspend fun getUsers(): List<DbUser>
}