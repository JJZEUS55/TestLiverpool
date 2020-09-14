package com.example.baseandroid.network

import com.example.baseandroid.domain.SearchResultObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://shoppapp.liverpool.com.mx/appclienteservices/services/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface LiverpoolApiService {
    @GET("v3/plp?force-plp=true")
    suspend fun getSearch(@Query("search-string") search: String, @Query("page-number") pageNumber: Long, @Query("number-of-items-per-page") itemsPerPage: Long): SearchResultObject

    @GET("v3/plp?force-plp=true&search-string=computadora&page-number=1&number-of-items-per-page=30")
    suspend fun getSearchTest(): SearchResultObject
}

object LiverpoolApi {
    val retrofitService: LiverpoolApiService by lazy {
        retrofit.create(LiverpoolApiService::class.java)
    }
}