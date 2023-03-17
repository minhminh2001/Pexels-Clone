package com.example.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Source : java.io.Serializable {
    @SerializedName("original")
    @Expose
    var original: String = ""

    @SerializedName("large2x")
    @Expose
    var large2x: String = ""

    @SerializedName("large")
    @Expose
    var large: String = ""

    @SerializedName("medium")
    @Expose
    var medium: String = ""

    @SerializedName("small")
    @Expose
    var small: String = ""

    @SerializedName("portrait")
    @Expose
    var portrait: String = ""

    @SerializedName("landscape")
    @Expose
    var landscape: String = ""

    @SerializedName("tiny")
    @Expose
    var tiny: String = ""
}