package com.example.githubperson.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubperson.viewmodel.FollowingFollowersViewModel

class FollowingFollowersFactory(private val username: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FollowingFollowersViewModel::class.java)) {
            return FollowingFollowersViewModel(username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}