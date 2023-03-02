package com.syntax.haering.marvelsyntaxfinalproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syntax.haering.marvelsyntaxfinalproject.data.Repository
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.MarvelApi
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    val repository = Repository(MarvelApi)
    val character = repository.characters
    val advertComics = repository.comicAdverts
    val homeSeriesList = repository.homeSeriesList

    val singleCharacter = repository.singleCharacter
    val searchedCharacter = repository.searchedTermCharacter
    val searchedSerie = repository.searchedTermSerie

    val detailSeriesForCharacter = repository.detailSeriesForCharacter
    val detailComicsForCharacter = repository.detailComicsForCharacter


    private var _searchCategoryBtnState = MutableLiveData<Boolean>(true)
    val searchCategoryBtnState: LiveData<Boolean> get() = _searchCategoryBtnState


    init {
        loadHomeScreenWithData()
    }

    fun loadHomeScreenWithData() {
        viewModelScope.launch {
            repository.loadCharacters()
            repository.loadAdvertComicList()
            repository.loadHomeSeriesList()
        }
    }

    fun loadSingleCharacter(id: Int?) {
        viewModelScope.launch {
            repository.loadSingleCharacter(id)
        }
    }

    fun searchedTerm(namesStartWith: String, titleStartsWith: String) {
        viewModelScope.launch {
            repository.loadSearchedTerm(namesStartWith, titleStartsWith)
        }
    }

    fun changeSearchCategory(isOnChar: Boolean) {
        if (isOnChar)
            _searchCategoryBtnState.value = true
        if (!isOnChar)
            _searchCategoryBtnState.value = false
        _searchCategoryBtnState.value = _searchCategoryBtnState.value
    }

    fun getCharacterSeriesList(SeriesCollectionURI: String, ComicsCollectionURI: String){
        viewModelScope.launch {
            repository.loadCharacterSeriesList(SeriesCollectionURI, ComicsCollectionURI)
        }
        Log.d("ViewModel", SeriesCollectionURI)
    }
}
