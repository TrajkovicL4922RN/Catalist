package com.example.myapplication.model

import retrofit2.http.GET

interface CatApi {
    @GET("/v1/breeds/")
    suspend fun getAllBreads(): List<Cat>
}