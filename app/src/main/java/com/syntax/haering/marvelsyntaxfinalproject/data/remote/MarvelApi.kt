package com.syntax.haering.marvelsyntaxfinalproject.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.syntax.haering.marvelsyntaxfinalproject.BuildConfig
import com.syntax.haering.marvelsyntaxfinalproject.data.importCharacterData.CharacterDTO
import com.syntax.haering.marvelsyntaxfinalproject.data.importComicData.ComicDTO
import com.syntax.haering.marvelsyntaxfinalproject.data.importSerieData.SerieDTO
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface MarvelApiService {
    @GET("/v1/public/characters")
    suspend fun getAllCharacters(
        @Query("ts") ts: String = Constants.timestamp,
        @Query("apikey") apikey: String = Constants.API_KEY,
        @Query("hash") hash: String = Constants.hash(),
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): CharacterDTO

    @GET("/v1/public/comics")
    suspend fun getAllComics(
        @Query("ts") ts: String = Constants.timestamp,
        @Query("apikey") apikey: String = Constants.API_KEY,
        @Query("hash") hash: String = Constants.hash(),
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ComicDTO

    @GET("/v1/public/series")
    suspend fun getAllSeries(
        @Query("ts") ts: String = Constants.timestamp,
        @Query("apikey") apikey: String = Constants.API_KEY,
        @Query("hash") hash: String = Constants.hash(),
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): SerieDTO
}

object MarvelApi {
    val retrofitService: MarvelApiService by lazy { retrofit.create(MarvelApiService::class.java) }
}

class Constants {
    companion object {
        const val BASE_URL = "https://gateway.marvel.com"
        val timestamp = Timestamp(System.currentTimeMillis()).time.toString()
        const val API_KEY = BuildConfig.API_KEY
        const val PRIVATE_KEY = BuildConfig.PRIVATE_KEY
        const val limit = "20"
        fun hash(): String {
            val input = "$timestamp$PRIVATE_KEY$API_KEY"
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }
    }
}




