package com.example.mobiletestzemogamiguelpuentes.services

import com.example.mobiletestzemogamiguelpuentes.model.Comment
import com.example.mobiletestzemogamiguelpuentes.model.Post
import com.example.mobiletestzemogamiguelpuentes.model.User
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @GET("/posts")
    @Headers("Accept:Application/json")
    fun requestAllPosts(
    ): Call<List<Post>>

    @GET("/posts/{id}/comments")
    @Headers("Accept:Application/json")
    fun requestPostComments(
        @Path("id") id: Int
    ): Call<List<Comment>>

    @GET("/posts/{id}")
    @Headers("Accept:Application/json")
    fun requestPostDetail(
        @Path("id") id: Int
    ): Call<Post>

    @GET("/users/{id}")
    @Headers("Accept:Application/json")
    fun requestUserDetail(
        @Path("id") id: Int
    ): Call<User>
}
