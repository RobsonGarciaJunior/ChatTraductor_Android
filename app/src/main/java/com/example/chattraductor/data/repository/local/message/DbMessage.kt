package com.example.chattraductor.data.repository.local.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.chattraductor.data.repository.local.chat.DbChat
import com.example.chattraductor.data.repository.local.user.DbUser
import java.util.Date

@Entity(tableName = "messages", foreignKeys = [
    ForeignKey(
        entity = DbChat::class,
        parentColumns = ["id"],
        childColumns = ["chatId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = DbUser::class,
        parentColumns = ["id"],
        childColumns = ["senderId"],
        onDelete = ForeignKey.NO_ACTION
    )
])
data class DbMessage(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "chatId") val chatId: Int,
    @ColumnInfo(name = "senderId") val senderId: Int
)
