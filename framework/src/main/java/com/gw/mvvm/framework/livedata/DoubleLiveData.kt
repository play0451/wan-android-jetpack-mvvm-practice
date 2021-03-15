package com.gw.mvvm.framework.livedata

import androidx.lifecycle.MutableLiveData

/**
 *  自定义的Double类型 MutableLiveData,默认值0.0
 */
class DoubleLiveData(value: Double = 0.0) : MutableLiveData<Double>(value)