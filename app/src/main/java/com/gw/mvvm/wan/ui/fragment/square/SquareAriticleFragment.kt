package com.gw.mvvm.wan.ui.fragment.square

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.framework.ext.navigation.navigateUp
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.enum.SquareAriticleType
import com.gw.mvvm.wan.databinding.FragmentSquareAriticleBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.common.AriticleAdapter
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.gw.mvvm.wan.viewModel.square.SquareViewModel
import com.kingja.loadsir.core.LoadService
import splitties.resources.str

/**
 * 广场广场Fragment
 */
class SquareAriticleFragment : BaseFragment<SquareViewModel, FragmentSquareAriticleBinding>() {

    private lateinit var ariticleAdapter: AriticleAdapter
    private val collectViewModel: CollectViewModel by viewModels()

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>
    private var ariticleType: SquareAriticleType = SquareAriticleType.Plaza

    companion object {
        const val KEY_ARITICLE_TYPE: String = "ariticleType"

        fun newInstance(ariticleType: SquareAriticleType): Fragment {
            val fragment: SquareAriticleFragment = SquareAriticleFragment()
            fragment.arguments = bundleOf(KEY_ARITICLE_TYPE to ariticleType)
            return fragment
        }

        fun makeData(ariticleType: SquareAriticleType): Bundle {
            return bundleOf(KEY_ARITICLE_TYPE to ariticleType)
        }
    }

    override fun layoutId(): Int = R.layout.fragment_square_ariticle

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let { bundle ->
            val s = bundle.getSerializable(KEY_ARITICLE_TYPE)
            s?.let {
                ariticleType = it as SquareAriticleType
            }
        }

        ariticleAdapter = AriticleAdapter(mutableListOf())

        mDataBinding.let {
            //如果是广场,创建分享文章菜单
            if (ariticleType == SquareAriticleType.Plaza) {
                it.includeToolbar.toolbar.initWithNavIcon(
                    title = str(R.string.square_square),
                    navCallback = {
                        navigateUp()
                    },
                    menuId = R.menu.menu_square,
                    menuClickListener = { item ->
                        if (item.itemId == R.id.item_add) {
                            checkToLogin {
                                nav().navigateAction(R.id.action_squareAriticleFragment_to_shareAriticleFragment)
                            }
                        }
                        true
                    })
            } else {
                it.includeToolbar.toolbar.initClose(str(R.string.square_ask_every_day)) {
                    navigateUp()
                }
            }
            it.includeRefreshList.includeList.viewRecycler.init(
                LinearLayoutManager(requireContext()),
                ariticleAdapter,
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
                mViewModel.getAriticleList(ariticleType, true)
            }, {
                mViewModel.getAriticleList(ariticleType, false)
            })
        }

        //初始化AriticleAdapter
        ariticleAdapter.init(
            this@SquareAriticleFragment,
            null,
            intArrayOf(R.id.btn_collect, R.id.tv_author),
            null,
            collectViewModel
        )
    }

    override fun initObserver() {
        //文章列表
        mViewModel.ariticleList.observe(viewLifecycleOwner, {
            loadsir.dealResult(it)
            ariticleAdapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)
        })
        //用户数据,用户登录登出后更新收藏状态
        mGlobalEventViewModel.loginOutEvent.observeInFragment(this) {
            ariticleAdapter.updateCollect(GlobalData.userInfo?.collectIds)
        }
        //收藏结果
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
        loadsir.showLoading()
        mViewModel.getAriticleList(ariticleType, true)
    }
}