package com.gw.mvvm.framework.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的Int类型 MutableLiveData,默认值0
 */
class IntLiveData(value: Int = 0) : MutableLiveData<Int>(value)