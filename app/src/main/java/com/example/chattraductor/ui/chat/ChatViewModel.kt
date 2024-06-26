package com.example.chattraductor.ui.chat

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.repository.remote.RemoteUserRepository
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val remoteUserRepository: RemoteUserRepository,
    private var context: Context
) : ViewModel() {

    /*
    private val _text = MutableLiveData<String>().apply {
         value = "This is chat Fragment"
     }
     val text: LiveData<String> = _text
     */

    private val _chat = MutableLiveData<Resource<List<User>>>()
    val chat: LiveData<Resource<List<User>>> get() = _chat

    private val _create = MutableLiveData<Resource<Chat>>()
    val create: LiveData<Resource<Chat>> get() = _create


    private val _text = MutableLiveData<String>().apply {
        value = "MY CONTACTS"
    }
    val text: LiveData<String> = _text


    fun updateChatList() {
        val userId = MyApp.userPreferences.getUser()?.id
        Log.d("User", userId.toString())
        viewModelScope.launch {
            if (userId != null) {
                _chat.value = getChats(userId)
            }

        }
    }

    private suspend fun getChats(userId: Int): Resource<List<User>> {
        return withContext(Dispatchers.IO) {
            remoteUserRepository.findUsers(userId)
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    // FUNCIONES CREATE CHAT
    /*
       fun onCreate(name: String, chatter1: Int, chatter2: Int) {
           val newChat = Chat(null, name, chatter1, chatter2)
           viewModelScope.launch {
               _create.value = createLocal(newChat)
           }
       }

       private suspend fun createLocal(chat: Chat): Resource<Chat> {
           return withContext(Dispatchers.IO) {
               localChatRepository.createChat(chat)
           }
       }
   */
    class ChatViewModelFactory(
        private val remoteUserRepository: RemoteUserRepository,
        private val context: Context
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return ChatViewModel(remoteUserRepository, context) as T
        }

    }
}
