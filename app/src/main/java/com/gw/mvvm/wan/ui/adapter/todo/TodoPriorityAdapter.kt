package com.gw.mvvm.wan.ui.adapter.todo

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.TodoPriorityInfo
import com.gw.mvvm.wan.databinding.ItemTodoPriorityBinding

/**
 * To do优先级Adapter
 * @author play0451
 */
class TodoPriorityAdapter(datas: MutableList<TodoPriorityInfo>) :
    BaseQuickAdapter<TodoPriorityInfo, BaseDataBindingHolder<ItemTodoPriorityBinding>>(
        R.layout.item_todo_priority, datas
    ) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemTodoPriorityBinding>,
        item: TodoPriorityInfo
    ) {
        holder.dataBinding?.info = item
    }
}