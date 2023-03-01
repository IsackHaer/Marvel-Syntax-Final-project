package com.syntax.haering.marvelsyntaxfinalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.syntax.haering.marvelsyntaxfinalproject.R

class LibraryCharacterAdapter: RecyclerView.Adapter<LibraryCharacterAdapter.ItemViewHolder>() {

    private var dataset = mutableListOf<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>()

    inner class ItemViewHolder(view: View): ViewHolder(view){
        val characterImage: ImageView = view.findViewById(R.id.detail_image_iv)
        val characterCard: CardView = view.findViewById(R.id.detail_card_cv)
    }

    fun submitSavedCharacters(list: MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>) {
        dataset = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_library_charactercards, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val character = dataset[position]
        val https = character.thumbnail.path.replace("http", "https")

        holder.characterImage.load("$https/portrait_medium.${character.thumbnail.extension}"){
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_foreground)
        }
    }
}