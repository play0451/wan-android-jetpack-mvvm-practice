package com.gw.mvvm.wan.ui.adapter.common

import android.content.Context
import android.graphics.Color
import com.gw.mvvm.framework.core.appContext
import com.gw.mvvm.framework.ext.utils.toHtml
import com.gw.mvvm.wan.core.view.indicator.ScaleTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * 文字NavigatorAdapter
 * @author play0451
 */
class TextNavigatorAdapter(
    var texts: MutableList<String>,
    var textSize: Float = 17F,
    var textScale: Float = 0.75F,
    var normalColor: Int = Color.WHITE,
    var selectedColor: Int = Color.WHITE,
    var lineColor: Int = Color.WHITE,
    var onSelected: ((index: Int) -> Unit)? = null
) : CommonNavigatorAdapter() {
    override fun getCount(): Int = texts.size

    override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
        val ts = textSize
        val nc = normalColor
        val sc = selectedColor
        return ScaleTransitionPagerTitleView(appContext).apply {
            //缩放
            minScale = textScale
            //设置文本
            text = texts[index].toHtml()
            //字体大小
            textSize = ts
            //未选中颜色
            normalColor = nc
            //选中颜色
            selectedColor = sc
            //点击事件
            setOnClickListener {
                onSelected?.invoke(index)
            }
        }
    }

    override fun getIndicator(context: Context?): IPagerIndicator {
        return LinePagerIndicator(context).apply {
            mode = LinePagerIndicator.MODE_WRAP_CONTENT
            setColors(lineColor)
        }
    }
}