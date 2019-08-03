package ru.bychek.asciiwars

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bychek.asciiwars.alertDialog.LooserAlertDialog
import ru.bychek.asciiwars.alertDialog.WinnerAlerDialog
import ru.bychek.asciiwars.game.engine.GameV2


class MainActivity : AppCompatActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var gameTextView: TextView
    }

    private var gameAreaWidth: Int = 0
    private var gameAreaHeigth: Int = 0
    private var userStartPositionX: Int = 0
    private var userStartPositionY: Int = 0

    lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        gameTextView = findViewById(R.id.game)

        gameTextView.movementMethod = ScrollingMovementMethod()

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        gameAreaWidth = 10
        gameAreaHeigth = 20
        userStartPositionX = gameAreaWidth - 2
        userStartPositionY = gameAreaHeigth - 2

        GameV2.prepareGameEngine()
        GameV2.initGameArea(gameAreaWidth, gameAreaHeigth)
        GameV2.updatePlayerOnePosition(userStartPositionX, userStartPositionY)
        GameV2.updatePlayerTwoPosition(1, 1)
        GameV2.startGame()
        GameV2.drawPlayerTwoShootLoop()

        val leftMoveBtn: Button = findViewById(R.id.leftMoveBtn)
        val rightMoveBtn: Button = findViewById(R.id.rightMoveBtn)
        val fireBtn: Button = findViewById(R.id.fireBtn)

        leftMoveBtn.setOnClickListener {
            //clear gameText view before rendering new area
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
                            WinnerAlerDialog().show(this@MainActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    break
                } else if (GameV2.isPlayerTwoWin) {
                    try {
                        Handler(Looper.getMainLooper()).post {
                            LooserAlertDialog().show(this@MainActivity)
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
