package ru.bychek.asciiwars.alertDialog

import android.content.Context
import android.support.v7.app.AlertDialog
import ru.bychek.asciiwars.R
import ru.bychek.asciiwars.fragments.GameFragment
import ru.bychek.asciiwars.fragments.utils.FragmentHelper


class WinnerAlertDialog : AlertDialogFactory {


    override fun show(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("YOU WIN")
        builder.setMessage("Congratulations, you are champion. Tap anywhere to close window.")

        builder.setPositiveButton("Try again?") { _, _ ->
            // recreate fragment => it will produce new game
            FragmentHelper().performFragmentReplace(context, GameFragment.newInstance(), R.id.container)
        }

//        builder.setNegativeButton("Tap") { _, _ ->
//            FragmentHelper().performFragmentReplace(context, MenuFragment.newInstance(), R.id.container)
//        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}