package net.minikloon.generals.game.queue

enum class QueueType(val id: String?) {
    FFA("ffa"),
    ONE_VS_ONE("1v1"),
    TWO_VS_TWO("2v2"),
    PRIVATE(null)
}