package net.minikloon.generals.game.world

import net.minikloon.generals.game.Player
import net.minikloon.generals.game.world.position.Direction
import net.minikloon.generals.game.world.position.Pos
import net.minikloon.generals.game.world.terrain.Terrain
import net.minikloon.generals.game.world.terrain.TerrainType

open class Tile(
        val world: World,
        val index: Int,
        val troops: Int,
        val terrain: Terrain,
        val isCity: Boolean,
        val general: General?
) {
    val pos by lazy { Pos.fromIndex(index, world.width) }
    val owner = (terrain as? PlayerOwned)?.owner
    
    fun getRelative(dir: Direction) : Tile? {
        return getRelative(dir.x, dir.y)
    }
    
    fun getRelative(x: Int, y: Int) : Tile? {
        return world[pos.x + x, pos.y + y]
    }
    
    fun getRelatives(predicate: (Tile) -> Boolean = { true }) : List<Tile> {
        return Direction.values()
                .mapNotNull { getRelative(it) }
                .filter(predicate)
    }
}

class PlayerOwned(type: TerrainType, val owner: Player) : Terrain(type)