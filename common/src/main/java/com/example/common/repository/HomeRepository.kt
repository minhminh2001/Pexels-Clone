package com.example.common.repository

import com.example.common.network.HomeRetrofitProvider
import com.example.common.utils.ServerPath

class HomeRepository {
    suspend fun getListImage(page: Int, perPage: Int, token: String) =
        HomeRetrofitProvider.api.getListImage(page, perPage, token)

    suspend fun searchImage(page: Int, valueSearch: String, perPage: Int, token: String) =
        HomeRetrofitProvider.api.searchImage(page, perPage, valueSearch, token)

    suspend fun getImageDetail(id: Long) =
        HomeRetrofitProvider.api.getImageDetail(id, ServerPath.token)
}