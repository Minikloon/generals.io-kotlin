package net.minikloon.generals.game

import net.minikloon.generals.game.generalsapi.messages.GameUpdate
import net.minikloon.generals.game.world.Tile
import net.minikloon.generals.game.world.World
import net.minikloon.generals.utils.versionLazy

class Game(val replayId: String, val chatRoom: String, val teams: Map<Int, Team>) {
    val world = World(this)
    val players = teams.flatMap { it.value.players }.sortedBy { it.index }.toTypedArray()
    
    var turn = 0
        private set
    
    fun update(gu: GameUpdate) {
        turn = gu.turn
        world.update(gu.mapDiff, gu.citiesDiff, gu.generals)
        gu.scores.forEach { 
            val player = players[it.playerIndex]
            player.tilesCount = it.tiles
            player.troops = it.troops
            player.dead = it.dead
        }
        gu.stars?.forEachIndexed { index, stars -> players[index].stars = stars }
    }
    
    val generals by versionLazy({turn}) { world.generalTiles.map(Tile::general) }
}