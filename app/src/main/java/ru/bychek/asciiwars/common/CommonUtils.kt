package ru.bychek.asciiwars.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import ru.bychek.asciiwars.MainActivity
import ru.bychek.asciiwars.R
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class CommonUtils {

    private val TAG = "CommonUtils"

    private val GAME_CFG = R.raw.with_bot_cfg
    private val withBotCfgFname = "with_bot_cfg.properties"

    fun getProp(propName: String, context: Context): String {
        val internalStoragePath = context.filesDir
        val properties = Properties()
        val inputStream = File("$internalStoragePath/$withBotCfgFname").inputStream()
        properties.load(inputStream)
        return properties.getProperty(propName)
    }

    @SuppressLint("ResourceType")
    fun overrideProp(propName: String, value: String, context: Context) {
        val internalStoragePath = context.filesDir
        val properties = Properties()
        val inputStream = File("$internalStoragePath/$withBotCfgFname").inputStream()
        val fileName = "$internalStoragePath/$withBotCfgFname"
        val fos = FileOutputStream(fileName)
        properties.load(inputStream)
        properties.setProperty(propName, value)
        properties.store(fos, null)
    }

    fun copyResources(resId: Int) {
        Log.i(TAG, "copyResources")

        val context = MainActivity.applicationContext()
        val internalStoragePath = context.filesDir
        val inputStream = context.resources.openRawResource(resId)
        val filename = context.resources.getResourceEntryName(resId) + ".properties"

        Log.d(TAG, internalStoragePath.absolutePath)

        val fileName = "$internalStoragePath/$withBotCfgFname"
        if (!isFileExist(fileName)) {
            try {
                val out = FileOutputStream(File("$internalStoragePath", filename))
                val buffer = 1024
                inputStream.copyTo(out, buffer)
                inputStream.close()
                out.close()
                Log.i(TAG, "files succesfuly copied")

            } catch (e: FileNotFoundException) {
                Log.i(TAG, "copyResources - " + e.message)
            } catch (e: IOException) {
                Log.i(TAG, "copyResources - " + e.message)
            }
        }
    }

    private fun isFileExist(filename: String): Boolean {
        val file = File(filename)
        return file.exists()
    }
}