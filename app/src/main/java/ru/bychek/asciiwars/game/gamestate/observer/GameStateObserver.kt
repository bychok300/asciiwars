package ru.bychek.asciiwars.game.gamestate.observer;

import java.util.*


class GameStateObserver : Observable() {

    fun changeState() {
        setChanged()
        notifyObservers()
    }

}
