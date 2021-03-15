package com.gw.mvvm.framework.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的Byte类型 MutableLiveData,默认值0
 */
class ByteLiveData(value: Byte = 0) : MutableLiveData<Byte>(value)