package com.example.chattraductor.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.chattraductor.data.model.User
import com.google.gson.Gson
import io.socket.client.Socket

class UserPreferences {
    private val sharedPreferences: SharedPreferences by lazy {
        MyApp.context.getSharedPreferences(
            MyApp.context.packageName, Context.MODE_PRIVATE
        )
    }
    lateinit var mSocket: Socket

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_INFO = "user_info"
    }

    fun saveAuthToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString(USER_TOKEN, null)
    }
    fun saveUser(loginUser: User) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val userJson = gson.toJson(loginUser)
        editor.putString(USER_INFO, userJson)
        editor.apply()
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(USER_INFO, null)
        if (userJson != null) {
            val gson = Gson()
            val loginUser = gson.fromJson(userJson, User::class.java)
            return loginUser
        }
        return null
    }
    fun removeData() {
        val editor = sharedPreferences.edit()
        editor.remove("user_token")
        editor.remove("user_info")
        editor.apply()
    }


}