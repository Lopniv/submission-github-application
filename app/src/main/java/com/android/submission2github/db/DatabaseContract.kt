package com.android.submission2github.db

import android.provider.BaseColumns
import com.google.gson.annotations.SerializedName

class DatabaseContract {
    internal class UserColumn : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatarUrl"
            const val NAME = "name"
            const val COMPANY = "company"
            const val BLOG = "blog"
            const val LOCATION = "location"
            const val EMAIL = "email"
            const val REPO = "repo"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"
        }
    }
}