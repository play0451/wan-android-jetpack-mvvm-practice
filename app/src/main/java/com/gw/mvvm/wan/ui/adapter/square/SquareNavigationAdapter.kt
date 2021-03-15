package com.gw.mvvm.wan.ui.adapter.square

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.SquareNavigationInfo
import com.gw.mvvm.wan.databinding.ItemSquareNavigationBinding
import com.gw.mvvm.wan.ext.init

/**
 * 广场导航Adapter
 * @author play0451
 */
class SquareNavigationAdapter(
    datas: MutableList<SquareNavigationInfo>,
    var onChildClick: ((item: SquareNavigationInfo, childPosition: Int) -> Unit)? = null
) :
    BaseQuickAdapter<SquareNavigationInfo, BaseDataBindingHolder<ItemSquareNavigationBinding>>(
        R.layout.item_square_navigation,
        datas
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemSquareNavigationBinding>,
        item: SquareNavigationInfo
    ) {
        holder.dataBinding?.let {
            it.info = item
            val layout: FlexboxLayoutManager = FlexboxLayoutManager(holder.itemView.context).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            val adapter: SquareNavigationChildAdapter = SquareNavigationChildAdapter(item.articles)
            adapter.setOnItemClickListener { _, _, position ->
                onChildClick?.invoke(item, position)
            }
            it.viewRecycler.init(layout, adapter, null, false)
            it.executePendingBindings()
        }
    }
}