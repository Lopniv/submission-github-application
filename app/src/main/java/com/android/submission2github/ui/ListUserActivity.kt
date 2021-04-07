package com.android.submission2github.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2github.R
import com.android.submission2github.adapter.FavoriteListener
import com.android.submission2github.adapter.UserListAdapter
import com.android.submission2github.adapter.UserListListener
import com.android.submission2github.databinding.ActivityListUserBinding
import com.android.submission2github.model.Item
import com.android.submission2github.ui.DetailActivity.Companion.KEY_DETAIL_DATA
import com.android.submission2github.utils.Utils
import com.android.submission2github.utils.Utils.loadFavoriteAsync
import com.android.submission2github.viewmodel.SearchUserViewModel
import kotlinx.android.synthetic.main.activity_list_user.*

class ListUserActivity : AppCompatActivity(), UserListListener, FavoriteListener, View.OnClickListener {

    private var username: String? = null
    private var userList: ArrayList<Item>? = null
    private var userListAdapter: UserListAdapter? = null

    private lateinit var b: ActivityListUserBinding
    private lateinit var viewModel: SearchUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(b.root)
        setClickListener()
        initiateValue()
        getData()
        inputUsername()
    }

    private fun setClickListener(){
        b.btnFavoriteList.setOnClickListener(this)
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
            b.etSearchUser.setText(it)
            loadData(it)
        }
    }

    private fun inputUsername(){
        b.etSearchUser.setOnEditorActionListener { _, actionId, _ ->
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
        b.rvUserList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
        b.swipeRefreshLayout.setOnRefreshListener {
            b.swipeRefreshLayout.isRefreshing = false
            viewModel.refresh(username)
        }
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.users.observe(this, { user ->
            user?.let {
                if (it.isEmpty()) {
                    Log.e("TAG", "NULL ITEM: $it")
                    b.tvNotFound.visibility = View.VISIBLE
                    userList = it
                } else {
                    Log.e("TAG", "ITEM: $it")
                    b.tvNotFound.visibility = View.GONE
                    b.rvUserList.visibility = View.VISIBLE
                    userListAdapter?.updateUsers(it)
                    userList = it
                }
            }
        })
        viewModel.loading.observe(this, { isLoading ->
            isLoading?.let {
                b.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    b.tvNotFound.visibility = View.GONE
                    b.rvUserList.visibility = View.GONE
                }
            }
        })
    }

    private fun search(){
        when {
            b.etSearchUser.text.isNullOrEmpty() ->
                Utils.showSnackbarMessage(b.root, "Fill the input field!")
            else -> {
                username = b.etSearchUser.text.toString().trim()
                username?.let {
                    loadData(it)
                }
            }
        }
    }

    private fun favoriteList(){

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_favorite_list -> favoriteList()
        }
    }

    override fun onItemUserList(view: View, item: Item, listItem: ArrayList<Item>) {
        val detail = Intent(this, DetailActivity::class.java)
        detail.putExtra(KEY_DETAIL_DATA, item)
        startActivity(detail)
    }

    override fun addFavoriteUser(view: View, item: Item, listItem: ArrayList<Item>) {
        loadFavoriteAsync(item, this, b.root, true)
    }
}