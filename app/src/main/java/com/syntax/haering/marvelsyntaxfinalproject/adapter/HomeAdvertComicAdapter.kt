package com.syntax.haering.marvelsyntaxfinalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.R
import com.syntax.haering.marvelsyntaxfinalproject.ui.HomeFragmentDirections

class HomeAdvertComicAdapter: RecyclerView.Adapter<HomeAdvertComicAdapter.ItemViewHolder>() {

    private var dataset = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>()

    inner class ItemViewHolder(view: View): ViewHolder(view) {
        val comicAdvertImage: ImageView = view.findViewById(R.id.item_advertComics_iv)
        val comicAdvertTitle: TextView = view.findViewById(R.id.item_advertComic_title_tv)
    }

    fun submitComicAdverts(list: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>){
        dataset = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_advert_comics, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val comic = dataset[position]

        val https = comic.thumbnail.path.replace("http", "https")

        holder.comicAdvertImage.load("$https/landscape_incredible.${comic.thumbnail.extension}"){
            placeholder(R.drawable.marvelcomics_loading)
            error(R.drawable.error404notfound_image)
        }
        holder.comicAdvertTitle.text = comic.title

        holder.comicAdvertImage.setOnClickListener {
            Navigation.findNavController(holder.itemView).navigate(HomeFragmentDirections.actionHomeFragmentToDetailComicFragment(comic.id))
        }
    }
}