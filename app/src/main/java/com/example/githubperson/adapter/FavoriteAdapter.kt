package com.example.githubperson.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubperson.data.local.FavoriteEntity
import com.example.githubperson.databinding.PersonItemBinding
import com.example.githubperson.ui.DetailsActivity

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val userFavorite = ArrayList<FavoriteEntity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = PersonItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = userFavorite.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userFavorite[position])
        holder.itemView.setOnClickListener{ onItemClickCallback.onItemClicked(userFavorite[position])}
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListFavorite(items: List<FavoriteEntity>) {
        userFavorite.clear()
        userFavorite.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: PersonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEntity: FavoriteEntity) {
            with(binding) {
                Glide.with(root)
                    .load(favoriteEntity.avatarUrl)
                    .into(binding.ivPerson)
                root.setOnClickListener {
                    val intent = Intent(itemView.context, DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.KEY_USER, favoriteEntity)
                    itemView.context.startActivity(intent)
                }
                binding.tvName.text = favoriteEntity.username
            }
        }

    }
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(favEntity: FavoriteEntity)
    }

}