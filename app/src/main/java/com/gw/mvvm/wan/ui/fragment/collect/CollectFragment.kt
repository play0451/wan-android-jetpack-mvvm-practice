package com.gw.mvvm.wan.ui.fragment.collect

import android.os.Bundle
import com.gw.mvvm.framework.base.viewmodel.EmptyViewModel
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.databinding.FragmentViewpagerBinding
import com.gw.mvvm.wan.ext.bindViewPager
import com.gw.mvvm.wan.ext.initClose
import com.gw.mvvm.wan.ui.adapter.common.FragmentViewPagerAdapter
import com.gw.mvvm.wan.ui.adapter.common.TextNavigatorAdapter
import splitties.resources.str

/**
 * 收藏Fragment
 * @property viewPagerAdapter FragmentViewPagerAdapter
 */
class CollectFragment : BaseFragment<EmptyViewModel, FragmentViewpagerBinding>() {

    private lateinit var viewPagerAdapter: FragmentViewPagerAdapter

    override fun layoutId(): Int = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let { it ->
            it.includeViewpager.includeToolbar.toolbar.initClose {
                nav().navigateUp()
            }
            //初始化ViewPager
            viewPagerAdapter = createViewPagerAdaper()
            it.includeViewpager.viewPager.adapter = viewPagerAdapter
            //Indicator绑定ViewPager
            it.includeViewpager.indicator.bindViewPager(
                it.includeViewpager.viewPager,
                TextNavigatorAdapter(texts = mutableListOf(
                    str(R.string.collect_title_ariticle),
                    str(R.string.collect_title_url)
                ),
                    onSelected = { index ->
                        //ViewPager不平滑滚动
                        it.includeViewpager.viewPager.setCurrentItem(index, false)
                        //在扩展方法里已经调用过MagidIndicator的onPageSelected方法了,但是还需要调用navigator的notifyDataSetChanged
                        //否则在ViewPager不平滑滚动的情况下,indicator不会刷新
                        it.includeViewpager.indicator.navigator.notifyDataSetChanged()
                        //it.includeViewpager.viewPager.currentItem = index
                    })
            )
        }
    }

    override fun initObserver() {
    }

    override fun initData() {
    }

    private fun createViewPagerAdaper(): FragmentViewPagerAdapter {
        return FragmentViewPagerAdapter(
            this@CollectFragment,
            mutableListOf(
                CollectAriticleFragment(),
                CollectUrlFragment()
            )
        )
    }
}