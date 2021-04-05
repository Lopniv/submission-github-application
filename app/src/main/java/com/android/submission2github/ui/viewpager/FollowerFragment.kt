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
import com.android.submission2github.db.UserFavoriteHelper
import com.android.submission2github.helper.MappingHelper
import com.android.submission2github.model.Item
import com.android.submission2github.viewmodel.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.android.synthetic.main.fragment_follower.loading_view
import kotlinx.android.synthetic.main.fragment_follower.rv_user_list
import kotlinx.android.synthetic.main.fragment_follower.swipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_follower.tv_not_found
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FollowerFragment(var username: String) : Fragment() {

    private var userList: ArrayList<Item>? = null
    private var userListAdapter: UserListAdapter? = null

    private lateinit var viewModel: FollowerViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initiate()
        getData()
    }

    private fun initiate(){
        userListAdapter = UserListAdapter(arrayListOf(), requireContext())
    }

    private fun getData(){
        loadData(username)
    }

    private fun loadData(username: String){
        viewModel = activity?.let { ViewModelProviders.of(it).get(FollowerViewModel::class.java) }!!
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
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }
}