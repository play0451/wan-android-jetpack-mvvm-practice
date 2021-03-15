package com.gw.mvvm.wan.ext

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gw.mvvm.wan.R
import com.scwang.smart.refresh.layout.api.RefreshLayout
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter

/**
 * @author play0451
 */

/**
 * 拦截BottomNavigation长按事件 防止长按时出现Toast
 * @receiver BottomNavigationView
 * @param ids IntArray  ID集合
 */
fun BottomNavigationView.interceptLongClick(vararg ids: Int) {
    val p: ViewGroup = (this.getChildAt(0) as ViewGroup)
    for (index in ids.indices) {
        p.getChildAt(index).findViewById<View>(ids[index])
            .setOnLongClickListener {
                true
            }
    }
}

/**
 * Toolbar初始化
 * @receiver Toolbar
 * @param title String  标题
 * @param menuId Int    菜单ID
 * @param menuClickListener OnMenuItemClickListener? 菜单回调
 * @return Toolbar  返回自身
 */
fun Toolbar.init(
    title: CharSequence? = "",
    menuId: Int = 0,
    menuClickListener: Toolbar.OnMenuItemClickListener? = null
): Toolbar {
    this.title = title
    if (menuId > 0) {
        inflateMenu(menuId)
        if (menuClickListener != null) {
            setOnMenuItemClickListener(menuClickListener)
        }
    }
    return this
}

/**
 * Toolbar初始化
 * @receiver Toolbar
 * @param title String  标题
 * @param icon Int  导航图标
 * @param navCallback Function1<Toolbar, Unit>? 导航图标点击回调
 * @return Toolbar  返回自身
 */
fun Toolbar.initClose(
    title: CharSequence? = "",
    icon: Int = R.drawable.icon_back,
    navCallback: ((Toolbar) -> Unit)? = null
) {
    this.title = title
    this.setNavigationIcon(icon)
    this.setNavigationOnClickListener { navCallback?.invoke(this) }
}

/**
 * Toolbar初始化
 * @receiver Toolbar
 * @param title String  标题
 * @param icon Int  导航图标
 * @param navCallback Function1<Toolbar, Unit>? 导航图标点击回调
 * @param menuId Int    菜单ID
 * @param menuClickListener OnMenuItemClickListener?    菜单回调
 * @return Toolbar  返回自身
 */
fun Toolbar.initWithNavIcon(
    title: CharSequence? = "", icon: Int = R.drawable.icon_back,
    navCallback: ((Toolbar) -> Unit)? = null,
    menuId: Int = 0,
    menuClickListener: Toolbar.OnMenuItemClickListener? = null
): Toolbar {
    this.title = title
    this.setNavigationIcon(icon)
    this.setNavigationOnClickListener { navCallback?.invoke(this) }
    if (menuId > 0) {
        inflateMenu(menuId)
        if (menuClickListener != null) {
            setOnMenuItemClickListener(menuClickListener)
        }
    }
    return this
}

/**
 * RecyclerView初始化
 * @receiver RecyclerView
 * @param layoutManager LayoutManager   布局管理器
 * @param adapter Adapter<*>    adapter
 * @param isNestedScroll Boolean    是否允许嵌套滚动,默认true
 */
fun RecyclerView.init(
    layoutManager: RecyclerView.LayoutManager,
    adapter: RecyclerView.Adapter<*>,
    itemDecoration: RecyclerView.ItemDecoration? = null,
    isNestedScroll: Boolean = true,
    floatbtn: FloatingActionButton? = null
): RecyclerView {
    this.layoutManager = layoutManager
    this.adapter = adapter
    this.isNestedScrollingEnabled = isNestedScroll
    this.setHasFixedSize(true)
    if (itemDecoration != null) {
        addItemDecoration(itemDecoration)
    }
    if (floatbtn != null) {
        initFloatBtn(floatbtn)
    }
    return this
}

/**
 *  初始化刷新布局
 * @receiver RefreshLayout  布局
 * @param onRefresh Function1<[@kotlin.ParameterName] RefreshLayout, Unit>? 刷新回调
 * @param onLoadMore Function1<[@kotlin.ParameterName] RefreshLayout, Unit>?    加载更多回调
 */
fun RefreshLayout.init(
    onRefresh: ((layout: RefreshLayout) -> Unit)? = null,
    onLoadMore: ((layout: RefreshLayout) -> Unit)? = null
) {
    if (onRefresh != null) {
        this.setOnRefreshListener {
            onRefresh(it)
        }
    }
    if (onLoadMore != null) {
        this.setOnLoadMoreListener {
            onLoadMore(it)
        }
    }
}

/**
 * 绑定ViewPager
 * @receiver MagicIndicator
 * @param viewPager ViewPager2
 * @param adapter CommonNavigatorAdapter
 * @param onPageSelected Function1<[@kotlin.ParameterName] Int, Unit>?  viewPager选择回调
 */
fun MagicIndicator.bindViewPager(
    viewPager: ViewPager2,
    adapter: CommonNavigatorAdapter,
    onPageSelected: ((index: Int) -> Unit)? = null
) {
    val commonNavigator = CommonNavigator(viewPager.context)
    commonNavigator.adapter = adapter
    this.navigator = commonNavigator

    viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            this@bindViewPager.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            this@bindViewPager.onPageSelected(position)
            onPageSelected?.invoke(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            this@bindViewPager.onPageScrollStateChanged(state)
        }
    })
}

/**
 * RecyclerView初始化FloatingActionButton
 * @receiver RecyclerView
 * @param floatbtn FloatingActionButton
 */
fun RecyclerView.initFloatBtn(floatbtn: FloatingActionButton) {
    val listener: FloatingActionButton.OnVisibilityChangedListener =
        object : FloatingActionButton.OnVisibilityChangedListener() {
            override fun onHidden(fab: FloatingActionButton?) {
                super.onHidden(fab)
                fab?.visibility = View.INVISIBLE
            }
        }
    //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        @SuppressLint("RestrictedApi")
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!canScrollVertically(-1)) {
                //源码里当hide动画完成时回把按钮设置为GONE
                // 当为GONE时在Behavior里不会收到onNestedScroll回调
                // 所以这里用一个listener把按钮设置为INVISIBLE
                floatbtn.hide(listener)
            }
        }
    })
    floatbtn.setOnClickListener {
        val layoutManager = layoutManager as LinearLayoutManager
        //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
        if (layoutManager.findLastVisibleItemPosition() >= 40) {
            scrollToPosition(0)//没有动画迅速返回到顶部(马上)
        } else {
            smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
        }
    }
}