package com.android.submission2github.ui.viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2github.adapter.UserListAdapter
import com.android.submission2github.databinding.FragmentFollowingBinding
import com.android.submission2github.model.Item
import com.android.submission2github.viewmodel.FollowingViewModel

class FollowingFragment(var username: String) : Fragment() {

    private var userList: ArrayList<Item>? = null
    private var userListAdapter: UserListAdapter? = null
    private var binding: FragmentFollowingBinding? = null
    private val b get() = binding

    private lateinit var viewModel: FollowingViewModel

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
        viewModel = activity?.let { ViewModelProviders.of(it).get(FollowingViewModel::class.java) }!!
        viewModel.refresh(username)
        b?.rvUserList?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
        b?.swipeRefreshLayout?.setOnRefreshListener {
            b?.swipeRefreshLayout?.isRefreshing = false
            viewModel.refresh(username)
        }
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.users.observe(viewLifecycleOwner, { user ->
            user?.let {
                if (it.isEmpty()) {
                    Log.e("TAG", "NULL ITEM: $it")
                    b?.tvNotFound?.visibility = View.VISIBLE
                    userList = it
                } else {
                    Log.e("TAG", "ITEM: $it")
                    b?.tvNotFound?.visibility = View.GONE
                    b?.rvUserList?.visibility = View.VISIBLE
                    userListAdapter?.updateUsers(it)
                    userList = it
                }
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            isLoading?.let {
                b?.loadingView?.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    b?.tvNotFound?.visibility = View.GONE
                    b?.rvUserList?.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return b?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}