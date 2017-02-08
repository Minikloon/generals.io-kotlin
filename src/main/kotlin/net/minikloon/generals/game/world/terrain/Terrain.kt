package net.minikloon.generals.game.world.terrain

open class Terrain(val type: TerrainType) {
    override fun equals(other: Any?): Boolean {
        if(other is Int) {
            return type.id == other
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }
}