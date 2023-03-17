package com.example.common.network

import com.example.common.utils.ServerPath
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class HomeRetrofitProvider {
    companion object {
        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(ServerPath.baseUri)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
        val api: HomeService by lazy {
            retrofit.create(HomeService::class.java)
        }
    }
}