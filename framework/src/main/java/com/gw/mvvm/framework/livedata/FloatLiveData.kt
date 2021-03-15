package com.gw.mvvm.framework.livedata

import androidx.lifecycle.MutableLiveData

/**
 *  自定义的Float类型 MutableLiveData,默认值0.0f
 */
class FloatLiveData(value: Float = 0.0f) : MutableLiveData<Float>(value)