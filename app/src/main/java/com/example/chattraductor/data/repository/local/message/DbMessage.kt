package com.example.chattraductor.data.repository.local.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.chattraductor.data.repository.local.chat.DbChat
import com.example.chattraductor.data.repository.local.user.DbUser
import java.util.Date

@Entity(tableName = "messages")
data class DbMessage(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "senderId") val senderId: Int,
    @ColumnInfo(name = "receiverId") val receiverId: Int
)
