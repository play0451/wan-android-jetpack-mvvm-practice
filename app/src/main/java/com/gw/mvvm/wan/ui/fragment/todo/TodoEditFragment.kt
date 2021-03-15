package com.gw.mvvm.wan.ui.fragment.todo

import android.os.Bundle
import androidx.core.os.bundleOf
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.customListAdapter
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.TimeUtils
import com.gw.mvvm.framework.ext.navigation.navigateUp
import com.gw.mvvm.framework.ext.view.onClick
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.core.base.BaseFragment
import com.gw.mvvm.wan.data.enum.TodoPriority
import com.gw.mvvm.wan.data.model.TodoInfo
import com.gw.mvvm.wan.data.model.TodoPriorityInfo
import com.gw.mvvm.wan.databinding.FragmentTodoEditBinding
import com.gw.mvvm.wan.ext.initClose
import com.gw.mvvm.wan.ext.resolveThemeColor
import com.gw.mvvm.wan.ui.adapter.todo.TodoPriorityAdapter
import com.gw.mvvm.wan.viewModel.todo.TodoViewModel
import splitties.resources.str
import splitties.toast.toast
import java.util.*

/**
 * To do编辑Fragment
 * @property todoInfo TodoInfo?
 */
class TodoEditFragment : BaseFragment<TodoViewModel, FragmentTodoEditBinding>() {

    private var todoInfo: TodoInfo? = null
    private var opType: Int = OP_TYPE_ADD

    companion object {

        private const val OP_TYPE_ADD: Int = 0
        private const val OP_TYPE_EDIT: Int = 1

        const val KEY_TODO_DATA: String = "todoData"
        fun makeData(todoInfo: TodoInfo?): Bundle {
            return if (todoInfo != null) {
                bundleOf(KEY_TODO_DATA to todoInfo)
            } else {
                Bundle()
            }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_todo_edit

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            todoInfo = it.getParcelable(KEY_TODO_DATA)
            if (todoInfo != null) {
                opType = OP_TYPE_EDIT
            }
        }
        mDataBinding.let {
            it.includeToolbar.toolbar.initClose(
                if (todoInfo == null) str(R.string.todo_add_title) else str(R.string.todo_edit_title)
            ) {
                navigateUp()
                KeyboardUtils.hideSoftInput(requireActivity())
            }
            it.llPriority.onClick {
                selectPriority()
            }
            it.llDate.onClick {
                selectDate()
            }
            it.btnSubmit.onClick {
                checkSubmit()
            }
            mDataBinding.viewModel = mViewModel
        }
    }

    override fun initObserver() {
        mViewModel.addResult.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                mGlobalEventViewModel.sendTodoRefresh()
                navigateUp()
            } else {
                toast(it.errorMsg)
            }
        })
        mViewModel.editResult.observe(viewLifecycleOwner, {
            if (it.isSuccess) {
                mGlobalEventViewModel.sendTodoRefresh()
                navigateUp()
            } else {
                toast(it.errorMsg)
            }
        })
    }

    override fun initData() {
        if (todoInfo != null) {
            mViewModel.setTodoInfo(todoInfo!!)
        }
    }

    private fun selectPriority() {
        val adapter: TodoPriorityAdapter = TodoPriorityAdapter(
            mutableListOf(
                TodoPriorityInfo(
                    TodoPriority.Low.proiority,
                    str(TodoPriority.Low.strRes),
                    TodoPriority.Low.colorRes
                ),
                TodoPriorityInfo(
                    TodoPriority.Meduim.proiority,
                    str(TodoPriority.Meduim.strRes),
                    TodoPriority.Meduim.colorRes
                ),
                TodoPriorityInfo(
                    TodoPriority.High.proiority,
                    str(TodoPriority.High.strRes),
                    TodoPriority.High.colorRes
                ),
                TodoPriorityInfo(
                    TodoPriority.VeryHigh.proiority,
                    str(TodoPriority.VeryHigh.strRes),
                    TodoPriority.VeryHigh.colorRes
                ),
            )
        )
        val dialog: MaterialDialog = MaterialDialog(requireContext())
            .lifecycleOwner(this)
            .show {
                title(R.string.todo_edit_select_priority)
                customListAdapter(adapter)
                cancelable(true)
                cancelOnTouchOutside(true)
            }
        adapter.setOnItemClickListener { _, _, position ->
            val info: TodoPriorityInfo = adapter.getItem(position)
            mViewModel.todoInfo.value?.priority = info.priority
            mViewModel.todoInfo.value = mViewModel.todoInfo.value   //通知数据变化
            dialog.dismiss()
        }
    }

    private fun selectDate() {
        val d: Calendar = Calendar.getInstance()
        //如果是编辑模式,并且完成时间大于当前时间就设置为完成时间,否则设置为当前的下一天
        if (opType == OP_TYPE_EDIT && System.currentTimeMillis() < mViewModel.todoInfo.value!!.date) {
            d.timeInMillis = mViewModel.todoInfo.value!!.date
        } else {
            d.add(Calendar.DATE, 1)
        }
        MaterialDialog(requireContext())
            .lifecycleOwner(this)
            .show {
                datePicker(minDate = Calendar.getInstance(), currentDate = d) { _, datetime ->
                    mViewModel.todoInfo.value?.apply {
                        date = datetime.timeInMillis
                        dateStr = TimeUtils.millis2String(datetime.timeInMillis, "yyyy-MM-dd")
                    }
                    mViewModel.todoInfo.value = mViewModel.todoInfo.value   //通知数据变化
                }
                val color: Int = resolveThemeColor(R.attr.colorDialogButton)
                getActionButton(WhichButton.POSITIVE).updateTextColor(color)
                getActionButton(WhichButton.NEGATIVE).updateTextColor(color)
            }
    }

    private fun checkSubmit() {
        if (mViewModel.todoInfo.value?.content.isNullOrBlank()) {
            mDataBinding.tilTitle.error = str(R.string.todo_edit_error_title)
            return
        }
        KeyboardUtils.hideSoftInput(requireActivity())
        mViewModel.save(opType == OP_TYPE_ADD)
    }
}