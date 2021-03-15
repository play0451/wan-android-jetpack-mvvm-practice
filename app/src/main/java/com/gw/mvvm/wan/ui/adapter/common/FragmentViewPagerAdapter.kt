package com.gw.mvvm.wan.ui.adapter.common

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gw.mvvm.wan.data.enum.SquareAriticleType
import com.gw.mvvm.wan.ui.fragment.square.SquareAriticleFragment
import com.gw.mvvm.wan.ui.fragment.square.SquareNavigationFragment
import com.gw.mvvm.wan.ui.fragment.square.SquareSystemFragment

/**
 * 通用的Fragment的ViewPagerAdapter
 * @author play0451
 */
class FragmentViewPagerAdapter(parent: Fragment, fragmentsList: MutableList<Fragment>) :
    FragmentStateAdapter(parent) {

    private val fragments: MutableList<Fragment> = fragmentsList.toMutableList()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addFragment(vararg fragment: Fragment) {
        fragments.addAll(fragment)
    }

    fun getFragment(pos: Int): Fragment? {
        if (pos < 0 || pos >= fragments.size) {
            return null
        }
        return fragments[pos]
    }

    fun removeFragment(pos: Int): Boolean {
        if (pos < 0 || pos >= fragments.size) {
            return false
        }
        fragments.removeAt(pos)
        return true
    }
}