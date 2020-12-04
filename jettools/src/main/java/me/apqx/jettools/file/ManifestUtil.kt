package me.apqx.jettools.file

import android.content.Context
import android.content.pm.PackageManager
import me.apqx.jettools.log.LogUtil
import java.lang.Exception

/**
 * 读取AndroidManifest数据
 */
private const val TAG = "ManifestUtil"
object ManifestUtil {

    fun readString(context: Context, key: String, def: String = "def"): String {
        var result = def
        try {
            result = getMetaData(context).getString(key, def)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LogUtil.i(TAG, "readString $key : $result")
        return result
    }

    fun readBoolean(context: Context, key: String, def: Boolean = false): Boolean {
        var result = def
        try {
            result = getMetaData(context).getBoolean(key, def)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        LogUtil.i(TAG, "readBoolean $key : $result")
        return result
    }

    private fun getMetaData(context: Context) =
            context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                    .metaData
}