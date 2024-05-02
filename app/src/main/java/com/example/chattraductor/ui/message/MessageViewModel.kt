package com.example.chattraductor.ui.message

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.chattraductor.data.model.Message
import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.repository.local.message.RoomMessageDataSource
import com.example.chattraductor.data.repository.remote.RemoteMessageRepository
import com.example.chattraductor.data.repository.remote.RemoteUserRepository
import com.example.chattraductor.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageViewModel(
    private val remoteMessageRepository: RemoteMessageRepository,
    private val roomMessageRepository: RoomMessageDataSource,
    private val context: Context
) : ViewModel() {

    private val _message = MutableLiveData<Resource<List<Message>>>()
    val message: LiveData<Resource<List<Message>>> get() = _message

    private val _incomingMessage = MutableLiveData<Resource<Message>>()
    val incomingMessage: LiveData<Resource<Message>> get() = _incomingMessage

    private val _createLocalMessage = MutableLiveData<Resource<Message>>()
    val createLocalMessage: LiveData<Resource<Message>> get() = _createLocalMessage

    fun updateMessageList(chatter1Id: User, chatter2Id: User) {
        viewModelScope.launch {
            _message.value = chatter1Id.id?.let { chatter2Id.id?.let { it1 -> getMessages(it, it1) } }
        }
    }

    private suspend fun getMessages(chatter1Id: Int, chatter2Id: Int): Resource<List<Message>> {
        return withContext(Dispatchers.IO) {
            roomMessageRepository.getMessages(chatter1Id, chatter2Id)
        }
    }


}

class MessageViewModelFactory(
    private val remoteMessageRepository: RemoteMessageRepository,
    private val roomMessageRepository: RoomMessageDataSource,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return MessageViewModel(remoteMessageRepository, roomMessageRepository, context) as T
    }

}