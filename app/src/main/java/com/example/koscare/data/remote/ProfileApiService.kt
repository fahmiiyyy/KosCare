package com.example.koscare.data.remote

import com.example.koscare.data.model.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ProfileApiService {

    @GET("rest/v1/profiles")
    suspend fun getProfile(

        @Query("select")
        select: String = "*",

        @Query("user_id")
        userId: String

    ): Response<List<UserProfile>>

    @POST("rest/v1/profiles")
    suspend fun insertProfile(

        @Body profile: UserProfile,

        @Header("Prefer")
        prefer: String = "return=representation"

    ): Response<List<UserProfile>>

    @PATCH("rest/v1/profiles")
    suspend fun updateProfile(

        @Query("user_id")
        userId: String,

        @Body body: Map<String, String?>

    ): Response<Unit>
}