package com.gw.mvvm.wan.ui.fragment.usercoin

import android.os.Bundle
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.model.NormalWebInfo
import com.gw.mvvm.wan.data.model.UserCoinInfo
import com.gw.mvvm.wan.data.model.UserInfo
import com.gw.mvvm.wan.databinding.FragmentUserCoinBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.usercoin.UserCoinAdapter
import com.gw.mvvm.wan.ui.fragment.user.UserDetailFragment
import com.gw.mvvm.wan.ui.fragment.web.WebviewFragment
import com.gw.mvvm.wan.viewModel.usercoin.UserCoinViewModel
import com.kingja.loadsir.core.LoadService
import splitties.resources.str

/**
 * 用户积分Fragment
 */
class UserCoinFragment : BaseFragment<UserCoinViewModel, FragmentUserCoinBinding>() {

    val adapter: UserCoinAdapter by lazy { UserCoinAdapter(mutableListOf()) }

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>

    companion object {
        const val KEY_COIN_INFO: String = "coinInfo"

        fun makeData(coinInfo: UserCoinInfo): Bundle {
            return bundleOf(KEY_COIN_INFO to coinInfo)
        }
    }

    override fun layoutId(): Int = R.layout.fragment_user_coin

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            val coinInfo: UserCoinInfo? = it.getParcelable(KEY_COIN_INFO)
            coinInfo?.let { info ->
                mDataBinding.coinInfo = info
            }
        }
        mDataBinding.let {
            it.includeToolbar.toolbar.initWithNavIcon(title = str(R.string.user_coin_title),
                navCallback = {
                    nav().navigateUp()
                },
                menuId = R.menu.menu_user_coin,
                menuClickListener = { item ->
                    onMenuClick(item)
                    true
                })

            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(it.includeRefreshList.layoutRefresh) {
                initData()
            }

            it.includeRefreshList.layoutRefresh.init({
                mViewModel.getUserCoinList(true)
            }, {
                mViewModel.getUserCoinList(false)
            })
            it.includeRefreshList.includeList.viewRecycler.init(
                LinearLayoutManager(requireContext()), adapter,
                null,
                true,
                it.includeRefreshList.includeList.btnFloating
            )
        }
        adapter.setOnItemClickListener { _, _, position ->
            val info: UserCoinInfo = adapter.getItem(position)
            val userInfo: UserInfo? = GlobalData.userInfo
            if (userInfo == null || userInfo.id.toInt() != info.userId) {
                nav().navigateAction(
                    R.id.action_global_userDetailFragment,
                    UserDetailFragment.makeData(info.userId)
                )
            }
        }
    }

    override fun initObserver() {
        mViewModel.userCoinList.observe(viewLifecycleOwner, {
            loadsir.dealResult(it)
            adapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)
        })
    }

    override fun initData() {
        loadsir.showLoading()
        mViewModel.getUserCoinList(true)
    }

    private fun onMenuClick(item: MenuItem) {
        when (item.itemId) {
            R.id.item_rule -> {
                nav().navigateAction(
                    R.id.action_global_webviewFragment,
                    WebviewFragment.makeData(
                        WebviewFragment.TYPE_NORMAL_WEB,
                        NormalWebInfo(
                            str(R.string.user_coin_rule_url),
                            str(R.string.user_coin_rule)
                        ),
                        false
                    )
                )
            }
            R.id.item_history -> {
                checkToLogin {
                    nav().navigateAction(R.id.action_userCoinFragment_to_userCoinHistoryFragment)
                }
            }
        }
    }
}