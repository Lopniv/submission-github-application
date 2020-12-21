package com.android.submission2github.api

import com.android.submission2github.viewmodel.SearchUserViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: UsersService)
    fun inject(searchUserViewModel: SearchUserViewModel)
}