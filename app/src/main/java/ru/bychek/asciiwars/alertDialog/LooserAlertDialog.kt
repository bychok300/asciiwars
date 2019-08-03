package ru.bychek.asciiwars.alertDialog

import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.Toast

class LooserAlertDialog: AlertDialogFactory {

    override fun show(context: Context) {
        val builder = AlertDialog.Builder(context)
        // Set the alert dialog title
        builder.setTitle("YOU LOOSE")
        // Display a message on alert dialog
        builder.setMessage("Improve your skills and ty again!")
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("New Game?"){dialog, which ->
            // TODO create new game
            Toast.makeText(context,"Ok", Toast.LENGTH_SHORT).show()
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton("Back To Menu"){dialog,which ->
            // TODO: back to menu
            Toast.makeText(context,"Menu", Toast.LENGTH_SHORT).show()
        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()
    }
}