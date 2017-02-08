package net.minikloon.generals.game.generalsapi.messages

import org.json.JSONObject

data class Score(
        val playerIndex: Int,
        val tiles: Int,
        val troops: Int,
        val dead: Boolean
) {
    companion object {
        fun parse(json: JSONObject) : Score {
            return Score(
                    playerIndex = json["i"] as Int,
                    tiles = json["tiles"] as Int,
                    troops = json["total"] as Int,
                    dead = json["dead"] as Boolean
            )
        }
    }
}