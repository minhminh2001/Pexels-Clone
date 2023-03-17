package com.example.common.utils

class ServerPath {
    companion object {
        var baseUri = "https://api.pexels.com"
        var token = "563492ad6f9170000100000102628799b032460b975a4192c71626d2"
        const val LIST_IMAGE = "/v1/curated"
        const val SEARCH_IMAGE = "/v1/search"
        const val IMAGE_DETAIL = "/v1/photos/{id}"
    }
}