package com.example.githubperson.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubperson.data.local.FavoriteEntity
import com.example.githubperson.data.local.FavoriteDao
import com.example.githubperson.data.local.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoritesDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoritesDao.getAllFavorite()

    fun getUserFavoriteByUsername(username: String): LiveData<List<FavoriteEntity>> =
        mFavoritesDao.getUserFavoriteByUsername(username)

    fun insert(note: FavoriteEntity) {
        executorService.execute { mFavoritesDao.insert(note) }
    }
    fun delete(note: FavoriteEntity) {
        executorService.execute { mFavoritesDao.delete(note) }
    }

}