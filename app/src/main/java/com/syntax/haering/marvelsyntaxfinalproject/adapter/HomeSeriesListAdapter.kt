package com.syntax.haering.marvelsyntaxfinalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.R
import com.syntax.haering.marvelsyntaxfinalproject.ui.HomeFragmentDirections

class HomeSeriesListAdapter(): RecyclerView.Adapter<HomeSeriesListAdapter.ItemViewHolder>() {

    private var dataset = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>()

    inner class ItemViewHolder(view: View): ViewHolder(view) {
        val serieImage: ImageView = view.findViewById(R.id.item_serieslist_iv)
        val serieCardView: CardView = view.findViewById(R.id.item_serieslist_cv)
        val serieTitle: TextView = view.findViewById(R.id.item_seriesTitle_tv)
    }

    fun submitSeriesList(list: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>){
        dataset = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_serieslist, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val series = dataset[position]

        val https = series.thumbnail.path.replace("http", "https")

        holder.serieImage.load("$https/portrait_medium.${series.thumbnail.extension}"){
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_foreground)
        }
        holder.serieTitle.text = series.title
        holder.serieCardView.setOnClickListener {
            Navigation.findNavController(holder.itemView).navigate(HomeFragmentDirections.actionHomeFragmentToDetailSerieFragment(series.id))
        }

    }
}