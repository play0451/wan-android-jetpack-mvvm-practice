package com.gw.mvvm.wan.ui.fragment.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.framework.ext.viewmodel.parseState
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.enum.SquareAriticleType
import com.gw.mvvm.wan.data.model.BannerInfo
import com.gw.mvvm.wan.data.model.NormalWebInfo
import com.gw.mvvm.wan.data.model.UserCoinInfo
import com.gw.mvvm.wan.databinding.FragmentMainBinding
import com.gw.mvvm.wan.databinding.LayoutBannerBinding
import com.gw.mvvm.wan.databinding.LayoutMainDrawerHeaderBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.common.AriticleAdapter
import com.gw.mvvm.wan.ui.adapter.home.HomeBannerAdapter
import com.gw.mvvm.wan.ui.fragment.square.SquareAriticleFragment
import com.gw.mvvm.wan.ui.fragment.usercoin.UserCoinFragment
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import com.gw.mvvm.wan.viewModel.collect.CollectViewModel
import com.gw.mvvm.wan.viewModel.home.HomeViewModel
import com.gw.mvvm.wan.viewModel.me.MeViewModel
import com.kingja.loadsir.core.LoadService
import com.youth.banner.indicator.CircleIndicator
import splitties.resources.str

/**
 * 根Fragment
 */
