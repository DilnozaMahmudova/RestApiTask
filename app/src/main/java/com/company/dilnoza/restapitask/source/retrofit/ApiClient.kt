package com.company.dilnoza.restapitask.source.retrofit

import com.company.dilnoza.restapitask.app.App
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val logging =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(ChuckInterceptor(App.instance))
        .build()
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}