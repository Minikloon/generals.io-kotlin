package net.minikloon.generals.api

import net.minikloon.generals.GeneralsBot
import net.minikloon.generals.game.EndReason
import net.minikloon.generals.game.Game
import net.minikloon.generals.game.Player

abstract class GameAi(val game: Game, val self: Player, private val bot: GeneralsBot) {
    open fun onGameStart() {}

    open fun onGameEnd(reason: EndReason) {}
    
    abstract fun computeTurn() : Order
    
    open fun onReceiveChat(sender: Player?, text: String) {}
}