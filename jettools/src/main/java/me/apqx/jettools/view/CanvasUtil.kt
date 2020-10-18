package me.apqx.libtools.view

import android.graphics.Rect

object CanvasUtil {
    /**
     * 在一个Container中绘制垂直居中显示的Text，获取它的基准点Y坐标
     *
     * @param containerTop 容器的Top坐标
     * @param containerBottom 容器的Bottom坐标
     * @param textBounds 测量后Text的边界矩形
     */
    fun getDrawTextVerticalCenterY(containerTop: Int, containerBottom: Int, textBounds: Rect) : Int {
        val gap = ((containerBottom - containerTop) - (textBounds.bottom - textBounds.top)) / 2
        return containerTop + (gap - textBounds.top)
    }

    /**
     * 在一个Container中绘制横向居中显示的Text，获取它的基准点X坐标
     *
     * @param containerLeft 容器的Left坐标
     * @param containerRight 容器的Right坐标
     * @param textBounds 测量后Text的边界矩形
     */
    fun getDrawTextHorizontalCenterX(containerLeft: Int, containerRight: Int, textBounds: Rect) : Int {
        val gap = ((containerRight - containerLeft) - (textBounds.right - textBounds.left)) / 2
        return containerLeft + gap
    }

}