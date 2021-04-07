package com.android.submission2github.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2github.adapter.FavoriteListener
import com.android.submission2github.adapter.UserListAdapter
import com.android.submission2github.adapter.UserListListener
import com.android.submission2github.databinding.ActivityListUserBinding
import com.android.submission2github.db.DatabaseContract
import com.android.submission2github.db.UserFavoriteHelper
import com.android.submission2github.helper.MappingHelper
import com.android.submission2github.model.Item
import com.android.submission2github.ui.DetailActivity.Companion.KEY_DETAIL_DATA
import com.android.submission2github.utils.Utils
import com.android.submission2github.viewmodel.SearchUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListUserActivity : AppCompatActivity(), UserListListener, FavoriteListener {

    private var username: String? = null
    private var userList: ArrayList<Item>? = null
    private var favoriteList: ArrayList<Item>? = null
    private var userListAdapter: UserListAdapter? = null
    private var favoriteHelper: UserFavoriteHelper? = null

    private lateinit var binding: ActivityListUserBinding
    private lateinit var viewModel: SearchUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initiateValue()
        getData()
        inputUsername()
    }

    private fun initiateValue(){
        userListAdapter = UserListAdapter(arrayListOf(), this)
        userListAdapter?.userListListener = this
        userListAdapter?.favoriteListener = this
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
        binding.etSearchUser.setOnEditorActionListener { _, actionId, _ ->
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
        viewModel.users.observe(this, { user ->
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
        viewModel.loading.observe(this, { isLoading ->
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
                Utils.showSnackbarMessage(binding.root, "Fill the input field!")
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
        loadFavoriteAsync(item)
    }

    private fun loadFavoriteAsync(item: Item) {
        GlobalScope.launch(Dispatchers.Main) {
            favoriteHelper = UserFavoriteHelper.getInstance(applicationContext)
            favoriteHelper?.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favoriteHelper?.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val notes = deferredNotes.await()
            favoriteList = if (notes.size > 0) {
                notes
            } else {
                ArrayList()
            }
            favoriteHelper?.close()
            if (favoriteList?.any { it.id == item.id} == true){
                Utils.showSnackbarMessage(binding.root, "User is already in favorites")
            } else {
                insertValue(item)
            }
        }
    }

    private fun insertValue(item: Item) {
        favoriteHelper?.open()

        val values = ContentValues()
        values.put(DatabaseContract.UserColumn._ID, item.id)
        values.put(DatabaseContract.UserColumn.LOGIN, item.login)
        values.put(DatabaseContract.UserColumn.AVATAR_URL, item.avatarUrl)
        values.put(DatabaseContract.UserColumn.NODE_ID, item.nodeId)
        values.put(DatabaseContract.UserColumn.TYPE, item.type)

        favoriteHelper?.insert(values)
        Utils.showSnackbarMessage(binding.root, "User has been successfully added to favorites")
    }
}