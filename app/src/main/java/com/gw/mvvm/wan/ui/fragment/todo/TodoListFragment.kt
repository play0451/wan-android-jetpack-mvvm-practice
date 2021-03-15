package com.gw.mvvm.wan.ui.fragment.todo

import android.os.Bundle
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.gw.mvvm.framework.ext.navigation.nav
import com.gw.mvvm.framework.ext.navigation.navigateAction
import com.gw.mvvm.framework.ext.navigation.navigateUp
import com.gw.mvvm.framework.ext.utils.dp2px
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.core.view.recylerview.SpaceItemDecoration
import com.gw.mvvm.wan.data.model.TodoInfo
import com.gw.mvvm.wan.databinding.FragmentTodoListBinding
import com.gw.mvvm.wan.ext.*
import com.gw.mvvm.wan.ui.adapter.todo.TodoListAdapter
import com.gw.mvvm.wan.viewModel.todo.TodoViewModel
import com.kingja.loadsir.core.LoadService
import splitties.resources.str
import splitties.toast.toast

/**
 * To do列表Fragment
 */
class TodoListFragment : BaseFragment<TodoViewModel, FragmentTodoListBinding>() {

    //loading界面管理
    private lateinit var loadsir: LoadService<Any>
    private val adapter: TodoListAdapter by lazy { createAdapter() }

    override fun layoutId(): Int = R.layout.fragment_todo_list

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let {
            it.includeToolbar.toolbar.initWithNavIcon(
                title = str(R.string.todo_title),
                navCallback = {
                    navigateUp()
                },
                menuId = R.menu.menu_todo_list,
                menuClickListener = { item ->
                    if (item.itemId == R.id.item_add) {
                        nav().navigateAction(
                            R.id.action_todoListFragment_to_todoEditFragment,
                            TodoEditFragment.makeData(null)
                        )
                    }
                    true
                })
            //初始化loading界面管理
            loadsir = initLoadServiceWithReloadCallback(it.includeRefreshList.layoutRefresh) {
                initData()
            }
            it.includeRefreshList.layoutRefresh.init({
                mViewModel.getTodoList(true)
            }, {
                mViewModel.getTodoList(false)
            })
            it.includeRefreshList.includeList.viewRecycler.init(
                LinearLayoutManager(requireContext()), adapter,
                SpaceItemDecoration(0, requireContext().dp2px(8), true),
                false,
                it.includeRefreshList.includeList.btnFloating
            )
        }
        //adapter初始化
        adapter.run {
            //条目点击事件
            setOnItemClickListener { _, _, position ->
                val info: TodoInfo = adapter.getItem(position)
                nav().navigateAction(
                    R.id.action_todoListFragment_to_todoEditFragment,
                    TodoEditFragment.makeData(info)
                )
            }
            //增加子点击项
            addChildClickViewIds(R.id.btn_more)
            //子点击项点击事件
            setOnItemChildClickListener { _, view, position ->
                if (view.id != R.id.btn_more) {
                    return@setOnItemChildClickListener
                }
                val info: TodoInfo = adapter.getItem(position)
                //创建popup菜单
                val pop: PopupMenu = PopupMenu(requireContext(), view)
                requireActivity().menuInflater.inflate(R.menu.menu_todo_list_item, pop.menu)
                //如果todo已经完成了,就隐藏完成菜单项
                if (info.isDone) {
                    val itemDone: MenuItem? = pop.menu.findItem(R.id.item_done)
                    itemDone?.isVisible = false
                }
                //菜单点击回调
                pop.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.item_edit -> {
                            editTodo(info)
                        }
                        R.id.item_delete -> {
                            deleteTodo(info, position)
                        }
                        R.id.item_done -> {
                            finishTodo(info, position)
                        }
                    }
                    true
                }
                //显示菜单
                pop.show()
            }
            //条目长按事件
            setOnItemLongClickListener { _, _, position ->
                //记录长按的位置,以便点击ContextMenu时使用
                adapter.contextMenuItemPos = position
                //如果这里返回了true消耗掉了点击事件的话,创建ContexMenu的相关流程就不会走了
                false
            }
        }
    }

    override fun initObserver() {
        mViewModel.todoList.observe(viewLifecycleOwner, {
            loadsir.dealResult(it)
            adapter.updateData(it, mDataBinding.includeRefreshList.layoutRefresh)
        })
        mViewModel.deleteResult.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                it.data?.let { pos ->
                    adapter.removeAt(pos)
                }
                if (adapter.itemCount <= 0) {
                    loadsir.showEmpty()
                }
            } else {
                toast(it.errorMsg)
            }
        })
        mViewModel.doneResult.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                it.data?.let {
                    mDataBinding.includeRefreshList.layoutRefresh.autoRefresh()
                }
            } else {
                toast(it.errorMsg)
            }
        })
        mGlobalEventViewModel.todoRefreshEvent.observeInFragment(this) {
            mDataBinding.includeRefreshList.layoutRefresh.autoRefresh()
        }
    }

    override fun initData() {
        loadsir.showLoading()
        mViewModel.getTodoList(true)
    }

    private fun createAdapter(): TodoListAdapter {
        return TodoListAdapter(mutableListOf()) {
            requireActivity().menuInflater.inflate(R.menu.menu_todo_list_item, it)
            val info: TodoInfo = adapter.getItem(adapter.contextMenuItemPos)
            if (info.isDone) {
                it.findItem(R.id.item_done)?.isVisible = false
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info: TodoInfo = adapter.getItem(adapter.contextMenuItemPos)
        when (item.itemId) {
            R.id.item_edit -> {
                editTodo(info)
            }
            R.id.item_delete -> {
                deleteTodo(info, adapter.contextMenuItemPos)
            }
            R.id.item_done -> {
                finishTodo(info, adapter.contextMenuItemPos)
            }
        }

        return super.onContextItemSelected(item)
    }

    /**
     * 编辑Todo
     * @param info TodoInfo
     */
    private fun editTodo(info: TodoInfo) {
        nav().navigateAction(
            R.id.action_todoListFragment_to_todoEditFragment,
            TodoEditFragment.makeData(info)
        )
    }

    /**
     * 删除todo
     * @param info TodoInfo
     * @param position Int
     */
    private fun deleteTodo(info: TodoInfo, position: Int) {
        showDialog(
            message = str(R.string.todo_delete_message),
            positiveAction = {
                mViewModel.delete(info.id, position)
            }, negativeButtonText = str(R.string.text_cancel)
        )
    }

    /**
     * 完成todo
     * @param info TodoInfo
     * @param position Int
     */
    private fun finishTodo(info: TodoInfo, position: Int) {
        mViewModel.done(info.id, position)
    }
}