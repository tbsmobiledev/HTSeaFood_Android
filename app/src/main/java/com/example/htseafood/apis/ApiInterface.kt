package com.example.htseafood.apis

import com.example.htseafood.model.request.LoginRequest
import com.example.htseafood.utils.Constants
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @POST(Constants.SIGN_IN)
    fun login(@Body loginRequest: LoginRequest): Call<JsonObject>

}