package com.om.moviedemo.ui

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.om.moviedemo.R
import com.om.moviedemo.api.APIClient
import com.om.moviedemo.api.APIConstant
import com.om.moviedemo.api.responsemodel.ResponseMovieDetailModel
import com.om.moviedemo.databinding.ActivityMovieDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivityMovieDetail : AppCompatActivity() {
    companion object{
        public const val MOVIE_ID: String = "movie_id"
        public const val MOVIE_TITLE: String = "movie_title"
    }
    private lateinit var binding: ActivityMovieDetailBinding
    private var movieId:String? = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent==null){
            finish()
            return
        }

        movieId = intent.getStringExtra(MOVIE_ID)
        binding.mTxtToolbarTitle.text = intent.getStringExtra(MOVIE_TITLE)

        getMovieDetails()
    }

    fun getMovieDetails(){
        binding.mProgressBarView.visibility= View.VISIBLE
        binding.mRelDetailContainer.visibility = View.GONE
        val call = APIClient.getInstance().getMovieDetails(movieId!!,APIConstant.API_KEY)
        call.enqueue(object : Callback<ResponseMovieDetailModel>{
            override fun onResponse(
                call: Call<ResponseMovieDetailModel>,
                response: Response<ResponseMovieDetailModel>
            ) {
                if(response.isSuccessful && response.body()!=null){
                    binding.mProgressBarView.visibility= View.GONE
                    binding.mRelDetailContainer.visibility = View.VISIBLE
                    setDetailData(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ResponseMovieDetailModel>, t: Throwable) {

            }

        })
    }

    fun setDetailData(body: ResponseMovieDetailModel) {
        Glide.with(this)
            .load(APIConstant.IMAGE_URL+body.backdrop_path)
            .placeholder(R.drawable.movie_place_holder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(binding.mIvMoviePoster)

        binding.mTxtVoteCount.text = body.vote_count.toString() + " votes"
        binding.mTxtVoteAverage.text = String.format("%.1f",body.vote_average)

        var stringBuilder: StringBuilder = StringBuilder()
        if(body.geners.isNotEmpty()){
            stringBuilder.append(this.getString(R.string.strDot)+" ")
            for(gener in body.geners){
                stringBuilder.append(gener.name+",")
            }
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
        }

        if(body.release_date.isNotEmpty()){
            stringBuilder.append(" "+this.getString(R.string.strDot)+" "+body.release_date)
        }

        binding.mTxtSubTitle.text = stringBuilder.toString()

        binding.mTxtDescription.text = body.overview

        if(body.movieLanguages.isNotEmpty()){
            for(lang in body.movieLanguages){
                val itemTextView : TextView = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0,10,0,0)
                itemTextView.layoutParams = layoutParams
                itemTextView.text = lang.name
                itemTextView.setTextColor(ContextCompat.getColor(this,R.color.black))
                itemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f)
                itemTextView.setBackgroundColor(ContextCompat.getColor(this,R.color.black_transparent_50))
                itemTextView.setTypeface(null,Typeface.BOLD)
                itemTextView.setPadding(2,2,2,2)

                binding.mLinearLanguageContainer.addView(itemTextView)
            }
        }
    }

}