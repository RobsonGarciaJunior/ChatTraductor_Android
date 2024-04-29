package com.example.chattraductor.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.chattraductor.data.repository.local.chat.ChatDao
import com.example.chattraductor.data.repository.local.chat.DbChat
import com.example.chattraductor.data.repository.local.message.DbMessage
import com.example.chattraductor.data.repository.local.message.MessageDao
import com.example.chattraductor.data.repository.local.user.DbUser
import com.example.chattraductor.data.repository.local.user.UserDao

@Database(
    entities = [
        DbChat::class,
        DbMessage::class,
        DbUser::class],
    version = 1,
    exportSchema = false
)

abstract class MyAppRoomDataBase: RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao


}
