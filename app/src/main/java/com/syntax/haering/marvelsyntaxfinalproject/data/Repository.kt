package com.syntax.haering.marvelsyntaxfinalproject.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.syntax.haering.marvelsyntaxfinalproject.HomeViewModel
import com.syntax.haering.marvelsyntaxfinalproject.data.model.FirestoreSaveModel
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.Constants
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.MarvelApi

private val TAG = "repository"
class Repository(private val MarvelApi: MarvelApi) {

    private var _characters =
        MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>(mutableListOf())
    val characters: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>
        get() = _characters

    private var _singleCharacter = MutableLiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>()
    val singleCharacter: LiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>
        get() = _singleCharacter

    private var _singleSerie = MutableLiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>()
    val singleSerie: LiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>
        get() = _singleSerie

    private var _singleComic = MutableLiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>()
    val singleComic: LiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>
        get() = _singleComic



    private var _searchedTermCharacter = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>()
    val searchedTermCharacter: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>
        get() = _searchedTermCharacter

    private var _searchedTermSerie = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>()
    val searchedTermSerie: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>
        get() = _searchedTermSerie



    private var _comicAdverts = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>>(mutableListOf())
    val comicAdverts: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>>
        get() = _comicAdverts

    private var _homeSeriesList = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>(mutableListOf())
    val homeSeriesList: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>
        get() = _homeSeriesList



    private var _detailSeriesCollection = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>()
    val detailSeriesCollection: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>
        get() = _detailSeriesCollection

    private var _detailComicsCollection = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>>()
    val detailComicsCollection: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>>
        get() = _detailComicsCollection


    private var _libraryCharList = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>?>(mutableListOf())
    val libraryCharList: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>?>
        get() = _libraryCharList

    private var _librarySeriesList = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>?>(mutableListOf())
    val librarySeriesList: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>?>
        get() = _librarySeriesList

    suspend fun loadCharacters() {
        val characterImport =  MarvelApi.retrofitService.getAllCharacters(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            offset = 0,
            limit = 20
        )

        _characters.value?.addAll(characterImport.data.results)
        /*for (i in 99..characterImport.data.total step 100){
            _characters.value?.addAll(MarvelApi.retrofitService.getAllCharacters(
                apikey = Constants.API_KEY,
                ts = Constants.timestamp,
                hash = Constants.hash(),
                offset = i,
                limit = 100
            ).data.results)
        }*/
        _characters.value = _characters.value
    }

    suspend fun loadAdvertComicList(){
        val randomOffset = listOf(10,20,40,100,250,400,550,780,1250).random()

        val comicImport = MarvelApi.retrofitService.getAllComics(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            offset = randomOffset,
            limit = 10
        )

        _comicAdverts.value?.addAll(comicImport.data.results.shuffled())
        _comicAdverts.value = _comicAdverts.value
    }

    suspend fun loadHomeSeriesList(){
        val randomOffset = listOf(50, 85, 150, 230, 350, 440, 580).random()

        val serieImport = MarvelApi.retrofitService.getAllSeries(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            offset = randomOffset,
            limit = 6
        )

        _homeSeriesList.value?.addAll(serieImport.data.results.shuffled())
        _homeSeriesList.value = _homeSeriesList.value
    }



    suspend fun loadSingleCharacter(id:Int){
        val singleCharacterImport = MarvelApi.retrofitService.getSingleCharacter(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            id = id,
        )
        _singleCharacter.value = singleCharacterImport.data.results.first()
        _singleCharacter.value = _singleCharacter.value
    }


    suspend fun loadSingleSerie(id: Int){
        val singleSerieImport = MarvelApi.retrofitService.getSingleSerie(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            id = id,
        )
        _singleSerie.value = singleSerieImport.data.results.first()
    }

    suspend fun loadSingleComic(id: Int){
        val singleComicImport = MarvelApi.retrofitService.getSingleComic(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            id = id,
        )
        _singleComic.value = singleComicImport.data.results.first()
    }

    suspend fun loadSearchedTerm(nameStartWith: String, titleStartsWith: String) {
        val searchedCharImport = MarvelApi.retrofitService.searchCharacter(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            limit = 100,
            nameStartWith = nameStartWith
        )
        val searchedSeriesImport = MarvelApi.retrofitService.searchedSeries(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            limit = 100,
            titleStartWith = titleStartsWith
        )

        _searchedTermCharacter.value = searchedCharImport.data.results.toMutableList()
        _searchedTermSerie.value = searchedSeriesImport.data.results.toMutableList()
    }

    suspend fun loadSeriesCollection(SeriesCollectionURI: String){
        val url = SeriesCollectionURI.replace("http","https")
        val importSeries = MarvelApi.retrofitService.getSeriesCollection(
            url= url,
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            limit = 90
        )

        _detailSeriesCollection.value = importSeries.data.results.toMutableList()
    }

    suspend fun loadComicsCollection(ComicCollectionURI: String){
        val url = ComicCollectionURI.replace("http","https")

        val importComics = MarvelApi.retrofitService.getComicsCollection(
            url= url,
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            limit = 90
        )

        _detailComicsCollection.value = importComics.data.results.toMutableList()
    }

    suspend fun loadLibraryCharList(id: Int){
        val import = MarvelApi.retrofitService.getSingleCharacter(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            id = id
        )


        _libraryCharList.value?.add(import.data.results.first())
        _libraryCharList.value = _libraryCharList.value

        Log.d(TAG, "$id : ${import.data.results.first()}")
    }

    suspend fun loadLibrarySeriesList(id: Int){
        val import = MarvelApi.retrofitService.getSingleSerie(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            id = id
        )

        _librarySeriesList.value?.add(import.data.results.first())
        _librarySeriesList.value = _librarySeriesList.value

        Log.d(TAG, "$id : ${import.data.results.first()}")
    }

    fun deleteLibraryCharacter(id: Int){
        for (i in _libraryCharList.value!!){
            if (id == i.id){
                _libraryCharList.value?.remove(i)
                break
            }
        }
        _libraryCharList.value = _libraryCharList.value
    }

    fun deleteLibrarySerie(id: Int){
        for (i in _librarySeriesList.value!!){
            if (id == i.id){
                _librarySeriesList.value?.remove(i)
                break
            }
        }
        _librarySeriesList.value = _librarySeriesList.value
    }
}