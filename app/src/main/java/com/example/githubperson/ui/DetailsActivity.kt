package com.example.githubperson.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubperson.R
import com.example.githubperson.adapter.SectionsPageAdapter
import com.example.githubperson.data.local.FavoriteEntity
import com.example.githubperson.databinding.ActivityDetailsBinding
import com.example.githubperson.viewmodel.factory.DetailFactory
import com.example.githubperson.viewmodel.DetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import androidx.activity.viewModels




class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var isFavorite = false

    companion object {
        const val KEY_USER = "user"
        const val KEY_USERNAME = "username"
        @StringRes
        private val TAB_NAME = intArrayOf(
            R.string.followers,
            R.string.following

        )
        var username : String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val favorite = FavoriteEntity()
        val detailViewModel : DetailViewModel by viewModels {
            DetailFactory(username, application)
        }

        if (Build.VERSION.SDK_INT >= 33) {
            username = intent.getStringExtra("username").toString()
            detailViewModel.findUserDetails(username)

        } else {
            @Suppress("DEPRECATION")
            username = intent.getStringExtra("username") as String
            detailViewModel.findUserDetails(username)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel.avatarUrl.observe(this){ avatar ->
            Glide.with(this)
                .load(avatar)
                .into(binding.profilePicture)
            favorite.avatarUrl = avatar
        }
        detailViewModel.username.observe(this){ login ->
            binding.username.text = login
            favorite.username = login!!
        }
        detailViewModel.nama.observe(this){ name ->
            binding.nama.text = name
        }
        detailViewModel.followers.observe(this){followers ->
            binding.followers.text = "$followers followers"
        }
        detailViewModel.following.observe(this){following ->
            binding.following.text = "$following following"
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPageAdapter = SectionsPageAdapter(this)
        val viewPager : ViewPager2 = findViewById(R.id.viewpager)
        viewPager.adapter = sectionsPageAdapter
        val tabs : TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs,viewPager) { tab, position ->
            tab.text = resources.getString(TAB_NAME[position])
        }.attach()

        detailViewModel.getFavoriteByUsername(username)
            .observe(this@DetailsActivity) { listFav ->
                isFavorite = listFav.isNotEmpty()
                binding.detailFabFavorite.setImageDrawable(
                    if (listFav.isEmpty()) {
                        binding.detailFabFavorite.imageTintList =
                            ColorStateList.valueOf(Color.rgb(255, 255, 255))
                        ContextCompat.getDrawable(
                            binding.detailFabFavorite.context,
                            R.drawable.baseline_favorite_border
                        )
                    } else {
                        binding.detailFabFavorite.imageTintList =
                            ColorStateList.valueOf(Color.rgb(247, 106, 123))
                        ContextCompat.getDrawable(
                            binding.detailFabFavorite.context,
                            R.drawable.baseline_favorite_24
                        )
                    }
                )
            }

        binding.detailFabFavorite.apply {
            setOnClickListener {
                if (isFavorite) {
                    detailViewModel.delete(favorite)
                    Toast.makeText(
                        this@DetailsActivity,
                        "${favorite.username} Remove From Favorite ",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    detailViewModel.insert(favorite)
                    Toast.makeText(
                        this@DetailsActivity,
                        "${favorite.username} Add To Favorite",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progresBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}