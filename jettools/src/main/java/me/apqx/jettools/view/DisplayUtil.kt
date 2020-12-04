package me.apqx.jettools.view

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import me.apqx.jettools.log.LogUtil.d
import me.apqx.jettools.log.LogUtil.i

object DisplayUtil {
    /**
     * 将dp转换为px
     */
    fun dpToPx(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 将px转换为dp
     */
    fun pxToDp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将sp转换为px
     */
    fun spToPx(context: Context, dpValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (dpValue * fontScale + 0.5f).toInt()
    }

    /**
     * 将px转换为sp
     */
    fun pxToSp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 把半角字符切换为全角字符
     */
    fun halfToFull(input: String): String {
        val chars = input.toCharArray()
        for (i in chars.indices) {
            // 半角空格比较特殊
            if (chars[i].toInt() == 32) {
                chars[i] = 12288.toChar()
                continue
            }
            // 其他符号都转换为全角
            if (chars[i].toInt() in 33..126) chars[i] = (chars[i] + 65248)
        }
        return String(chars)
    }

    /**
     * 把全角字符切换为半角字符
     */
    fun fullToHalf(input: String): String {
        val chars = input.toCharArray()
        for (i in chars.indices) {
            // 半角空格比较特殊
            if (chars[i].toInt() == 12288) {
                chars[i] = 32.toChar()
                continue
            }
            // 其他符号都转换为全角
            if (chars[i].toInt() in 65281..65374) chars[i] = (chars[i] - 65248)
        }
        return String(chars)
    }

    /**
     * 当Theme设定Activity顶部延伸到系统顶部状态栏中显示时，通过这个方法，设置顶部TitleBar的paddingTop为系统状态栏高度，
     * TitleBar的高度需要为wrap_content
     * @param titleBar 头部控件的ViewGroup,若为null,整个界面将和状态栏重叠
     */
    fun initTitleBar(titleBar: View?) {
        if (titleBar == null) return
        val context = titleBar.context
        // 设置头部控件ViewGroup的PaddingTop,防止界面与状态栏重叠
        // 本质是增大titleBar的高度，并设置上padding
        val statusBarHeight: Int = getStatusBarHeight(context)
        titleBar.measure(0, 0)
        val measuredHeight = titleBar.measuredHeight
        val layoutParams = titleBar.layoutParams
        layoutParams.height = measuredHeight + statusBarHeight
        titleBar.layoutParams = layoutParams
        titleBar.setPadding(titleBar.paddingLeft,
                titleBar.paddingTop + statusBarHeight,
                titleBar.paddingRight,
                titleBar.paddingBottom)
    }

    /**
     * 获取系统状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier(
                "status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 设置指定Activity的状态栏透明显示
     *
     * @param darkStatusIcon 是否显示深色的状态栏图标、文字
     */
    fun setStatusBarTransparent(activity: Activity, darkStatusIcon: Boolean) {
        setStatusBarTransparent(activity)
        setStatusDarkIcon(activity, darkStatusIcon)
    }

    /**
     * 在Activity运行时，设置状态栏为透明，必须设置Theme#windowTranslucentStatus
     * 安卓5.0 SDK21及以上有效
     */
    fun setStatusBarTransparent(activity: Activity) {
        i("setStatusBarTransparent " + activity.javaClass.simpleName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            // 添加Flag
            var systemUiVisibility = window.decorView.systemUiVisibility
            systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.decorView.systemUiVisibility = systemUiVisibility
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 在Activity运行时，动态设置状态栏颜色，不需要设置Theme
     * 安卓5.0 SDK21及以上有效
     */
    fun setStatusBarColor(activity: Activity, color: Int) {
        i("setStatusBarColor " + activity.javaClass.simpleName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.statusBarColor = color
        }
    }

    /**
     * 在Activity运行时，动态设置状态栏图标颜色，不需要设置Theme
     * 安卓6.0 SDK23及以上有效，在5.0~6.0之间的设备，无法设置状态栏图标颜色，如果因为状态栏透明显示，需要设置深色图标，避免状态栏一片白，
     * 可以[setStatusBarColor][DisplayUtil.setStatusBarColor]给状态栏设置一个深色的背景
     *
     * @param darkStatusIcon 是否显示深色的状态栏图标、文字
     */
    fun setStatusDarkIcon(activity: Activity, darkStatusIcon: Boolean) {
        i("setStatusDarkIcon " + activity.javaClass.simpleName + ", "
                + "darkStatusIcon = " + darkStatusIcon)
        val window = activity.window
        var systemUiVisibility = window.decorView.systemUiVisibility
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            systemUiVisibility = if (darkStatusIcon) {
                // 添加Flag
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // 删除Flag
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            window.decorView.systemUiVisibility = systemUiVisibility
        }
    }

    /**
     * 对于一个32位二进制，指定的位是否为1
     *
     * @param bits       原始数据段
     * @param targetBits 目标数据段
     */
    private fun bitAlreadyEnable(bits: Int, targetBits: Int): Boolean {
        return bits and targetBits == targetBits
    }


    /**
     * 打印View层级
     */
    fun listViews(view: View, level: Int) {
        if (view is ViewGroup) {
            d(getLevelSpace(level) + "|-" + view + "\\")
            val newLevel = level + 1
            for (i in 0 until view.childCount) {
                listViews(view.getChildAt(i), newLevel)
            }
        } else {
            d(getLevelSpace(level) + "|-" + view)
        }
    }

    private fun getLevelSpace(level: Int): String {
        val sb = StringBuilder()
        for (i in 0 until level) {
            sb.append("_")
        }
        return sb.toString()
    }

    /**
     * 隐藏输入法
     * @param view 与弹出输入法的View在同一个Window中的其它任何一个View
     */
    fun hideSoftInputKeyboard(view: View) {
        val imm = (view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 指定的EditText获取焦点并弹出输入法
     */
    fun focusAndShowSoftInputKeyboard(et: EditText) {
        et.requestFocus()
        val imm = (et.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 清除该View和其子View的所有EditText的焦点
     * @param view View或ViewGroup
     */
    fun clearEditFocus(view: View?) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                clearEditFocus(view.getChildAt(i))
            }
        } else {
            if (view is EditText) {
                view.isFocusableInTouchMode = false
                view.clearFocus()
                view.isFocusableInTouchMode = true
            }
        }
    }

    /**
     * 获取指定Activity的View截图
     */
    fun getScreenShot(activity: Activity): Bitmap {
        val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
        return getViewBitmap(rootView)
    }

    /**
     * 获取一个View显示内容的Bitmap，这个View必须是测量、布局、绘制过的，否则返回的Bitmap可能尺寸为0
     */
    fun getViewBitmap(view: View): Bitmap {
        // 创建一个用于承载视图的Bitmap
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        // 创建一个以该Bitmap作为画布的Canvas
        val canvas = Canvas(bitmap)
        // 把View绘制到这个画布上
        view.draw(canvas)
        // 将绘制后的画布Bitmap作为结果返回
        return bitmap
    }

    /**
     * 获取屏幕尺寸
     *
     * @return width, height
     */
    fun getScreenSize(activity: Activity): Array<Int> {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return arrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }
}