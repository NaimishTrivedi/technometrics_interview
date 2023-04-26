package com.om.moviedemo.api.responsemodel

import com.google.gson.annotations.SerializedName

data class ResponseMovieDetailModel(
    @SerializedName("adult")
    var adult: Boolean,
    @SerializedName("poster_path")
    var poster_path: String,
    @SerializedName("backdrop_path")
    var backdrop_path: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("original_language")
    var original_language: String,
    @SerializedName("original_title")
    var original_title: String,
    @SerializedName("release_date")
    var release_date: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("vote_average")
    var vote_average: Float,
    @SerializedName("vote_count")
    var vote_count: Int,
    @SerializedName("overview")
    var overview: String,
    @SerializedName("genres")
    var geners: List<Genres>,
    @SerializedName("spoken_languages")
    var movieLanguages: List<MovieLanguageModel>

)
