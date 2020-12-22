package com.android.submission2github.adapter

import android.view.View
import com.android.submission2github.model.Item

interface UserListListener {
    fun onItemUserList(view: View, item: Item, listItem: ArrayList<Item>)
}