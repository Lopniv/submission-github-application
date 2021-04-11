package com.android.submission2github.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.submission2github.R
import com.android.submission2github.databinding.ItemFavoriteBinding
import com.android.submission2github.model.Item
import com.bumptech.glide.Glide

class FavoriteUserAdapter(private var favoriteList: ArrayList<Item>, var context: Context) : RecyclerView.Adapter<FavoriteUserAdapter.FavoriteUserViewHolder>() {

//    var listFavorite = java.util.ArrayList<Item>()
//        set(listFavorite) {
//            if (listFavorite.size > 0) {
//                this.listFavorite.clear()
//            }
//            this.listFavorite.addAll(listFavorite)
//
//            notifyDataSetChanged()
//        }

//    fun addItem(item: Item) {
//        this.listFavorite.add(item)
//        notifyItemInserted(this.listFavorite.size - 1)
//    }
//
//    fun removeItem(position: Int) {
//        this.listFavorite.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, this.listFavorite.size)
//    }

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
    }

    override fun getItemCount() = favoriteList.size

    class FavoriteUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemFavoriteBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(user: Item) {
            binding.tvUsername.text = user.login
            binding.tvId.text = "${user.id}"
            Glide.with(itemView.context).load(user.avatarUrl).into(binding.ivUserProfile)
            binding.btnRemove.setOnClickListener {

            }
            itemView.setOnClickListener {

            }
        }
    }
}