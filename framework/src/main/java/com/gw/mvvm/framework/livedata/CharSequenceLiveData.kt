package com.gw.mvvm.framework.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 自定CharSequence类型LiveData,默认值空字符串
 * @author play0451
 */
class CharSequenceLiveData(value: CharSequence = "") : MutableLiveData<CharSequence>(value)