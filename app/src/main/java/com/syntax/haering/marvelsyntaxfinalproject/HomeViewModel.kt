package com.syntax.haering.marvelsyntaxfinalproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syntax.haering.marvelsyntaxfinalproject.data.Repository
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.MarvelApi
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    val repository = Repository(MarvelApi)
    val character = repository.character
    val advertComics = repository.comicAdverts
    val homeSeriesList = repository.homeSeriesList

    val singleCharacter = repository.singleCharacter
    val searchedCharacter = repository.searchedCharacter


    init {
        loadHomeScreenWithData()
        loadSingleCharacter(null, null)
        searchCharacter(null)
    }

    fun loadHomeScreenWithData(){
        viewModelScope.launch {
            repository.loadCharacters()
            repository.loadAdvertComicList()
            repository.loadHomeSeriesList()
        }
    }

    fun loadSingleCharacter(id: Int?, nameStartWith: String?){
        viewModelScope.launch {
            repository.loadSingleCharacter(id, nameStartWith)
        }
    }

    fun searchCharacter(namesStartWith: String?){
        viewModelScope.launch {
            repository.loadSearchedCharacter(namesStartWith)
        }
    }
}