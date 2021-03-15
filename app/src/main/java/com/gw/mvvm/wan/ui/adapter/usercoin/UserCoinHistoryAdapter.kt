package com.gw.mvvm.wan.ui.adapter.usercoin

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.UserCoinHistoryInfo
import com.gw.mvvm.wan.databinding.ItemUserCoinHistoryBinding

/**
 * 积分历史Adapter
 * @author play0451
 */
class UserCoinHistoryAdapter(datas: MutableList<UserCoinHistoryInfo>) :
    BaseQuickAdapter<UserCoinHistoryInfo, BaseDataBindingHolder<ItemUserCoinHistoryBinding>>(
        R.layout.item_user_coin_history, datas
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemUserCoinHistoryBinding>,
        item: UserCoinHistoryInfo
    ) {
        holder.dataBinding?.info = item
        holder.dataBinding?.executePendingBindings()
    }
}