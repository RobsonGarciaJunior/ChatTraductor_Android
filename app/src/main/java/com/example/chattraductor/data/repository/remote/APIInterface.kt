package com.example.chattraductor.data.repository.remote

import com.example.chattraductor.data.model.login.AuthRequest
import com.example.chattraductor.data.model.Message
import com.example.chattraductor.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIInterface {

    /*API HIBERNATE */
    @POST("auth/login")
    suspend fun login(@Body authRequest: AuthRequest) : Response<User>
    @GET("messages/findAll/{id}")
    suspend fun getMessages(@Path("id") messageId: Int?) : Response<List<Message>>
    @GET("messages/chat/{chatId}")
    suspend fun getMessageByChatId(@Path("chatId") idChat:Int) : Response<List<Message>>
    @POST("messages")
    suspend fun createMessage(@Body message: Message) : Response<Message>
    @GET("users/find/{email}")
    suspend fun getUserByEmail(@Path("email") email:String) : Response<Int>
    @GET("users/findAll/{id}")
    suspend fun findUsers(@Path("id") userId: Int?) : Response<List<User>>

}