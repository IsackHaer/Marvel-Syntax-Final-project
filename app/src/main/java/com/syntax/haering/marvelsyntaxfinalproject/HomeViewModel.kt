package com.syntax.haering.marvelsyntaxfinalproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.syntax.haering.marvelsyntaxfinalproject.data.Repository
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.MarvelApi
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    enum class APIStatus { LOADING, DONE, ERROR }

    val repository = Repository(MarvelApi)
    val authentification = Firebase.auth
    var currentUser = authentification.currentUser


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

    private var _apiStatus = MutableLiveData<APIStatus>(APIStatus.DONE)
    val apiStatus: LiveData<APIStatus> get() = _apiStatus
    
    private var _loadComicStatus = MutableLiveData(APIStatus.DONE)
    val loadComicStatus: LiveData<APIStatus> get() = _loadComicStatus
    
    private var _loadSerieStatus = MutableLiveData(APIStatus.DONE)
    val loadSerieStatus: LiveData<APIStatus> get() = _loadSerieStatus

    private var _searchCategoryBtnState = MutableLiveData<Boolean>(true)
    val searchCategoryBtnState: LiveData<Boolean> get() = _searchCategoryBtnState

    init {
        loadHomeScreenWithData()
    }

    fun loadHomeScreenWithData() {
        viewModelScope.launch {
            try {
                _apiStatus.value = APIStatus.LOADING
                repository.loadCharacters()
                repository.loadAdvertComicList()
                repository.loadHomeSeriesList()
                _apiStatus.value = APIStatus.DONE
            } catch (e: Exception){
                _apiStatus.value = APIStatus.ERROR
            }
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
            try {
                _apiStatus.value = APIStatus.LOADING
                repository.loadSearchedTerm(namesStartWith, titleStartsWith)
                _apiStatus.value = APIStatus.DONE
            } catch (e: Exception){
                _apiStatus.value = APIStatus.ERROR
                Log.d("MainViewModel","could not load searchTerm : $e")
            }
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
            try {
                _loadSerieStatus.value = APIStatus.LOADING
                repository.loadSeriesCollection(SeriesCollectionURI)
                _loadSerieStatus.value = APIStatus.DONE
            } catch (e: Exception){
                _loadSerieStatus.value = APIStatus.ERROR
                Log.d("HomeViewModel","could not load Serie collection: $e")
            }

        }
    }

    fun loadComicCollection(ComicsCollectionURI: String){
        viewModelScope.launch {
            try {
                _loadComicStatus.value = APIStatus.LOADING
                repository.loadComicsCollection(ComicsCollectionURI)
                _loadComicStatus.value = APIStatus.DONE
            } catch (e: Exception){
                _loadComicStatus.value = APIStatus.ERROR
                Log.d("HomeViewModel","could not load Comics collection: $e")
            }

        }
    }
}
