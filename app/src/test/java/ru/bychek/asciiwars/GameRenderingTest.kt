package ru.bychek.asciiwars

import android.content.res.Resources
import org.junit.Test
import ru.bychek.asciiwars.game.gamestate.observer.GameStateObserver
import java.util.*


class GameRenderingTest : Observer {
    override fun update(p0: Observable?, p1: Any?) {
        println("changes")
    }

    var gameAreaWidth: Int = 10
    var gameAreaHeight: Int = 20

    private var gameArea: Array<IntArray> = Array(gameAreaWidth) { IntArray(gameAreaHeight) }

    @Test
    fun testGamev2() {
//        GameV2.prepareGameEngine()
//        GameV2.initGameArea(10, 20)
//        GameV2.updatePlayerOnePosition(2, 2)
//        GameV2.updatePlayerOnePosition(3, 3)
//        GameV2.startGame()
    }

    @Test
    fun detectChangesInsideGameArea() {
        println(gameArea.hashCode())
        gameArea = Array(11) { IntArray(20) }
        println(gameArea.hashCode())
    }

    @Test
    fun getResourceTest() {
        var gameArea =  Resources.getSystem().getString(R.string.game)
        println(gameArea)
    }

    @Test
    fun unicodeTest(){
        val s : String = "ˬ"
        print(s)
    }

    @Test
    fun observerTest() {
        val observer = GameStateObserver()
        val t = GameRenderingTest()
        observer.addObserver(t)
        observer.changeState()
    }

    @Test
    fun caseTest() {
        val t = 1
        when (t) {
            1 -> println(true)
        }
    }

    @Test
    fun pizdec() {
        var z = StringBuilder()
        var x = StringBuilder()
        val s = arrayOf(1, 2, 3, 4)
        for (v in s) {
            x.append(v)
        }

        s.forEach { i ->
            z.append(i)
        }
        println(x.toString())
        println(z.toString())
    }
}