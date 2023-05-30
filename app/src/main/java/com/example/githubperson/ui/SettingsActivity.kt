package com.example.githubperson.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubperson.data.datastore.SettingPreference
import com.example.githubperson.databinding.ActivitySettingBinding
import com.example.githubperson.viewmodel.SettingViewModel
import com.example.githubperson.viewmodel.factory.SettingViewModelFactory


class SettingsActivity : AppCompatActivity() {
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreference.getInstance(dataStore)
        val viewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        viewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchtheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchtheme.isChecked = false
            }
        }

        binding.switchtheme.setOnCheckedChangeListener { _, isChecked ->
            binding.switchtheme.isChecked = isChecked
            viewModel.saveThemeSetting(isChecked)
        }
    }
}