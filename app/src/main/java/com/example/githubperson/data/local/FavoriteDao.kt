package com.example.githubperson.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteEntity: FavoriteEntity)
    @Update
    fun update(favoriteEntity: FavoriteEntity)
    @Delete
    fun delete(favoriteEntity: FavoriteEntity)

    @Query("SELECT  * from favorite")
    fun getAllFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT  * from favorite WHERE username = :username")
    fun getUserFavoriteByUsername(username: String): LiveData<List<FavoriteEntity>>
}