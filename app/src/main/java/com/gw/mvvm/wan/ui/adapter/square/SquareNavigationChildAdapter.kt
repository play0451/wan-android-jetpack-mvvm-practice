package com.gw.mvvm.wan.ui.adapter.square

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.AriticleInfo
import com.gw.mvvm.wan.databinding.ItemSquareNavigationChildBinding

/**
 * 广场导航子Adapter
 * @author play0451
 */
class SquareNavigationChildAdapter(datas: MutableList<AriticleInfo>) :
    BaseQuickAdapter<AriticleInfo, BaseDataBindingHolder<ItemSquareNavigationChildBinding>>(
        R.layout.item_square_navigation_child, datas
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemSquareNavigationChildBinding>,
        item: AriticleInfo
    ) {
        holder.dataBinding?.info = item
        holder.dataBinding?.executePendingBindings()
    }
}