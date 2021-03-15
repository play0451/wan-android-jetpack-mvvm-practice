package com.gw.mvvm.wan.ext

import android.app.Activity
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.callbacks.onCancel
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.gw.mvvm.wan.R
import com.gw.mvvm.wan.databinding.LayoutLoadingDialogBinding
import splitties.resources.color
import splitties.resources.resolveThemeAttribute
import splitties.resources.str

/**
 * 对话框扩展
 * @author play0451
 */

/**
 * LoadingDialog
 */
private var loadingDialog: MaterialDialog? = null

/**
 * 显示对话框
 * @receiver AppCompatActivity
 * @param message String    内容
 * @param title String  标题
 * @param positiveButtonText String 确定按钮文字
 * @param positiveAction Function0<Unit>    确定按钮回调
 * @param negativeButtonText String 取消按钮文字
 * @param negativeAction Function0<Unit>    取消按钮回调
 * @param cancelable Boolean    是否可以取消
 * @param touchcancelable Boolean    是否可以点击对话框外部以取消
 * @param positiveColor Int 确定按钮颜色
 * @param negativeColor Int 取消按钮颜色
 * @param onCancelAction Function0<Unit> 取消回调
 */
fun AppCompatActivity.showDialog(
    message: String,
    title: String = str(R.string.text_hint),
    positiveButtonText: String = str(R.string.text_sure),
    positiveAction: (() -> Unit)? = null,
    negativeButtonText: String = "",
    negativeAction: (() -> Unit)? = null,
    cancelable: Boolean = true,
    touchcancelable: Boolean = true,
    onCancelAction: (() -> Unit)? = null,
    positiveColor: Int = color(resolveThemeAttribute(R.attr.colorDialogButton)),
    negativeColor: Int = color(resolveThemeAttribute(R.attr.colorDialogButton))
) {
    MaterialDialog(this)
        .cancelable(cancelable)
        .cancelOnTouchOutside(touchcancelable)
        .lifecycleOwner(this)
        .show {
            title(text = title)
            message(text = message)
            positiveButton(text = positiveButtonText) {
                positiveAction?.invoke()
            }
            if (negativeButtonText.isNotEmpty()) {
                negativeButton(text = negativeButtonText) {
                    negativeAction?.invoke()
                }
            }
            getActionButton(WhichButton.POSITIVE).updateTextColor(positiveColor)
            getActionButton(WhichButton.NEGATIVE).updateTextColor(negativeColor)
            onCancel {
                onCancelAction?.invoke()
            }
        }
}

/**
 * 显示对话框
 * @receiver Fragment
 * @param message String    内容
 * @param title String  标题
 * @param positiveButtonText String 确定按钮文字
 * @param positiveAction Function0<Unit>    确定按钮回调
 * @param negativeButtonText String 取消按钮文字
 * @param negativeAction Function0<Unit>    取消按钮回调
 * @param cancelable Boolean    是否可以取消
 * @param touchcancelable Boolean    是否可以点击对话框外部以取消
 * @param positiveColor Int 确定按钮颜色
 * @param negativeColor Int 取消按钮颜色
 * @param onCancelAction Function0<Unit> 取消回调
 */
fun Fragment.showDialog(
    message: String,
    title: String = str(R.string.text_hint),
    positiveButtonText: String = str(R.string.text_sure),
    positiveAction: (() -> Unit)? = null,
    negativeButtonText: String = "",
    negativeAction: (() -> Unit)? = null,
    cancelable: Boolean = true,
    touchcancelable: Boolean = true,
    onCancelAction: (() -> Unit)? = null,
    positiveColor: Int = color(resolveThemeAttribute(R.attr.colorDialogButton)),
    negativeColor: Int = color(resolveThemeAttribute(R.attr.colorDialogButton))
) {
    MaterialDialog(requireContext())
        .cancelable(cancelable)
        .cancelOnTouchOutside(touchcancelable)
        .lifecycleOwner(this)
        .show {
            title(text = title)
            message(text = message)
            positiveButton(text = positiveButtonText) {
                positiveAction?.invoke()
            }
            if (negativeButtonText.isNotEmpty()) {
                negativeButton(text = negativeButtonText) {
                    negativeAction?.invoke()
                }
            }
            getActionButton(WhichButton.POSITIVE).updateTextColor(positiveColor)
            getActionButton(WhichButton.NEGATIVE).updateTextColor(negativeColor)
            onCancel {
                onCancelAction?.invoke()
            }
        }
}

/**
 * 显示加载对话框
 * @receiver AppCompatActivity
 * @param message String    内容
 * @param cancelable Boolean    是否可以取消,默认true
 * @param toucheCancelable Boolean  是否可以点击外部取消,默认false
 */
fun AppCompatActivity.showLoadingDialog(
    message: String = str(R.string.text_loading_dialog_default),
    cancelable: Boolean = true,
    toucheCancelable: Boolean = false
) {
    if (!this.isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = MaterialDialog(this)
                .cancelable(cancelable)
                .cancelOnTouchOutside(toucheCancelable)
                .cornerRadius(8F)
                .customView(R.layout.layout_loading_dialog)
                .lifecycleOwner(this)
            loadingDialog?.getCustomView()?.run {
                val binding: LayoutLoadingDialogBinding? = DataBindingUtil.bind(this)
                binding?.tip =
                    if (message.isNotBlank()) message else str(R.string.text_loading_dialog_default)
            }
        }
        loadingDialog?.show()
    }
}

/**
 * 显示加载对话框
 * @receiver Fragment
 * @param message String    内容
 * @param cancelable Boolean    是否可以取消,默认true
 * @param toucheCancelable Boolean  是否可以点击外部取消,默认false
 */
fun Fragment.showLoadingDialog(
    message: CharSequence = "",
    cancelable: Boolean = true,
    toucheCancelable: Boolean = false
) {
    requireActivity().let {
    }
    if (!requireActivity().isFinishing) {
        if (loadingDialog == null) {
            loadingDialog = MaterialDialog(requireContext())
                .cancelable(cancelable)
                .cancelOnTouchOutside(toucheCancelable)
                .cornerRadius(8F)
                .customView(R.layout.layout_loading_dialog)
                .lifecycleOwner(this)
            loadingDialog?.getCustomView()?.run {
                val binding: LayoutLoadingDialogBinding? = DataBindingUtil.bind(this)
                binding?.tip =
                    if (message.isNotBlank()) message else str(R.string.text_loading_dialog_default)
            }
        }
        loadingDialog?.show()
    }
}

/**
 * 关闭加载对话框
 * @receiver Activity
 */
fun Activity.hideLoadingDialog() {
    loadingDialog?.dismiss()
    loadingDialog = null
}

/**
 * 关闭加载对话框
 * @receiver Fragment
 */
fun Fragment.hideLoadingDialog() {
    loadingDialog?.dismiss()
    loadingDialog = null
}