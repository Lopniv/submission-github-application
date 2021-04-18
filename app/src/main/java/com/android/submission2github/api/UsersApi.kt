package com.android.submission2github.api

import com.android.submission2github.model.Item
import com.android.submission2github.model.SearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {
    /**
     * Token Header
     * @Headers("Authorization: token fc389f9526f6a885c233df67c09fb8f4a53cf843")
     * **/

    @GET("search/users")
    fun searchUser(
        @Query("q") user: String
    ): Single<SearchResponse>

    @GET("users/{user}")
    fun user(
        @Path("user") user: String
    ): Single<Item>

    @GET("users/{user}/followers")
    fun followers(
        @Path("user") user: String
    ): Single<ArrayList<Item>>

    @GET("users/{user}/following")
    fun following(
        @Path("user") user: String
    ): Single<ArrayList<Item>>
}