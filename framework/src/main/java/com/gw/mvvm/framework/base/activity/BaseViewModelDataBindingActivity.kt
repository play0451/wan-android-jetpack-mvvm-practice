package com.gw.mvvm.framework.base.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gw.mvvm.framework.base.viewmodel.BaseViewModel

/**
 *  具有ViewModel和DataiBinding的Activity基类
 *  如果某个activity不需要ViewMoel,则第一个参数可传EmptyViewModel
 */
abstract class BaseViewModelDataBindingActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseViewModelActivity<VM>() {

    lateinit var mDataBinding: DB

    override fun createView() {
        val id: Int = layoutId()
        if (id <= 0) {
            return;
        }
        mDataBinding = DataBindingUtil.setContentView(this, id)
        mDataBinding.lifecycleOwner = this
    }
}