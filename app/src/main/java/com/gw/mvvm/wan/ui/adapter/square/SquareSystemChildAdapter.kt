package com.gw.mvvm.wan.ui.adapter.square

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.ClassifyInfo
import com.gw.mvvm.wan.databinding.ItemSquareSystemChildBinding

/**
 * 广场体系子条目Adapter
 * @author play0451
 */
class SquareSystemChildAdapter(datas: MutableList<ClassifyInfo>) :
    BaseQuickAdapter<ClassifyInfo, BaseDataBindingHolder<ItemSquareSystemChildBinding>>(
        R.layout.item_square_system_child, datas
    ) {
    override fun convert(holder: BaseDataBindingHolder<ItemSquareSystemChildBinding>, item: ClassifyInfo) {
        holder.dataBinding?.info = item
        holder.dataBinding?.executePendingBindings()
    }
}