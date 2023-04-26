package com.om.moviedemo.api.responsemodel

import com.google.gson.annotations.SerializedName

data class ResponseMoviesModel(
    @SerializedName("dates")
    var dates: Dates,
    @SerializedName("page")
    var page: Int,
    @SerializedName("total_pages")
    var total_pages: Int,
    @SerializedName("total_results")
    var total_results: Int,
    @SerializedName("results")
    var results: List<MovieItemModel>
)
