package com.gw.mvvm.wan.ui.fragment.me

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.framework.ext.view.clickNoRepeat
import com.gw.mvvm.framework.ext.viewmodel.parseState
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.enum.MeMenuType
import com.gw.mvvm.wan.data.model.MeMenuInfo
import com.gw.mvvm.wan.data.model.NormalWebInfo
import com.gw.mvvm.wan.data.model.UserCoinInfo
import com.gw.mvvm.wan.databinding.FragmentMeBinding
import com.gw.mvvm.wan.ext.checkToLogin
import com.gw.mvvm.wan.ext.init
import com.gw.mvvm.wan.ui.adapter.me.MeMenuAdapter
import com.gw.mvvm.wan.ui.fragment.usercoin.UserCoinFragment
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import com.gw.mvvm.wan.viewModel.me.MeViewModel
import splitties.resources.str
import splitties.toast.toast

/**
 * 我的Fragment
 */
class MeFragment : BaseFragment<MeViewModel, FragmentMeBinding>() {

    private val adapter: MeMenuAdapter by lazy { MeMenuAdapter(mutableListOf()) }
    private var coinInfo: UserCoinInfo? = null

    override fun layoutId(): Int {
        return R.layout.fragment_me
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let {
            it.viewRecycler.init(LinearLayoutManager(requireContext()), adapter, null, false)
            it.layoutRefresh.init({ layout ->
                if (GlobalData.isLogin) {
                    mViewModel.getUserCoin()
                } else {
                    layout.finishRefresh()
                }
            })
            it.viewModel = mViewModel
            it.llUserBack.clickNoRepeat {
                checkToLogin()
            }
        }
        adapter.setOnItemClickListener { _, _, position ->
            onMenuClick(adapter.getItem(position))
        }
    }

    override fun initObserver() {
        //菜单数据
        mViewModel.menuList.observe(viewLifecycleOwner, {
            adapter.setList(it)
        })
        //用户积分
        mViewModel.userCoin.observe(viewLifecycleOwner, {
            parseState(it, { user ->
                coinInfo = user
                mDataBinding.layoutRefresh.finishRefresh()
                mDataBinding.idRank =
                    String.format(str(R.string.me_id_rank), user.userId, user.rank)
                for (i in 0 until adapter.itemCount) {
                    val info: MeMenuInfo = adapter.getItem(i)
                    if (info.type == MeMenuType.Coin) {
                        info.coin = user.coinCount
                        adapter.setData(i, info)
                    }
                }
            }, { e ->
                toast(e.errorMsg)
                mDataBinding.layoutRefresh.finishRefresh()
            })
        })
        //登录登出事件
        mGlobalEventViewModel.loginOutEvent.observeInFragment(this) {
            mViewModel.updateUserName()
            if (it.data!!) {
                mDataBinding.layoutRefresh.autoRefresh()
            } else {
                mViewModel.getMenuList()
                mDataBinding.idRank = ""
            }
        }
    }

    override fun initData() {
        mViewModel.getMenuList()
        if (GlobalData.isLogin) {
            mDataBinding.layoutRefresh.autoRefresh()
        }
    }

    private fun onMenuClick(item: MeMenuInfo) {
        if (!item.clickable) {
            return
        }
        when (item.action) {
            //我的积分
            MeMenuInfo.ACTION_COIN -> {
                checkToLogin {
                    coinInfo?.let {
                        nav().navigateAction(
                            R.id.action_mainFragment_to_userCoinFragment,
                            UserCoinFragment.makeData(it)
                        )
                    }
                }
            }
            //我的收藏
            MeMenuInfo.ACTION_COLLETION -> {
                checkToLogin {
                    nav().navigateAction(R.id.action_mainFragment_to_collectFragment)
                }
            }
            //我的文章
            MeMenuInfo.ACTION_ARITICLE -> {
                checkToLogin {
                    nav().navigateAction(R.id.action_mainFragment_to_myShareAriticleFragment)
                }
            }
            //TO DO
            MeMenuInfo.ACTION_TODO -> {
                checkToLogin {
                    nav().navigateAction(R.id.action_mainFragment_to_todoListFragment)
                }
            }
            //开放API网站
            MeMenuInfo.ACTION_SITE -> {
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
            //设置
            MeMenuInfo.ACTION_SETTINGS -> {
                nav().navigateAction(R.id.action_mainFragment_to_settingsFragment)
            }
        }
    }
}