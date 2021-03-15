package com.gw.mvvm.wan.ui.adapter.collect

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.CollectUrlInfo
import com.gw.mvvm.wan.databinding.ItemCollectUrlBinding

/**
 * 收藏网址Adapter
 * @author play0451
 */
class CollectUrlAdapter(datas: MutableList<CollectUrlInfo>) :
    BaseQuickAdapter<CollectUrlInfo, BaseDataBindingHolder<ItemCollectUrlBinding>>(
        R.layout.item_collect_url, datas
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemCollectUrlBinding>,
        item: CollectUrlInfo
    ) {
        holder.dataBinding?.info = item
        holder.dataBinding?.executePendingBindings()
    }
}