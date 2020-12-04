package me.apqx.jettools.notify

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.widget.Toast

@SuppressLint("StaticFieldLeak")
object ToastUtil {
    lateinit var handler: Handler
    lateinit var context: Context
    lateinit var toast: Toast

    fun init(context: Context) {
        ToastUtil.context = context
        handler = Handler()
    }

    @SuppressLint("ShowToast")
    private fun showToastBase(string: String) {
        if (ToastUtil::toast.isInitialized) {
            toast.cancel()
        }
        toast = Toast.makeText(context, string, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun showToast(string: String) {
        handler.post {
            showToastBase(string)
        }
    }

    fun showToast(strId: Int) {
        handler.post {
            showToastBase(strId)
        }
    }

    @SuppressLint("ShowToast")
    private fun showToastBase(strId: Int) {
        if (ToastUtil::toast.isInitialized) {
            toast.cancel()
        }
        toast = Toast.makeText(context, strId, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun showCustomToast() {
        handler.post {
            showCustomToastBase()
        }
    }

    private fun showCustomToastBase() {
        if (ToastUtil::toast.isInitialized) {
            toast.cancel()
        }
        toast = Toast(context)
//        toast.view = LayoutInflater.from(context).inflate(R.layout.dialog_cus, null, false)
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }
}