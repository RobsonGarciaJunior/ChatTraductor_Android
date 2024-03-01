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
import com.example.chattraductor.data.model.UserChatInfo
import com.example.chattraductor.data.repository.local.chat.RoomChatDataSource
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(private val localChatRepository: RoomChatDataSource,
                    private var context: Context): ViewModel() {

   /*
   private val _text = MutableLiveData<String>().apply {
        value = "This is chat Fragment"
    }
    val text: LiveData<String> = _text
    */

        private val _chat = MutableLiveData<Resource<List<Chat>>>()
        val chat : LiveData<Resource<List<Chat>>> get() = _chat

        private val _create = MutableLiveData<Resource<Chat>>()
        val create : LiveData<Resource<Chat>> get() = _create

        private val _delete = MutableLiveData<Resource<Void>>()
        val delete : LiveData<Resource<Void>> get() = _delete

        private val _chatPermission = MutableLiveData<Resource<Int>>()
        val chatPermission : LiveData<Resource<Int>> get() = _chatPermission

        private val _chatPermissionToDelete = MutableLiveData<Resource<Int>>()
        val chatPermissionToDelete : LiveData<Resource<Int>> get() = _chatPermissionToDelete

        private val _userHasAlreadyInChat = MutableLiveData<Resource<Int>>()
        val userHasAlreadyInChat : LiveData<Resource<Int>> get() = _userHasAlreadyInChat

        private val _joinChat = MutableLiveData<Resource<Int>>()
        val joinChat : LiveData<Resource<Int>> get() = _joinChat

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
    init {
        viewModelScope.launch {
            getChats(1)
        }
    }
        private suspend fun getChats(userId:Int) : Resource<List<Chat>> {
            return withContext(Dispatchers.IO) {
                localChatRepository.getChats(userId)
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////
        // FUNCIONES CREATE GROUP
        fun onCreate(name:String) {
            val newChat = Chat(null, name)
            viewModelScope.launch {
                        _create.value = createLocal(newChat)
                    }
        }

        private suspend fun createLocal(chat : Chat) : Resource<Chat> {
            return withContext(Dispatchers.IO) {
                localChatRepository.createChat(chat)
            }
        }

    class RoomChatViewModelFactory(
        private val roomChatRepository: RoomChatDataSource,
        private val context: Context
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            return ChatViewModel(roomChatRepository, context) as T
        }

    }
}
