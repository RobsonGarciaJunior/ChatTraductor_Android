package com.example.chattraductor.ui.message

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.repository.local.message.RoomMessageDataSource
import com.example.chattraductor.data.repository.remote.RemoteMessageDataSource
import com.example.chattraductor.databinding.MessageActivityBinding
import com.example.chattraductor.utils.MyApp

class MessageActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: MessageActivityBinding
    private lateinit var messageAdapter: MessageAdapter
    private val remoteMessageRepository = RemoteMessageDataSource()
    private val roomMessageRepository = RoomMessageDataSource()
    private val chatter1 = MyApp.userPreferences.getUser()
    private lateinit var chatter2: User
    private val messageViewModel: MessageViewModel by viewModels {
        MessageViewModelFactory(
            remoteMessageRepository,
            roomMessageRepository,
            applicationContext
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MessageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDefaultData()
    }

    private fun setDefaultData() {
        val receivedUser: User? = intent.getParcelableExtra("usuario Seleccionado")
        if (receivedUser != null) {
            Log.d("User", receivedUser.name)
        }
        // Verificar si se recibió el objeto Group
        if (receivedUser != null) {
            Log.d("Prueba", "" + receivedUser.id)
            this.chatter2 = receivedUser
            receivedUser.id?.let {
                if (chatter1 != null) {
                    messageViewModel.updateMessageList(chatter1, chatter2)
                }
            }
            binding.configurationTitle.text = this.chatter2.name
        } else {
            Log.d("Usuario recibido", "Objeto Usuario es nulo")
        }
    }

}