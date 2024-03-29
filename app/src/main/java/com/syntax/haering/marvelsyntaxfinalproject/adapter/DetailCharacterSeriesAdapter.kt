package com.syntax.haering.marvelsyntaxfinalproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.R
import com.syntax.haering.marvelsyntaxfinalproject.databinding.ItemDetailCardBinding
import com.syntax.haering.marvelsyntaxfinalproject.ui.DetailCharacterFragmentDirections

class DetailCharacterSeriesAdapter: RecyclerView.Adapter<DetailCharacterSeriesAdapter.ItemViewHolder>() {

    private var seriesList = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>()

    inner class ItemViewHolder(val binding: ItemDetailCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(series: com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result){
            binding.detailCardCv.setOnClickListener {
                Navigation.findNavController(itemView).navigate(DetailCharacterFragmentDirections.actionDetailCharacterFragmentToDetailSerieFragment(series.id))
            }
            val https = series.thumbnail.path.replace("http", "https")
            binding.detailImageIv.load("$https/portrait_medium.${series.thumbnail.extension}"){
                placeholder(R.drawable.marvelcomics_loading)
                error(R.drawable.error404notfound_image)
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