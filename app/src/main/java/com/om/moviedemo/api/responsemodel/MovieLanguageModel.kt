package com.om.moviedemo.api.responsemodel

import com.google.gson.annotations.SerializedName

data class MovieLanguageModel(
    @SerializedName("name")
    var name: String,
    @SerializedName("english_name")
    var english_name: String
)
