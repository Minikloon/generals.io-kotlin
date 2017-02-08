package net.minikloon.generals.game.world.position

data class Pos(val x: Int, val y: Int) {
    fun toIndex(width: Int) : Int {
        return toIndex(x, y, width)
    }
    
    companion object {
        fun fromIndex(index: Int, width: Int) : Pos {
            val x = index % width
            val y = Math.floorDiv(index, width)
            return Pos(x, y)
        }
        
        fun toIndex(x: Int, y: Int, width: Int) : Int {
            return y * width + x
        }
    }
}