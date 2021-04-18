package com.android.submission2github.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.submission2github.R
import com.android.submission2github.adapter.FavoriteUserAdapter
import com.android.submission2github.adapter.RemoveUserListener
import com.android.submission2github.adapter.UserListListener
import com.android.submission2github.databinding.ActivityFavoriteBinding
import com.android.submission2github.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.android.submission2github.helper.MappingHelper
import com.android.submission2github.model.Item
import com.android.submission2github.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private var favoriteUserAdapter: FavoriteUserAdapter? = null
    private var itemList: ArrayList<Item>? = null
    private var favoriteList: ArrayList<Item>? = null

    private lateinit var b: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(b.root)
        setClickListener()
        setupRecyclerView(false)
    }

    private fun setClickListener() {
        b.btnEdit.setOnClickListener(this)
        b.btnCancel.setOnClickListener(this)
        b.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun setupRecyclerView(isEdit: Boolean) {
        b.swipeRefreshLayout.isRefreshing = false
        b.loadingView.visibility = VISIBLE
        b.rvFavoriteList.visibility = INVISIBLE
        b.rvFavoriteList.apply {
            favoriteUserAdapter = FavoriteUserAdapter(arrayListOf(), isEdit)
            favoriteUserAdapter?.userListListener = onItemUserList
            favoriteUserAdapter?.removeUserListener = onRemoveUser
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteUserAdapter
        }
        loadFavoriteAsync()
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
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
        if (favoriteList?.size!! > 0){
            favoriteList?.let { favoriteUserAdapter?.updateUsers(it) }
            b.loadingView.visibility = INVISIBLE
            b.rvFavoriteList.visibility = VISIBLE
            b.tvNotFound.visibility = INVISIBLE
        } else {
            b.loadingView.visibility = INVISIBLE
            b.tvNotFound.visibility = VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_edit -> edit()
            R.id.btn_cancel -> cancel()
        }
    }

    private val onItemUserList = object : UserListListener{
        override fun onItemUserList(view: View, item: Item, listItem: ArrayList<Item>) {
            val detail = Intent(this@FavoriteActivity, DetailActivity::class.java)
            detail.putExtra(DetailActivity.KEY_DETAIL_DATA, item)
            detail.putExtra(DetailActivity.INTENT, "FAVORITE")
            startActivity(detail)
        }
    }

    private val onRemoveUser = object: RemoveUserListener {
        override fun removeUser(item: Item) {
            Utils.removeUser(this@FavoriteActivity, item, b.root)
            cancel()
        }
    }

    private fun cancel() {
        setupRecyclerView(false)
        b.btnEdit.visibility = VISIBLE
        b.btnCancel.visibility = INVISIBLE
    }

    private fun edit() {
        setupRecyclerView(true)
        b.btnEdit.visibility = INVISIBLE
        b.btnCancel.visibility = VISIBLE
    }

    override fun onRefresh() {
        setupRecyclerView(false)
    }
}