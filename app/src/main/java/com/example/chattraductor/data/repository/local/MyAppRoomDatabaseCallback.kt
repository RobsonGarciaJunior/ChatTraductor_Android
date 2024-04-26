package com.example.chattraductor.data.repository.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MyAppRoomDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // Código para crear datos de prueba al crear la base de datos
        scope.launch {

        }

    }
}