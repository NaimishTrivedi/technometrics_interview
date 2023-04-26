package com.om.moviedemo.api

import com.om.moviedemo.api.responsemodel.ResponseMovieDetailModel
import com.om.moviedemo.api.responsemodel.ResponseMoviesModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIInterface {
    @GET("movie/now_playing")
    fun getMoviesData(@Query("api_key") api_key:String) : Call<ResponseMoviesModel>

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") movieId: String,@Query("api_key") api_key:String) : Call<ResponseMovieDetailModel>
}