package net.minikloon.generals.game

class Team(
        val id: Int,
        val players: List<Player>,
        val chatroom: String? // if null then it's unknown, might be some other's team secret room!
)