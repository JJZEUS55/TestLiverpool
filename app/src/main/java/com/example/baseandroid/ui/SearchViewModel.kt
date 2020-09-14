package com.example.baseandroid.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseandroid.domain.SearchObject
import com.example.baseandroid.domain.SearchResultObject
import com.example.baseandroid.repository.SearchRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SearchViewModel : ViewModel() {
    private val searchRepository by lazy {
        SearchRepository()
    }
    val liveDataSearchItems = MutableLiveData<List<SearchObject>>()
    val liveDataError = MutableLiveData<Int>()
    val liveDataSearching = MutableLiveData<Boolean>()
    private val listProducts = mutableListOf<SearchObject>()

    fun getSearch(searchWords: String, pageNumber: Long) {
        viewModelScope.launch {
            try {
                if (pageNumber == 1L) {
                    listProducts.clear()
                    liveDataSearchItems.value = listProducts
                }
                liveDataSearching.value = true
                val response = searchRepository.getSearch(searchWords, pageNumber)
                listProducts.addAll(response.plpResults.records)
                liveDataSearchItems.value = listProducts
                liveDataSearching.value = false
            } catch (e: HttpException) {
                liveDataError.value = e.code()
            }
        }
    }
}