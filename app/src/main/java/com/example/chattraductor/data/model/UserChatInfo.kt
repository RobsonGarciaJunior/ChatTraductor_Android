package com.example.chattraductor.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserChatInfo (
    val userId: Int,
    val chatId: Int,
) : Parcelable