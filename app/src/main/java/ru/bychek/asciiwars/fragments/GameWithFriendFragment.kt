package ru.bychek.asciiwars.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.util.Log
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

class GameWithFriendFragment : Fragment() {

    private var gameAreaWidth: Int = 0
    private var gameAreaHeigth: Int = 0
    private var userStartPositionX: Int = 0
    private var userStartPositionY: Int = 0


    companion object {
        fun newInstance(): GameWithFriendFragment = GameWithFriendFragment()
        @SuppressLint("StaticFieldLeak")
        lateinit var gameTextView: TextView
        var withFriendMode = false
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.game_with_friend_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        GameV2.context = this.context

        gameTextView = view.findViewById(R.id.game_with_friend)

        gameAreaWidth = 10
        gameAreaHeigth = 20
        userStartPositionX = gameAreaWidth - 2
        userStartPositionY = gameAreaHeigth - 2

        GameV2.prepareGameEngine()
        GameV2.startGameWithFriend(gameAreaWidth, gameAreaHeigth, userStartPositionX, userStartPositionY)


        val leftMoveBtn: Button = view!!.findViewById(R.id.leftMoveBtn)
        val rightMoveBtn: Button = view!!.findViewById(R.id.rightMoveBtn)
        val fireBtn: Button = view!!.findViewById(R.id.fireBtn)

        leftMoveBtn.setOnClickListener {
            GameV2.movePlayerOneLeft()
        }

        rightMoveBtn.setOnClickListener {
            GameV2.movePlayerOneRight()
        }

        fireBtn.setOnClickListener {
            GameV2.drawPlayerOneShoot()
        }

        val leftMoveBtnP2: Button = view!!.findViewById(R.id.left_p2)
        val rightMoveBtnP2: Button = view!!.findViewById(R.id.right_p2)
        val fireBtnP2: Button = view!!.findViewById(R.id.fire_p2)

        leftMoveBtnP2.setOnClickListener {
            GameV2.movePlayerTwoRight()
        }

        rightMoveBtnP2.setOnClickListener {
            GameV2.movePlayerTwoLeft()
        }

        fireBtnP2.setOnClickListener {
            GameV2.drawPlayerTwoShoot()
        }

        GlobalScope.launch {
            while (!GameV2.isGameFinish) {
                delay(500)
                if (GameV2.isPlayerOneWin) {
                    try {
                        Handler(Looper.getMainLooper()).post {
                            try {
                                WinnerAlertDialog().show(context)
                            }catch (e: java.lang.Exception) {
                                Log.d("WinnerAlertDialog","context is null. Seems that fragmen has changed")
                                GameV2.isGameFinish = true
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    break
                } else if (GameV2.isPlayerTwoWin) {
                    try {
                        Handler(Looper.getMainLooper()).post {
                            try {
                                LooserAlertDialog().show(context)
                            }catch (e: java.lang.Exception) {
                                Log.d("WinnerAlertDialog","context is null. Seems that fragmen has changed")
                                GameV2.isGameFinish = true
                            }

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    break
                }
            }
        }
    }

    fun displayGameArea() {
        GameWithFriendFragment.gameTextView.text = GameV2.drawGameArea()
    }
}