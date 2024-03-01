package com.example.chattraductor.data.repository.local.userChat

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.chattraductor.data.repository.local.chat.DbChat
import com.example.chattraductor.data.repository.local.user.DbUser
import java.util.Date

@Entity(tableName = "user_chat",
    primaryKeys = ["userId1", "userId2"],
    foreignKeys = [
        ForeignKey(
            entity = DbUser::class,
            parentColumns = ["id"],
            childColumns = ["userId1"]
        ),
        ForeignKey(
            entity = DbUser::class,
            parentColumns = ["id"],
            childColumns = ["userId2"]
        )
    ])

data class DbUserChat (
    val chatId: Int,
    val userId1: Int,
    val userId2: Int,
)