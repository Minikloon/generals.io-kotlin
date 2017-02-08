package net.minikloon.generals.game.queue

import net.minikloon.generals.GeneralsBot
import net.minikloon.generals.game.generalsapi.ApiMethod.*
import net.minikloon.generals.game.queue.QueueType.*


abstract class JoinQueue(val type: QueueType) {
    abstract fun execute(bot: GeneralsBot)
}

class JoinQueueFFA : JoinQueue(FFA) {
    override fun execute(bot: GeneralsBot) {
        bot.send(JOINQUEUE_FFA, bot.userId)
    }
}

class JoinQueue1v1 : JoinQueue(ONE_VS_ONE) {
    override fun execute(bot: GeneralsBot) {
        bot.send(JOINQUEUE_1V1, bot.userId)
    }
}

class JoinQueue2v2(val teamId: String) : JoinQueue(TWO_VS_TWO) {
    override fun execute(bot: GeneralsBot) {
        bot.send(JOINQUEUE_2v2, teamId, bot.userId)
    }
}

class JoinQueuePrivate(val gameId: String) : JoinQueue(PRIVATE) {
    val link = "${GeneralsBot.botServer}/games/$gameId"
    
    override fun execute(bot: GeneralsBot) {
        bot.send(JOINQUEUE_PRIVATE, gameId, bot.userId)
    }
}