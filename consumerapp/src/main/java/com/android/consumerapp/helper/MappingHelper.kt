package com.android.consumerapp.helper

import android.database.Cursor
import com.android.consumerapp.db.DatabaseContract
import com.android.consumerapp.model.Item
import java.util.ArrayList

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Item> {
        val notesList = ArrayList<Item>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.AVATAR_URL))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.NAME))
                val email = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.EMAIL))
                val location = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.LOCATION))
                val repo = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.REPO))
                val follower = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.FOLLOWERS))
                val following = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn.FOLLOWING))
                notesList.add(Item(login, id, avatarUrl, name, location, email, repo, follower, following))
            }
        }
        return notesList
    }
}