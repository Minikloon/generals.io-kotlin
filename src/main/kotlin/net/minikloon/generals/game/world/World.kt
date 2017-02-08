package net.minikloon.generals.game.world

import net.minikloon.generals.game.Game
import net.minikloon.generals.game.world.position.Pos
import net.minikloon.generals.game.world.terrain.Terrain
import net.minikloon.generals.game.world.terrain.TerrainData
import net.minikloon.generals.game.world.terrain.TerrainType
import net.minikloon.generals.utils.patch
import net.minikloon.generals.utils.versionLazy

class World(private val game: Game) {
    private var terrainData = TerrainData.unknownTerrain()
    private var citiesData = IntArray(0)
    private var generalsData = IntArray(0)
    var updates = 0
    
    fun update(mapPatch: IntArray, citiesPatch: IntArray, generalsState: IntArray) {
        if(terrainData.unknown)
            terrainData = TerrainData.firstPatch(mapPatch)
        else
            terrainData.patch(mapPatch)
        
        citiesData = patch(citiesData, citiesPatch)
        citiesData.sort()
        generalsData = generalsState
        ++updates
    }
    
    val width: Int
        get() = terrainData.width
    val height: Int
        get() = terrainData.height

    val tiles : Collection<Tile>
        get() = tilesMap.values
    
    val generalTiles: List<Tile>
        get() = generalsData.map { getCached(it)!! }

    operator fun get(pos: Pos) : Tile? { return get(pos.x, pos.y) }
    operator fun get(x: Int, y: Int) : Tile? { return getCached(Pos.toIndex(x, y, width)) }
    private fun getCached(index: Int) : Tile? { return tilesMap[index] }

    private val tilesMap by versionLazy({updates}) {
        (0 until width*height).associate { Pair(it, createTile(it)) }
    }
    
    private fun createTile(index: Int) : Tile {
        val tat = terrainData[index]!!

        val terrainType = TerrainType.fromId(tat.terrain) ?: throw IllegalStateException("Unknown terrain type ${tat.terrain}")
        val terrain: Terrain
        if(tat.terrain < 0)
            terrain = Terrain(terrainType)
        else {
            val player = game.players[tat.terrain]
            terrain = PlayerOwned(terrainType, player)
        }

        val isCity = citiesData.binarySearch(index) >= 0
        
        val general: General?
        if(terrain is PlayerOwned) {
            val generalData = generalsData[terrain.owner.index]
            general = 
                    if(generalData == -1) null 
                    else General(Pos.fromIndex(generalData, width), terrain.owner)
        }
        else general = null

        return Tile(this, index, tat.troops, terrain, isCity, general)
    }
}