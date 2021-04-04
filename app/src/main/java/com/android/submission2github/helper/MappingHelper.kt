package com.android.submission2github.helper

import android.database.Cursor
import com.android.submission2github.db.DatabaseContract
import com.android.submission2github.model.Item
import java.util.ArrayList

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Item> {
        val notesList = ArrayList<Item>()

        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumn._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.LOGIN))
                val nodeId = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.NODE_ID))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.AVATAR_URL))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.UserColumn.TYPE))
                notesList.add(Item(login, id, nodeId, avatarUrl, type))
            }
        }
        return notesList
    }
}