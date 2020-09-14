package com.example.baseandroid

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.baseandroid.databinding.MainActivityBinding
import com.example.baseandroid.ui.SearchDataPass
import com.example.baseandroid.ui.SuggestionsProvider
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var searchDataPass: SearchDataPass

    private val navController by lazy {
        this.findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.lifecycleOwner = this
        setSupportActionBar(toolbar)
//        appBarConfiguration = AppBarConfiguration(
//            setOf(R.id.mainFragment, R.id.secondFragment),
//            drawer_layout)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search_bar)?.actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(
                searchManager.getSearchableInfo(
                    ComponentName(
                        applicationContext,
                        MainActivity::class.java
                    )
                )
            )

            setOnSuggestionListener(object : SearchView.OnSuggestionListener {
                override fun onSuggestionSelect(position: Int): Boolean {
                    return true
                }

                override fun onSuggestionClick(position: Int): Boolean {
                    val cursor = suggestionsAdapter.cursor
                    cursor.moveToPosition(position)
                    val suggestion: String = cursor.getString(2) //2 is the index of col containing suggestion name.
                    setQuery(suggestion, true) //setting suggestion
                    return true
                }
            })
        }

        (menu.findItem(R.id.search_bar)?.actionView as SearchView).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                SearchRecentSuggestions(applicationContext, SuggestionsProvider.AUTHORITY, SuggestionsProvider.MODE)
                    .saveRecentQuery(query, null)
                searchDataPass.search(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                TODO("Not yet implemented")
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    fun passSearch(searchDataPass: SearchDataPass) {
        this.searchDataPass = searchDataPass
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
            val suggestions = SearchRecentSuggestions(
                this,
                SuggestionsProvider.AUTHORITY,
                SuggestionsProvider.MODE
            )
            suggestions.saveRecentQuery(query, null)
        }
    }

}
