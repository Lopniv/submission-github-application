package com.android.submission2github.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.submission2github.R
import com.android.submission2github.adapter.PagerAdapter
import com.android.submission2github.model.Item
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    var item: Item? = null
    var username: String? = null
    var idUser: Int? = null
    var type: String? = null
    var imageUrl: String? = null

    companion object{
        var KEY_DETAIL_DATA = "detail_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
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
        tv_username.text = username
        tv_id.text = "$idUser"
        tv_type.text = type
        Glide.with(this).load(imageUrl).into(iv_user_profile)
    }

    private fun setupViewPager(){
        viewpager.adapter = username?.let { PagerAdapter(supportFragmentManager, it) }
        tablayout.setupWithViewPager(viewpager)
    }
}