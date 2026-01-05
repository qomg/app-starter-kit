package com.example.appstarterkit.data.remote

import com.example.appstarterkit.data.remote.dto.ExampleDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("examples")
    suspend fun getExamples(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): List<ExampleDto>

    @GET("examples/{id}")
    suspend fun getExampleById(@Path("id") id: String): ExampleDto
}
