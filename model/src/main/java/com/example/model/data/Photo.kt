package com.example.model.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
class Photo : Parcelable {
    @SerializedName("id")
    @Expose
    var id: Long = 0

    @SerializedName("width")
    @Expose
    var width: Long = 0

    @SerializedName("height")
    @Expose
    var height: Long = 0

    @SerializedName("url")
    @Expose
    var url: String = ""

    @SerializedName("photographer")
    @Expose
    var photographer: String = ""

    @SerializedName("photographer_url")
    @Expose
    var photographerUrl: String = ""

    @SerializedName("photographer_id")
    @Expose
    var photographerId: Long = 0

    @SerializedName("avg_color")
    @Expose
    var avgColor: String = ""

    @SerializedName("liked")
    @Expose
    var liked: Boolean = false

    @SerializedName("alt")
    @Expose
    var alt: String = ""

    @SerializedName("src")
    @Expose
    var source: Source = Source()
}