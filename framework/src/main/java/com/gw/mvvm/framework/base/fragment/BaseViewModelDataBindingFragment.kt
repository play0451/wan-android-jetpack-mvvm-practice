package com.gw.mvvm.framework.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel

abstract class BaseViewModelDataBindingFragment<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseViewModelFragment<VM>() {

    lateinit var mDataBinding: DB

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (layoutId() <= 0) {
            null
        } else {
            mDataBinding = DataBindingUtil.inflate<DB>(inflater, layoutId(), container, false)
            mDataBinding.lifecycleOwner = this
            mDataBinding.root
        }
    }
}