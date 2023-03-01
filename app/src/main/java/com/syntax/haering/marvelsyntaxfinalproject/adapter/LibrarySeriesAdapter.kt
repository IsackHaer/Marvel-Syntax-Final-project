package com.syntax.haering.marvelsyntaxfinalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.R

class LibrarySeriesAdapter: RecyclerView.Adapter<LibrarySeriesAdapter.ItemViewHolder>() {

    private var dataset = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>()

    inner class ItemViewHolder(view: View): ViewHolder(view){
        val LibraryConstraintLayout: ConstraintLayout = view.findViewById(R.id.search_constraintLayout)
        val LibraryImage: ImageView = view.findViewById(R.id.search_image_iv)
        val LibraryTitle: TextView = view.findViewById(R.id.search_title_tv)
        val LibraryCreator: TextView = view.findViewById(R.id.search_creator_tv)
    }

    fun submitSavedSeries(list: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>){
        dataset = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_results, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val serie = dataset[position]
        val https = serie.thumbnail.path.replace("http", "https")

        holder.LibraryImage.load("$https/portrait_small.${serie.thumbnail.extension}")
        holder.LibraryTitle.text = serie.title
        holder.LibraryCreator.text = serie.creators.items.first().name

        holder.LibraryConstraintLayout.setOnClickListener {
            //todo code here
        }
    }


}