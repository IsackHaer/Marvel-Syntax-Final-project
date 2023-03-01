package com.syntax.haering.marvelsyntaxfinalproject.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.Constants
import com.syntax.haering.marvelsyntaxfinalproject.data.remote.MarvelApi

class Repository(private val MarvelApi: MarvelApi) {

    private var _character =
        MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>(mutableListOf())
    val character: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>
        get() = _character

    private var _singleCharacter = MutableLiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>()
    val singleCharacter: LiveData<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>
        get() = _singleCharacter

    private var _searchedCharacter = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>()
    val searchedCharacter: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.Result>>
        get() = _searchedCharacter

    private var _comicAdverts = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>>(mutableListOf())
    val comicAdverts: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.Result>>
        get() = _comicAdverts

    private var _homeSeriesList = MutableLiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>(mutableListOf())
    val homeSeriesList: LiveData<MutableList<com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.Result>>
        get() = _homeSeriesList

    suspend fun loadCharacters() {
        val characterImport =  MarvelApi.retrofitService.getAllCharacters(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            offset = 0,
            limit = 100
        )

        _character.value?.addAll(characterImport.data.results)
        /*for (i in 99..characterImport.data.total step 100){
            _character.value?.addAll(MarvelApi.retrofitService.getAllCharacters(
                apikey = Constants.API_KEY,
                ts = Constants.timestamp,
                hash = Constants.hash(),
                offset = i,
                limit = 100
            ).data.results)
        }*/
        _character.value = _character.value
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

    suspend fun loadSingleCharacter(id:Int?, nameStartWith: String?){
        val singleCharacterImport = MarvelApi.retrofitService.getSingleCharacter(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            id = id,
            nameStartWith = nameStartWith
        )
        _singleCharacter.value = singleCharacterImport.data.results.first()
        _singleCharacter.value = _singleCharacter.value
    }

    suspend fun loadSearchedCharacter(nameStartWith: String?) {
        val searchedCharImport = MarvelApi.retrofitService.searchCharacter(
            apikey = Constants.API_KEY,
            ts = Constants.timestamp,
            hash = Constants.hash(),
            nameStartWith = nameStartWith
        )

        _searchedCharacter.value?.addAll(searchedCharImport.data.results)
    }
}