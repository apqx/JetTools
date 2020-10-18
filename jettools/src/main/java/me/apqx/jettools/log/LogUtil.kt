package me.apqx.libtools.log

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object LogUtil {
    private const val TAG = "apqx"
    private val gson: Gson by lazy {
        GsonBuilder()
                .serializeNulls()
                .create()
    }

    var debugOn: Boolean = true

    fun d(str: String) {
        d(TAG, str)
    }

    fun d(tag: String, str: String) {
        if (debugOn)
            Log.d(tag, str)
    }

    fun dJson(obj: Any) {
        if (debugOn)
            dJson(TAG, gson.toJson(obj))
    }

    fun dJson(tag: String, obj: Any) {
        if (debugOn)
            d(tag, gson.toJson(obj))
    }

    fun e(str: String) {
        e(TAG, str)
    }

    fun e(tag: String, str: String) {
        if (debugOn)
            Log.e(tag, str)
    }

    fun eJson(obj: Any) {
        if (debugOn)
            eJson(TAG, gson.toJson(obj))
    }

    fun eJson(tag: String, obj: Any) {
        if (debugOn)
            e(tag, gson.toJson(obj))
    }

    fun i(str: String) {
        i(TAG, str)
    }

    fun i(tag: String, str: String) {
        if (debugOn)
            Log.i(tag, str)
    }

    fun iJson(obj: Any) {
        if (debugOn)
            iJson(TAG, gson.toJson(obj))
    }

    fun iJson(tag: String, obj: Any) {
        if (debugOn)
            i(tag, gson.toJson(obj))
    }


}