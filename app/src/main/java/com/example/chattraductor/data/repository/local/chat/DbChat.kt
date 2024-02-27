package com.example.chattraductor.data.repository.local.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "chats",
    indices = [
        Index(value = ["name"],
            unique = true)
    ])
data class DbChat (
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "name") val name: String,
)