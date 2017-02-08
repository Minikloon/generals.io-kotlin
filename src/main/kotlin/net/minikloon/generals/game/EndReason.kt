package net.minikloon.generals.game

open class EndReason(val won: Boolean)

class Won : EndReason(true)

class Killed(val killer: Player) : EndReason(false)

class Disconnected : EndReason(false)