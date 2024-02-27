package com.example.chattraductor.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat (
    var id: Int?,
    val name: String
    ) : Parcelable
