package com.example.githubperson.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.example.githubperson.data.datastore.SettingPreference
import com.example.githubperson.data.remote.ApiConfig
import com.example.githubperson.data.remote.ItemsItem
import com.example.githubperson.data.remote.ResponseGithubPerson
import com.example.githubperson.data.remote.ResponseGithubPersonDetails
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreference): ViewModel() {


    //Progress Bar
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    //List User
    private val _person = MutableLiveData<List<ItemsItem>>()
    val person: LiveData<List<ItemsItem>> = _person

    //Details
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

    //List Following and Followers Di User Details
    private val _followingfollower = MutableLiveData<List<ItemsItem>>()
    val follow: LiveData<List<ItemsItem>> = _followingfollower


    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun findPerson(username: String){
        privateFindPerson(username)
    }

    fun getPersonDetails(username: String){
        privateGetPersonDetails(username)
    }

    fun getListFollowing (username: String){
        privateGetListFollowing(username)
    }

    fun getListFollowers (username: String){
        privateGetListFollowers(username)
    }


    private fun privateFindPerson(username: String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getPerson(username)
        //Get Person
        client.enqueue(object : Callback<ResponseGithubPerson> {
            override fun onResponse(
                call: Call<ResponseGithubPerson>,
                response: Response<ResponseGithubPerson>
            ) {
                _isLoading.value = false
                if(response.isSuccessful){
                    _person.value = response.body()?.items
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }override fun onFailure(call: Call<ResponseGithubPerson>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })

    }


    private fun privateGetPersonDetails(username: String){
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
    }


    private fun privateGetListFollowing (username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)

        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _followingfollower.value = response.body()
                }else{
                    Log.d("ESLE_FOLLOWERS", response.message())
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e("ERRORFOLLOWER", t.message.toString())
            }

        })

    }


    private fun privateGetListFollowers (username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)

        //Fetching witch API
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _followingfollower.value = response.body()
                }else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })

    }

}
