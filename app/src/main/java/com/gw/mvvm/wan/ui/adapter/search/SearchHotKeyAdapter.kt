package com.gw.mvvm.wan.ui.adapter.search

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.SearchHotKeyInfo
import com.gw.mvvm.wan.databinding.ItemSearchHotKeyBinding

/**
 * 搜索热词Adapter
 * @author play0451
 */
class SearchHotKeyAdapter(datas: MutableList<SearchHotKeyInfo>) :
    BaseQuickAdapter<SearchHotKeyInfo, BaseDataBindingHolder<ItemSearchHotKeyBinding>>(
        R.layout.item_search_hot_key,
        datas
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemSearchHotKeyBinding>,
        item: SearchHotKeyInfo
    ) {
        holder.dataBinding?.key = item.name
        holder.dataBinding?.executePendingBindings()
    }
}