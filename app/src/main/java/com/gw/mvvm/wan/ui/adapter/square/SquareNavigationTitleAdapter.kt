package com.gw.mvvm.wan.ui.adapter.square

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.databinding.ItemSquareNavigationTitleBinding
import com.gw.mvvm.wan.databinding.ItemSquareSystemTitleBinding

/**
 * @author play0451
 */
class SquareNavigationTitleAdapter(datas: MutableList<Pair<String, Boolean>>) :
    BaseQuickAdapter<Pair<String, Boolean>, BaseDataBindingHolder<ItemSquareNavigationTitleBinding>>(
        R.layout.item_square_navigation_title, datas
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemSquareNavigationTitleBinding>,
        item: Pair<String, Boolean>
    ) {
        holder.dataBinding?.title = item.first
        holder.dataBinding?.isChecked = item.second
    }
}