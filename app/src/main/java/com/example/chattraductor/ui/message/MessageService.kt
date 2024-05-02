package com.example.chattraductor.ui.message

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.MutableLiveData
import com.example.chattraductor.R
import com.example.chattraductor.data.model.Message
import com.example.chattraductor.data.repository.remote.RemoteMessageDataSource
import com.example.chattraductor.data.socket.SocketEvents
import com.example.chattraductor.data.socket.SocketMessageResponse
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.MyApp.Companion.API_SERVER
import com.example.chattraductor.utils.MyApp.Companion.API_SOCKET_PORT
import com.example.chattraductor.utils.MyApp.Companion.AUTHORIZATION_HEADER
import com.example.chattraductor.utils.MyApp.Companion.BEARER
import com.example.chattraductor.utils.Resource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.greenrobot.eventbus.EventBus


class MessageService : Service() {
    private val channelId = "download_channel"
    private val notificationId = 1
    private lateinit var serviceScope: CoroutineScope

    private val messageRepository = RemoteMessageDataSource()
    private val _savedMessage = MutableLiveData<Resource<Message>>()

    override fun onCreate() {
        super.onCreate()
        serviceScope = CoroutineScope(Dispatchers.Main)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("services", "onStartCommand")
        val contentText = "Iniciando socket"
        startForeground(notificationId, createNotification(contentText))
        startSocket()
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Descargas Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(contentText: String): Notification {
        val context = this
        val intent = Intent(context, MessageActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Chat en directo")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.baseline_chat_24)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onDestroy() {
        Log.i("services", "onDestroy")
        if (MyApp.userPreferences.mSocket.connected()) {
            MyApp.userPreferences.mSocket.disconnect()
        }
        super.onDestroy()
    }

    private fun startSocket() {
        val socketOptions = createSocketOptions()
        MyApp.userPreferences.mSocket = IO.socket("${API_SERVER}:${API_SOCKET_PORT}", socketOptions)
        MyApp.userPreferences.mSocket.on(SocketEvents.ON_CONNECT.value, onConnect())
        MyApp.userPreferences.mSocket.on(SocketEvents.ON_DISCONNECT.value, onDisconnect())
        MyApp.userPreferences.mSocket.on(SocketEvents.ON_MESSAGE_RECEIVED.value, onNewMessage())
        serviceScope.launch {
            connect()
        }
    }

    private suspend fun connect() {
        withContext(Dispatchers.IO) {
            MyApp.userPreferences.mSocket.connect()
        }
    }

    private fun onConnect(): Emitter.Listener {
        return Emitter.Listener {
            EventBus.getDefault().post("connect")
            updateNotification("conectado")
        }
    }

    private fun onDisconnect(): Emitter.Listener {
        return Emitter.Listener {
            updateNotification("disConnect")
        }
    }

    private fun onNewMessage(): Emitter.Listener {
        return Emitter.Listener {
            Log.d("Prueba", "Lo recibio")
            val response =
                onJSONtoAnyClass(it[0], SocketMessageResponse::class.java) as SocketMessageResponse
            val newMessage = response.toMessage()
            val updatedMessage = onNewMessageOwner(newMessage)
            Log.d("Hola", "$updatedMessage")
            if (_savedMessage.value?.status != Resource.Status.ERROR) {
                EventBus.getDefault().post(newMessage)
            }
            updateNotification(response.message)
        }
    }

    private fun onNewMessageOwner(incomingMessage: Message) {
        serviceScope.launch {
            val loginUser = MyApp.userPreferences.getUser()
            if (loginUser != null) {
                val updateMessage = incomingMessage
                Log.d("prueba1", "$updateMessage")
                messageRepository.createMessage(updateMessage)
            }
        }
    }

    private fun updateNotification(contentText: String) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            val notification = createNotification(contentText)

            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(notificationId, notification)
        }
    }

    private fun createSocketOptions(): IO.Options {
        val options = IO.Options()
        val headers = mutableMapOf<String, MutableList<String>>()
        headers[AUTHORIZATION_HEADER] =
            mutableListOf(BEARER + MyApp.userPreferences.fetchAuthToken())

        options.extraHeaders = headers

        return options
    }

    private fun onJSONtoAnyClass(data: Any, convertClass: Class<*>): Any? {
        return try {
            val jsonObject = data as JSONObject
            val jsonObjectString = jsonObject.toString()
            Gson().fromJson(jsonObjectString, convertClass)
        } catch (ex: Exception) {
//            Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()
            null
        }
    }


    private fun SocketMessageResponse.toMessage() = Message(id, message, senderId, receiverId)

}