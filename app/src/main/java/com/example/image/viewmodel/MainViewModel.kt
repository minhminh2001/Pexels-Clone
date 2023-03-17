package com.example.image.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.common.repository.HomeRepository
import com.example.common.utils.Resource
import com.example.common.utils.ServerPath
import com.example.image.utils.Constants
import com.example.model.data.Photo
import com.example.model.data.remote.DataListImageResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val homeRepository: HomeRepository, private val app: Application
) : AndroidViewModel(app) {
    val imageListLiveData: MutableLiveData<Resource<DataListImageResponse>> = MutableLiveData()
    var imageListNewsPage = 1

    val searchImageLiveData: MutableLiveData<Resource<DataListImageResponse>> = MutableLiveData()
    var searchNewsPage = 1

    val deleteImageLiveData: MutableLiveData<Photo> = MutableLiveData()

    val imageDetailLiveData: MutableLiveData<Resource<Photo>> = MutableLiveData()

    init {
        getListImages(1, Constants.DEFAULT_PER_PAGE, ServerPath.token)
    }

    fun getImageDetail(id: Long) = viewModelScope.launch {
        imageDetailLiveData.postValue(Resource.Loading())
        try {
            val response = homeRepository.getImageDetail(id)
            imageDetailLiveData.postValue(handleImageDetailResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> imageDetailLiveData.postValue(Resource.Error("Network Failure"))
                else -> imageDetailLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    fun getListImages(page: Int, perPage: Int, token: String) = viewModelScope.launch {
        getListImage(page, perPage, token)
    }

    fun searchImages(page: Int, value: String, perPage: Int, token: String) =
        viewModelScope.launch {
            searchImage(page, value, perPage, token)
        }

    private suspend fun getListImage(page: Int, perPage: Int, token: String) {
        imageListLiveData.postValue(Resource.Loading())
        try {
            val response = homeRepository.getListImage(page, perPage, token)
            imageListLiveData.postValue(handleListImageResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> imageListLiveData.postValue(Resource.Error("Network Failure"))
                else -> imageListLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun searchImage(page: Int, value: String, perPage: Int, token: String) {
        searchImageLiveData.postValue(Resource.Loading())
        try {
            val response = homeRepository.searchImage(page, value, perPage, token)
            searchImageLiveData.postValue(handleSearchNewsResponse(response))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchImageLiveData.postValue(Resource.Error("Network Failure"))
                else -> searchImageLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleSearchNewsResponse(response: Response<DataListImageResponse>): Resource<DataListImageResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleListImageResponse(response: Response<DataListImageResponse>): Resource<DataListImageResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                imageListNewsPage++
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleImageDetailResponse(response: Response<Photo>): Resource<Photo> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
