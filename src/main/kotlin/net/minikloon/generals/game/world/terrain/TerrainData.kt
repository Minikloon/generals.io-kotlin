package net.minikloon.generals.game.world.terrain

import net.minikloon.generals.game.world.position.Pos
import net.minikloon.generals.utils.patchInPlace

class TerrainData(private val data: IntArray, val width: Int, val height: Int) {
    val unknown: Boolean
        get() = size == 0
    val size = width * height
    
    fun patch(diff: IntArray) {
        patchInPlace(data, diff)
    }
    
    operator fun get(pos: Pos) : TroopsAndTerrain? { return get(pos.toIndex(width)) }
    operator fun get(x: Int, y: Int) : TroopsAndTerrain? { return get(Pos.toIndex(x, y, width)) }
    
    operator fun get(index: Int) : TroopsAndTerrain? {
        if(index < 0 || 2 + size + index >= data.size) return null
        val troops = data[2 + index]
        val terrain = data[2 + size + index]
        return TroopsAndTerrain(troops, terrain)
    }
    
    companion object {
        fun firstPatch(patch: IntArray) : TerrainData {
            val width = patch[2]
            val height = patch[3]
            val data = IntArray(width * height * 2 + 2)
            val map = TerrainData(data, width, height)
            map.patch(patch)
            return map
        }
        
        fun unknownTerrain() : TerrainData {
            return TerrainData(intArrayOf(), 0, 0)
        }
    }
}