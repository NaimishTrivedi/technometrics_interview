package com.om.moviedemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.om.moviedemo.R
import com.om.moviedemo.api.APIConstant
import com.om.moviedemo.api.responsemodel.MovieItemModel
import com.om.moviedemo.database.entity.CartModel
import com.om.moviedemo.interfaces.OnItemClickListener

class CartAdapter(private val context: Context, private val cartItems: List<CartModel>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cart_items,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartItems.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTxtMovieTitle.text = cartItems.get(position).title
        holder.mTxtTotalSeats.text = "Total seats : "+cartItems.get(position).seats+" X "+context.getString(R.string.lblseatprice)
        holder.mTxtPrice.text = context.getString(R.string.lblrupeesymbol)+" "+(cartItems.get(position).seats * 100.00)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTxtPrice = itemView.findViewById<TextView>(R.id.mTxtPrice)
        val mTxtTotalSeats = itemView.findViewById<TextView>(R.id.mTxtTotalSeats)
        val mTxtMovieTitle = itemView.findViewById<TextView>(R.id.mTxtMovieTitle)
    }
}