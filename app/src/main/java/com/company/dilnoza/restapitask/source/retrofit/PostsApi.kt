package com.company.dilnoza.restapitask.source.retrofit

import com.company.dilnoza.restapitask.source.room.entity.PostData
import retrofit2.Call
import retrofit2.http.*


interface PostsApi {
    /**
     * 1. Get all posts
     * */
    @GET("posts")
    fun getPosts(): Call<List<PostData>>

    /**
     * 2. add new a post
     * */
    @POST("posts")
    fun add(@Body postData: PostData): Call<PostData>

    /**
     * 3. remove post
     * */
    @DELETE("posts/{id}")
    fun remove(@Path("id")id:Int): Call<Any>

    /**
     * 4. update post
     */
    @PUT("posts/{id}")
    fun update(@Path("id")id:Int,@Body postData: PostData): Call<PostData>
}