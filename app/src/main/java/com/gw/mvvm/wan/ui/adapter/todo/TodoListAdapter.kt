package com.gw.mvvm.wan.ui.adapter.todo

import android.view.Menu
import android.view.View
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.data.model.TodoInfo
import com.gw.mvvm.wan.databinding.ItemTodoListBinding

/**
 * To do列表Adapter
 * @property createContextMenuCallback Function1<[@kotlin.ParameterName] Menu, Unit>?
 * @property contextMenuItemPos Int
 * @constructor
 */
class TodoListAdapter(
    datas: MutableList<TodoInfo>,
    val createContextMenuCallback: ((menu: Menu) -> Unit)? = null
) :
    BaseQuickAdapter<TodoInfo, TodoListAdapter.TodoListItemHolder<ItemTodoListBinding>>(
        R.layout.item_todo_list, datas
    ) {

    /**
     * 记录长按的item的位置,方便外部ContextMenu点击时寻找item使用
     */
    var contextMenuItemPos: Int = 0

    override fun convert(holder: TodoListItemHolder<ItemTodoListBinding>, item: TodoInfo) {
        holder.dataBinding?.info = item
        holder.dataBinding?.executePendingBindings()
        //注册长按事件来保存item的位置,因为BaseQuickAdapter本身就支持setOnItemLongClickListener,所以在外部设置了,这里就不设置了
        /*holder.itemView.setOnLongClickListener {
            contextMenuItemPos = data.indexOf(item)
            false
        }*/
    }

    /*override fun onViewRecycled(holder: TodoListItemHolder<ItemTodoListBinding>) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }*/

    inner class TodoListItemHolder<BD : ViewDataBinding>(view: View) :
        BaseDataBindingHolder<BD>(view) {
        init {
            //实现接口并通知外部创建ContextMenu
            view.setOnCreateContextMenuListener { menu, _, _ ->
                createContextMenuCallback?.invoke(menu)
            }
        }
    }
}