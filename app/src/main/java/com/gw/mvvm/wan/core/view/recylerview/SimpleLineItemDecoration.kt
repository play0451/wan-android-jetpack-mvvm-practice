package com.gw.mvvm.wan.core.view.recylerview

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.gw.mvvm.wan.R

/**
 * 简单的线性ItemDecoration,如果传递colorRes参数,则使用colorRes设置drawable,否则使用drawableRes设置drawable
 * @author play0451
 */
class SimpleLineItemDecoration(
    context: Context,
    orientation: Int = VERTICAL,
    drawableRes: Int = R.drawable.shape_divider_decoration,
    colorRes: Int = 0
) : DividerItemDecoration(context, orientation) {
    init {
        if (colorRes > 0) {
            setDrawable(ColorDrawable(ContextCompat.getColor(context, colorRes)))
        } else {
            val drawable: Drawable? = ContextCompat.getDrawable(context, drawableRes)
            drawable?.let {
                setDrawable(it)
            }
        }
    }
}