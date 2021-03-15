package com.gw.mvvm.wan.ui.fragment.search

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.databinding.FragmentSearchResultBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.common.AriticleAdapter
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.gw.mvvm.wan.viewModel.search.SearchViewModel
import com.kingja.loadsir.core.LoadService

/**
 * 搜索结果页面
 * @property searchKey String?
 */
class SearchResultFragment : BaseFragment<SearchViewModel, FragmentSearchResultBinding>() {

    companion object {
        /**
         * 搜索key
         */
        const val KEY_SEARCH_KEY: String = "searchKey"

        fun makeData(key: String): Bundle {
            return Bundle().apply {
                putString(KEY_SEARCH_KEY, key)
            }
        }
    }

    private lateinit var ariticleAdapter: AriticleAdapter
    private val collectViewModel: CollectViewModel by viewModels()

    private var searchKey: String? = null

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>

    override fun layoutId(): Int = R.layout.fragment_search_result

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            searchKey = it.getString(KEY_SEARCH_KEY)
        }
        if (searchKey.isNullOrBlank()) {
            throw IllegalArgumentException("缺少查询关键字")
        }
        ariticleAdapter=AriticleAdapter(mutableListOf())
        mDataBinding.let {
            //初始化工具栏
            it.includeToolbar.toolbar.initClose(searchKey!!) {
                nav().navigateUp()
            }
            //初始化RecylerView
            it.includeRefreshList.includeList.viewRecycler.init(
                LinearLayoutManager(requireContext()),
                ariticleAdapter,
                null,
                true,
                it.includeRefreshList.includeList.btnFloating
            )
            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(it.includeRefreshList.layoutRefresh) {
                initData()
            }

            //初始化刷新布局
            it.includeRefreshList.layoutRefresh.init({
                mViewModel.getSearchResult(searchKey!!, true)
            }, {
                mViewModel.getSearchResult(searchKey!!, false)
            })
        }
        //初始化AriticleAdapter
        ariticleAdapter.init(
            this@SearchResultFragment,
            null,
            intArrayOf(R.id.btn_collect, R.id.tv_author),
            null,
            collectViewModel
        )
    }

    override fun initObserver() {
        //搜索结果列表
        mViewModel.searchResult.observe(viewLifecycleOwner, {
            loadsir.dealResult(it)
            ariticleAdapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)

        })
        //用户数据,用户登录登出后更新收藏状态
        mGlobalEventViewModel.loginOutEvent.observeInFragment(this){
            ariticleAdapter.updateCollect(GlobalData.userInfo?.collectIds)
        }
        //收藏事件
        collectViewModel.collectEvent.observe(viewLifecycleOwner, {
            //it.dealCollectEvent(ariticleAdapter)
            it.dealCollectEvent(mGlobalEventViewModel, onSuccess = {
                requireContext().sendAriticleCollectNotification(
                    ariticleAdapter.data,
                    it.id,
                    it.isCollect
                )
            })
        })
        //收藏事件
        mGlobalEventViewModel.collectAriticleEvent.observeInFragment(this) {
            ariticleAdapter.updateCollect(it.data!!.id, it.data!!.isCollected)
        }
    }

    override fun initData() {
        //mDataBinding.layoutRefresh.autoRefresh()
        loadsir.showLoading()
        mViewModel.getSearchResult(searchKey!!, true)
    }
}