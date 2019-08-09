package ru.bychek.asciiwars.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bychek.asciiwars.R
import ru.bychek.asciiwars.alertDialog.LooserAlertDialog
import ru.bychek.asciiwars.alertDialog.WinnerAlertDialog
import ru.bychek.asciiwars.game.engine.GameV2


class GameFragment : Fragment() {

    private var gameAreaWidth: Int = 0
    private var gameAreaHeigth: Int = 0
    private var userStartPositionX: Int = 0
    private var userStartPositionY: Int = 0


    companion object {
        fun newInstance(): GameFragment = GameFragment()
        @SuppressLint("StaticFieldLeak")
        lateinit var gameTextView: TextView
        var withBotMode = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.game_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        GameV2.context = this.context

        gameTextView = view.findViewById(R.id.game)

        gameAreaWidth = 10
        gameAreaHeigth = 20
        userStartPositionX = gameAreaWidth - 2
        userStartPositionY = gameAreaHeigth - 2

        GameV2.prepareGameEngine()
        GameV2.startGameWithBot(gameAreaWidth, gameAreaHeigth, userStartPositionX, userStartPositionY)


        val leftMoveBtn: Button = view.findViewById(R.id.leftMoveBtn)
        val rightMoveBtn: Button = view.findViewById(R.id.rightMoveBtn)
        val fireBtn: Button = view.findViewById(R.id.fireBtn)

        leftMoveBtn.setOnClickListener {
            GameV2.movePlayerOneLeft()
        }

        rightMoveBtn.setOnClickListener {
            GameV2.movePlayerOneRight()
        }

        fireBtn.setOnClickListener {
            GameV2.drawPlayerOneShoot()
        }

        //TODO refactor. move it to standalone class
        GlobalScope.launch {
            while (!GameV2.isGameFinish) {
                delay(500)
                if (GameV2.isPlayerOneWin) {
                    try {
                        Handler(Looper.getMainLooper()).post {
                            WinnerAlertDialog().show(context)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    break
                } else if (GameV2.isPlayerTwoWin) {
                    try {
                        Handler(Looper.getMainLooper()).post {
                            LooserAlertDialog().show(context)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    break
                }
            }
        }

    }

}