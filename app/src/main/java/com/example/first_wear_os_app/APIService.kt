package com.example.first_wear_os_app

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface APIService {
    @POST
    suspend fun postData(@Url url: String, @Body location: Location): Response<APIResponse>
}