package com.example.githubperson.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubperson.data.local.FavoriteEntity
import com.example.githubperson.data.remote.ApiConfig
import com.example.githubperson.data.remote.ResponseGithubPersonDetails
import com.example.githubperson.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(username: String,app: Application) : ViewModel() {

    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(app)
    private val _username = MutableLiveData<String?>()
    val username : LiveData<String?> = _username

    private val _nama = MutableLiveData<String?>()
    val nama : LiveData<String?> = _nama

    private val _avatarUrl = MutableLiveData<String?>()
    val avatarUrl : LiveData<String?> = _avatarUrl

    private val _following = MutableLiveData<Int?>()
    val following : LiveData<Int?> = _following

    private val _followers = MutableLiveData<Int?>()
    val followers : LiveData<Int?> = _followers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun findUserDetails(username: String) {
        getUserDetail(username)
    }

    fun insert(favEntity: FavoriteEntity) {
        mFavoriteRepository.insert(favEntity)
    }

    fun delete(favEntity: FavoriteEntity) {
        mFavoriteRepository.delete(favEntity)
    }

    fun getFavoriteByUsername(username: String ): LiveData<List<FavoriteEntity>> {
        return mFavoriteRepository.getUserFavoriteByUsername(username)
    }

    private fun getUserDetail(username: String) {

        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailPerson(username)

        //Get Details
        client.enqueue(object : Callback<ResponseGithubPersonDetails>{
            override fun onResponse(
                call: Call<ResponseGithubPersonDetails>,
                response: Response<ResponseGithubPersonDetails>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _nama.value = response.body()?.name
                    _avatarUrl.value = response.body()?.avatarUrl
                    _username.value = response.body()?.login
                    _following.value = response.body()?.following
                    _followers.value = response.body()?.followers
                }else{
                    Log.d("ELSE_DETAILS", response.message())
                }
            }

            override fun onFailure(call: Call<ResponseGithubPersonDetails>, t: Throwable) {
                _isLoading.value = false
                Log.e("ERRORDETAILS", t.message.toString())
            }

        })

//        _isLoading.value = true
//        val userDetails = ApiConfig.getApiService().getDetailPerson(username)
//        userDetails.enqueue(object : Callback<ResponseGithubPersonDetails> {
//            override fun onResponse(
//                call: Call<ResponseGithubPersonDetails>,
//                response: Response<ResponseGithubPersonDetails>
//            ) {
//                _isLoading.value = false
//                if(response.isSuccessful) {
//                    _name.value = response.body()?.name
//                    _username.value = response.body()?.login
//                    _avatarUrl.value = response.body()?.avatarUrl
//                    _followers.value = response.body()?.followers
//                    _following.value = response.body()?.following
//                } else {
//                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseGithubPersonDetails>, t: Throwable) {
//                _isLoading.value = false
//                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
//            }
//
//        })

    }
}