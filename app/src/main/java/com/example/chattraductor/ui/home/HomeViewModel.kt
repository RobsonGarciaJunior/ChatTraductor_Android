package com.example.chattraductor.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.model.login.AuthRequest
import com.example.chattraductor.data.repository.remote.RemoteUserRepository
import com.example.chattraductor.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val userRepository: RemoteUserRepository,
    private val context: Context
) : ViewModel() {
    private val _loginUser = MutableLiveData<Resource<User>>()
    val loginUser: LiveData<Resource<User>> get() = _loginUser
    private val _text = MutableLiveData<String>().apply {
        value = "LOGIN"
    }
    val text: LiveData<String> = _text
    private suspend fun logIn(email: String, password: String): Resource<User> {
        return withContext(Dispatchers.IO) {
            val user = AuthRequest(email, password)
            userRepository.login(user)
        }
    }

    fun onLogIn(email: String, password: String) {
        viewModelScope.launch {
            _loginUser.value = logIn(email, password)
        }
    }

}
class HomeViewModelFactory(
    private val userRepository: RemoteUserRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return HomeViewModel(userRepository, context) as T
    }

}