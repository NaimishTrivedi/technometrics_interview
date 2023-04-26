package com.om.moviedemo.api.responsemodel

import com.google.gson.annotations.SerializedName

data class Dates(
    @SerializedName("maximum")
    var maximum: String,
    @SerializedName("minimum")
    var minimum: String
)
