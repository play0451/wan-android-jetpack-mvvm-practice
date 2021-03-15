package com.gw.mvvm.wan.ui.adapter.usercoin

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.UserCoinInfo
import com.gw.mvvm.wan.databinding.ItemUserCoinBinding

/**
 * 用户积分Adapter
 * @author play0451
 */
class UserCoinAdapter(datas: MutableList<UserCoinInfo>) :
    BaseQuickAdapter<UserCoinInfo, BaseDataBindingHolder<ItemUserCoinBinding>>(
        R.layout.item_user_coin,
        datas
    ) {
    override fun convert(holder: BaseDataBindingHolder<ItemUserCoinBinding>, item: UserCoinInfo) {
        holder.dataBinding?.info = item
        holder.dataBinding?.executePendingBindings()
    }
}