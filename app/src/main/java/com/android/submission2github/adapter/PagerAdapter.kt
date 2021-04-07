package com.android.submission2github.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.submission2github.ui.viewpager.FollowerFragment
import com.android.submission2github.ui.viewpager.FollowingFragment

class PagerAdapter(fm: FragmentManager, var username: String): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val pages = listOf(
        FollowerFragment(username),
        FollowingFragment(username)
    )

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Follower"
            else -> "Following"
        }
    }
}