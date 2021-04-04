package com.android.submission2github.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class SearchResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    val items: ArrayList<Item>
)

@Parcelize
data class Item(
    var login: String? = null,
    var id: Int = 0,
    @SerializedName("node_id")
    var nodeId: String? = null,
    @SerializedName("avatar_url")
    var avatarUrl: String? = null,
    var type: String? = null
): Parcelable