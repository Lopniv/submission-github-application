package com.android.submission2github.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.submission2github.R
import com.android.submission2github.databinding.ItemFavoriteBinding
import com.android.submission2github.model.Item
import com.bumptech.glide.Glide

class FavoriteUserAdapter(private var favoriteList: ArrayList<Item>) : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>() {

    var userListListener: UserListListener? = null

    fun updateUsers(user: ArrayList<Item>) {
        favoriteList.clear()
        favoriteList.addAll(user)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FavoriteUserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
    )

    override fun onBindViewHolder(holder: FavoriteUserViewHolder, position: Int) {
        holder.bind(favoriteList[position])
        holder.itemView.setOnClickListener{
            userListListener?.onItemUserList(it, favoriteList[position], favoriteList)
        }
    }

    override fun getItemCount() = this.favoriteList.size

    class FavoriteUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFavoriteBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(user: Item) {
            binding.tvName.text = user.name
            binding.tvLocation.text = user.location
            Glide.with(itemView.context).load(user.avatarUrl).into(binding.ivUserProfile)
            binding.btnRemove.setOnClickListener {

            }
        }
    }
}