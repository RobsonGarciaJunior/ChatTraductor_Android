package com.example.chattraductor.data.repository.remote

import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.model.login.AuthRequest
import com.example.chattraductor.data.model.login.LoginUser
import com.example.chattraductor.utils.Resource

interface RemoteUserRepository {

    suspend fun login(authRequest: AuthRequest): Resource<LoginUser>
    suspend fun findUsers(user: Int?): Resource<List<User>>
    suspend fun getUserByChatId(idChat:Int) : Resource<List<User>>
}