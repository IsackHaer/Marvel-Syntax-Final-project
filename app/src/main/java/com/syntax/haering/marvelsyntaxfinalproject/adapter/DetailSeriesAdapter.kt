package com.syntax.haering.marvelsyntaxfinalproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.R
import com.syntax.haering.marvelsyntaxfinalproject.databinding.ItemDetailCardBinding

class DetailSeriesAdapter: RecyclerView.Adapter<DetailSeriesAdapter.ItemViewHolder>() {

    private var seriesList = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>()

    inner class ItemViewHolder(val binding: ItemDetailCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(series: com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result){
            binding.detailCardCv.setOnClickListener {
                TODO("code goes here")
            }
            val https = series.thumbnail.path.replace("http", "https")
            binding.detailImageIv.load("$https/portrait_medium.${series.thumbnail.extension}"){
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }

            binding.detailTitleNameTv.text = series.title
        }
    }

    fun submitSerieList(list: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>){
        seriesList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemDetailCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(seriesList[position])
    }
}