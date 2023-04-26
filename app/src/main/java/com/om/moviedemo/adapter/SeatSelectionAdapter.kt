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
import com.om.moviedemo.interfaces.OnItemClickListener
import com.om.moviedemo.model.SeatSelectionModel

class SeatSelectionAdapter() : RecyclerView.Adapter<SeatSelectionAdapter.ViewHolder>() {

    private val seats: ArrayList<SeatSelectionModel> = ArrayList()
    private var onSeatSelectionListener:OnSeatSelectionListener? = null
    init {
        for(i in 1..10){
            seats.add(SeatSelectionModel(i,i == 1))
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_seat_selection,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return seats.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.mTxtSeat.setText(seats.get(position).seatCount.toString())
        holder.mTxtSeat.isSelected = seats.get(position).isSelected
        holder.mTxtSeat.setOnClickListener {
            if(!seats.get(position).isSelected){
                resetSelection()
                seats.get(position).isSelected = true
                onSeatSelectionListener?.onSeatSelection(seats.get(position).seatCount)
                notifyDataSetChanged()
            }
        }
    }

    fun setOnSeatSelectionListener(onSeatSelectionListener: OnSeatSelectionListener){
        this.onSeatSelectionListener = onSeatSelectionListener
    }

    fun resetSelection(){
        for(item in seats){
            item.isSelected = false
        }
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mTxtSeat = itemView.findViewById<TextView>(R.id.mTxtSeat)
    }

    interface OnSeatSelectionListener{
        fun onSeatSelection(count : Int)
    }
}