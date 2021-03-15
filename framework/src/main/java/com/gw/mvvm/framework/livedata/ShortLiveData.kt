package com.gw.mvvm.framework.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的Short类型 MutableLiveData,默认值0
 */
class ShortLiveData(value: Short = 0) : MutableLiveData<Short>(value)