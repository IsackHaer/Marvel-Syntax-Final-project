package com.syntax.haering.marvelsyntaxfinalproject

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
    val singleSerie = repository.singleSerie
    val singleComic = repository.singleComic

    val searchedCharacter = repository.searchedTermCharacter
    val searchedSerie = repository.searchedTermSerie

    val detailSeriesCollection = repository.detailSeriesCollection
    val detailComicsCollection = repository.detailComicsCollection


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

    fun loadSingleCharacter(id: Int) {
        viewModelScope.launch {
            repository.loadSingleCharacter(id)
        }
    }

    fun loadSingleSerie(id: Int){
        viewModelScope.launch {
            repository.loadSingleSerie(id)
        }
    }

    fun loadSingleComic(id: Int) {
        viewModelScope.launch {
            repository.loadSingleComic(id)
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

    fun loadSerieCollection(SeriesCollectionURI: String){
        viewModelScope.launch {
            repository.loadSeriesCollection(SeriesCollectionURI)
        }
    }

    fun loadComicCollection(ComicsCollectionURI: String){
        viewModelScope.launch {
            repository.loadComicsCollection(ComicsCollectionURI)
        }
    }
}
