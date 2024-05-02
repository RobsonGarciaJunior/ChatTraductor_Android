package com.example.chattraductor.ui.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.chattraductor.data.model.Message
import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.repository.local.PopulateLocalDataBase
import com.example.chattraductor.data.repository.local.PopulateLocalDataBaseFactory
import com.example.chattraductor.data.repository.local.chat.RoomChatDataSource
import com.example.chattraductor.data.repository.local.message.RoomMessageDataSource
import com.example.chattraductor.data.repository.local.user.RoomUserDataSource
import com.example.chattraductor.data.repository.remote.RemoteMessageDataSource
import com.example.chattraductor.data.repository.remote.RemoteUserDataSource
import com.example.chattraductor.data.socket.SocketEvents
import com.example.chattraductor.data.socket.SocketMessageRequest
import com.example.chattraductor.databinding.MessageActivityBinding
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.util.Date

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
            remoteMessageRepository, roomMessageRepository, applicationContext
        )
    }

    private val chatRepository = RoomChatDataSource()

    private val messageRepository = RoomMessageDataSource()
    //private val remoteMessageRepository = RemoteMessageDataSource()

    private val userRepository = RoomUserDataSource()
    private val remoteUserRepository = RemoteUserDataSource()
    private val populateLocalDataBase: PopulateLocalDataBase by viewModels {
        PopulateLocalDataBaseFactory(
            chatRepository,
            messageRepository,
            remoteMessageRepository,
            userRepository,
            remoteUserRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MessageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setDefaultData()

        messageAdapter = MessageAdapter {
            // Handle item click here
        }
        binding.messageList.adapter = messageAdapter

        binding.messageList.postDelayed({
            if (messageAdapter.itemCount > 0) {
                binding.messageList.scrollToPosition(messageAdapter.itemCount - 1)
            }
        }, 50)

        messageViewModel.message.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("prueba3", "" + (it.data))
                    messageAdapter.submitList(it.data)
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                }
            }
        }

        messageViewModel.incomingMessage.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("prueba1", "" + (it.data?.text))
                    if (chatter1 != null) {
                        messageViewModel.updateMessageList(chatter1, chatter2)
                    }
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                }
            }
        }
        /*
                messageViewModel.createLocalMessage.observe(this) {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            val newList = ArrayList(messageAdapter.currentList)
                            val newMessage = it.data
                            newList.add(newMessage)

                            messageAdapter.submitList(newList)

                            if (newMessage?.id != null) {

                            }

                        }

                        Resource.Status.ERROR -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }

                        Resource.Status.LOADING -> {
                        }
                    }
                }
                */
        binding.include.send.setOnClickListener {
            val message = binding.include.inputMessage.text.toString()
            if (message.isNotBlank()) {
                binding.include.inputMessage.setText("")
                val socketMessage = SocketMessageRequest(
                    message, chatter2.id
                )
                val jsonObject = JSONObject(Gson().toJson(socketMessage))
                MyApp.userPreferences.mSocket.emit(
                    SocketEvents.ON_SEND_MESSAGE.value, jsonObject
                )
                val newMessage = chatter1?.id?.let { it1 ->
                    chatter2.id?.let { it2 ->
                        Message(
                            0, message, it1,
                            it2
                        )
                    }
                }
                if (newMessage != null) {
                    messageViewModel.onSaveIncomingMessage(newMessage)
                }
            }
        }
        startMessageService(this)

    }


    private fun startMessageService(context: Context) {
        val intent = Intent(context, MessageService::class.java)
        ContextCompat.startForegroundService(context, intent)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(message: String) {
        populateLocalDataBase.toInit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSocketIncomingMessage(message: Message) {
        Log.d("Hola", "Ta kbron")
        if (chatter1 != null) {
            messageViewModel.updateMessageList(chatter1, chatter2)
        }
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