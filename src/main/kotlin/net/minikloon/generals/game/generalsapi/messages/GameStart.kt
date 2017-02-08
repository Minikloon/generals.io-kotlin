package net.minikloon.generals.game.generalsapi.messages

import net.minikloon.generals.game.Player
import net.minikloon.generals.game.Team
import net.minikloon.generals.utils.toIntArray
import net.minikloon.generals.utils.toTypedArray
import org.json.JSONArray
import org.json.JSONObject

class GameStart(
        val self: Player,
        val replayId: String,
        val gameChatRoom: String,
        val teams: Map<Int, Team>
) {
    companion object {
        fun parse(json: JSONObject) : GameStart {
            val selfId = json["playerIndex"] as Int
            val selfTeamChatRoom = json.opt("team_chat_room")

            val usernames = (json["usernames"] as JSONArray).toTypedArray<String>()
            val teamsJson = json.opt("teams")
            val teamAffiliations =
                    if(teamsJson == null)
                        IntArray(usernames.size) { it } 
                    else 
                        (teamsJson as JSONArray).toIntArray()

            val teams = usernames
                    .mapIndexed(::Player)
                    .groupBy { teamAffiliations[it.index] }
                    .mapValues {
                        val teamChatRoom = if(it.key == teamAffiliations[selfId]) selfTeamChatRoom as String? else null
                        Team(it.key, it.value, teamChatRoom)
                    }

            val self = teams.flatMap { it.value.players }.first { it.index == selfId }
            
            return GameStart(
                    self = self,
                    replayId = json["replay_id"] as String,
                    gameChatRoom = json["chat_room"] as String,
                    teams = teams
            )
        }
    }
}