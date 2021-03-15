package com.gw.mvvm.wan.ui.fragment.share

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.databinding.FragmentMyShareAriticleBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.share.MyShareAriticleAdapter
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import com.gw.mvvm.wan.viewModel.share.ShareViewModel
import com.kingja.loadsir.core.LoadService
import splitties.resources.str
import splitties.toast.toast

/**
 * 我的分享Fragment
 * @property adapter MyShareAriticleAdapter
 * @property loadsir LoadService<Any>
 */
class MyShareAriticleFragment : BaseFragment<ShareViewModel, FragmentMyShareAriticleBinding>() {

    private val adapter: MyShareAriticleAdapter by lazy { MyShareAriticleAdapter(mutableListOf()) }

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>

    override fun layoutId(): Int = R.layout.fragment_my_share_ariticle

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let {
            it.includeToolbar.toolbar.initWithNavIcon(
                title = str(R.string.my_share_title),
                navCallback = {
                    nav().navigateUp()
                },
                menuId = R.menu.menu_my_share,
                menuClickListener = { item ->
                    if (item.itemId == R.id.item_share) {
                        nav().navigateAction(R.id.action_myShareAriticleFragment_to_shareAriticleFragment)
                    }
                    true
                })
            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(it.includeRefreshList.layoutRefresh) {
                initData()
            }
            it.includeRefreshList.layoutRefresh.init({
                mViewModel.getAriticleList(true)
            }, {
                mViewModel.getAriticleList(false)
            })
            it.includeRefreshList.includeList.viewRecycler.init(
                LinearLayoutManager(requireContext()),
                adapter,
                null,
                false,
                it.includeRefreshList.includeList.btnFloating
            )
        }
        adapter.init(this, { _, _, position ->
            nav().navigateAction(
                R.id.action_global_webviewFragment,
                WebviewFragment.makeData(
                    WebviewFragment.TYPE_ARITICLE,
                    adapter.getItem(position),
                    false
                )
            )
        },
            intArrayOf(R.id.btn_delete),
            { _, _, position ->
                showDialog(message = str(R.string.my_share_delete_message), positiveAction = {
                    val info: AriticleInfo = adapter.getItem(position)
                    mViewModel.deleteAriticle(info.id, position)
                }, negativeButtonText = str(R.string.text_cancel))
            })
    }

    override fun initObserver() {
        mViewModel.ariticleList.observe(viewLifecycleOwner, {
            loadsir.dealResult(it)
            adapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)
        })
        mViewModel.deleteAriticleResult.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                adapter.removeAt(it.data!!)
                if (adapter.itemCount <= 0) {
                    loadsir.showEmpty()
                }
            } else {
                toast(it.errorMsg)
            }
        })
        //分享文章通知
        mGlobalEventViewModel.shareAriticleEvent.observeInFragment(this) {
            if (adapter.itemCount <= 0) {
                initData()
            } else {
                mDataBinding.includeRefreshList.layoutRefresh.autoRefresh()
            }
        }
    }

    override fun initData() {
        loadsir.showLoading()
        mViewModel.getAriticleList(true)
    }
}