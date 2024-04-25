package com.example.chattraductor.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat (
    var id: Int?,
    val name: String,
    val chatter1: Int?,
    val chatter2: Int?
    ) : Parcelable
