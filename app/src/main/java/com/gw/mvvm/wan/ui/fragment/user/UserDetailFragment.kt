package com.gw.mvvm.wan.ui.fragment.user

import android.os.Bundle
import androidx.core.math.MathUtils
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.databinding.FragmentUserDetailBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.common.AriticleAdapter
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.gw.mvvm.wan.viewModel.user.UserDetailViewModel
import com.kingja.loadsir.core.LoadService
import splitties.resources.str
import splitties.toast.toast
import kotlin.math.abs

class UserDetailFragment : BaseFragment<UserDetailViewModel, FragmentUserDetailBinding>() {

    companion object {
        const val KEY_USER_ID: String = "userId"

        fun makeData(userId: Int): Bundle {
            return bundleOf(KEY_USER_ID to userId)
        }
    }

    private var userId: Int = 0
    private val ariticleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf()) }
    private val collectViewModel: CollectViewModel by viewModels()

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>

    override fun layoutId(): Int = R.layout.fragment_user_detail

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            userId = it.getInt(KEY_USER_ID, 0)
        }
        mDataBinding.let {
            //设置AppBarLayout的滑动位移监听器
            it.ablLayout.apply {
                addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
                    //计算总位移距离的三分之一
                    val a = totalScrollRange.toFloat() / 3
                    //已移动距离减去总移动距离的三分之二
                    val b = abs(verticalOffset).toFloat() - a * 2
                    //设置小头像的alpha
                    it.ivSmallHead.alpha = MathUtils.clamp(b / a, 0F, 1F)
                })
            }
            //设置CollapsingToolbarLayout的属性
            it.ctlToolbar.apply {
                setExpandedTitleColor(resolveThemeColor(R.attr.colorOnPrimary)) //展开时的title颜色
                setCollapsedTitleTextColor(resolveThemeColor(R.attr.colorOnPrimary))    //折叠时的title颜色
            }
            it.toolbar.initClose {
                nav().navigateUp()
            }

            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(it.layoutRefresh) {
                initData()
            }

            //设置SmartRefreshLayout的刷新和加载更多的回调
            it.layoutRefresh.init({
                mViewModel.getUserInfo(userId, true)
            }, {
                mViewModel.getUserInfo(userId, false)
            })

            //初始化RecyclerView,设置item间隔
            it.viewRecycler.init(
                LinearLayoutManager(requireContext()),
                ariticleAdapter,
                null,
                true,
                it.btnFloating
            )

            it.viewModel = mViewModel
        }

        //初始化AriticleAdapter
        ariticleAdapter.init(
            this@UserDetailFragment,
            null,
            intArrayOf(R.id.btn_collect),
            null,
            collectViewModel
        )
    }

    override fun initObserver() {
        //用户信息
        mViewModel.userInfo.observe(viewLifecycleOwner, { info ->
            mDataBinding.let {
                it.tvCoin.text = String.format(str(R.string.user_detail_conin), info.coinCount)
                it.tvRank.text = String.format(str(R.string.user_detail_rank), info.rank)
                it.ctlToolbar.title = info.username
            }
        })
        //分享列表
        mViewModel.shareList.observe(viewLifecycleOwner, {
            if (!it.isSuccess) {
                toast(R.string.user_detail_no_user)
            }
            loadsir.dealResult(it)
            ariticleAdapter.updateData(it, mDataBinding.layoutRefresh)
        })
        //收藏结果
        collectViewModel.collectEvent.observe(viewLifecycleOwner) {
            //it.dealCollectEvent(ariticleAdapter)
            it.dealCollectEvent(mGlobalEventViewModel, onSuccess = {
                requireContext().sendAriticleCollectNotification(
                    ariticleAdapter.data,
                    it.id,
                    it.isCollect
                )
            })
        }
        //收藏事件
        mGlobalEventViewModel.collectAriticleEvent.observeInFragment(this) {
            ariticleAdapter.updateCollect(it.data!!.id, it.data!!.isCollected)
        }
    }

    override fun initData() {
        //mDataBinding.layoutRefresh.autoRefresh()
        loadsir.showLoading()
        mViewModel.getUserInfo(userId, true)
    }
}