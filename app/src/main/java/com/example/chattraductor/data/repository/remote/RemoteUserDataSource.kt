package com.example.chattraductor.data.repository.remote

import com.example.chattraductor.data.model.User
import com.example.chattraductor.data.model.login.AuthRequest
import com.example.chattraductor.utils.Resource

class RemoteUserDataSource: BaseDataSource(), RemoteUserRepository{

    override suspend fun login(authRequest: AuthRequest) = getResult{
        RetrofitClient.apiInterface.login(authRequest)
    }

    override suspend fun findUsers(user: Int?): Resource<List<User>> = getResult {
        RetrofitClient.apiInterface.findUsers(0)
    }

    override suspend fun getUserByChatId(idChat: Int): Resource<List<User>> {
        TODO("Not yet implemented")
    }
}