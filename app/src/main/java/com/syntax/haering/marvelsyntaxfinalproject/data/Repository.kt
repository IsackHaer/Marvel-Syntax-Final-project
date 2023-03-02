package com.syntax.haering.marvelsyntaxfinalproject.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.Constants
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.MarvelApi

class Repository(private val MarvelApi: MarvelApi) {

    private var _characters =
        MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>(mutableListOf())
    val characters: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>
        get() = _characters

    private var _singleCharacter = MutableLiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>()
    val singleCharacter: LiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>
        get() = _singleCharacter



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



    private var _detailSeriesForCharacter = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>()
    val detailSeriesForCharacter: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>
        get() = _detailSeriesForCharacter

    private var _detailComicsForCharacter = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>>()
    val detailComicsForCharacter: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>>
        get() = _detailComicsForCharacter

    suspend fun loadCharacters() {
        val characterImport =  MarvelApi.retrofitService.getAllCharacters(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            offset = 0,
            limit = 100
        )

        _characters.value?.addAll(characterImport.data.results)
        /*for (i in 99..characterImport.data.total step 100){
            _character.value?.addAll(MarvelApi.retrofitService.getAllCharacters(
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

    suspend fun loadSingleCharacter(id:Int?){
        val singleCharacterImport = MarvelApi.retrofitService.getSingleCharacter(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            id = id,
        )
        _singleCharacter.value = singleCharacterImport.data.results.first()
        _singleCharacter.value = _singleCharacter.value
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

    suspend fun loadCharacterSeriesList(SeriesCollectionURI: String, ComicCollectionURI: String){
        val url1 = SeriesCollectionURI.replace("http","https")
        val url2 = ComicCollectionURI.replace("http","https")

        val importSeries = MarvelApi.retrofitService.getSeriesForCharacter(
            url= url1,
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash()
        )

        val importComics = MarvelApi.retrofitService.getComicsForCharacter(
            url= url2,
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash()
        )

        _detailSeriesForCharacter.value = importSeries.data.results.toMutableList()
        _detailComicsForCharacter.value = importComics.data.results.toMutableList()
    }
}