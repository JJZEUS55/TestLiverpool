package com.example.baseandroid.ui

import android.content.SearchRecentSuggestionsProvider

class SuggestionsProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "com.example.baseandroid.ui.SuggestionsProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }
}