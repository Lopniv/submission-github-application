package com.android.submission2github.utils

import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.view.View
import com.android.submission2github.db.DatabaseContract
import com.android.submission2github.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.android.submission2github.helper.MappingHelper
import com.android.submission2github.model.Item
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object Utils {
    fun showSnackbarMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun addFavoriteUser(item: Item, view: View, isInsert: Boolean, activity: Activity) {
        var itemList: ArrayList<Item>
        var favoriteList: ArrayList<Item>
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = activity.contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            itemList = deferredNotes.await()
            favoriteList = if (itemList.size > 0) {
                itemList
            } else {
                ArrayList()
            }
            if (isInsert){
                if (favoriteList.any {it.id == item.id}){
                    showSnackbarMessage(view, "User is already in favorites")
                } else {
                    insertValue(item, activity, view)
                }
            }
        }
    }

    private fun insertValue(item: Item, activity: Activity, view: View) {
        val values = ContentValues()
        values.put(DatabaseContract.UserColumn._ID, item.id)
        values.put(DatabaseContract.UserColumn.LOGIN, item.login)
        values.put(DatabaseContract.UserColumn.AVATAR_URL, item.avatarUrl)
        values.put(DatabaseContract.UserColumn.NAME, if(item.name != null){ item.name } else { "null" })
        values.put(DatabaseContract.UserColumn.EMAIL, if(item.email != null){ item.email } else { "null" })
        values.put(DatabaseContract.UserColumn.LOCATION, if(item.location != null){ item.location } else { "null" })
        values.put(DatabaseContract.UserColumn.REPO, item.repo)
        values.put(DatabaseContract.UserColumn.FOLLOWERS, item.followers)
        values.put(DatabaseContract.UserColumn.FOLLOWING, item.following)

        activity.contentResolver.insert(CONTENT_URI, values)
        showSnackbarMessage(view, "User has been successfully added to favorites")
    }

    fun removeUser(activity: Activity, item: Item, view: View) {
        val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + item.id)
        activity.contentResolver.delete(uriWithId, null, null)
        showSnackbarMessage(view, "User has been successfully deleted")
    }
}