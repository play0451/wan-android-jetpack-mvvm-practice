package com.gw.mvvm.framework.utils

import android.graphics.Color
import androidx.core.math.MathUtils
import java.util.*

/**
 * 工具类
 * @author play0451
 */

/**
 * 随机一个颜色
 * @param maxR Int  最大红色取值,默认255
 * @param maxG Int  最大绿色取值,默认255
 * @param maxB Int  最大蓝色取值,默认255
 * @return Int
 */
fun randomColor(maxR: Int = 255, maxG: Int = 255, maxB: Int = 255): Int {
    Random().run {
        //限定取值范围
        val red = nextInt(MathUtils.clamp(maxR,1,255))
        val green = nextInt(MathUtils.clamp(maxG,1,255))
        val blue = nextInt(MathUtils.clamp(maxB,1,255))
        //使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
        return Color.rgb(red, green, blue)
    }
}