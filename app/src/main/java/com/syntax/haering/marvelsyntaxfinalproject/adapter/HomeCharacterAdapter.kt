package com.syntax.haering.marvelsyntaxfinalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.R

class HomeCharacterAdapter: RecyclerView.Adapter<HomeCharacterAdapter.ItemViewHolder>() {

    private var dataset = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>()

    inner class ItemViewHolder(view: View): ViewHolder(view){
        val characterImage: ImageView = view.findViewById(R.id.home_charImage_iv)
        val characterName: TextView = view.findViewById(R.id.home_charName_tv)
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
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_foreground)
        }
        holder.characterName.text = character.name
    }
}