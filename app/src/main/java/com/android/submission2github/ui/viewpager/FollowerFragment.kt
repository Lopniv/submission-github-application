package com.android.submission2github.ui.viewpager

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.submission2github.adapter.FavoriteListener
import com.android.submission2github.adapter.UserListAdapter
import com.android.submission2github.databinding.FragmentFollowerBinding
import com.android.submission2github.model.Item
import com.android.submission2github.utils.Utils.loadFavoriteAsync
import com.android.submission2github.viewmodel.FollowerViewModel

class FollowerFragment(var username: String) : Fragment() {

    private var userList: ArrayList<Item>? = null
    private var userListAdapter: UserListAdapter? = null
    private var binding: FragmentFollowerBinding? = null
    private val b get() = binding

    private lateinit var viewModel: FollowerViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initiate()
        getData()
    }

    private fun initiate(){
        userListAdapter = UserListAdapter(arrayListOf(), requireContext())
        userListAdapter?.favoriteListener = favoriteUser
    }

    private fun getData(){
        loadData(username)
    }

    private fun loadData(username: String){
        viewModel = activity?.let { ViewModelProviders.of(it).get(FollowerViewModel::class.java) }!!
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return b?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private val favoriteUser = object: FavoriteListener {
        override fun addFavoriteUser(view: View, item: Item, listItem: ArrayList<Item>) {
            //b?.root?.let { loadFavoriteAsync(item, requireContext(), it, true) }
        }
    }
}