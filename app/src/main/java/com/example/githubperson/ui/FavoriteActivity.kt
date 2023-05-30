package com.example.githubperson.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubperson.adapter.FavoriteAdapter
import com.example.githubperson.data.local.FavoriteEntity
import com.example.githubperson.databinding.ActivityFavoriteBinding
import com.example.githubperson.viewmodel.FavoriteViewModel
import com.example.githubperson.viewmodel.factory.FavoriteFactory


class FavoriteActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val adapter: FavoriteAdapter by lazy {
        FavoriteAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        setUpList()
        setUserFavorite()
    }
    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = FavoriteFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setUpList() {
        with(binding) {
            val layoutManager = LinearLayoutManager(this@FavoriteActivity)
            this?.rvFavorite?.layoutManager = layoutManager
            val itemDecoration =
                DividerItemDecoration(this@FavoriteActivity, layoutManager.orientation)
            this?.rvFavorite?.addItemDecoration(itemDecoration)
            this?.rvFavorite?.adapter = adapter
            adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                override fun onItemClicked(favEntity: FavoriteEntity) {
                    val intent = Intent(this@FavoriteActivity, DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.KEY_USERNAME, favEntity.username)
                    startActivity(intent)
                }
            })
        }
    }

    private fun setUserFavorite() {
        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getAllFavorites().observe(this@FavoriteActivity) { favList ->
            if (favList != null) {
                adapter.setListFavorite(favList)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}