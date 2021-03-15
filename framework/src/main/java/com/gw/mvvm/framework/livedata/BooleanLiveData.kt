package com.gw.mvvm.framework.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定义的Boolean类型MutableLiveData,默认值false
 */
class BooleanLiveData(value: Boolean = false) : MutableLiveData<Boolean>(value)