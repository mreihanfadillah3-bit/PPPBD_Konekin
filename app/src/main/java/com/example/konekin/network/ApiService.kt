package com.example.konekin.network

import com.example.konekin.model.Data
import com.example.konekin.model.UserRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("employees")
    fun getAllUsers(): Call<List<Data>>

    @GET("employees/{id}")
    fun getUserById(@Path("id") id: String): Call<Data>

    @POST("employees")
    fun addUser(@Body user: UserRequest): Call<Data>

    @PUT("employees/{id}")
    fun updateUser(@Path("id") id: String, @Body user: UserRequest): Call<Data>

    @DELETE("employees/{id}")
    fun deleteUser(@Path("id") id: String): Call<Data>
}