package com.example.chattraductor.ui.home

import android.content.Context
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chattraductor.data.model.login.AuthRequest
import com.example.chattraductor.data.model.login.LoginUser
import com.example.chattraductor.data.repository.local.CommonUserRepository
import com.example.chattraductor.data.repository.remote.RemoteUserRepository
import com.example.chattraductor.utils.MyApp
import com.example.chattraductor.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val userRepository: RemoteUserRepository,
    private val context: Context
) : ViewModel() {
    private val _loginUser = MutableLiveData<Resource<LoginUser>>()
    val loginUser: LiveData<Resource<LoginUser>> get() = _loginUser
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    private suspend fun logIn(email: String, password: String): Resource<LoginUser> {
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