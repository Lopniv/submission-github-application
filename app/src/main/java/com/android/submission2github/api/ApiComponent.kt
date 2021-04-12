package com.android.submission2github.api

import com.android.submission2github.viewmodel.FollowerViewModel
import com.android.submission2github.viewmodel.FollowingViewModel
import com.android.submission2github.viewmodel.GetUserViewModel
import com.android.submission2github.viewmodel.SearchUserViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: UsersService)
    fun inject(searchUserViewModel: SearchUserViewModel)
    fun inject(followerViewModel: FollowerViewModel)
    fun inject(followingViewModel: FollowingViewModel)
    fun inject(getUserViewModel: GetUserViewModel)
}