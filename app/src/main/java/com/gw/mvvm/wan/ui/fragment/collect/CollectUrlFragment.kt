package com.gw.mvvm.wan.ui.fragment.collect

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.model.CollectAriticleInfo
import com.gw.mvvm.wan.data.model.CollectUrlInfo
import com.gw.mvvm.wan.databinding.FragmentCollectUrlBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.collect.CollectUrlAdapter
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.kingja.loadsir.core.LoadService
import splitties.toast.toast

/**
 * 收藏网址Fragment
 */
class CollectUrlFragment : BaseFragment<CollectViewModel, FragmentCollectUrlBinding>() {

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>
    private val adapter: CollectUrlAdapter by lazy { CollectUrlAdapter(mutableListOf()) }

    override fun layoutId(): Int = R.layout.fragment_collect_url

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let {
            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(
                it.includeRefreshList.layoutRefresh,
                this::initData
            )
            it.includeRefreshList.layoutRefresh.run {
                setEnableAutoLoadMore(false)
                setEnableLoadMore(false)
                init({
                    mViewModel.getCollectUrlList()
                })
            }
        }
        initAdapter()
    }

    private fun initAdapter() {
        adapter.run {
            setOnItemClickListener { _, _, position ->
                val info: CollectUrlInfo = getItem(position)
                nav().navigateAction(
                    R.id.action_global_webviewFragment,
                    WebviewFragment.makeData(WebviewFragment.TYPE_COLLECT_URL, info)
                )
            }
            this.addChildClickViewIds(R.id.btn_collect)
            this.setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    //收藏
                    R.id.btn_collect -> {
                        checkToLogin({
                            val info: CollectUrlInfo = getItem(position)
                            if (info.collected) {
                                mViewModel.uncollectUrl(info.id)
                            } else {
                                mViewModel.collectUrl(info.name, info.link)
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
        mViewModel.collectUrlList.observe(viewLifecycleOwner, {
            loadsir.dealResult(it)
            adapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)
        })
        mViewModel.collectUrlEvent.observe(viewLifecycleOwner, {
            it.run {
                if (!this.isSuccess) {
                    if (this.errorMsg.isNotBlank()) {
                        toast(this.errorMsg)
                    }
                } else {
                    val info: CollectUrlInfo = adapter.data.firstOrNull { i ->
                        i.id == this.id
                    } ?: return@observe

                    info.collected = this.isCollect
                    adapter.setData(adapter.data.indexOf(info), info)
                    toast(if (this.isCollect) R.string.hint_collect_success else R.string.hint_uncollect_success)
                }
            }
        })
    }

    override fun initData() {
        loadsir.showLoading()
        mViewModel.getCollectUrlList()
    }
}