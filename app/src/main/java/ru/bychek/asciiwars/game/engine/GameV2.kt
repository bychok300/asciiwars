package ru.bychek.asciiwars.game.engine

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bychek.asciiwars.R
import ru.bychek.asciiwars.game.configs.GameConfig
import ru.bychek.asciiwars.game.gamestate.GameState
import ru.bychek.asciiwars.game.gamestate.observer.GameStateObserver

class GameV2 {

    companion object {
        private const val BLANK_CHAR = R.drawable.ic_blank_24dp
        private const val BLANK_CHAR_CODE = 0
        private const val AREA_BORDER_CHAR = R.drawable.ic_border
        private const val AREA_BORDER_CHAR_CODE = 1
        private const val PLAYER_ONE = R.drawable.ic_player_one_sceen
        private const val PLAYER_TWO = R.drawable.ic_player_two_sceen
        private const val PLAYER_ONE_CODE = 2
        private const val SHOOT = R.drawable.ic_bullet_up_direct
        private const val SHOOT_CODE = 3
        private const val PLAYER_TWO_CODE = 4
        private val SHOOT_SPEED = GameConfig.shootSpeed
        private const val BOT_EACH_MOVE_SPEED = 600L
        private const val DECREASING_AREA_SPEED = 5000L

        var isPlayerOneWin = false
        var isPlayerTwoWin = false
        var winner = ""
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        var gameAreaWidth: Number = 0
        var gameAreaHeigth: Number = 0

        private var gameArea = Array(0) { IntArray(0) }
        private val gameRenderObserver = GameStateObserver()
        private val gameState = GameState()

        var isGameFinish = false

        var shootPositionY: Int = 0
        var shootPositionX: Int = 0
        var playerOnePositionX: Int = 0
        var playerOnePositionY: Int = 0

        var playerTwoPositionX: Int = 0
        var playerTwoPositionY: Int = 0

        var bulletPlayerOnePositionX: Int = 0
        var bulletPlayerOnePositionY: Int = 0

        var bulletPlayerTwoPositionY: Int = 0
        var bulletPlayerTwoPositionX: Int = 0

        fun prepareGameEngine() {
            // because it all static fields the doesnt change their last state
            // thats why i always reassign it
            isGameFinish = false
            isPlayerOneWin = false
            isPlayerTwoWin = false
            gameRenderObserver.addObserver(gameState)
            gameRenderObserver.changeState()
        }

        fun initGameArea(x: Int, y: Int) {
            gameAreaWidth = y
            gameAreaHeigth = x
            gameArea = Array(x) { IntArray(y) }
            var i = 1
            var j: Int

            while (i <= x) {
                j = 1
                while (j <= y) {
                    if (i == 1 || i == x || j == 1 || j == y) {
                        gameArea[i - 1][j - 1] = 1
                    } else {
                        gameArea[i - 1][j - 1] = 0
                    }
                    j++
                }
                i++
            }

        }

        fun drawGameArea(): SpannableStringBuilder {
            val sb = SpannableStringBuilder()
            gameArea.forEach { i ->
                //println()
                sb.append("\n")
                i.forEach { j ->
                    if (j == BLANK_CHAR_CODE) {
                        //      print(BLANK_CHAR)
                        sb.append(" ", ImageSpan(context, BLANK_CHAR), 0)
                    }
                    if (j == AREA_BORDER_CHAR_CODE) {
                        //    print(AREA_BORDER_CHAR)
                        sb.append(" ", ImageSpan(context, AREA_BORDER_CHAR), 0)
                    }
                    if (j == PLAYER_ONE_CODE) {
                        //    print(PLAYER_ONE)
                        sb.append(" ", ImageSpan(context, PLAYER_ONE), 0)
                    }
                    if (j == SHOOT_CODE) {
                        //    print(SHOOT)
                        sb.append(" ", ImageSpan(context, SHOOT), 0)
                    }
                    if (j == PLAYER_TWO_CODE) {
                        sb.append(" ", ImageSpan(context, PLAYER_TWO), 0)
                    }

                }
            }

            return sb
        }

        private fun clearGameAreaSpaceInCoords(x: Int, y: Int) {
            gameArea[x][y] = BLANK_CHAR_CODE
        }

        fun updatePlayerOnePosition(x: Int, y: Int) {
            playerOnePositionX = x
            playerOnePositionY = y

            gameArea[playerOnePositionX][playerOnePositionY] = PLAYER_ONE_CODE
            gameRenderObserver.changeState()
        }

        fun updatePlayerTwoPosition(x: Int, y: Int) {
            playerTwoPositionX = x
            playerTwoPositionY = y

            gameArea[playerTwoPositionX][playerTwoPositionY] = PLAYER_TWO_CODE
            gameRenderObserver.changeState()
        }

        fun movePlayerOneRight() {
            if (playerOnePositionY < gameAreaWidth.toInt() - 2) {
                // example: y - 1 = move left; x - 1  = move up
                clearGameAreaSpaceInCoords(playerOnePositionX, playerOnePositionY)
                updatePlayerOnePosition(playerOnePositionX, playerOnePositionY + 1)
            }
        }

        fun movePlayerOneLeft() {
            if (playerOnePositionY > 1) {
                clearGameAreaSpaceInCoords(playerOnePositionX, playerOnePositionY)
                updatePlayerOnePosition(playerOnePositionX, playerOnePositionY - 1)
            }
        }

        fun movePlayerTwoRight() {
            if (playerTwoPositionY < gameAreaWidth.toInt() - 2) {
                // example: y - 1 = move left; x - 1  = move up
                clearGameAreaSpaceInCoords(playerTwoPositionX, playerTwoPositionY)
                updatePlayerTwoPosition(playerTwoPositionX, playerTwoPositionY + 1)
            }
        }

        fun movePlayerTwoLeft() {
            if (playerTwoPositionY > 1) {
                clearGameAreaSpaceInCoords(playerTwoPositionX, playerTwoPositionY)
                updatePlayerTwoPosition(playerTwoPositionX, playerTwoPositionY - 1)
            }
        }

        fun drawPlayerOneShoot() {
            // playerOnePositionX - 1 mean that 1 point higher than player character
            val shootPositionY = playerOnePositionY
            val shootPositionX = playerOnePositionX
            // launch a new coroutine in background and continue
            GlobalScope.launch {
                while (!isGameFinish) {
                    for (i in 1..gameAreaHeigth.toInt() - 3) {
                        gameArea[shootPositionX - i][shootPositionY] = SHOOT_CODE
                        gameRenderObserver.changeState()
                        delay(SHOOT_SPEED)
                        clearGameAreaSpaceInCoords(shootPositionX - i, shootPositionY)
                        if (shootPositionX - i == playerTwoPositionX && shootPositionY == playerTwoPositionY) {
                            isGameFinish = true
                            isPlayerOneWin = true
                        }
                        gameRenderObserver.changeState()
                    }
                }
            }
        }

        fun drawPlayerTwoShoot() {
            GlobalScope.launch {
                val shootPositionY = playerTwoPositionY
                val shootPositionX = playerTwoPositionX
                while (!isGameFinish) {
                    for (i in 1..gameAreaHeigth.toInt() - 3) {
                        gameArea[shootPositionX + i][shootPositionY] = SHOOT_CODE
                        gameRenderObserver.changeState()
                        delay(SHOOT_SPEED)
                        clearGameAreaSpaceInCoords(shootPositionX + i, shootPositionY)
                        if (shootPositionX + i == playerOnePositionX && shootPositionY == playerOnePositionY) {
                            isGameFinish = true
                            isPlayerTwoWin = true
                        }
                        gameRenderObserver.changeState()
                    }
                }
            }
        }

        private fun drawBotShootLoop() {
            //TODO: draw shoot on correct way if area dereasing
            GlobalScope.launch {
                while (!isGameFinish) {
                    try {
                        val shootPositionY = playerTwoPositionY
                        val shootPositionX = playerTwoPositionX
                        for (i in 1..gameAreaHeigth.toInt() - 3) {
                            gameArea[shootPositionX + i][shootPositionY] = SHOOT_CODE
                            gameRenderObserver.changeState()
                            delay(SHOOT_SPEED)
                            clearGameAreaSpaceInCoords(shootPositionX + i, shootPositionY)
                            if (shootPositionX + i == playerOnePositionX && shootPositionY == playerOnePositionY) {
                                isGameFinish = true
                                isPlayerTwoWin = true
                            }
                            gameRenderObserver.changeState()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    movePlayerTwoToPlayerOnePosition()
                    delay(BOT_EACH_MOVE_SPEED)
                }
            }
        }

        //dummy player two gameplay
        private fun movePlayerTwoToPlayerOnePosition() {
            clearGameAreaSpaceInCoords(playerTwoPositionX, playerTwoPositionY)
            updatePlayerTwoPosition(playerTwoPositionX, playerOnePositionY)
        }

        private fun isGameStateChanged(): Boolean {
            return GameStateObserver().hasChanged()
        }

        fun startGameWithBot(
            gameAreaWidth: Int, gameAreaHeigth: Int,
            userStartPositionX: Int, userStartPositionY: Int
        ) {
            initGameArea(gameAreaWidth, gameAreaHeigth)
            updatePlayerOnePosition(userStartPositionX, userStartPositionY)
            updatePlayerTwoPosition(1, 1)
            drawBotShootLoop()
            //TODO: make this optional enabling via setting fragment
            //decreaseGameArea()
        }

        fun startGameWithFriend(
            gameAreaWidth: Int, gameAreaHeigth: Int,
            userStartPositionX: Int, userStartPositionY: Int
        ) {
            initGameArea(gameAreaWidth, gameAreaHeigth)
            updatePlayerOnePosition(userStartPositionX, userStartPositionY)
            updatePlayerTwoPosition(1, 1)
            //TODO: make this optional enabling via setting fragment
            decreaseGameArea()
        }


        fun decreaseGameArea() {
            //TODO fix bullet move on area decrease
            GlobalScope.launch {
                while (!isGameFinish) {
                    delay(DECREASING_AREA_SPEED)
                    // currrent player one position local should be saved before area decreased
                    val currPlOnePosX = playerOnePositionX
                    val currPlOnePosY = playerOnePositionY

                    val currPlTwoPosX = playerTwoPositionX
                    val currPlTwoPosY = playerTwoPositionY

                    if (gameAreaHeigth != 6 && gameAreaWidth != 3) {
                        gameAreaHeigth = gameAreaHeigth.toInt() - 1
                        gameAreaWidth = gameAreaWidth.toInt() - 1
                        gameArea = Array(gameAreaHeigth.toInt()) { IntArray(gameAreaWidth.toInt()) }
                        initGameArea(gameAreaHeigth.toInt(), gameAreaWidth.toInt())

                        val plOnePosStartX = gameAreaHeigth.toInt() - 2
                        val plOnePosStartY = gameAreaWidth.toInt() - 2

                        if (plOnePosStartY != currPlOnePosY && currPlOnePosY != 1) {
                            // remember : y - move left; x - 1 mode down
                            updatePlayerOnePosition(plOnePosStartX, currPlOnePosY - 1)
                        } else {
                            updatePlayerOnePosition(plOnePosStartX, currPlOnePosY)
                        }

                        val plTwoPosStartX = 1
                        val plTwoPosStartY = 1

                        if (plTwoPosStartY != currPlTwoPosY && currPlTwoPosY != gameAreaWidth.toInt() - 2) {
                            updatePlayerTwoPosition(plTwoPosStartX, currPlTwoPosY - 1)
                        } else {
                            updatePlayerTwoPosition(plTwoPosStartX, currPlTwoPosY)
                        }
                    }
                }
            }
        }

        fun restartGame() {

        }


    }

    enum class WINNERS {
        PLAYER_ONE,
        PLAYER_TWO
    }
}