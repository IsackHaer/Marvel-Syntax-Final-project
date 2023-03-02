package com.syntax.haering.marvelsyntaxfinalproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.R
import com.syntax.haering.marvelsyntaxfinalproject.databinding.ItemDetailCardBinding

class DetailComicsAdapter: RecyclerView.Adapter<DetailComicsAdapter.ItemViewHolder>() {

    private var comicsList = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>()

    inner class ItemViewHolder(val binding: ItemDetailCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comics: com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result){
            binding.detailCardCv.setOnClickListener {
                TODO("code goes here")
            }
            val https = comics.thumbnail.path.replace("http", "https")
            binding.detailImageIv.load("$https/portrait_medium.${comics.thumbnail.extension}"){
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_foreground)
            }

            binding.detailTitleNameTv.text = comics.title
        }
    }

    fun submitComicsList(list: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>){
        comicsList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemDetailCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return comicsList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(comicsList[position])
    }
}