package com.android.submission2github.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.android.submission2github.adapter.PagerAdapter
import com.android.submission2github.databinding.ActivityDetailBinding
import com.android.submission2github.model.Item
import com.android.submission2github.viewmodel.GetUserViewModel
import com.android.submission2github.viewmodel.SearchUserViewModel
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private var item: Item? = null
    private var username: String? = null

    private lateinit var viewModel: GetUserViewModel
    private lateinit var b : ActivityDetailBinding

    companion object{
        var KEY_DETAIL_DATA = "detail_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        setupColoStatusBar()
        getData()
        setupViewPager()
    }

    private fun setupColoStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.BLACK
        }
    }

    private fun getData(){
        item = intent.getParcelableExtra(KEY_DETAIL_DATA)
        username = item?.login
        loadData()
//        name = item?.name
//        location = item?.location
//        company = item?.company
//        email = item?.email
//        blog = item?.blog
//        imageUrl = item?.avatarUrl
//        setupItem()
    }

    private fun loadData() {
        viewModel = ViewModelProviders.of(this).get(GetUserViewModel::class.java)
        username?.let {
            viewModel.refresh(it)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.users.observe(this, { user ->
            user?.let {
                setupItem(it)
            }
        })
        viewModel.loading.observe(this, { isLoading ->
            isLoading?.let {
                b.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    b.placeholderDetail.visibility = View.GONE
                } else {
                    b.placeholderDetail.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setupItem(item: Item) {
        b.tvName.text = item.name
        b.tvLocation.text = item.location
        b.tvCompany.text = item.company
        b.tvBlog.text = item.blog
        b.tvEmail.text = item.email
        Glide.with(this).load(item.avatarUrl).into(b.ivUserProfile)
    }

    private fun setupViewPager(){
        b.viewpager.adapter = username?.let { PagerAdapter(supportFragmentManager, it) }
        b.tablayout.setupWithViewPager(b.viewpager)
    }
}