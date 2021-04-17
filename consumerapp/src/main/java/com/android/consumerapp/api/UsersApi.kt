package com.android.consumerapp.api

import com.android.consumerapp.model.Item
import com.android.consumerapp.model.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {
    @GET("users/{user}")
    //@Headers("Authorization: token fc389f9526f6a885c233df67c09fb8f4a53cf843")
    fun user(
        @Path("user") user: String
    ): Single<Item>

    @GET("users/{user}/followers")
    //@Headers("Authorization: token fc389f9526f6a885c233df67c09fb8f4a53cf843")
    fun followers(
        @Path("user") user: String
    ): Single<ArrayList<Item>>

    @GET("users/{user}/following")
    //@Headers("Authorization: token fc389f9526f6a885c233df67c09fb8f4a53cf843")
    fun following(
        @Path("user") user: String
    ): Single<ArrayList<Item>>
}