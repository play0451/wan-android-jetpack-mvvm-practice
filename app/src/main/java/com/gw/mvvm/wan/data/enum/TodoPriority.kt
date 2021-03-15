package com.gw.mvvm.wan.data.enum

import com.gw.mvvm.wan.R

/**
 * To do优先级
 * @author play0451
 */
enum class TodoPriority(val proiority: Int, val colorRes: Int, val strRes: Int) {
    Low(0, R.color.green_500, R.string.text_low),
    Meduim(1, R.color.blue_500, R.string.text_medium),
    High(2, R.color.orange_500, R.string.text_hight),
    VeryHigh(3, R.color.red_500, R.string.text_very_high);

    companion object {
        fun formPriority(proiority: Int): TodoPriority {
            values().forEach {
                if (proiority == it.proiority) {
                    return it
                }
            }
            return Low
        }
    }
}