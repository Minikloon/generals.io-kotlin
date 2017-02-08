package net.minikloon.generals.game.world.terrain

enum class TerrainType(val id: Int, val obstructed: Boolean) {
    PLAYED_OWNED(0, false),
    EMPTY(-1, false),
    MOUNTAIN(-2, true),
    FOG(-3, false),
    FOG_OBSTACLE(-4, true),
    ;
    
    companion object {
        private val fromId = values().associateBy { it.id }
        
        fun fromId(id: Int) : TerrainType? {
            if(id >= 0) return PLAYED_OWNED
            return fromId[id]
        }
    }
}