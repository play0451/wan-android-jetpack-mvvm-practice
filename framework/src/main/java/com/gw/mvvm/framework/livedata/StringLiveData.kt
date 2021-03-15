package com.gw.mvvm.framework.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的String类型 MutableLiveData,默认值""
 */
class StringLiveData(value: String = "") : MutableLiveData<String>(value)