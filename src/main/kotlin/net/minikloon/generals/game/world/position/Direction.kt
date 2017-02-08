package net.minikloon.generals.game.world.position

enum class Direction(val x: Int, val y: Int) {
    NORTH(0, 1),
    EAST(1, 0),
    SOUTH(0, -1),
    WEST(-1, 0),
}