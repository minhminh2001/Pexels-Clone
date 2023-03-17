package com.example.common.network

import com.example.common.utils.ServerPath
import com.example.model.data.Photo
import com.example.model.data.remote.DataListImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {
    @GET(ServerPath.LIST_IMAGE)
    suspend fun getListImage(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Header("Authorization") token: String
    ): Response<DataListImageResponse>

    @GET(ServerPath.SEARCH_IMAGE)
    suspend fun searchImage(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("query") value: String,
        @Header("Authorization") token: String
    ): Response<DataListImageResponse>

    @GET(ServerPath.IMAGE_DETAIL)
    suspend fun getImageDetail(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Response<Photo>
}