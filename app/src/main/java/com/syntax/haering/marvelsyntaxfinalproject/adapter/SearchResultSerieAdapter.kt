package com.syntax.haering.marvelsyntaxfinalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.R
import com.syntax.haering.marvelsyntaxfinalproject.ui.SearchFragmentDirections

class SearchResultSerieAdapter: RecyclerView.Adapter<SearchResultSerieAdapter.ItemViewHolder>() {

    private var dataset = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>()

    inner class ItemViewHolder(view: View): ViewHolder(view){
        val searchConstraintLayout: ConstraintLayout = view.findViewById(R.id.search_constraintLayout)
        val searchImage: ImageView = view.findViewById(R.id.search_image_iv)
        val searchTitle: TextView = view.findViewById(R.id.search_title_tv)
        val searchCreator: TextView = view.findViewById(R.id.search_creator_tv)
        val searchPageCount: TextView = view.findViewById(R.id.search_pagecount_tv)
    }

    fun submitSearchList(list: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>){
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
        val result = dataset[position]
        val https = result.thumbnail.path.replace("http", "https")

        holder.searchImage.load("$https/portrait_fantastic.${result.thumbnail.extension}"){
            placeholder(R.drawable.marvelcomics_loading)
            error(R.drawable.error404notfound_image)
        }

        holder.searchTitle.text = result.title

        try {
            holder.searchCreator.text = result.creators.items.first().name
        } catch (e: Exception){
            holder.searchCreator.text = "Unknown"
        }

        holder.searchPageCount.text = "Comics: ${result.comics.items.size}"

        holder.searchConstraintLayout.setOnClickListener {
            Navigation.findNavController(holder.itemView).navigate(SearchFragmentDirections.actionSearchFragmentToDetailSerieFragment(result.id))
        }
    }
}