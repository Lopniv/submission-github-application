package com.android.consumerapp.api

import com.android.consumerapp.viewmodel.FollowerViewModel
import com.android.consumerapp.viewmodel.FollowingViewModel
import com.android.consumerapp.viewmodel.GetUserViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: UsersService)
    fun inject(followerViewModel: FollowerViewModel)
    fun inject(followingViewModel: FollowingViewModel)
    fun inject(getUserViewModel: GetUserViewModel)
}