package com.example.chattraductor.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.chattraductor.data.repository.local.MyAppRoomDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class MyApp: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var userPreferences: UserPreferences
        lateinit var db: MyAppRoomDataBase
        lateinit var appScope: CoroutineScope
        const val AUTHORIZATION_HEADER = "Authorization"
        const val API_SERVER = "https://10.5.7.31"
        const val API_PORT = "8063"
        const val API_SOCKET_PORT = "8085"
        const val DEFAULT_PASS = "elorrieta00"
        const val BEARER = "Bearer "

    }
    override fun onCreate(){
        super.onCreate()
        context = this
        userPreferences = UserPreferences()
        appScope = CoroutineScope(Dispatchers.IO)
        db = Room
            .databaseBuilder(this, MyAppRoomDataBase::class.java, "chat-db")
            .build()

    }
}