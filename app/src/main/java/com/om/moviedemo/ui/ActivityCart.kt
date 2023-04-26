package com.om.moviedemo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.om.moviedemo.R
import com.om.moviedemo.adapter.CartAdapter
import com.om.moviedemo.database.cartdatabase.CartDatabase
import com.om.moviedemo.database.entity.CartModel
import com.om.moviedemo.databinding.ActivityCartBinding
import kotlinx.coroutines.launch

class ActivityCart : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private val cartDatabase by lazy { CartDatabase(this).getCartDao() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mRVCartList.layoutManager =LinearLayoutManager(this)
        binding.mRVCartList.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        binding.mIvBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        hideFooterView()

        getCartData()
    }

    fun getCartData(){
        lifecycleScope.launch {
            cartDatabase.getCartList().collect{
                if(it.isNotEmpty()){
                    binding.mRVCartList.adapter = CartAdapter(this@ActivityCart,it)
                    setTotalPrice(it)
                    showFooterView()
                }
            }

        }
    }

    fun hideFooterView(){
        binding.mFooterView.visibility = View.GONE
    }

    fun showFooterView(){
        binding.mFooterView.visibility = View.VISIBLE
    }

    fun setTotalPrice(items : List<CartModel>){
        var count: Int = 0

        for(cart in items){
            count += cart.seats
        }

        binding.mTxtTotalPrice.text = this.getString(R.string.lblrupeesymbol)+" "+(count * 100.00)
    }
}