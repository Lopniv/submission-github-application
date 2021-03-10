package com.android.submission2github.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.submission2github.R
import com.android.submission2github.model.Item
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*


class UserListAdapter(var users: ArrayList<Item>) : RecyclerView.Adapter<UserListAdapter.UserListViewHolder>() {

    var userListListener: UserListListener? = null

    fun updateUsers(user: ArrayList<Item>) {
        users.clear()
        users.addAll(user)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserListViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
    )

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(users[position])
        holder.itemView.setOnClickListener {
            userListListener?.onItemUserList(it, users[position], users)
        }
    }

    override fun getItemCount() = users.size

    class UserListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val username = view.tv_username
        private val idUser = view.tv_id
        private val profilePicture = view.iv_user_profile
        @SuppressLint("SetTextI18n")
        fun bind(user: Item) {
            username.text = user.login
            idUser.text = "${user.id}"
            Glide.with(itemView.context).load(user.avatarUrl).into(profilePicture)
        }
    }
}