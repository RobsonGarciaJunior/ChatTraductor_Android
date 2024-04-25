package com.example.chattraductor.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    var id: Int?,
    var text: String,
    var senderId: Int,
    val receiverId: Int,
): Parcelable
