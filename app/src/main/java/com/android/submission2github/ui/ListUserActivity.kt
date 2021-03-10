package com.android.submission2github.ui

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
import com.android.submission2github.R
import com.android.submission2github.adapter.UserListAdapter
import com.android.submission2github.adapter.UserListListener
import com.android.submission2github.model.Item
import com.android.submission2github.ui.DetailActivity.Companion.KEY_DETAIL_DATA
import com.android.submission2github.viewmodel.SearchUserViewModel
import kotlinx.android.synthetic.main.activity_list_user.*
import kotlinx.android.synthetic.main.activity_list_user.et_search_user
import kotlinx.android.synthetic.main.activity_list_user.loading_view
import kotlinx.android.synthetic.main.activity_list_user.rv_user_list
import kotlinx.android.synthetic.main.activity_list_user.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_list_user.tv_not_found
import kotlinx.android.synthetic.main.activity_search.*

class ListUserActivity : AppCompatActivity(), UserListListener {

    private var username: String? = null
    private lateinit var viewModel: SearchUserViewModel
    private val userListAdapter = UserListAdapter(arrayListOf())
    private var userList: ArrayList<Item>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        initiateValue()
        getData()
        inputUsername()
    }

    private fun initiateValue(){
        userListAdapter.userListListener = this
    }

    private fun getData(){
        username = intent.getStringExtra("USERNAME")
        username?.let {
            Log.e("TAG", "USERNAME: $it")
            et_search_user.setText(it)
            loadData(it)
        }
    }

    private fun inputUsername(){
        et_search_user.setOnEditorActionListener { v, actionId, event ->
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
        rv_user_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            viewModel.refresh(username)
        }
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.users.observe(this, Observer { user ->
            user?.let {
                if (it.isEmpty()) {
                    Log.e("TAG", "NULL ITEM: $it")
                    tv_not_found.visibility = View.VISIBLE
                    userList = it
                } else {
                    Log.e("TAG", "ITEM: $it")
                    tv_not_found.visibility = View.GONE
                    rv_user_list.visibility = View.VISIBLE
                    userListAdapter.updateUsers(it)
                    userList = it
                }
            }
        })
        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    tv_not_found.visibility = View.GONE
                    rv_user_list.visibility = View.GONE
                }
            }
        })
    }

    private fun search(){
        when {
            et_search_user.text.isNullOrEmpty() ->
                Toast.makeText(this, "Fill the input field!", Toast.LENGTH_SHORT).show()
            else -> {
                username = et_search_user.text.toString().trim()
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
}