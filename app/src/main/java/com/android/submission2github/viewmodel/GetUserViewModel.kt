package com.android.submission2github.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.submission2github.api.DaggerApiComponent
import com.android.submission2github.api.UsersService
import com.android.submission2github.model.Item
import com.android.submission2github.model.SearchResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GetUserViewModel: ViewModel() {
    @Inject
    lateinit var usersService: UsersService
    init {
        DaggerApiComponent.create().inject(this)
    }
    private val disposable = CompositeDisposable()
    val users = MutableLiveData<Item>()
    val loading = MutableLiveData<Boolean>()
    fun refresh(username: String) {
        getUser(username)
    }
    private fun getUser(username: String) {
        loading.value = true
        disposable.add(
                usersService.user(username)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Item>() {
                            override fun onSuccess(data: Item) {
                                Log.d("success ", "" + data)
                                users.value = data
                                loading.value = false
                            }
                            @SuppressLint("NewApi")
                            override fun onError(e: Throwable) {
                                Log.d("error ", "" + e.message)
                                users.value = null
                                loading.value = false
                            }
                        })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}