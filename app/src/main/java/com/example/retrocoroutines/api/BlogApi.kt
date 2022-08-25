package com.example.retrocoroutines.api

import com.example.retrocoroutines.models.Post
import com.example.retrocoroutines.models.User
import retrofit2.http.*

interface BlogApi {
    //suspend keyword means that the function should be called from a coroutine

    @GET("posts")
    suspend fun getPosts(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = 10
    ): List<Post>

    @Headers("Platform:Android")   //adding headers static
    @GET("posts/{id}")
    suspend fun getPost(@Path("id") postId: Int): Post

    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Int): User

    //resource body contains the complete new version
    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") postId: Int, @Body post: Post): Post

    //resource body contains only the specific new changes
    @PATCH("posts/{id}")
    suspend fun patchPost(@Path("id") postId: Int, @Body params: Map<String, String>): Post

    @DELETE("posts/{id}") //adding headers dynamically
    suspend fun deletePost(@Header("Auth-Token") auth: String, @Path("id") postId: Int)

    @POST("posts/{id}")
    suspend fun createPost(@Body post: Post): Post

    //Form Data
    @FormUrlEncoded
    @POST("posts/{id}")
    suspend fun createPostUrlEncoded(
        @Field("userId") userId: Int,
        @Field("title") title: String,
        @Field("body") body: String,
    ): Post
}
