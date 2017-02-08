package net.minikloon.generals.game.generalsapi

enum class ApiNotification(val str: String) {
    GAME_START("game_start"),
    GAME_UPDATE("game_update"),
    GAME_LOST("game_lost"),
    GAME_WON("game_won"),
    
    RECEIVE_CHAT("chat_message"),
    
    UPDATE_STARS("stars"),
    UPDATE_RANK("rank"),
}