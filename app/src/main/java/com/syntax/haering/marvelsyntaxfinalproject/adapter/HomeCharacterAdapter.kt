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

class HomeCharacterAdapter: RecyclerView.Adapter<HomeCharacterAdapter.ItemViewHolder>() {

    private var dataset = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>()

    inner class ItemViewHolder(view: View): ViewHolder(view){
        val characterImage: ImageView = view.findViewById(R.id.home_charImage_iv)
        val characterName: TextView = view.findViewById(R.id.home_charName_tv)
        val characterCard: CardView = view.findViewById(R.id.home_charCard_cv)
    }

    fun submitList(list: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>){
        dataset = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character_card, parent,false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val character = dataset[position]

        val https = character.thumbnail.path.replace("http", "https")

        holder.characterImage.load("$https/portrait_fantastic.${character.thumbnail.extension}"){
            placeholder(R.drawable.marvelcomics_loading)
            error(R.drawable.error404notfound_image)
        }
        holder.characterName.text = character.name

        holder.characterCard.setOnClickListener {
            Navigation.findNavController(holder.itemView).navigate(HomeFragmentDirections.actionHomeFragmentToDetailCharacterFragment(character.id))
        }
    }
}