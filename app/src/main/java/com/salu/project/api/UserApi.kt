package com.salu.project.api

import com.salu.project.model.LoginResponse
import com.salu.project.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @POST("auth/register")
    suspend fun register(@Body user: User): User

    @POST("auth/login")
    suspend fun login(@Body credentials: Map<String, String>): LoginResponse

    @GET("auth/me")
    suspend fun getCurrentUser(): User

    @GET("users")
    suspend fun getAllUsers(): List<User>
}
