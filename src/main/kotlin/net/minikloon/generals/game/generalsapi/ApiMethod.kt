package net.minikloon.generals.game.generalsapi

enum class ApiMethod(val str: String) {
    SET_USERNAME("set_username"),
    
    JOINQUEUE_FFA("play"),
    JOINQUEUE_1V1("join_1v1"),
    JOINQUEUE_2v2("join_team"),
    JOINQUEUE_PRIVATE("join_private"),
    LEAVE_QUEUE("cancel"),
    
    SET_FORCE_START("set_force_start"),
    PICK_TEAM("set_custom_team"),
    LEAVE_2v2_TEAM("leave_team"),
    LEAVE_GAME("leave_game"),
    
    REQUEST_STARS_AND_RANK("stars_and_rank"),
    
    MOVE("attack"),
    CLEAR_MOVES("clear_moves"),
    PING_TILE("ping_tile"),
    SEND_CHAT("chat_message"),
}