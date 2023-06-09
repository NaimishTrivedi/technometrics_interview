package com.om.moviedemo.ui

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.om.moviedemo.R
import com.om.moviedemo.adapter.SeatSelectionAdapter
import com.om.moviedemo.api.APIClient
import com.om.moviedemo.api.APIConstant
import com.om.moviedemo.api.responsemodel.ResponseMovieDetailModel
import com.om.moviedemo.database.cartdatabase.CartDatabase
import com.om.moviedemo.database.entity.CartModel
import com.om.moviedemo.databinding.ActivityMovieDetailBinding
import kotlinx.coroutines.launch
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
    private var movieTitle:String? = "";
    private val cartDatabase by lazy { CartDatabase(this).getCartDao() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent==null){
            finish()
            return
        }

        movieId = intent.getStringExtra(MOVIE_ID)
        movieTitle = intent.getStringExtra(MOVIE_TITLE)
        binding.mTxtToolbarTitle.text = movieTitle!!

        binding.mIvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.mBtnBookTicktes.setOnClickListener {
            showSeatSelectionDialog()
        }
        getMovieDetails()

        checkAlreadyAddedInCart()
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

    fun showSeatSelectionDialog(){
        val dialog = BottomSheetDialog(this,R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.dialog_seat_selection, null)
        dialog.setContentView(view)

        val mTxtTotalPrice = view.findViewById<TextView>(R.id.mTxtTotalPrice)
        mTxtTotalPrice.setText(getString(R.string.lblrupeesymbol)+" 100.00") // default first selection
        var cartModel = CartModel(movieId!!,movieTitle!!,1) //Default cart model prepare

        val mRvSeatSelectionList = view.findViewById<RecyclerView>(R.id.mRvSeatSelectionList)
        mRvSeatSelectionList.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        val seatSelectionAdapter = SeatSelectionAdapter()
        seatSelectionAdapter.setOnSeatSelectionListener(object : SeatSelectionAdapter.OnSeatSelectionListener{
            override fun onSeatSelection(count: Int) {
                mTxtTotalPrice.setText(getString(R.string.lblrupeesymbol)+" "+(count * 100.00))
                cartModel.seats = count
            }

        })
        mRvSeatSelectionList.adapter = seatSelectionAdapter

       view.findViewById<Button>(R.id.mBtnAddCart).setOnClickListener {
            lifecycleScope.launch {
                cartDatabase.addCart(cartModel)
                Toast.makeText(this@ActivityMovieDetail,"Added Successfully!",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                checkAlreadyAddedInCart();
            }
       }
        dialog.show()
    }

    fun checkAlreadyAddedInCart(){
        lifecycleScope.launch {
            if(cartDatabase.isAlreadyInCart(movieId!!)){
                binding.mBtnBookTicktes.backgroundTintList = ContextCompat.getColorStateList(this@ActivityMovieDetail,R.color.ligth_grey)
                binding.mBtnBookTicktes.isEnabled = false
            }else{
                binding.mBtnBookTicktes.backgroundTintList = ContextCompat.getColorStateList(this@ActivityMovieDetail,R.color.orange)
                binding.mBtnBookTicktes.isEnabled = true
            }
        }
    }
}