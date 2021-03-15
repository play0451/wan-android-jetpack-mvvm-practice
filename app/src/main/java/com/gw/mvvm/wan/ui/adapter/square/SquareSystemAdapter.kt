package com.gw.mvvm.wan.ui.adapter.square

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.SquareSystemInfo
import com.gw.mvvm.wan.databinding.ItemSquareSystemBinding
import com.gw.mvvm.wan.ext.init

/**
 * 广场体系Adapter
 * @author play0451
 */
class SquareSystemAdapter(
    datas: MutableList<SquareSystemInfo>,
    var onChildClick: ((systemInfo: SquareSystemInfo, childPosition: Int) -> Unit)? = null
) :
    BaseQuickAdapter<SquareSystemInfo, BaseDataBindingHolder<ItemSquareSystemBinding>>(
        R.layout.item_square_system,
        datas
    ) {

    override fun convert(
        holder: BaseDataBindingHolder<ItemSquareSystemBinding>,
        item: SquareSystemInfo
    ) {
        holder.dataBinding?.let {
            it.info = item
            val layout: FlexboxLayoutManager = FlexboxLayoutManager(holder.itemView.context).apply {
                //方向 主轴为水平方向，起点在左端
                flexDirection = FlexDirection.ROW
                //左对齐
                justifyContent = JustifyContent.FLEX_START
            }

            val adapter: SquareSystemChildAdapter = SquareSystemChildAdapter(item.children).apply {
                setOnItemClickListener { _, _, position ->
                    onChildClick?.invoke(item, position)
                }
            }
            it.viewRecycler.init(layout, adapter, null, false)
            it.executePendingBindings()
        }
    }
}