package com.android.submission2github.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2github.R
import com.android.submission2github.adapter.FavoriteUserAdapter
import com.android.submission2github.adapter.UserListAdapter
import com.android.submission2github.adapter.UserListListener
import com.android.submission2github.databinding.ActivityFavoriteBinding
import com.android.submission2github.db.UserFavoriteHelper
import com.android.submission2github.helper.MappingHelper
import com.android.submission2github.model.Item
import com.android.submission2github.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(), View.OnClickListener, UserListListener {

    private var favoriteUserAdapter: FavoriteUserAdapter? = null
    private var itemList: ArrayList<Item>? = null
    private var favoriteList: ArrayList<Item>? = null

    private lateinit var favoriteHelper: UserFavoriteHelper
    private lateinit var b: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(b.root)
        setClickListener()
        initiateValue()
        setupRecyclerView()
        loadFavoriteAsync()
    }

    private fun setClickListener() {
        b.btnEdit.setOnClickListener(this)
    }

    private fun initiateValue(){
        favoriteHelper = UserFavoriteHelper.getInstance(this)
        favoriteUserAdapter = FavoriteUserAdapter(arrayListOf())
        favoriteUserAdapter?.userListListener = this
    }

    private fun setupRecyclerView() {
        b.rvFavoriteList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteUserAdapter
        }
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            favoriteHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            itemList = deferredNotes.await()
            favoriteList = if (itemList?.size!! > 0) {
                itemList
            } else {
                ArrayList()
            }
            loadData()
        }
    }

    private fun loadData() {
        favoriteList?.let { favoriteUserAdapter?.updateUsers(it) }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            //R.id.btn_edit ->
        }
    }

    override fun onItemUserList(view: View, item: Item, listItem: ArrayList<Item>) {
        val detail = Intent(this, DetailActivity::class.java)
        detail.putExtra(DetailActivity.KEY_DETAIL_DATA, item)
        detail.putExtra(DetailActivity.INTENT, "FAVORITE")
        startActivity(detail)
    }
}