package com.gw.mvvm.wan.ui.fragment.collect

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.model.CollectAriticleInfo
import com.gw.mvvm.wan.databinding.FragmentCollectAriticleBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.collect.CollectAriticleAdapter
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.kingja.loadsir.core.LoadService
import splitties.toast.toast

/**
 * 收藏文章Fragment
 */
class CollectAriticleFragment : BaseFragment<CollectViewModel, FragmentCollectAriticleBinding>() {

    private val adapter: CollectAriticleAdapter by lazy { CollectAriticleAdapter(mutableListOf()) }

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>

    override fun layoutId(): Int = R.layout.fragment_collect_ariticle

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let {
            it.includeRefreshList.includeList.viewRecycler.init(
                LinearLayoutManager(requireContext()),
                adapter,
                null,
                true,
                it.includeRefreshList.includeList.btnFloating
            )
            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(
                it.includeRefreshList.layoutRefresh,
                this::initData
            )

            //初始化刷新布局
            it.includeRefreshList.layoutRefresh.init({
                mViewModel.getCollectAriticleList(true)
            }, {
                mViewModel.getCollectAriticleList(false)
            })
        }

        initAdapter()
    }

    private fun initAdapter() {
        adapter.run {
            this.setOnItemClickListener { _, _, position ->
                val info: CollectAriticleInfo = this.getItem(position)
                nav().navigateAction(
                    R.id.action_global_webviewFragment,
                    WebviewFragment.makeData(WebviewFragment.TYPE_COLLECT, info)
                )
            }
            this.addChildClickViewIds(R.id.btn_collect)
            this.setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    //收藏
                    R.id.btn_collect -> {
                        checkToLogin({
                            val info: CollectAriticleInfo = this.getItem(position)
                            if (info.collected) {
                                mViewModel.uncollect(info.originId)
                            } else {
                                mViewModel.collect(info.originId)
                            }
                        }, {
                            toast(R.string.hint_login_please)
                        })
                    }
                }
            }
        }
    }

    override fun initObserver() {
        mViewModel.collectAriticleList.observe(viewLifecycleOwner, {
            loadsir.dealResult(it)
            adapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)
        })
        //收藏结果
        mViewModel.collectEvent.observe(viewLifecycleOwner, {
            it.dealCollectEvent(mGlobalEventViewModel)
        })
        //收藏事件
        mGlobalEventViewModel.collectAriticleEvent.observeInFragment(this) {
            val info: CollectAriticleInfo = adapter.data.firstOrNull { i ->
                i.originId == it.data!!.id
            } ?: return@observeInFragment

            info.collected = it.data!!.isCollected
            adapter.setData(adapter.data.indexOf(info), info)
        }
    }

    override fun initData() {
        loadsir.showLoading()
        mViewModel.getCollectAriticleList(true)
    }
}