package com.android.submission2github.utils

import android.content.ContentValues
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.submission2github.R
import com.android.submission2github.db.DatabaseContract
import com.android.submission2github.db.UserFavoriteHelper
import com.android.submission2github.helper.MappingHelper
import com.android.submission2github.model.Item
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object Utils {
    fun toast(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackbarMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun addFavoriteUser(item: Item, context: Context, view: View, isInsert: Boolean) {
        var itemList: ArrayList<Item>
        var favoriteList: ArrayList<Item>
        var favoriteHelper: UserFavoriteHelper
        GlobalScope.launch(Dispatchers.Main) {
            favoriteHelper = UserFavoriteHelper.getInstance(context)
            favoriteHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            itemList = deferredNotes.await()
            favoriteList = if (itemList.size > 0) {
                itemList
            } else {
                ArrayList()
            }
            favoriteHelper.close()
            if (isInsert){
                if (favoriteList.any {it.id == item.id}){
                    showSnackbarMessage(view, "User is already in favorites")
                } else {
                    insertValue(item, favoriteHelper, view)
                }
            }
        }
    }

    private fun insertValue(item: Item, favoriteHelper: UserFavoriteHelper, view: View) {
        favoriteHelper.open()

        val values = ContentValues()
        values.put(DatabaseContract.UserColumn._ID, item.id)
        values.put(DatabaseContract.UserColumn.LOGIN, item.login)
        values.put(DatabaseContract.UserColumn.AVATAR_URL, item.avatarUrl)
        values.put(DatabaseContract.UserColumn.NAME, if(item.name != null){ item.name } else { "null" })
        values.put(DatabaseContract.UserColumn.EMAIL, if(item.email != null){ item.email } else { "null" })
        values.put(DatabaseContract.UserColumn.LOCATION, if(item.location != null){ item.location } else { "null" })
        values.put(DatabaseContract.UserColumn.COMPANY, if(item.company != null){ item.company } else { "null" })
        values.put(DatabaseContract.UserColumn.BLOG, if(item.blog != null){ item.blog } else { "null" })
        values.put(DatabaseContract.UserColumn.REPO, item.repo)
        values.put(DatabaseContract.UserColumn.FOLLOWERS, item.followers)
        values.put(DatabaseContract.UserColumn.FOLLOWING, item.following)

        favoriteHelper.insert(values)
        showSnackbarMessage(view, "User has been successfully added to favorites")
    }

    fun removeUser(context: Context, item: Item, view: View) {
        val favoriteHelper = UserFavoriteHelper.getInstance(context)
        favoriteHelper.open()
        favoriteHelper.deleteFavorite(item.id)
        showSnackbarMessage(view, "User has been successfully deleted")
        favoriteHelper.close()
    }
}