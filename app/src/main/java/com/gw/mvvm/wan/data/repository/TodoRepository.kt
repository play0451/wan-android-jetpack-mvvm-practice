package com.gw.mvvm.wan.data.repository

import com.gw.mvvm.wan.core.network.ApiResponse
import com.gw.mvvm.wan.core.network.apiService
import com.gw.mvvm.wan.data.model.PageInfo
import com.gw.mvvm.wan.data.model.TodoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.Field

/**
 * To do数据仓库
 * @author play0451
 */
class TodoRepository {
    /**
     * 获取todo列表
     * @param pageIndex Int 页码
     * @return ApiResponse<PageInfo<ArrayList<TodoInfo>>>
     */
    suspend fun getTodoList(pageIndex: Int): ApiResponse<PageInfo<ArrayList<TodoInfo>>> {
        return withContext(Dispatchers.IO) {
            apiService.getTodoList(pageIndex)
        }
    }

    /**
     * 新增Todo
     * @param title String  标题
     * @param content String    内容
     * @param date String   完成日期,要求yyyy-MM-dd格式
     * @param type Int  类型
     * @param priority Int  优先级
     * @return ApiResponse<Any?>
     */
    suspend fun addTodo(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int
    ): ApiResponse<Any?> {
        return withContext(Dispatchers.IO) {
            apiService.addTodo(title, content, date, type, priority)
        }
    }

    /**
     * 更新Todo
     * @param id Int    ID
     * @param title String  标题
     * @param content String    内容
     * @param date String   完成日期,要求yyyy-MM-dd格式
     * @param type Int  类型
     * @param priority Int  优先级
     * @return ApiResponse<Any?>
     */
    suspend fun updateTodo(
        id: Int,
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int
    ): ApiResponse<Any?> {
        return withContext(Dispatchers.IO) {
            apiService.updateTodo(title, content, date, type, priority, id)
        }
    }

    /**
     * 删除一个todo
     * @param id Int    ID
     * @return ApiResponse<Any?>
     */
    suspend fun deleteTodo(id: Int): ApiResponse<Any?> {
        return withContext(Dispatchers.IO) {
            apiService.deleteTodo(id)
        }
    }

    /**
     * 完成一个Todo
     * @param id Int    ID
     * @param status Int    状态
     * @return ApiResponse<Any?>
     */
    suspend fun finishTodo(id: Int, status: Int): ApiResponse<Any?> {
        return withContext(Dispatchers.IO) {
            apiService.finishTodo(id, status)
        }
    }
}