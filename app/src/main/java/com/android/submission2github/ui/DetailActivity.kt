package com.android.submission2github.ui

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.android.submission2github.R
import com.android.submission2github.adapter.PagerAdapter
import com.android.submission2github.databinding.ActivityDetailBinding
import com.android.submission2github.db.UserFavoriteHelper
import com.android.submission2github.helper.MappingHelper
import com.android.submission2github.model.Item
import com.android.submission2github.utils.Utils.addFavoriteUser
import com.android.submission2github.viewmodel.GetUserViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private var item: Item? = null
    private var detailItem: Item? = null
    private var username: String? = null
    private var status: String? = null

    private lateinit var viewModel: GetUserViewModel
    private lateinit var b : ActivityDetailBinding

    companion object{
        var KEY_DETAIL_DATA = "detail_data"
        var INTENT = "intent_from"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        setupColoStatusBar()
        setClickListener()
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

    private fun setClickListener() {
        b.btnAddFavorite.setOnClickListener(this)
    }

    private fun getData(){
        status = intent.getStringExtra(INTENT)
        if (status == "FAVORITE"){
            item = intent.getParcelableExtra(KEY_DETAIL_DATA)
            b.loadingView.visibility = GONE
            username = item?.login
            item?.let { setupItem(it) }
        } else if (status == "LISTUSER"){
            item = intent.getParcelableExtra(KEY_DETAIL_DATA)
            username = item?.login
            loadData()
        }
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
                detailItem = it
            }
        })
        viewModel.loading.observe(this, { isLoading ->
            isLoading?.let {
                b.loadingView.visibility = if (it) VISIBLE else GONE
                if (it) {
                    b.placeholderDetail.visibility = GONE
                } else {
                    b.placeholderDetail.visibility = VISIBLE
                }
            }
        })
    }

    private fun setupItem(item: Item) {
        b.tvName.text = if (item.name != null){ item.name } else { "null" }
        b.tvUsername.text = if (item.login != null){ "@${item.login}" } else { "null" }
        b.tvLocation.text = if (item.location != null){ item.location } else { "null" }
        b.tvEmail.text = if (item.email != null){ item.email } else { "null" }
        b.tvTotalRepo.text = "${item.repo}"
        b.tvTotalFollower.text = "${item.followers}"
        b.tvTotalFollowing.text = "${item.following}"
        Glide.with(this).load(item.avatarUrl).into(b.ivUserProfile)
        checkUserFavorite(item, this)
    }

    private fun checkUserFavorite(item: Item, context: Context) {
        var itemList: ArrayList<Item>
        var favoriteList: ArrayList<Item>
        var favoriteHelper: UserFavoriteHelper
        GlobalScope.launch(Dispatchers.Main) {
            favoriteHelper = UserFavoriteHelper.getInstance(context)
            favoriteHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            itemList = deferredNotes.await()
            favoriteList = if (itemList.size > 0) {
                itemList
            } else {
                ArrayList()
            }
            favoriteHelper.close()
            if (favoriteList.any {it.id == item.id}){
                b.btnAddFavorite.setColorFilter(ContextCompat.getColor(context, R.color.red))
            }
        }
    }

    private fun setupViewPager(){
        b.viewpager.adapter = username?.let { PagerAdapter(supportFragmentManager, it) }
        b.tablayout.setupWithViewPager(b.viewpager)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_add_favorite -> detailItem?.let {
                addFavoriteUser(it, this, b.root, true)
                b.btnAddFavorite.setColorFilter(ContextCompat.getColor(this, R.color.red))
            }
        }
    }
}