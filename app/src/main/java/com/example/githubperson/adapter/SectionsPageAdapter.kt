package com.example.githubperson.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubperson.ui.DetailsActivity
import com.example.githubperson.ui.FollowingFollowersFragment

class SectionsPageAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username = DetailsActivity.username

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowingFollowersFragment()

        fragment.arguments = Bundle().apply {
            putInt(FollowingFollowersFragment._POSITION, position+1)
            putString(FollowingFollowersFragment._USERNAME, username)
        }
        return fragment

    }

}