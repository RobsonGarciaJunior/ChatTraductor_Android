package com.example.chattraductor.data.repository.local

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.chattraductor.data.model.Chat
import com.example.chattraductor.data.model.Message
import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.repository.local.chat.RoomChatDataSource
import com.example.chattraductor.data.repository.local.message.RoomMessageDataSource
import com.example.chattraductor.data.repository.local.user.RoomUserDataSource
import com.example.chattraductor.data.repository.remote.RemoteMessageRepository
import com.example.chattraductor.data.repository.remote.RemoteUserRepository
import com.example.chattraductor.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopulateLocalDataBase(
    private val chatLocalRepository: RoomChatDataSource,

    private val messageLocalRepository: RoomMessageDataSource,
    private val remoteMessageRepository: RemoteMessageRepository,

    private val userLocalRepository: RoomUserDataSource,
    private val remoteUserRepository: RemoteUserRepository,


    ) : ViewModel() {

    private val _allMessage = MutableLiveData<Resource<List<Message>>>()

    private val _allChat = MutableLiveData<Resource<List<Chat>>>()

    private val _allUser = MutableLiveData<Resource<List<User>>>()

    private val _finish = MutableLiveData<Resource<Boolean>>()
    val finish: LiveData<Resource<Boolean>> get() = _finish

    //////////////////////////////////////////////////////////////////////
    // FUNCION DE INICIO
    fun toInit() {

        viewModelScope.launch {

//                Log.d("p1", "GetAllLastData")
            getAllData()
////                Log.d("p1", "${_allMessage.value?.data}")
//                Log.d("p1", "${_allUser.value?.data}")
//                Log.d("p1", "${_allChat.value?.data}")
//                Log.d("p1", "${_allPendingMessages.value?.status}")
            if (_allMessage.value?.status == Resource.Status.SUCCESS
                && _allUser.value?.status == Resource.Status.SUCCESS
                && _allChat.value?.status == Resource.Status.SUCCESS
//                    && _allPendingMessages.value?.status == Resource.Status.SUCCESS
            ) {
//                    Log.d("p1", "getAllData")
                setAllData()
            }
            _finish.value = Resource.success(true)

        }

    }


    /////////////////////////////////////////////////////////////////////////
    // LLAMADAS A BBDD REMOTA PARA POBLAR ROOM
    private suspend fun getAllData() {
//        _allRole.value = getAllRoles()
        _allUser.value = getAllUsers()
//        _allChat.value = getAllChats()
        _allMessage.value = getAllMessages()
//        val pendingMessage = _pendingMessage.value?.data
//        val pendingMessageRequest = pendingMessage?.map { it.toPendingMessageRequest()}
//        _allPendingMessages.value = setPendingMessages(pendingMessageRequest)
//        val pendingChat = _pendingChat.value?.data
//        val pendingChatRequest = pendingChat?.map { it.toPendingChatRequest()}
//        _allPendingChats.value = setPendingChats(pendingChatRequest)
    }

    private suspend fun getAllMessages(): Resource<List<Message>> {
        return withContext(Dispatchers.IO) {
            Log.d("HOLA", "ENTRA EN GET ALL")

            Log.d("HOLA", "ENTRA IF")
            remoteMessageRepository.getMessages(0)

        }
    }


    private suspend fun getAllUsers(): Resource<List<User>> {
        return withContext(Dispatchers.IO) {
            remoteUserRepository.findUsers(0)
        }
    }

    /* private suspend fun getAllChats(chat: Chat?): Resource<List<Chat>> {
         return withContext(Dispatchers.IO) {
             if (chat != null) {
                 remoteChatRepository.getChats(chat.id)
             } else {
                 remoteChatRepository.getChats(0)
             }
         }
     }*/


    ////////////////////////////////////////////////////////////////////////
    // INSERTS EN ROOM DE LA INFORMACION RECOGIDA EN REMOTO
    private suspend fun setAllData() {
        setAllUsers()
        setAllChats()
//        setAllUsersToChats()
        setAllMessages()
//        updateAllPendingMessages()
//        updateAllPendingChats()
    }

    private suspend fun setAllUsers() {
        return withContext(Dispatchers.IO) {
            val allUser = _allUser.value?.data
            if (allUser != null) {
                for (userRequest in allUser) {
                    Log.d("VENGA", "" + userRequest.phoneNumber1)
                    val user = User(
                        userRequest.id,
                        userRequest.name,
                        userRequest.surname,
                        userRequest.phoneNumber1,
                    )
                    userLocalRepository.createUser(user)
//                    userChatInfo.addAll(userRequest.userChatInfo)
                }
            }
        }
    }

    private suspend fun setAllChats() {
        withContext(Dispatchers.IO) {
            val allChat = _allChat.value?.data
            if (allChat != null) {
                for (chat in allChat) {
                    chatLocalRepository.createChat(chat)
                }
            }
        }
    }

    /*private suspend fun setAllUsersToChats() {
        return withContext(Dispatchers.IO) {
            for (userChatInfo in userChatInfo) {
                chatLocalRepository.addUserToChat(userChatInfo)
            }
        }
    }*/

    private suspend fun setAllMessages() {
        return withContext(Dispatchers.IO) {
            val allMessage = _allMessage.value?.data
            if (allMessage != null) {
                for (messageResponse in allMessage) {
                    val message = Message(
                        messageResponse.id,
                        messageResponse.text,
                        messageResponse.senderId,
                        messageResponse.receiverId,
                    )
                    messageLocalRepository.createMessage(message)
                }
            }
        }
    }
    /*
        private suspend fun updateAllPendingMessages() {
            return withContext(Dispatchers.IO) {
                val allPendingMessagesResponse = _allPendingMessages.value?.data
                if (allPendingMessagesResponse != null) {
                    for (pendingMessageResponse in allPendingMessagesResponse) {
                        val pendingMessage = Message(
                            pendingMessageResponse.id,
                            pendingMessageResponse.text,
                            pendingMessageResponse.sent,
                            pendingMessageResponse.saved,
                            pendingMessageResponse.type,
                            pendingMessageResponse.chatId,
                            pendingMessageResponse.userId
                        )
                        messageLocalRepository.updateMessage(pendingMessage)
                    }
                }

            }
        }

        private suspend fun updateAllPendingChats() {
            return withContext(Dispatchers.IO) {
                val allPendingChatsResponse = _allPendingChats.value?.data
                if (allPendingChatsResponse != null) {
                    for (pendingChatResponse in allPendingChatsResponse) {
                        val pendingChat = Chat(
                            pendingChatResponse.id,
                            pendingChatResponse.name,
                            pendingChatResponse.type,
                            pendingChatResponse.created,
                            pendingChatResponse.deleted,
                            pendingChatResponse.localDeleted,
                            pendingChatResponse.adminId
                        )
                        chatLocalRepository.updateChat(pendingChat)
                    }
                }

            }
        }

        private fun Chat.toPendingChatRequest() =
            PendingChatRequest(
                id,
                name,
                type,
                created,
                deleted,
                adminId
            )

        private fun Message.toPendingMessageRequest() =
            id?.let {
                PendingMessageRequest(
                    chatId,
                    userId,
                    it,
                    text,
                    sent,
                    type
                )
            }
    */
}

class PopulateLocalDataBaseFactory(
    private val chatLocalRepository: RoomChatDataSource,
    //  private val remoteChatRepository: RemoteChatRepository,

    private val messageLocalRepository: RoomMessageDataSource,
    private val remoteMessageRepository: RemoteMessageRepository,

    private val userLocalRepository: RoomUserDataSource,
    private val remoteUserRepository: RemoteUserRepository,

    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return PopulateLocalDataBase(
            chatLocalRepository,
            messageLocalRepository,
            remoteMessageRepository,
            userLocalRepository,
            remoteUserRepository
        ) as T
    }

}