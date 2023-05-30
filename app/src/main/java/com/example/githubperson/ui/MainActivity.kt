package com.example.githubperson.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubperson.data.remote.ItemsItem
import com.example.githubperson.adapter.MainAdapter
import com.example.githubperson.viewmodel.MainViewModel
import com.example.githubperson.R
import com.example.githubperson.data.datastore.SettingPreference
import com.example.githubperson.databinding.ActivityMainBinding
import com.example.githubperson.viewmodel.factory.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Context Untuk RecyclerView
        val layoutManager = LinearLayoutManager(this)
        binding.rvPerson.layoutManager = layoutManager

        //Declare View model
        val pref = SettingPreference.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, MainViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.person.observe(this) { person ->
            setPersonData(person)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        mainViewModel.getThemeSettings().observe(this@MainActivity) { isDarkModeActive ->
            if (isDarkModeActive) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        //Observe Livedata
        mainViewModel.person.observe(this) { dataperson -> setPersonData(dataperson) }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.option_menu, menu)
        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.btn_search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            //Gunakan method ini ketika search selesai atau OK
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findPerson(query)//calling from MainViewModel
                searchView.clearFocus()
                return true
            }

            //Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }


    //Fungsi untuk get data from api and pass it to adapter
    private fun setPersonData(personSearch: List<ItemsItem>){

        //Lopping List<itemsItem> dan memplot kedalam data class itemItems di Respons Github
        val getPersonData = personSearch.map {
            ItemsItem(it.login, it.id)
        }

        //Kirim data ke adapter
        val adapter = MainAdapter(getPersonData)

        //bind data kedalam view recycler View
        binding.rvPerson.adapter =adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_tofavorite -> {
                // do something when menu_item1 is clicked
                val favorite = Intent(this, FavoriteActivity::class.java)
                startActivity(favorite)
                true
            }
            R.id.btn_tosetting -> {
                // do something when menu_item2 is clicked
                val setting = Intent(this, SettingsActivity::class.java)
                startActivity(setting)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}
