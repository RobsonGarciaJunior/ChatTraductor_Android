package com.example.chattraductor.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginUser(

    val email: String,
    val id: Int,
    var name: String,
    var surname: String,
    var phoneNumber1: Int,
    var accessToken: String

): Parcelable