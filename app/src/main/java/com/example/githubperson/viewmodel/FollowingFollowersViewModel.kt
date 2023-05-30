package com.example.githubperson.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubperson.data.remote.ApiConfig
import com.example.githubperson.data.remote.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFollowersViewModel(username: String) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _follow = MutableLiveData<List<ItemsItem>>()
    val follow: LiveData<List<ItemsItem>> = _follow

    private val _following = MutableLiveData<Int?>()
    val following : LiveData<Int?> = _following

    private val _followers = MutableLiveData<Int?>()
    val followers : LiveData<Int?> = _followers

    fun findUserFollowers(username: String) {
        getUserFollowers(username)
    }
    fun findUserFollowing(username: String) {
        getUserFollowing(username)
    }

    private fun getUserFollowers(username: String) {
        _isLoading.value = true
        val userFollowers = ApiConfig.getApiService().getFollowers(username)

        userFollowers.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if(response.isSuccessful) {
                    _isLoading.value = false
                    _follow.value = response.body()
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }

            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
    private fun getUserFollowing(username: String) {
        _isLoading.value = true
        val userFollowing = ApiConfig.getApiService().getFollowing(username)

        userFollowing.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if(response.isSuccessful) {
                    _isLoading.value = false
                    _follow.value = response.body()
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}