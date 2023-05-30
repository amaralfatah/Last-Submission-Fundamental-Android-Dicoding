package com.example.githubperson.adapter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.githubperson.data.remote.ItemsItem
import com.example.githubperson.databinding.PersonItemBinding
import com.example.githubperson.ui.DetailsActivity

class MainAdapter(private val listPerson: List<ItemsItem>) : RecyclerView.Adapter<MainAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val binding = PersonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val(name,id) = listPerson[position]

        holder.binding.tvName.text = name

        Glide.with(holder.itemView.context)
            .load("https://avatars.githubusercontent.com/u/${id}?v=4")
            .into(holder.binding.ivPerson)

        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, DetailsActivity::class.java)
            intentDetail.putExtra("username", listPerson[position].login)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int = listPerson.size
}