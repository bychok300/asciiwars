package ru.bychek.asciiwars.game.gamestate

import ru.bychek.asciiwars.fragments.GameFragment
import ru.bychek.asciiwars.fragments.GameWithFriendFragment
import ru.bychek.asciiwars.game.engine.GameV2
import java.util.*


class GameState : Observer {

    override fun update(p0: Observable?, p1: Any?) {
        //TODO: refactor it. Need to get activity fields via context instead of static initialisation
        if (GameFragment.withBotMode){
            GameFragment.gameTextView.text = GameV2.drawGameArea()
        }
        if (GameWithFriendFragment.withFriendMode) {
            GameWithFriendFragment.gameTextView.text = GameV2.drawGameArea()
        }

        //TODO: create new observer for winner detecting becasue current implementation create huge load
        if (GameV2.isGameFinish) {
            if (GameV2.isPlayerOneWin) {
                GameV2.winner = GameV2.WINNERS.PLAYER_ONE.name
            } else if (GameV2.isPlayerTwoWin) {
                GameV2.winner = GameV2.WINNERS.PLAYER_TWO.name
            }
        }
    }
}