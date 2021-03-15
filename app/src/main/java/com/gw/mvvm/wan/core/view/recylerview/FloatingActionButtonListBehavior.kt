package com.gw.mvvm.wan.core.view.recylerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * @author play0451
 */
class FloatingActionButtonListBehavior(context: Context, attrs: AttributeSet) :
    FloatingActionButton.Behavior(context, attrs) {
    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(
            coordinatorLayout,
            child,
            directTargetChild,
            target,
            axes,
            type
        )
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
        if (dyConsumed > 0 && child.isOrWillBeShown) {
            //源码里当hide动画完成时回把按钮设置为GONE
            // 当为GONE时不会收到onNestedScroll回调
            // 所以这里用一个listener把按钮设置为INVISIBLE
            child.hide(listener)
        } else if (dyConsumed < 0 && child.isOrWillBeHidden) {
            child.show()
        }
    }

    private val listener: FloatingActionButton.OnVisibilityChangedListener = object :
        FloatingActionButton.OnVisibilityChangedListener() {

        override fun onHidden(fab: FloatingActionButton?) {
            super.onHidden(fab)
            fab?.visibility = View.INVISIBLE
        }
    }
}