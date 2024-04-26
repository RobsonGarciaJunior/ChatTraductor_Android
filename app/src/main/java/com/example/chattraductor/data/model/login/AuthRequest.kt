package com.example.chattraductor.data.model.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthRequest(
    val email: String,
    val password: String
) : Parcelable