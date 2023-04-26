package com.om.moviedemo.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.om.moviedemo.adapter.MoviesAdapter
import com.om.moviedemo.api.APIClient
import com.om.moviedemo.api.APIConstant
import com.om.moviedemo.api.responsemodel.MovieItemModel
import com.om.moviedemo.api.responsemodel.ResponseMoviesModel
import com.om.moviedemo.database.cartdatabase.CartDatabase
import com.om.moviedemo.databinding.ActivityMainBinding
import com.om.moviedemo.interfaces.OnItemClickListener
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var moviesItems:ArrayList<MovieItemModel> = ArrayList()
    private var moviesAdapter: MoviesAdapter? = null
    private val cartDatabase by lazy { CartDatabase(this).getCartDao() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mIvCartButton.setOnClickListener {
            startActivity(Intent(this,ActivityCart::class.java))
        }

        binding.mRVMovieList.layoutManager = GridLayoutManager(this,2)
        moviesAdapter = MoviesAdapter(this,moviesItems)
        moviesAdapter?.setOnItemClickListener(object : OnItemClickListener{
            override fun onItemClick(pos: Int) {
                val intent:Intent = Intent(this@MainActivity,ActivityMovieDetail::class.java)
                intent.putExtra(ActivityMovieDetail.MOVIE_ID,moviesItems.get(pos).id.toString())
                intent.putExtra(ActivityMovieDetail.MOVIE_TITLE,moviesItems.get(pos).title)
                startActivity(intent)
            }

        })
        binding.mRVMovieList.adapter = moviesAdapter

        getMoviesData()

        observeCart()
    }

    fun getMoviesData(){
        binding.mProgressBarView.visibility = View.VISIBLE
        val call = APIClient.getInstance().getMoviesData(APIConstant.API_KEY)
        call.enqueue(object : Callback<ResponseMoviesModel>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseMoviesModel>,
                response: Response<ResponseMoviesModel>
            ) {
               if(response.isSuccessful){
                   binding.mProgressBarView.visibility = View.GONE
                   if(response.body()?.results!=null && response.body()?.results!!.isNotEmpty()){
                       moviesItems.addAll(response.body()?.results!!)
                       moviesAdapter?.notifyDataSetChanged()
                   }
               }
            }

            override fun onFailure(call: Call<ResponseMoviesModel>, t: Throwable) {

            }

        })
    }

    fun observeCart(){
        lifecycleScope.launch {
            cartDatabase.getCartList().collect{
                if(it.isNotEmpty()){
                    binding.mTxtCartItemCount.visibility = View.VISIBLE
                    binding.mTxtCartItemCount.setText(it.size.toString())
                }else{
                    binding.mTxtCartItemCount.visibility = View.GONE
                }
            }
        }
    }
}