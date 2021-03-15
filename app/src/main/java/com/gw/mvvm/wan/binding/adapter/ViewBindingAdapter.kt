package com.gw.mvvm.wan.binding.adapter

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.gw.mvvm.framework.core.appContext
import com.gw.mvvm.framework.utils.randomColor
import com.gw.mvvm.wan.R

/**
 * 视图的BindingAdapter集合
 * @author play0451
 */

/**
 * 显示或隐藏
 * @param view View
 * @param bo Boolean
 */
@BindingAdapter("visibleOrGone")
fun viewVisibleOrGone(view: View, bo: Boolean) {
    view.visibility = if (bo) View.VISIBLE else View.GONE
}

/**
 * 显示或隐藏
 * @param view View
 * @param bo Boolean
 */
@BindingAdapter("visibleOrInvisible")
fun viewVisibleOrInvisible(view: View, bo: Boolean) {
    view.visibility = if (bo) View.VISIBLE else View.INVISIBLE
}

/**
 * 加载图片
 * @param imageView ImageView
 * @param url String
 * @param placehloder Int
 * @param error Int
 */
@BindingAdapter(value = ["imageUrl", "placeholder", "error"], requireAll = false)
fun imageUrl(imageView: ImageView, url: String, placehloder: Int = 0, error: Int = 0) {
    if (url.isBlank()) {
        return
    }
    imageView.load(url) {
        crossfade(true)
        if (placehloder > 0) {

            placeholder(placehloder)
        }
        if (error > 0) {
            error(error)
        }
    }
}

/**
 * 加载图片资源
 * @param imageView ImageView
 * @param drawable Drawable
 * @param placehloder Int
 * @param error Int
 */
@BindingAdapter(value = ["imageDrawable", "placeholder", "error"], requireAll = false)
fun imageDrawable(
    imageView: ImageView,
    drawable: Drawable,
    placehloder: Drawable? = null,
    error: Drawable? = null
) {
    imageView.load(drawable) {
        crossfade(true)
        if (placehloder != null) {
            placeholder(placehloder)
        }
        if (error != null) {
            error(error)
        }
    }
}

/**
 * 加载图片资源
 * @param imageView ImageView
 * @param resId Int
 * @param placehloder Int
 * @param error Int
 */
@BindingAdapter(value = ["imageResId", "placeholder", "error"], requireAll = false)
fun imageResId(
    imageView: ImageView,
    resId: Int,
    placehloder: Drawable? = null,
    error: Drawable? = null
) {
    imageView.load(resId) {
        crossfade(true)
        if (placehloder != null) {
            placeholder(placehloder)
        }
        if (error != null) {
            error(error)
        }
    }
}

/**
 * 设置图片的Tint
 * @param imageView ImageView
 * @param colorRes Int
 */
@BindingAdapter("imageTint")
fun imageTint(imageView: ImageView, colorRes: Int) {
    imageView.imageTintList =
        ColorStateList.valueOf(ContextCompat.getColor(imageView.context, colorRes))
}

/**
 * TextView随机文本颜色
 * @param textview TextView
 * @param maxR Int  最大红色取值
 * @param maxG Int  最大绿色取值
 * @param maxB Int  最大蓝色取值
 * @return Int
 */
@BindingAdapter(
    value = ["randomTextColor", "maxColorR", "maxColorG", "maxColorB"],
    requireAll = false
)
fun textViewRandomTextColor(
    textview: TextView,
    isRandom: Boolean,
    maxR: Int, maxG: Int, maxB: Int
) {
    if (!isRandom) {
        return
    }
    textview.setTextColor(
        randomColor(
            if (maxR <= 0) 255 else maxR,
            if (maxG <= 0) 255 else maxG,
            if (maxB <= 0) 255 else maxB
        )
    )
}

/**
 * 使用Color资源设置TextView的文本颜色
 * @param textview TextView
 * @param colorRes Int
 */
@BindingAdapter("textColorByRes")
fun textViewTextColorByRes(textview: TextView, colorRes: Int) {
    textview.setTextColor(textview.context.getColor(colorRes))
}