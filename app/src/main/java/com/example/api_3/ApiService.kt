package com.example.api_3

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

// Interface Retrofit pour les appels API
interface ApiService {
    @GET("HotelAPI/readAll.php")
    fun getHotel(): Call<List<Hotel>>
    @POST("HotelAPI/create.php")
    fun addRoom(@Body hotel: Hotel): Call<AddResponse>

}