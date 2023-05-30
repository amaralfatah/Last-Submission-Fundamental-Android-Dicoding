package com.example.githubperson.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubperson.data.local.FavoriteEntity
import com.example.githubperson.repository.FavoriteRepository

class FavoriteViewModel(application : Application) : ViewModel() {
    private val mFavRepository : FavoriteRepository = FavoriteRepository(application)
    fun getAllFavorites() : LiveData<List<FavoriteEntity>> = mFavRepository.getAllFavorites()
}