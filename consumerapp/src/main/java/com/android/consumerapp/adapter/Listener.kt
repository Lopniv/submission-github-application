package com.android.consumerapp.adapter

import android.view.View
import com.android.consumerapp.model.Item

interface UserListListener {
    fun onItemUserList(view: View, item: Item, listItem: ArrayList<Item>)
}

interface RemoveUserListener {
    fun removeUser(item: Item)
}