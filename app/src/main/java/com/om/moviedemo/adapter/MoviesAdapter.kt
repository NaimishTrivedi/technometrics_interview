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

class MoviesAdapter(private val context: Context,private val moviesItems: ArrayList<MovieItemModel>) : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_movies,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return moviesItems.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTxtMovieTitle.text = moviesItems.get(position).title
        holder.mTxtVoteCount.text = moviesItems.get(position).vote_count.toString() + " votes"
        holder.mTxtVoteAverage.text = moviesItems.get(position).vote_average.toString()
        Glide.with(context)
            .load(APIConstant.IMAGE_URL+moviesItems.get(position).poster_path)
            .placeholder(R.drawable.movie_place_holder)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(holder.mIvMoviePoster)

        holder.mIvMoviePoster.setOnClickListener {
                onItemClickListener?.onItemClick(position)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mIvMoviePoster = itemView.findViewById<ImageView>(R.id.mIvMoviePoster)
        val mTxtVoteAverage = itemView.findViewById<TextView>(R.id.mTxtVoteAverage)
        val mTxtVoteCount = itemView.findViewById<TextView>(R.id.mTxtVoteCount)
        val mTxtMovieTitle = itemView.findViewById<TextView>(R.id.mTxtMovieTitle)
    }
}