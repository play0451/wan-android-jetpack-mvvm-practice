package com.gw.mvvm.wan.ui.fragment.usercoin

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.databinding.FragmentUserCoinHistoryBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.usercoin.UserCoinHistoryAdapter
import com.gw.mvvm.wan.viewModel.usercoin.UserCoinViewModel
import com.kingja.loadsir.core.LoadService
import splitties.resources.str

/**
 * 积分历史Fragment
 * @property adapter UserCoinHistoryAdapter
 * @property loadsir LoadService<Any>
 */
class UserCoinHistoryFragment : BaseFragment<UserCoinViewModel, FragmentUserCoinHistoryBinding>() {

    private val adapter: UserCoinHistoryAdapter by lazy { UserCoinHistoryAdapter(mutableListOf()) }

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>

    override fun layoutId(): Int = R.layout.fragment_user_coin_history

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let {
            it.includeToolbar.toolbar.initClose(str(R.string.user_coin_history_title)) {
                nav().navigateUp()
            }

            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(it.includeRefreshList.layoutRefresh) {
                initData()
            }

            it.includeRefreshList.layoutRefresh.init({
                mViewModel.getUserCoinHistoryList(true)
            }, {
                mViewModel.getUserCoinHistoryList(false)
            })
            it.includeRefreshList.includeList.viewRecycler.init(
                LinearLayoutManager(requireContext()), adapter,
                null,
                true,
                it.includeRefreshList.includeList.btnFloating
            )
        }
    }

    override fun initObserver() {
        mViewModel.userCoinHistoryList.observe(viewLifecycleOwner, {
            loadsir.dealResult(it)
            adapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)
        })
    }

    override fun initData() {
        loadsir.showLoading()
        mViewModel.getUserCoinHistoryList(true)
    }
}