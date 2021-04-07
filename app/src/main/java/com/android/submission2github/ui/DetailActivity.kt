package com.android.submission2github.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.submission2github.adapter.PagerAdapter
import com.android.submission2github.databinding.ActivityDetailBinding
import com.android.submission2github.model.Item
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private var item: Item? = null
    private var username: String? = null
    private var idUser: Int? = null
    private var type: String? = null
    private var imageUrl: String? = null

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
        idUser = item?.id
        type = item?.type
        imageUrl = item?.avatarUrl
        setupItem()
    }

    private fun setupItem() {
        b.tvUsername.text = username
        b.tvId.text = "$idUser"
        b.tvType.text = type
        Glide.with(this).load(imageUrl).into(b.ivUserProfile)
    }

    private fun setupViewPager(){
        b.viewpager.adapter = username?.let { PagerAdapter(supportFragmentManager, it) }
        b.tablayout.setupWithViewPager(b.viewpager)
    }
}