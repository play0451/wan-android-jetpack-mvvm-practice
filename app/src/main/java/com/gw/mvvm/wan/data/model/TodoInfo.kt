package com.gw.mvvm.wan.data.model

import android.os.Parcelable
import com.blankj.utilcode.util.StringUtils
import com.gw.mvvm.wan.data.enum.TodoPriority
import kotlinx.parcelize.Parcelize

/**
 * To do数据
 * @author play0451
 */
@Parcelize
data class TodoInfo(
    var completeDate: Long,
    var completeDateStr: String,
    var content: String,
    var date: Long,
    var dateStr: String,
    var id: Int,
    var priority: Int,
    var status: Int,
    var title: String,
    var type: Int,
    var userId: Int
) : Parcelable {
    val isDone: Boolean
        get() {
            return status == 1
        }
    val isExpire: Boolean
        get() {
            return status != 1 && System.currentTimeMillis() >= date
        }

    val priorityColorRes: Int
        get() {
            val tp: TodoPriority = TodoPriority.formPriority(this.priority)
            return tp.colorRes
        }

    val priorityString: String
        get() {
            val tp: TodoPriority = TodoPriority.formPriority(this.priority)
            return StringUtils.getString(tp.strRes)
        }
}