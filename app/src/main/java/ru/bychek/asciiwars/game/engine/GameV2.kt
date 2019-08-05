package ru.bychek.asciiwars.game.engine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.bychek.asciiwars.game.gamestate.GameState
import ru.bychek.asciiwars.game.gamestate.observer.GameStateObserver

class GameV2 {

    companion object {
        private const val BLANK_CHAR = "  "
        private const val BLANK_CHAR_CODE = 0
        private const val AREA_BORDER_CHAR = "x"
        private const val AREA_BORDER_CHAR_CODE = 1
        private const val PLAYER_ONE = "^"
        private const val PLAYER_TWO = "^"
        private const val PLAYER_ONE_CODE = 2
        private const val SHOOT = "o"
        private const val SHOOT_CODE = 3
        private const val PLAYER_TWO_CODE = 4
        var isPlayerOneWin = false
        var isPlayerTwoWin = false
        var winner = ""

        lateinit var gameAreaWidth: Number
        lateinit var gameAreaHeigth: Number

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

        fun drawGameArea(): String {
            val sb = StringBuffer()
            gameArea.forEach { i ->
                //println()
                sb.append("\n")
                i.forEach { j ->
                    if (j == BLANK_CHAR_CODE) {
                        //      print(BLANK_CHAR)
                        sb.append(BLANK_CHAR)
                    }
                    if (j == AREA_BORDER_CHAR_CODE) {
                        //    print(AREA_BORDER_CHAR)
                        sb.append(AREA_BORDER_CHAR)
                    }
                    if (j == PLAYER_ONE_CODE) {
                        //    print(PLAYER_ONE)
                        sb.append(PLAYER_ONE)
                    }
                    if (j == SHOOT_CODE) {
                        //    print(SHOOT)
                        sb.append(SHOOT)
                    }
                    if (j == PLAYER_TWO_CODE) {
                        sb.append(PLAYER_TWO)
                    }
                    //TODO check for player 2 shoot

                }
            }
            return sb.toString()
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
                for (i in 1..gameAreaHeigth.toInt() - 3) {
                    gameArea[shootPositionX - i][shootPositionY] = SHOOT_CODE
                    gameRenderObserver.changeState()
                    delay(150L)
                    clearGameAreaSpaceInCoords(shootPositionX - i, shootPositionY)
                    if (shootPositionX - i == playerTwoPositionX && shootPositionY == playerTwoPositionY) {
                        isGameFinish = true
                        isPlayerOneWin = true
                    }
                    gameRenderObserver.changeState()
                }
            }
        }

        fun drawPlayerTwoShoot() {
            GlobalScope.launch {
                val shootPositionY = playerTwoPositionY
                val shootPositionX = playerTwoPositionX
                for (i in 1..gameAreaHeigth.toInt() - 3) {
                    gameArea[shootPositionX + i][shootPositionY] = SHOOT_CODE
                    gameRenderObserver.changeState()
                    delay(150L)
                    clearGameAreaSpaceInCoords(shootPositionX + i, shootPositionY)
                    if (shootPositionX + i == playerOnePositionX && shootPositionY == playerOnePositionY) {
                        isGameFinish = true
                        isPlayerTwoWin = true
                    }
                    gameRenderObserver.changeState()
                }
            }
        }

        fun drawPlayerTwoShootLoop() {
            GlobalScope.launch {
                while (!isGameFinish) {
                    try {
                        val shootPositionY = playerTwoPositionY
                        val shootPositionX = playerTwoPositionX
                        for (i in 1..gameAreaHeigth.toInt() - 3) {
                            gameArea[shootPositionX + i][shootPositionY] = SHOOT_CODE
                            gameRenderObserver.changeState()
                            delay(150L)
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
                    movePlayerTwoTo()
                    delay(600L)
                }
            }
        }

        //dummy player two gameplay
        private fun movePlayerTwoTo() {
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
            drawPlayerTwoShootLoop()
        }

        fun startGameWithFriend(
            gameAreaWidth: Int, gameAreaHeigth: Int,
            userStartPositionX: Int, userStartPositionY: Int
        ) {
            initGameArea(gameAreaWidth, gameAreaHeigth)
            updatePlayerOnePosition(userStartPositionX, userStartPositionY)
            updatePlayerTwoPosition(1, 1)
        }

        fun restartGame() {

        }


    }

    enum class WINNERS {
        PLAYER_ONE,
        PLAYER_TWO
    }
}