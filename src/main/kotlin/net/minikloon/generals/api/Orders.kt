package net.minikloon.generals.api

import net.minikloon.generals.GeneralsBot
import net.minikloon.generals.game.generalsapi.ApiMethod.*
import net.minikloon.generals.game.world.Tile

interface Order {
    fun execute(bot: GeneralsBot)
}

class Move(val from: Tile, val to: Tile, val is50: Boolean = false) : Order {
    override fun execute(bot: GeneralsBot) {
        bot.send(MOVE, from.index, to.index, is50)
    }
}

class ClearMoveQueue : Order {
    override fun execute(bot: GeneralsBot) {
        bot.send(CLEAR_MOVES)
    }
}

class NoMove : Order {
    override fun execute(bot: GeneralsBot) {}
}