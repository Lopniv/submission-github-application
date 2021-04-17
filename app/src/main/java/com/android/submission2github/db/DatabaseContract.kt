package com.android.submission2github.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.android.submission2github"
    const val SCHEME = "content"

    internal class UserColumn : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatarUrl"
            const val NAME = "name"
            const val LOCATION = "location"
            const val EMAIL = "email"
            const val REPO = "repo"
            const val FOLLOWERS = "followers"
            const val FOLLOWING = "following"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()
        }
    }
}