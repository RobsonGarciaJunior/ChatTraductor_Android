package com.example.chattraductor.data.repository.local

import com.example.chattraductor.data.model.User
import com.example.chattraductor.utils.Resource

interface CommonUserRepository {
    suspend fun getUsers() : Resource<List<User>>
    suspend fun createUser(user: User) : Resource<User>
}