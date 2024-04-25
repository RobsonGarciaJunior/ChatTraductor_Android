package com.example.chattraductor.data.repository.local.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.chattraductor.data.repository.local.user.DbUser


@Entity(
    tableName = "chats",
    indices = [
        Index(
            value = ["name"],
            unique = true
        )],
    foreignKeys = [
        ForeignKey(
            entity = DbUser::class,
            parentColumns = ["id"],
            childColumns = ["chatter1"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DbUser::class,
            parentColumns = ["id"],
            childColumns = ["chatter2"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class DbChat(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "chatter1") val chatter1: Int?,
    @ColumnInfo(name = "chatter2") val chatter2: Int?,
)