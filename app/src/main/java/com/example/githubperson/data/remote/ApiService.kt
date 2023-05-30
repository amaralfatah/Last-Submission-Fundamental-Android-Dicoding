package com.example.githubperson.data.remote


import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getPerson(
        @Query("q") query: String
    ): Call<ResponseGithubPerson>

    @GET("users/{username}")
    fun getDetailPerson(@Path("username") username: String): Call<ResponseGithubPersonDetails>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}