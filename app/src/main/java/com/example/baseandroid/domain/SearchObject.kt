package com.example.baseandroid.domain

data class SearchObject(
    val productId: String,
    val productDisplayName: String,
    val listPrice: Double,
    val seller: String,
    val brand: String,
    val category: String,
    val xlImage: String
)