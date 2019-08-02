package ru.bychek.asciiwars.alertDialog

import android.content.Context

interface AlertDialogFactory {
    fun show(context: Context)
}