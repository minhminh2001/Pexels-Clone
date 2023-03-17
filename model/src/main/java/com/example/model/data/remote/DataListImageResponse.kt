package com.example.model.data.remote

import com.example.model.data.Photo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataListImageResponse {
    @SerializedName("page")
    @Expose
    var page: Int = 0

    @SerializedName("per_page")
    @Expose
    var perPage: Int = 0

    @SerializedName("photos")
    @Expose
    var photos: MutableList<Photo> = mutableListOf()

    @SerializedName("total_results")
    @Expose
    var totalResults: Int = 0

    @SerializedName("next_page")
    @Expose
    var nextPage: String = ""

}