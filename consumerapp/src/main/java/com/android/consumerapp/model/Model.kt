package com.android.consumerapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    var login: String? = null,
    var id: Int = 0,
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,
    var name: String? = null,
    var location: String? = null,
    var email: String? = null,
    @SerializedName("public_repos")
    var repo: Int? = 0,
    var followers: Int? = 0,
    var following: Int? = 0
): Parcelable