class MainFragment : BaseFragment<HomeViewModel, FragmentMainBinding>(),
    NavigationView.OnNavigationItemSelectedListener {

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>
    private lateinit var bannerAdapter: HomeBannerAdapter
    private lateinit var ariticleAdapter: AriticleAdapter
    private val collectViewModel: CollectViewModel by viewModels()
    private val meViewModel: MeViewModel by viewModels()

    private lateinit var userCoin: UserCoinInfo

    override fun layoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        bannerAdapter = HomeBannerAdapter(emptyList())
        ariticleAdapter = AriticleAdapter(mutableListOf())

        mDataBinding.let {
            it.includeToolbar.toolbar.initWithNavIcon(
                title = str(R.string.menu_main_home),
                icon = R.drawable.icon_menu_main_navigation,
                { _ ->
                    if (it.dlMain.isDrawerOpen(GravityCompat.START)) {
                        it.dlMain.close()
                    } else {
                        it.dlMain.open()
                    }
                },
                R.menu.menu_home,
                { item ->
                    if (item.itemId == R.id.item_search) {
                        nav().navigateAction(R.id.action_mainFragment_to_searchFragment)
                    }
                    true
                })
            //初始化抽屉
            it.nvMain.run {
                //抽屉菜单图标颜色
                itemIconTintList = ColorStateList.valueOf(resolveThemeColor(R.attr.colorOnSurface))
                setNavigationItemSelectedListener(this@MainFragment)
                getHeaderView(0).setOnClickListener { checkToLogin() }
            }

            //更新抽屉里显示的用户名
            updateUserName()

            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(it.includeRefreshList.layoutRefresh) {
                initData()
            }

            //设置SmartRefreshLayout的刷新和加载更多的回调
            it.includeRefreshList.layoutRefresh.init({
                mViewModel.getBannerList()
                mViewModel.getAriticleList(true)
            }, {
                mViewModel.getAriticleList(false)
            })

            //初始化RecyclerView,设置item间隔
            it.includeRefreshList.includeList.viewRecycler.init(
                LinearLayoutManager(requireContext()),
                ariticleAdapter,
                null,
                true,
                it.includeRefreshList.includeList.btnFloating
            )
        }
        //初始化banner,作为ariticleAdapter的HeaderView
        val bannerBinding: LayoutBannerBinding = DataBindingUtil.inflate<LayoutBannerBinding>(
            LayoutInflater.from(requireContext()),
            R.layout.layout_banner,
            mDataBinding.includeRefreshList.includeList.viewRecycler,
            false
        )
        //设置Banner的各种属性
        bannerBinding.banner.apply {
            val colorId: Int = resolveThemeAttribute(R.attr.colorOnPrimary)
            indicator = CircleIndicator(requireContext())
            isAutoLoop(true)
            setIndicatorNormalColorRes(colorId)
            setIndicatorSelectedColorRes(colorId)
            addBannerLifecycleObserver(viewLifecycleOwner)
            setAdapter(bannerAdapter, true)
            bannerAdapter.setOnBannerListener { _, position ->
                val data: BannerInfo = bannerAdapter.getData(position)
                nav().navigateAction(
                    R.id.action_global_webviewFragment,
                    WebviewFragment.makeData(WebviewFragment.TYPE_BANNER, data)
                )
            }
        }
        //初始化AriticleAdapter
        ariticleAdapter.init(
            this@MainFragment,
            null,
            intArrayOf(R.id.btn_collect, R.id.tv_author),
            null,
            collectViewModel
        )
        ariticleAdapter.addHeaderView(bannerBinding.root)
    }

    private fun updateUserName() {
        val binding: LayoutMainDrawerHeaderBinding? =
            DataBindingUtil.bind(mDataBinding.nvMain.getHeaderView(0))
        binding?.userName =
            if (GlobalData.isLogin) GlobalData.userInfo!!.username else str(R.string.me_login_please)
    }

    override fun initObserver() {
        //banner数据
        mViewModel.bannerList.observe(viewLifecycleOwner) {
            parseState(it, { list ->
                bannerAdapter.setDatas(list)
                bannerAdapter.notifyDataSetChanged()
            })
            mDataBinding.includeRefreshList.layoutRefresh.finishRefresh()
        }
        //文章列表数据
        mViewModel.ariticleList.observe(viewLifecycleOwner) {
            loadsir.dealResult(it)
            ariticleAdapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)
        }
        //用户数据,用户登录登出后更新收藏状态
        mGlobalEventViewModel.loginOutEvent.observeInFragment(this) {
            ariticleAdapter.updateCollect(GlobalData.userInfo?.collectIds)
            if (it.data!!) {
                meViewModel.getUserCoin()
            }
            updateUserName()
        }
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
        meViewModel.userCoin.observe(viewLifecycleOwner, {
            parseState(it, { info ->
                userCoin = info
            })
        })
    }

    override fun initData() {
        loadsir.showLoading()
        mViewModel.getBannerList()
        mViewModel.getAriticleList(true)
        meViewModel.getUserCoin()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_project -> {
                nav().navigateAction(R.id.action_mainFragment_to_projectFragment)
            }
            R.id.item_square -> {
                nav().navigateAction(
                    R.id.action_mainFragment_to_squareAriticleFragment,
                    SquareAriticleFragment.makeData(SquareAriticleType.Plaza)
                )
            }
            R.id.item_ask -> {
                nav().navigateAction(
                    R.id.action_mainFragment_to_squareAriticleFragment,
                    SquareAriticleFragment.makeData(SquareAriticleType.Ask)
                )
            }
            R.id.item_system -> {
                nav().navigateAction(R.id.action_mainFragment_to_squareSystemFragment)
            }
            R.id.item_navigation -> {
                nav().navigateAction(R.id.action_mainFragment_to_squareNavigationFragment)
            }
            R.id.item_official -> {
                nav().navigateAction(R.id.action_mainFragment_to_officialFragment)
            }
            R.id.item_me_coin -> {
                checkToLogin {
                    if (this::userCoin.isInitialized) {
                        userCoin.let {
                            nav().navigateAction(
                                R.id.action_mainFragment_to_userCoinFragment,
                                UserCoinFragment.makeData(it)
                            )
                        }
                    }
                }
            }
            R.id.item_me_collection -> {
                checkToLogin { nav().navigateAction(R.id.action_mainFragment_to_collectFragment) }
            }
            R.id.item_me_ariticle -> {
                checkToLogin { nav().navigateAction(R.id.action_mainFragment_to_myShareAriticleFragment) }
            }
            R.id.item_me_todo -> {
                checkToLogin { nav().navigateAction(R.id.action_mainFragment_to_todoListFragment) }
            }
            R.id.item_site -> {
                nav().navigateAction(
                    R.id.action_global_webviewFragment,
                    WebviewFragment.makeData(
                        WebviewFragment.TYPE_NORMAL_WEB,
                        NormalWebInfo(
                            str(R.string.open_api_site_url),
                            str(R.string.open_api_site_title)
                        ),
                        false
                    )
                )
            }
            R.id.item_settings -> {
                nav().navigateAction(R.id.action_mainFragment_to_settingsFragment)
            }
        }
        mDataBinding.dlMain.close()
        return true
    }
}