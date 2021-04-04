package com.android.submission2github.db

import android.provider.BaseColumns
import com.google.gson.annotations.SerializedName

class DatabaseContract {
    internal class UserColumn : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val NODE_ID = "nodeId"
            const val AVATAR_URL = "avatarUrl"
            const val TYPE = "type"
        }
    }
}