package ru.bychek.asciiwars.fragments.utils

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.app.Activity
import android.app.ActivityManager



class FragmentHelper {

    fun performFragmentReplace(context: Context, fragment: Fragment, viewId: Int) {
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(viewId, fragment)
            .commit()
    }
}