package com.gw.mvvm.wan.ui.adapter.search

import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.SearchHistoryInfo
import com.gw.mvvm.wan.databinding.ItemSearchHistoryBinding

/**
 * 搜索历史Adapter
 * @author play0451
 */
class SearchHistoryAdapter(datas: MutableList<SearchHistoryInfo>) :
    BaseQuickAdapter<SearchHistoryInfo, BaseDataBindingHolder<ItemSearchHistoryBinding>>(
        R.layout.item_search_history, datas
    ) {

    init {
        setDiffCallback(object : DiffUtil.ItemCallback<SearchHistoryInfo>() {
            override fun areItemsTheSame(
                oldItem: SearchHistoryInfo,
                newItem: SearchHistoryInfo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SearchHistoryInfo,
                newItem: SearchHistoryInfo
            ): Boolean {
                return oldItem.name == newItem.name && oldItem.date == newItem.date
            }

        })
    }

    override fun convert(
        holder: BaseDataBindingHolder<ItemSearchHistoryBinding>,
        item: SearchHistoryInfo
    ) {
        holder.dataBinding?.name = item.name
        holder.dataBinding?.executePendingBindings()
    }
}