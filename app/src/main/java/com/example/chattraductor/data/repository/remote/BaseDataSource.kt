package com.example.chattraductor.data.repository.remote

import com.example.chattraductor.utils.Resource
import retrofit2.Response

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resource.success(body)
                } else {
                    // podria no devolver datos...
                    return Resource.success()
                    // el 204 hay que tratarlo en algun lado. Dara success sin datos
                }
            }

            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        return Resource.error("Network call has failed for a following reason: $message")
    }

}