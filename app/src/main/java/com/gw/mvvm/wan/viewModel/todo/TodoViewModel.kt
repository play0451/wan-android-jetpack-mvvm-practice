package com.gw.mvvm.wan.viewModel.todo

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.TimeUtils
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel
import com.gw.mvvm.framework.ext.viewmodel.request
import com.gw.mvvm.wan.core.GlobalData
import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.data.enum.TodoPriority
import com.gw.mvvm.wan.data.model.TodoInfo
import com.gw.mvvm.wan.data.repository.TodoRepository
import com.gw.mvvm.wan.data.ui.ListUiData
import com.gw.mvvm.wan.data.ui.UpdateUiData
import splitties.toast.toast
import java.util.*

/**
 * To do ViewModel
 * @author play0451
 */
class TodoViewModel : BaseViewModel() {
    private var pageIndex: Int = 1
    private val repository: TodoRepository by lazy { TodoRepository() }

    val todoList: MutableLiveData<ListUiData<TodoInfo>> by lazy { MutableLiveData() }

    val todoInfo: MutableLiveData<TodoInfo> by lazy {
        //默认当前日期加1天
        val d: Calendar = Calendar.getInstance()
        d.add(Calendar.DATE, 1)
        MutableLiveData(
            TodoInfo(
                0,
                "",
                "",
                d.timeInMillis,
                TimeUtils.millis2String(d.timeInMillis, "yyyy-MM-dd"),
                0,
                TodoPriority.Low.proiority,
                0,
                "",
                0,
                0
            )
        )
    }

    val addResult: MutableLiveData<UpdateUiData<Int>> by lazy { MutableLiveData() }
    val editResult: MutableLiveData<UpdateUiData<Int>> by lazy { MutableLiveData() }
    val deleteResult: MutableLiveData<UpdateUiData<Int>> by lazy { MutableLiveData() }
    val doneResult: MutableLiveData<UpdateUiData<Int>> by lazy { MutableLiveData() }

    /**
     * 获取Todo列表
     * @param isRefresh Boolean 是否是刷新
     */
    fun getTodoList(isRefresh: Boolean = false) {
        if (isRefresh) {
            pageIndex = 1
        }
        request({ repository.getTodoList(pageIndex) }, {
            pageIndex++
            todoList.value = ListUiData.parsePageInfo(it, isRefresh)
        }, {
            todoList.value = ListUiData.parseAppException(it, isRefresh)
        })
    }

    fun setTodoInfo(info: TodoInfo) {
        todoInfo.postValue(info)
    }

    fun save(isAdd: Boolean) {
        todoInfo.value?.run {
            if (isAdd) {
                request({
                    repository.addTodo(
                        title,
                        content,
                        TimeUtils.millis2String(date, "yyyy-MM-dd"),
                        type,
                        priority
                    )
                }, {
                    addResult.value = UpdateUiData(true, 0, "")
                }, {
                    addResult.value = UpdateUiData(false, 0, it.errorMsg)
                }, true)
            } else {
                request({
                    repository.updateTodo(
                        id,
                        title,
                        content,
                        TimeUtils.millis2String(date, "yyyy-MM-dd"),
                        type,
                        priority
                    )
                }, {
                    editResult.value = UpdateUiData(true, id, "")
                }, {
                    editResult.value = UpdateUiData(false, id, "")
                }, true)
            }
        }
    }

    /**
     * 删除todo
     */
    fun delete(id: Int, position: Int) {
        request({ repository.deleteTodo(id) }, {
            deleteResult.value = UpdateUiData(isSuccess = true, data = position)
        }, {
            deleteResult.value = UpdateUiData(isSuccess = false, errorMsg = it.errorMsg)
        })
    }

    /**
     * 完成todo
     */
    fun done(id: Int, position: Int) {
        request({ repository.finishTodo(id, 1) }, {
            doneResult.value = UpdateUiData(isSuccess = true, data = position)
        }, {
            doneResult.value = UpdateUiData(isSuccess = false, errorMsg = it.errorMsg)
        })
    }
}