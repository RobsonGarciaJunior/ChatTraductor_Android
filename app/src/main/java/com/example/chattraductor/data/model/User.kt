package com.example.chattraductor.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String = "",
    var id: Int?,
    val name: String,
    val surname: String,
    val phoneNumber1: Long,
    var accessToken: String = "",
) : Parcelable {
    constructor(
        id: Int?,
        name: String,
        surname: String,
        phoneNumber1: Long
    ) : this(
        email = "",
        id = id,
        name = name,
        surname = surname,
        phoneNumber1 = phoneNumber1,
        accessToken = ""
    )
}