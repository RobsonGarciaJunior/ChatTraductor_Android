package com.example.chattraductor.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var id: Int?,
    val name: String,
    val surname: String,
    val phoneNumber1: Int,
    ): Parcelable