package com.example.baseandroid.repository

import com.example.baseandroid.domain.SearchResultObject
import com.example.baseandroid.network.LiverpoolApi

class SearchRepository {

    suspend fun getSearch(searchWords: String, pageNumber: Long): SearchResultObject {
        return LiverpoolApi.retrofitService.getSearch(searchWords, pageNumber, 30)
//        return LiverpoolApi.retrofitService.getSearchTest()
    }

}