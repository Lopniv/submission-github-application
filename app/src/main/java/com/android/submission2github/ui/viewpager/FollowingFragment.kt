package com.android.submission2github.ui.viewpager

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2github.R
import com.android.submission2github.adapter.UserListAdapter
import com.android.submission2github.model.Item
import com.android.submission2github.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment(var username: String) : Fragment() {

    private lateinit var viewModel: FollowingViewModel
    private var userListAdapter: UserListAdapter? = null
    private var userList: ArrayList<Item>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initiate()
        getData()
    }

    private fun initiate(){
        userListAdapter = activity?.let { UserListAdapter(arrayListOf(), it) }
    }

    private fun getData(){
        loadData(username)
    }

    private fun loadData(username: String){
        viewModel = activity?.let { ViewModelProviders.of(it).get(FollowingViewModel::class.java) }!!
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
        viewModel.users.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                if (it.isEmpty()) {
                    Log.e("TAG", "NULL ITEM: $it")
                    tv_not_found.visibility = View.VISIBLE
                    userList = it
                } else {
                    Log.e("TAG", "ITEM: $it")
                    tv_not_found.visibility = View.GONE
                    rv_user_list.visibility = View.VISIBLE
                    userListAdapter?.updateUsers(it)
                    userList = it
                }
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    tv_not_found.visibility = View.GONE
                    rv_user_list.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }
}