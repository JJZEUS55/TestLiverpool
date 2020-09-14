package com.example.baseandroid

import android.util.Log
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.baseandroid.database.dao.ExampleDao
import com.example.baseandroid.database.room.ExampleDatabase
import com.example.baseandroid.database.entity.ExampleObject
import com.example.baseandroid.database.sharedpreferences.AppSharedPreferences
import com.example.baseandroid.ui.mainOne.MainFragment
import com.example.baseandroid.ui.mainOne.MainViewModel
import kotlinx.coroutines.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException


@RunWith(JUnit4::class)
class ExampleDatabaseTest2 {

    private var exampleDao: ExampleDao? = null
    private var db: ExampleDatabase? = null
    private var mainRepository: MainRepository? = null
    private lateinit var mainViewModel: MainViewModel

    @ExperimentalCoroutinesApi
    private var mainCoroutineRule:MainCoroutineRule? = null

    @Rule
    @JvmField
    var activityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val appPreferences = AppSharedPreferences(context)
        mainCoroutineRule = MainCoroutineRule()
        mainViewModel = MainViewModel(activityRule.activity.application)
        db = Room.inMemoryDatabaseBuilder(context, ExampleDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        mainRepository = MainRepository(db, appPreferences)

        exampleDao = db?.exampleDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db?.close()
    }

    @Test
    fun insert() {
        val exampleObject = ExampleObject(1, "prueba")
        exampleDao?.insert(exampleObject)
        val getObject = exampleDao?.getById(1)
        assertThat(exampleObject.id, equalTo(getObject?.id))
    }

    @Test
    fun update() {
        val exampleObject = ExampleObject(1, "prueba")
        exampleDao?.insert(exampleObject)
        val getObject = exampleDao?.getById(1)
        getObject?.name = "prueba2"
        exampleDao?.update(getObject)
        val getObjectUpdate = exampleDao?.getById(1)
        assertThat(getObjectUpdate?.name, equalTo("prueba2"))
    }

    @Test
    fun getFromService() {
        GlobalScope.launch(Dispatchers.Main) {
            mainViewModel.getFromService()

            mainViewModel.liveDataService.observeOnce {
                val isBigger = it.isNotEmpty()
                Log.d("TESTLive", "$it")
                assertThat(isBigger, equalTo(true))
            }
        }

    }


}