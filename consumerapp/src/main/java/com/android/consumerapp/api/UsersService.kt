package com.android.consumerapp.api

import com.android.consumerapp.model.Item
import io.reactivex.Single
import javax.inject.Inject

class UsersService {
    @Inject
    lateinit var api: UsersApi
    init {
        DaggerApiComponent.create().inject(this)
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