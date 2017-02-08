package net.minikloon.generals.game.generalsapi.messages

import net.minikloon.generals.utils.toIntArray
import net.minikloon.generals.utils.toTypedArray
import org.json.JSONArray
import org.json.JSONObject

class GameUpdate(
        val turn: Int,
        val mapDiff: IntArray,
        val citiesDiff: IntArray,
        val generals: IntArray,
        val scores: List<Score>,
        val stars: IntArray?
) {
    companion object {
        fun parse(json: JSONObject) : GameUpdate {
            val turn = json["turn"] as Int
            
            val scores = (json["scores"] as JSONArray)
                    .toTypedArray<JSONObject>()
                    .map { Score.parse(it) }
            
            val starsJson = json.opt("stars")
            val stars = if(starsJson == null) null else intArray(starsJson)
            
            return GameUpdate(
                    turn = json["turn"] as Int,
                    mapDiff = intArray(json["map_diff"]),
                    citiesDiff = intArray(json["cities_diff"]),
                    generals = intArray(json["generals"]),
                    scores = scores,
                    stars = stars
            )
        }
        
        private fun intArray(obj: Any) : IntArray {
            return (obj as JSONArray).toIntArray()
        }
    }
}