package com.android.submission2github.api

import com.android.submission2github.model.Item
import com.android.submission2github.model.SearchResponse
import io.reactivex.Single
import javax.inject.Inject

class UsersService {
    @Inject
    lateinit var api: UsersApi
    init {
        DaggerApiComponent.create().inject(this)
    }

    fun searchUser(user: String): Single<SearchResponse> {
        return api.searchUser(user)
    }

    fun user(user: String): Single<Item>{
        return api.user(user)
    }

    fun followers(user: String): Single<ArrayList<Item>>{
        return api.followers(user)
    }

    fun following(user: String): Single<ArrayList<Item>>{
        return api.following(user)
    }
}