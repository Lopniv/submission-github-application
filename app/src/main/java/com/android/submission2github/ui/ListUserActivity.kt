package com.android.submission2github.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2github.adapter.UserListAdapter
import com.android.submission2github.adapter.UserListListener
import com.android.submission2github.databinding.ActivityListUserBinding
import com.android.submission2github.db.DatabaseContract
import com.android.submission2github.db.UserFavoriteHelper
import com.android.submission2github.helper.MappingHelper
import com.android.submission2github.model.Item
import com.android.submission2github.ui.DetailActivity.Companion.KEY_DETAIL_DATA
import com.android.submission2github.viewmodel.SearchUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListUserActivity : AppCompatActivity(), UserListListener {

    private var username: String? = null
    private var userList: ArrayList<Item>? = null
    private var favoriteList: ArrayList<Item>? = null
    private var userListAdapter: UserListAdapter? = null

    private lateinit var binding: ActivityListUserBinding
    private lateinit var viewModel: SearchUserViewModel
    private lateinit var userFavoriteHelper: UserFavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFavoriteAsync()
        initiateValue()
        getData()
        inputUsername()
    }

    private fun loadFavoriteAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            userFavoriteHelper = UserFavoriteHelper.getInstance(applicationContext)
            userFavoriteHelper.open()
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = userFavoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            userFavoriteHelper.close()
            favoriteList = deferredFavorites.await()
//            favoriteList = if (favorite.size > 0) {
//                favorite
//            } else {
//                ArrayList()
//            }
        }
    }

    private fun initiateValue(){
        userListAdapter = UserListAdapter(arrayListOf(), this)
        userListAdapter!!.userListListener = this
    }

    private fun getData(){
        username = intent.getStringExtra("USERNAME")
        username?.let {
            Log.e("TAG", "USERNAME: $it")
            binding.etSearchUser.setText(it)
            loadData(it)
        }
    }

    private fun inputUsername(){
        binding.etSearchUser.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadData(username: String){
        viewModel = ViewModelProviders.of(this).get(SearchUserViewModel::class.java)
        viewModel.refresh(username)
        binding.rvUserList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.refresh(username)
        }
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.users.observe(this, Observer { user ->
            user?.let {
                if (it.isEmpty()) {
                    Log.e("TAG", "NULL ITEM: $it")
                    binding.tvNotFound.visibility = View.VISIBLE
                    userList = it
                } else {
                    Log.e("TAG", "ITEM: $it")
                    binding.tvNotFound.visibility = View.GONE
                    binding.rvUserList.visibility = View.VISIBLE
                    userListAdapter?.updateUsers(it)
                    userList = it
                }
            }
        })
        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.tvNotFound.visibility = View.GONE
                    binding.rvUserList.visibility = View.GONE
                }
            }
        })
    }

    private fun search(){
        when {
            binding.etSearchUser.text.isNullOrEmpty() ->
                Toast.makeText(this, "Fill the input field!", Toast.LENGTH_SHORT).show()
            else -> {
                username = binding.etSearchUser.text.toString().trim()
                username?.let {
                    loadData(it)
                }
            }
        }
    }

    override fun onItemUserList(view: View, item: Item, listItem: ArrayList<Item>) {
        val detail = Intent(this, DetailActivity::class.java)
        detail.putExtra(KEY_DETAIL_DATA, item)
        startActivity(detail)
    }

    override fun addFavoriteUser(view: View, item: Item, listItem: ArrayList<Item>) {
        if (favoriteList?.any { it.id == item.id} == true){
            Toast.makeText(this, "User already favorite list", Toast.LENGTH_SHORT).show()
        } else {
            insertValue(item)
        }
    }

    private fun insertValue(item: Item) {
        userFavoriteHelper.open()

        val values = ContentValues()
        values.put(DatabaseContract.UserColumn._ID, item.id)
        values.put(DatabaseContract.UserColumn.LOGIN, item.login)
        values.put(DatabaseContract.UserColumn.AVATAR_URL, item.avatarUrl)
        values.put(DatabaseContract.UserColumn.NODE_ID, item.nodeId)
        values.put(DatabaseContract.UserColumn.TYPE, item.type)

        userFavoriteHelper.insert(values)
    }
}