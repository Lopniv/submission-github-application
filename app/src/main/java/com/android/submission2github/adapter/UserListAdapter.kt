package com.android.submission2github.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.submission2github.R
import com.android.submission2github.databinding.ItemUserBinding
import com.android.submission2github.model.Item
import com.bumptech.glide.Glide


class UserListAdapter(var users: ArrayList<Item>, var context: Context) : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {

    var userListListener: UserListListener? = null
    var favoriteListener: FavoriteListener? = null

    fun updateUsers(user: ArrayList<Item>) {
        users.clear()
        users.addAll(user)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
    )

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(users[position], context)
        holder.itemView.setOnClickListener {
            userListListener?.onItemUserList(it, users[position], users)
        }
//        holder.binding.btnFavorite.setOnClickListener {
//            favoriteListener?.addFavoriteUser(it, users[position], users)
//        }
    }

    override fun getItemCount() = users.size

    class UserListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemUserBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(user: Item, context: Context) {
            binding.tvName.text = user.login
            Glide.with(context).load(user.avatarUrl).into(binding.ivUserProfile)
        }
    }
}